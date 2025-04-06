package org.jetbrains.plugins.template.panels;


import com.crazyllama.llama_remote.common.dto.rest.host.HostInfoRequest;
import com.crazyllama.llama_remote.common.dto.rest.host.HostsListRequest;
import com.crazyllama.llama_remote.common.dto.rest.workspace.WorkspaceListRequest;
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
import java.util.List;

import static org.jetbrains.plugins.template.api.WorkspaceAPI.getWorkspaceName;

public class AddUserPanel extends JPanel {
	private final long workspace_id;

	private List<Long> workspaceIDList;
	private String[] workspaceOptions;
	private JComboBox<String> workspaceCombo;

	public AddUserPanel(long workspace_id) {
		super(new BorderLayout());
		this.workspace_id = workspace_id;
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = JBUI.insets(5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel workspaceLabel = new JLabel("Workspaces:");
		formPanel.add(workspaceLabel, gbc);

		gbc.gridx = 1;

		getWorkspaces();
		getWorkspacesNames();
		workspaceCombo = new JComboBox<>(workspaceOptions);
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

		this.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton saveButton = new JButton("Save");

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this::cancelButtonPressed);

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

		saveButton.addActionListener(e -> {
			String selectedWorkspace = (String) workspaceCombo.getSelectedItem();
			String username = usernameField.getText();
			java.util.List<String> selectedHosts = hostsCombo.getSelectedItems();

			System.out.println("Saved workspace: " + selectedWorkspace);
			System.out.println("Saved username: " + username);
			System.out.println("Saved hosts: " + selectedHosts);
		});
	}

	private void cancelButtonPressed(ActionEvent e) {
		LLamaWindowFactory.instance.updateToolWindowContent(new WorkspacesPanel());
	}

	private void getWorkspacesNames() {
		workspaceOptions = new String[workspaceIDList.size()];

		int index = 0;
		for (Long id : workspaceIDList) {
			workspaceOptions[index] = getWorkspaceName(id);
		}
	}

	private void getWorkspaces() {
		WorkspaceListRequest.Response response = new APIRequest<>("/workspace/list_owner", "GET",
				null, WorkspaceListRequest.Response.class)
				.getResponse();

		System.out.println("RESPONSE: " + response);
		System.out.println("RESPONSEsd: " + response.response);

		workspaceIDList = response.workspaces;
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

	private class MultiCheckComboBoxNoClose extends JComboBox<MultiCheckComboBoxNoClose.CheckComboBoxItem> {

		public MultiCheckComboBoxNoClose(CheckComboBoxItem[] items) {
			super(items);
			setRenderer(new CheckComboRenderer());
			updateUI();
			refreshHosts();
		}

		private void refreshHosts() {
			removeAllItems(); // Clear old items
			String[] hostNames = getHostsNames(workspaceIDList.get(workspaceCombo.getSelectedIndex()));
			for (String name : hostNames) {
				addItem(new CheckComboBoxItem(name, false));
			}
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
					refreshHosts();
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
