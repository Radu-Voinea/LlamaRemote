package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.annotations.NotNull;

public class TerminalWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Ensure the Terminal tool window is available
        ToolWindow terminalToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Terminal");
        if (terminalToolWindow == null) {
            return;
        }

        // Activate the Terminal tool window
        terminalToolWindow.activate(() -> {
            // Create a new terminal session
            TerminalToolWindowManager terminalManager = TerminalToolWindowManager.getInstance(project);
            ShellTerminalWidget terminalWidget = terminalManager.createLocalShellWidget(null, null);

            // Add the terminal widget as a new content tab
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(terminalWidget.getComponent(), "My Terminal", false);
            toolWindow.getContentManager().addContent(content);
        });
    }
}
