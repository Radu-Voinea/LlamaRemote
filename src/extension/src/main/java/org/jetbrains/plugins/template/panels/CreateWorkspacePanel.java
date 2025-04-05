package org.jetbrains.plugins.template.panels;

import javax.swing.*;
import java.awt.*;

public class CreateWorkspacePanel extends JPanel {

	public CreateWorkspacePanel() {
		super(new BorderLayout());

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
		this.add(workspacePanel, BorderLayout.CENTER);

		// Adăugăm butonul "Save" în partea de jos
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton saveButton = new JButton("Save");
		buttonPanel.add(saveButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
}
