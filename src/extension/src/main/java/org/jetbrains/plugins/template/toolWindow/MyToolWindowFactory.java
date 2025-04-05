package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.template.panels.WorkspacesPanel;
import org.jetbrains.plugins.template.services.MyProjectService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
        Content content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    private static class MyToolWindow {

        private final MyProjectService service;
        private final WorkspacesPanel workspacesPanel;

        public MyToolWindow(ToolWindow toolWindow) {
            this.service = ServiceManager.getService(toolWindow.getProject(), MyProjectService.class);
            this.workspacesPanel = new WorkspacesPanel();
        }

        public JComponent getContent() {
            JPanel mainPanel = new JPanel(new BorderLayout());

            // Create refresh button
            JButton refreshButton = new JButton("Refresh");
            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Your refresh logic here
                    // For example: workspacesPanel.reload(); if you have such a method
                    System.out.println("Refresh button clicked");

                    // Example of clearing and reloading the panel
                    workspacesPanel.refresh(); // You need to implement this in WorkspacesPanel
                }
            });

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(refreshButton);

            mainPanel.add(topPanel, BorderLayout.NORTH);
            mainPanel.add(workspacesPanel, BorderLayout.CENTER);

            return mainPanel;
        }
    }
}
