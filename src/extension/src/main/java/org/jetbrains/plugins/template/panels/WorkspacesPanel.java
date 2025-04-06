package org.jetbrains.plugins.template.panels;

import com.crazyllama.llama_remote.common.dto.rest.host.HostInfoRequest;
import com.crazyllama.llama_remote.common.dto.rest.host.HostsListRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
import com.raduvoinea.utils.message_builder.MessageBuilder;
import org.jetbrains.plugins.template.api.APIRequest;
import org.jetbrains.plugins.template.api.WorkspaceAPI;
import org.jetbrains.plugins.template.toolWindow.LLamaWindowFactory;

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

			addExpandableSection(contentPanel, workspaceName, workspaceID, getHostsNames(workspaceID));
		}

		JButton addWorkspaceButton = new JButton("Add new workspace");
		addWorkspaceButton.addActionListener(this::AddNewWorkspaceButtonPressed);
		contentPanel.add(addWorkspaceButton);

		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private String[] getHostsNames(Long workspaceID) {
		HostsListRequest.Response response = new APIRequest<>(
				new MessageBuilder("/host/{workspace}/list")
						.parse("workspace", workspaceID)
						.parse()
				, "GET", null, HostsListRequest.Response.class)
				.getResponse();

		if (response.hosts == null) {
			return new String[0];
		}

		String[] toReturn = new String[response.hosts.size()];
		int index = 0;
		for (Long host : response.hosts) {
			HostInfoRequest.Response infoResponse = new APIRequest<>(
					new MessageBuilder("/host/{workspace}/{host}")
							.parse("workspace", workspaceID)
							.parse("host", host)
							.parse(),
					"GET", null, HostInfoRequest.Response.class
			).getResponse();

			if (!infoResponse.response.equals("OK")) {
				System.out.println("GET HOME INFO REQUEST FAILED FOR " + host);
				continue;
			}
			toReturn[index] = infoResponse.name;
		}


		return toReturn;
	}

	private void addExpandableSection(JPanel parent, String title, Long workspaceID, String[] items) {
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

		JButton addNewUserButton = createEntryButton("Add new user");
		addNewUserButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		addNewUserButton.addActionListener((e) -> AddNewUserButtonPressed(e, workspaceID));

		JPanel dropdownPanel = new JPanel();
		dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.Y_AXIS));
		dropdownPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
		dropdownPanel.setVisible(false);

		for (String item : items) {
			JButton itemButton = createEntryButton(item);
			itemButton.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
			dropdownPanel.add(itemButton);
		}

		JButton addHostButton = new JButton("Add new host");
		addHostButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		addHostButton.addActionListener((e) -> AddNewHostButtonPressed(e, workspaceID));
		dropdownPanel.add(addHostButton);

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
		headerPanel.add(addNewUserButton);
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

	private void AddNewUserButtonPressed(ActionEvent e, Long workspaceID) {
		WorkspaceListRequest.Response response = new APIRequest<>("/workspace/list_owner", "GET",
				null, WorkspaceListRequest.Response.class)
				.getResponse();

		if (response.workspaces.contains(workspaceID)) {
			LLamaWindowFactory.instance.updateToolWindowContent(new AddUserPanel(workspaceID));
		}
	}

	private void AddNewHostButtonPressed(ActionEvent e, Long workspaceID) {
		LLamaWindowFactory.instance.updateToolWindowContent(new CreateHostPanel(workspaceID));
	}

	private void AddNewWorkspaceButtonPressed(ActionEvent e) {
		LLamaWindowFactory.instance.updateToolWindowContent(new CreateWorkspacePanel());
	}
}
