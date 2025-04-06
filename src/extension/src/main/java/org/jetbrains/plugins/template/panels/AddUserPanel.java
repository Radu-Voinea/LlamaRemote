package org.jetbrains.plugins.template.panels;


import com.crazyllama.llama_remote.common.dto.rest.host.HostInfoRequest;
import com.crazyllama.llama_remote.common.dto.rest.host.HostsListRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceAddUserRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceCreateRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceGetUsersRequest;
import com.intellij.util.ui.JBUI;
import com.raduvoinea.utils.message_builder.MessageBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.plugins.template.api.APIRequest;
import org.jetbrains.plugins.template.toolWindow.LLamaWindowFactory;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserPanel extends JPanel {
	private final long workspace_id;
	private final JTextField usernameField;
	private final Map<String, Long> hostNameToID = new HashMap<>();
	private JList<String> userList;
	private MultiCheckComboBoxNoClose hostsCombo;

	public AddUserPanel(long workspace_id) {
		super(new BorderLayout());
		this.workspace_id = workspace_id;

		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = JBUI.insets(5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridy = 1;
		gbc.gridx = 0;

		JPanel userListPanel = new JPanel(new BorderLayout());
		userListPanel.setBorder(BorderFactory.createTitledBorder("Current Users"));

		String[] currentUsers = getUsersForWorkspace();
		userList = new JList<>(currentUsers);
		userList.setVisibleRowCount(5); // adjust as needed
		userListPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

// Add above the formPanel
		this.add(userListPanel, BorderLayout.NORTH);


		JLabel usernameLabel = new JLabel("Username:");
		formPanel.add(usernameLabel, gbc);

		gbc.gridx = 1;
		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(150, usernameField.getPreferredSize().height));
		formPanel.add(usernameField, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		JLabel hostsLabel = new JLabel("Hosts:");
		formPanel.add(hostsLabel, gbc);

		gbc.gridx = 1;
		getHostsNames();
		createHostsCombo();
		formPanel.add(hostsCombo, gbc);

		this.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this::saveButtonPressed);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this::cancelButtonPressed);

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

	}

	private void cancelButtonPressed(ActionEvent e) {
		LLamaWindowFactory.instance.updateToolWindowContent(new WorkspacesPanel());
	}

	private void saveButtonPressed(ActionEvent e) {
		String username = usernameField.getText();
		List<String> selectedHosts = hostsCombo.getSelectedItems();
		List<Long> hostsIDs = new ArrayList<>(selectedHosts.size());
		for (String selectedHost : selectedHosts) {
			hostsIDs.add(hostNameToID.get(selectedHost));
		}


		WorkspaceGetUsersRequest.Response before = new APIRequest<>(
				new MessageBuilder("/workspace/{id}/get_users")
						.parse("id", workspace_id)
						.parse()
				, "GET",
				null, WorkspaceGetUsersRequest.Response.class)
				.getResponse();

		System.out.println("BEFORE ADD USER " + before.usernames);

		WorkspaceAddUserRequest request = new WorkspaceAddUserRequest(username, hostsIDs);

		WorkspaceCreateRequest.Response response = new APIRequest<>(
				new MessageBuilder("/workspace/{id}/add_user")
						.parse("id", workspace_id)
						.parse()
				, "POST",
				request, WorkspaceCreateRequest.Response.class)
				.getResponse();

		System.out.println("RESPONSE ADD USER " + response);
		System.out.println("RESPONSE ADD USER " + response.response);

		WorkspaceGetUsersRequest.Response eqweq = new APIRequest<>(
				new MessageBuilder("/workspace/{id}/get_users")
						.parse("id", workspace_id)
						.parse()
				, "GET",
				null, WorkspaceGetUsersRequest.Response.class)
				.getResponse();

		System.out.println("AFTER ADD USER " + eqweq.usernames);


		LLamaWindowFactory.instance.updateToolWindowContent(new WorkspacesPanel());

	}

	private String[] getUsersForWorkspace() {
		WorkspaceGetUsersRequest.Response response = new APIRequest<>(
				new MessageBuilder("/workspace/{id}/get_users")
						.parse("id", workspace_id)
						.parse()
				, "GET",
				null, WorkspaceGetUsersRequest.Response.class)
				.getResponse();

		String[] users = new String[response.usernames.size()];
		int index = 0;
		for (String username : response.usernames) {
			users[index] = username;
			index++;
		}

		return users;
	}

	private void getHostsNames() {
		HostsListRequest.Response response = new APIRequest<>(
				new MessageBuilder("/host/{workspace}/list")
						.parse("workspace", workspace_id)
						.parse()
				, "GET", null, HostsListRequest.Response.class)
				.getResponse();

		for (Long host : response.hosts) {
			HostInfoRequest.Response infoResponse = new APIRequest<>(
					new MessageBuilder("/host/{workspace}/{host}")
							.parse("workspace", workspace_id)
							.parse("host", host)
							.parse(),
					"GET", null, HostInfoRequest.Response.class
			).getResponse();

			if (!infoResponse.response.equals("OK")) {
				System.out.println("GET HOME INFO REQUEST FAILED FOR " + host);
				continue;
			}

			hostNameToID.put(infoResponse.name, host);
		}
	}

	private void createHostsCombo() {
		MultiCheckComboBoxNoClose.CheckComboBoxItem[] hostItems = new MultiCheckComboBoxNoClose.CheckComboBoxItem[hostNameToID.size()];

		int index = 0;
		for (String hostname : hostNameToID.keySet()) {
			hostItems[index] = new MultiCheckComboBoxNoClose.CheckComboBoxItem(hostname, false);
		}
		hostsCombo = new MultiCheckComboBoxNoClose(hostItems);

		hostsCombo.setPreferredSize(new Dimension(150, hostsCombo.getPreferredSize().height));
	}

	private class MultiCheckComboBoxNoClose extends JComboBox<MultiCheckComboBoxNoClose.CheckComboBoxItem> {

		public MultiCheckComboBoxNoClose(CheckComboBoxItem[] items) {
			super(items);
			setRenderer(new CheckComboRenderer());
			updateUI();
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
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				}

				@Override
				public void popupMenuCanceled(PopupMenuEvent e) {
				}

				@Override
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				}
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

		@Getter
		public static class CheckComboBoxItem {
			private final String text;
			@Setter
			private boolean selected;

			public CheckComboBoxItem(String text, boolean selected) {
				this.text = text;
				this.selected = selected;
			}

			@Override
			public String toString() {
				return text;
			}
		}
	}

}
