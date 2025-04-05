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

        public MyToolWindow(ToolWindow toolWindow) {
            // If you're using the built-in IntelliJ service, you can grab it here:
            this.service = ServiceManager.getService(toolWindow.getProject(), MyProjectService.class);
        }

        public JPanel getContent() {
            // Create a main panel (using BorderLayout as a convenient container layout)
            JPanel mainPanel = new JPanel(new BorderLayout());

            // Create a panel to hold the pictograms in a vertical list
            JPanel pictogramPanel = new JPanel();
            pictogramPanel.setLayout(new BoxLayout(pictogramPanel, BoxLayout.Y_AXIS));

            // Example: Add a few pictograms (labels with icons) for demonstration
            // In a real plugin, you might retrieve these from your MyProjectService or elsewhere.
            for (int i = 0; i < 50; i++) {
                // Replace with your actual icon:
                //  e.g. Icon icon = AllIcons.General.InspectionsOK; or a custom icon
                Icon dummyIcon = UIManager.getIcon("OptionPane.informationIcon");
                JLabel label = new JLabel("Workspace " + i, dummyIcon, SwingConstants.LEFT);

                // Add some spacing or set a preferred size if needed:
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                pictogramPanel.add(label);
            }

            // Wrap the pictogram panel in a scroll pane
            JScrollPane scrollPane = new JScrollPane(pictogramPanel,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            // Add the scroll pane to the main panel
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            return mainPanel;
        }
    }
}
