package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.template.services.MyProjectService;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AddUser implements com.intellij.openapi.wm.ToolWindowFactory {

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
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel workspaceLabel = new JLabel("Workspaces:");
            formPanel.add(workspaceLabel, gbc);

            gbc.gridx = 1;
            String[] workspaceOptions = { "Workspace 1", "Workspace 2", "Workspace 3" };
            JComboBox<String> workspaceCombo = new JComboBox<>(workspaceOptions);
            workspaceCombo.setPreferredSize(new Dimension(150, workspaceCombo.getPreferredSize().height));
            formPanel.add(workspaceCombo, gbc);

            gbc.gridy++;
            gbc.gridx = 0;
            JLabel usernameLabel = new JLabel("Username:");
            formPanel.add(usernameLabel, gbc);

            gbc.gridx = 1;
            JTextField usernameField = new JTextField();
            usernameField.setPreferredSize(new Dimension(150, usernameField.getPreferredSize().height));
            formPanel.add(usernameField, gbc);

            gbc.gridy++;
            gbc.gridx = 0;
            JLabel hostsLabel = new JLabel("Hosts:");
            formPanel.add(hostsLabel, gbc);

            gbc.gridx = 1;
            MultiCheckComboBoxNoClose.CheckComboBoxItem[] hostItems = {
                    new MultiCheckComboBoxNoClose.CheckComboBoxItem("Host 1", false),
                    new MultiCheckComboBoxNoClose.CheckComboBoxItem("Host 2", false),
                    new MultiCheckComboBoxNoClose.CheckComboBoxItem("Host 3", false),
                    new MultiCheckComboBoxNoClose.CheckComboBoxItem("Host 4", false)
            };
            MultiCheckComboBoxNoClose hostsCombo = new MultiCheckComboBoxNoClose(hostItems);
            hostsCombo.setPreferredSize(new Dimension(150, hostsCombo.getPreferredSize().height));
            formPanel.add(hostsCombo, gbc);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save");
            buttonPanel.add(saveButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            saveButton.addActionListener(e -> {
                String selectedWorkspace = (String) workspaceCombo.getSelectedItem();
                String username = usernameField.getText();
                java.util.List<String> selectedHosts = hostsCombo.getSelectedItems();

                System.out.println("Saved workspace: " + selectedWorkspace);
                System.out.println("Saved username: " + username);
                System.out.println("Saved hosts: " + selectedHosts);
            });

            return mainPanel;
        }
    }
}

class MultiCheckComboBoxNoClose extends JComboBox<MultiCheckComboBoxNoClose.CheckComboBoxItem> {

    public MultiCheckComboBoxNoClose(CheckComboBoxItem[] items) {
        super(items);
        setRenderer(new CheckComboRenderer());
    }

    @Override
    public void setSelectedItem(Object anObject) {
    }

    @Override
    public void setSelectedIndex(int index) {
    }

    @Override
    public void updateUI() {
        super.updateUI();
        SwingUtilities.invokeLater(() -> {
            Object comp = getUI().getAccessibleChild(this, 0);
            if (comp instanceof JPopupMenu) {
                JPopupMenu popup = (JPopupMenu) comp;
                JList<?> list = null;
                if (popup.getComponentCount() > 0 && popup.getComponent(0) instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
                    if (scrollPane.getViewport().getView() instanceof JList) {
                        list = (JList<?>) scrollPane.getViewport().getView();
                    }
                }
                if (list != null) {
                    for (MouseListener ml : list.getMouseListeners()) {
                        list.removeMouseListener(ml);
                    }
                    final JList<?> finalList = list;
                    list.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int index = finalList.locationToIndex(e.getPoint());
                            if (index >= 0) {
                                @SuppressWarnings("unchecked")
                                CheckComboBoxItem item = (CheckComboBoxItem) finalList.getModel().getElementAt(index);
                                item.setSelected(!item.isSelected());
                                finalList.repaint();
                            }
                        }
                    });
                }
            }
        });

        this.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) { }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
        });
    }

    public java.util.List<String> getSelectedItems() {
        java.util.List<String> selected = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            CheckComboBoxItem item = getItemAt(i);
            if (item.isSelected()) {
                selected.add(item.getText());
            }
        }
        return selected;
    }

    private static class CheckComboRenderer implements ListCellRenderer<CheckComboBoxItem> {
        private final JCheckBox checkBox = new JCheckBox();
        private final JLabel label = new JLabel();

        @Override
        public Component getListCellRendererComponent(
                JList<? extends CheckComboBoxItem> list,
                CheckComboBoxItem value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            if (index == -1) {
                label.setText("select host");
                label.setOpaque(true);
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
                return label;
            } else {
                checkBox.setText(value.getText());
                checkBox.setSelected(value.isSelected());
                if (isSelected) {
                    checkBox.setBackground(list.getSelectionBackground());
                    checkBox.setForeground(list.getSelectionForeground());
                } else {
                    checkBox.setBackground(list.getBackground());
                    checkBox.setForeground(list.getForeground());
                }
                return checkBox;
            }
        }
    }

    public static class CheckComboBoxItem {
        private final String text;
        private boolean selected;

        public CheckComboBoxItem(String text, boolean selected) {
            this.text = text;
            this.selected = selected;
        }

        public String getText() {
            return text;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
