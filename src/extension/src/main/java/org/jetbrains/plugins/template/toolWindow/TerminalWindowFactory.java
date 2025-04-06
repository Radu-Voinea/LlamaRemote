package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.ui.TerminalWidget;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.annotations.NotNull;

public class TerminalWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolWindow terminalToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Copilot Terminal");
        if (terminalToolWindow == null) {
            return;
        }

        terminalToolWindow.activate(() -> {
            TerminalToolWindowManager terminalManager = TerminalToolWindowManager.getInstance(project);
            TerminalWidget terminalWidget = terminalManager.createShellWidget(null, null, false, true);

            Content content = ContentFactory.getInstance().createContent(terminalWidget.getComponent(), "Copilot Terminal", false);
            toolWindow.getContentManager().addContent(content);
        });
    }
}
