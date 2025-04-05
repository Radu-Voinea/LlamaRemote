package org.jetbrains.plugins.template.panels;

import org.jetbrains.plugins.template.api.WorkspaceAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WorkspacesPanel extends JScrollPane implements IRefreshableComponent {

	private final JPanel contentPanel;

	public WorkspacesPanel() {
		super(null,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		this.setViewportView(contentPanel);
		refresh();
	}

	public void refresh() {
		contentPanel.removeAll();

		for (Long workspaceID : WorkspaceAPI.getWorkspaces()) {
			String workspaceName = WorkspaceAPI.getWorkspaceName(workspaceID);

			addExpandableSection(contentPanel, workspaceName, new String[]{"service-a", "service-b"});
		}

		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void addExpandableSection(JPanel parent, String title, String[] items) {
		JPanel sectionPanel = new JPanel();
		sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
		sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		String collapsedPrefix = "▶ ";
		String expandedPrefix = "▼ ";

		JButton headerButton = createEntryButton(collapsedPrefix + title);
		headerButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel dropdownPanel = new JPanel();
		dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.Y_AXIS));
		dropdownPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
		dropdownPanel.setVisible(false);

		for (String item : items) {
			JButton itemButton = createEntryButton(item);
			itemButton.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
			dropdownPanel.add(itemButton);
		}

		Action toggleDropdown = new AbstractAction() {
			private boolean isExpanded = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				isExpanded = !isExpanded;
				dropdownPanel.setVisible(isExpanded);
				headerButton.setText((isExpanded ? expandedPrefix : collapsedPrefix) + title);
				sectionPanel.revalidate();
			}
		};

		headerButton.addActionListener(toggleDropdown);

		headerPanel.add(headerButton);
		sectionPanel.add(headerPanel);
		sectionPanel.add(dropdownPanel);
		parent.add(sectionPanel);
	}

	private JButton createEntryButton(String text) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);

		return button;
	}
}
