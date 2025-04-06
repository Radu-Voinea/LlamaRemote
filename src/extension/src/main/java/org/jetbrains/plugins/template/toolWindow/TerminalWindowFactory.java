package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.ui.TerminalWidget;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.jediterm.terminal.TtyConnector;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.template.api.OpenAIAPI;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Method;

public class TerminalWindowFactory implements ToolWindowFactory {

	@Getter
	@Accessors(fluent = true)
	private static TerminalWindowFactory instance;

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

	private ToolWindow terminalToolWindow;
	private ShellTerminalWidget shellTerminalWidget;
	private TerminalWidget terminalWidget;
	private TerminalToolWindowManager terminalManager;


	public TerminalWindowFactory() {
		instance = this;
	}

	@Override
	@SneakyThrows
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		this.terminalToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Llama Terminal");

		this. terminalManager = TerminalToolWindowManager.getInstance(project);
		this. terminalWidget = terminalManager.createShellWidget(null, null, false, true);
		this. shellTerminalWidget = (ShellTerminalWidget) widgetMethod.invoke(terminalWidget);

		shellTerminalWidget.getTerminalPanel().addPreKeyEventHandler((event) -> {
			if (event.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
				String processedBuffer = lastBuffer.strip();

				if (processedBuffer.startsWith("##")) {
					event.consume();

					processedBuffer = processedBuffer.substring(2);
					processedBuffer = processedBuffer.strip();

					String response = OpenAIAPI.getChatGPTResponse(processedBuffer);
					try {
						for (int i = 0; i < lastBuffer.length(); i++) {
							shellTerminalWidget.getTtyConnector().write("\b");
						}
						shellTerminalWidget.getTtyConnector().write(response);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			} else {
				lastBuffer = shellTerminalWidget.getTypedShellCommand();
			}
		});

		Content content = ContentFactory.getInstance().createContent(terminalWidget.getComponent(), "Llama Terminal", false);
		toolWindow.getContentManager().addContent(content);
	}

	public void focus() {
		terminalToolWindow.activate(() -> {
		});
	}

	@SneakyThrows
	public void write(String text) {
		shellTerminalWidget.getTtyConnector().write(text);
	}

}
