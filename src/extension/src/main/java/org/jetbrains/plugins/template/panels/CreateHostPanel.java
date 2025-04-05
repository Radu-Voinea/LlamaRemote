package org.jetbrains.plugins.template.panels;

import javax.swing.*;
import java.awt.*;

public class CreateHostPanel extends JPanel {

	public CreateHostPanel() {
		super(new BorderLayout());

		JPanel pictogramPanel = new JPanel();
		pictogramPanel.setLayout(new BoxLayout(pictogramPanel, BoxLayout.Y_AXIS));

		JPanel workspacePanel = new JPanel();
		workspacePanel.setLayout(new BoxLayout(workspacePanel, BoxLayout.Y_AXIS));
		workspacePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10, 10, 10, 10),
				BorderFactory.createEtchedBorder()
		));
		workspacePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel titleLabel = new JLabel("Workspace");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(titleLabel);
		workspacePanel.add(Box.createVerticalStrut(5));

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(nameLabel);

		JTextField nameField = new JTextField();
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		Dimension namePrefSize = nameField.getPreferredSize();
		nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, namePrefSize.height));
		workspacePanel.add(nameField);
		workspacePanel.add(Box.createVerticalStrut(5));

		JLabel hostLabel = new JLabel("Host:");
		hostLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(hostLabel);

		JTextField hostField = new JTextField();
		hostField.setAlignmentX(Component.LEFT_ALIGNMENT);
		Dimension hostPrefSize = hostField.getPreferredSize();
		hostField.setMaximumSize(new Dimension(Integer.MAX_VALUE, hostPrefSize.height));
		workspacePanel.add(hostField);
		workspacePanel.add(Box.createVerticalStrut(5));

		JLabel portLabel = new JLabel("Port:");
		portLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(portLabel);

		JTextField portField = new JTextField();
		portField.setAlignmentX(Component.LEFT_ALIGNMENT);
		Dimension portPrefSize = portField.getPreferredSize();
		portField.setMaximumSize(new Dimension(Integer.MAX_VALUE, portPrefSize.height));
		workspacePanel.add(portField);
		workspacePanel.add(Box.createVerticalStrut(5));

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(usernameLabel);

		JTextField usernameField = new JTextField();
		usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		Dimension userPrefSize = usernameField.getPreferredSize();
		usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, userPrefSize.height));
		workspacePanel.add(usernameField);
		workspacePanel.add(Box.createVerticalStrut(10));

		JLabel pkLabel = new JLabel("Private Key:");
		pkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(pkLabel);

		JTextArea privateKeyArea = new JTextArea(6, 20);
		privateKeyArea.setLineWrap(true);
		privateKeyArea.setWrapStyleWord(true);
		privateKeyArea.setAlignmentX(Component.LEFT_ALIGNMENT);

		Dimension pkPrefSize = privateKeyArea.getPreferredSize();
		privateKeyArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, pkPrefSize.height));

		JScrollPane privateKeyScroll = new JScrollPane(
				privateKeyArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		privateKeyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		workspacePanel.add(privateKeyScroll);

		pictogramPanel.add(workspacePanel);

		JScrollPane scrollPane = new JScrollPane(
				pictogramPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		this.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton saveButton = new JButton("Save");
		buttonPanel.add(saveButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

}
