package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class TerminalAutoOpener implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Llama Terminal");
        if (toolWindow != null) {
            toolWindow.show();
        }
    }
}
