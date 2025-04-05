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

public class CreateHost implements com.intellij.openapi.wm.ToolWindowFactory {

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

            // Panoul care va conține workspace-ul într-un layout vertical (BoxLayout)
            JPanel pictogramPanel = new JPanel();
            pictogramPanel.setLayout(new BoxLayout(pictogramPanel, BoxLayout.Y_AXIS));

            // Crearea panoului "workspace"
            JPanel workspacePanel = new JPanel();
            workspacePanel.setLayout(new BoxLayout(workspacePanel, BoxLayout.Y_AXIS));
            workspacePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createEtchedBorder()
            ));
            // Aliniere la stânga (pentru ca setMaximumSize să funcționeze cum ne dorim)
            workspacePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Titlul
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
            // Setăm lățimea maximă la cât de mare e containerul (Integer.MAX_VALUE),
            // dar păstrăm înălțimea preferată
            nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, namePrefSize.height));
            workspacePanel.add(nameField);
            workspacePanel.add(Box.createVerticalStrut(5));

            // Câmpul Host
            JLabel hostLabel = new JLabel("Host:");
            hostLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(hostLabel);

            JTextField hostField = new JTextField();
            hostField.setAlignmentX(Component.LEFT_ALIGNMENT);
            Dimension hostPrefSize = hostField.getPreferredSize();
            hostField.setMaximumSize(new Dimension(Integer.MAX_VALUE, hostPrefSize.height));
            workspacePanel.add(hostField);
            workspacePanel.add(Box.createVerticalStrut(5));

            // Câmpul Port
            JLabel portLabel = new JLabel("Port:");
            portLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(portLabel);

            JTextField portField = new JTextField();
            portField.setAlignmentX(Component.LEFT_ALIGNMENT);
            Dimension portPrefSize = portField.getPreferredSize();
            portField.setMaximumSize(new Dimension(Integer.MAX_VALUE, portPrefSize.height));
            workspacePanel.add(portField);
            workspacePanel.add(Box.createVerticalStrut(5));

            // Câmpul Username
            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(usernameLabel);

            JTextField usernameField = new JTextField();
            usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
            Dimension userPrefSize = usernameField.getPreferredSize();
            usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, userPrefSize.height));
            workspacePanel.add(usernameField);
            workspacePanel.add(Box.createVerticalStrut(10));

            // Zona de text pentru Private Key
            JLabel pkLabel = new JLabel("Private Key:");
            pkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(pkLabel);

            // 6 linii, 20 coloane (poți ajusta după preferințe)
            JTextArea privateKeyArea = new JTextArea(6, 20);
            privateKeyArea.setLineWrap(true);
            privateKeyArea.setWrapStyleWord(true);
            privateKeyArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Setăm un maxim pentru înălțime, astfel încât să nu crească la infinit
            Dimension pkPrefSize = privateKeyArea.getPreferredSize();
            privateKeyArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, pkPrefSize.height));

            // Încapsulăm JTextArea într-un JScrollPane
            JScrollPane privateKeyScroll = new JScrollPane(
                    privateKeyArea,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
            privateKeyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            workspacePanel.add(privateKeyScroll);

            // Adăugăm workspacePanel în pictogramPanel
            pictogramPanel.add(workspacePanel);

            // Încapsulăm totul într-un JScrollPane (dacă vrei să poți face scroll întregului panou)
            JScrollPane scrollPane = new JScrollPane(
                    pictogramPanel,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Adăugăm un buton "Save" în partea de jos
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save");
            buttonPanel.add(saveButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            return mainPanel;
        }
    }
}
