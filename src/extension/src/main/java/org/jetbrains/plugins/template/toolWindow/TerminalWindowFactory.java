package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.ui.TerminalWidget;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TerminalWindowFactory implements ToolWindowFactory {

	private String lastBuffer = "";

	private static final Class<?> terminalWidgetClass;
	private static final Method widgetMethod;

	static {
		try {
			terminalWidgetClass = Class.forName("com.intellij.terminal.JBTerminalWidget$TerminalWidgetBridge");
			widgetMethod = terminalWidgetClass.getDeclaredMethod("widget");
			widgetMethod.setAccessible(true);
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ToolWindow terminalToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Copilot Terminal");
		if (terminalToolWindow == null) {
			return;
		}

		terminalToolWindow.activate(() -> {
			TerminalToolWindowManager terminalManager = TerminalToolWindowManager.getInstance(project);
			TerminalWidget terminalWidget = terminalManager.createShellWidget(null, null, false, true);

			try {
				ShellTerminalWidget shellTerminalWidget = (ShellTerminalWidget) widgetMethod.invoke(terminalWidget);

				shellTerminalWidget.getTerminalPanel().addPreKeyEventHandler((event) -> {
					if (event.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
						System.out.println(lastBuffer);
					} else {
						lastBuffer = shellTerminalWidget.getTypedShellCommand();
					}
				});
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

			Content content = ContentFactory.getInstance().createContent(terminalWidget.getComponent(), "Copilot Terminal", false);
			toolWindow.getContentManager().addContent(content);
		});
	}


}
