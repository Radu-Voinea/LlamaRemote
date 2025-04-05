package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.template.services.MyProjectService;

import javax.swing.*;
import java.awt.*;

public class CreateWorkspace implements com.intellij.openapi.wm.ToolWindowFactory {

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

        public MyToolWindow(ToolWindow toolWindow) {
            this.service = ServiceManager.getService(toolWindow.getProject(), MyProjectService.class);
        }

        public JPanel getContent() {
            // Panoul principal cu BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout());

            // Panoul de workspace cu BoxLayout vertical
            JPanel workspacePanel = new JPanel();
            workspacePanel.setLayout(new BoxLayout(workspacePanel, BoxLayout.Y_AXIS));
            workspacePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createEtchedBorder()
            ));
            workspacePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Titlu
            JLabel titleLabel = new JLabel("Workspace");
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(titleLabel);
            workspacePanel.add(Box.createVerticalStrut(5));

            // Câmpul Name
            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(nameLabel);

            JTextField nameField = new JTextField();
            nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
            Dimension namePrefSize = nameField.getPreferredSize();
            // Se întinde pe toată lățimea disponibilă, păstrând înălțimea preferată
            nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, namePrefSize.height));
            workspacePanel.add(nameField);
            workspacePanel.add(Box.createVerticalStrut(5));

            // Adăugăm workspacePanel în centrul panoului principal
            mainPanel.add(workspacePanel, BorderLayout.CENTER);

            // Adăugăm butonul "Save" în partea de jos
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save");
            buttonPanel.add(saveButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            return mainPanel;
        }
    }
}
