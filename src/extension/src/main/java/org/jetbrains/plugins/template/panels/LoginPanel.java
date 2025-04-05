package org.jetbrains.plugins.template.panels;

import com.crazyllama.llama_remote.common.dto.rest.auth.AuthRequest;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.plugins.template.Registry;
import org.jetbrains.plugins.template.api.APIRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
	private final JTextField emailField;
	private final JTextField serverField;
	private final JLabel errorLabel;
	private final JPasswordField passwordField;

	public LoginPanel() {
		super(new FlowLayout(FlowLayout.CENTER, 0, 50));

		JPanel LOGIN_PANEL = new JPanel(new GridBagLayout());
		LOGIN_PANEL.setPreferredSize(new Dimension(350, 250));
		this.add(LOGIN_PANEL);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = JBUI.insets(5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;

		// Server
		JLabel serverLabel = new JLabel("Server:");
		serverField = new JTextField();
		serverField.setPreferredSize(new Dimension(200, 30));

		LOGIN_PANEL.add(serverLabel, gbc);
		gbc.gridx = 1;
		LOGIN_PANEL.add(serverField, gbc);

		// Email
		gbc.gridy++;
		gbc.gridx = 0;
		JLabel emailLabel = new JLabel("Email:");
		emailField = new JTextField();
		emailField.setPreferredSize(new Dimension(200, 30));

		LOGIN_PANEL.add(emailLabel, gbc);
		gbc.gridx = 1;
		LOGIN_PANEL.add(emailField, gbc);

		// Password
		gbc.gridy++;
		gbc.gridx = 0;
		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(200, 30));

		LOGIN_PANEL.add(passwordLabel, gbc);
		gbc.gridx = 1;
		LOGIN_PANEL.add(passwordField, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		errorLabel = new JLabel();
		errorLabel.setVisible(false);
		errorLabel.setForeground(JBColor.RED);
		LOGIN_PANEL.add(errorLabel, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;

		JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(this::loginButtonPressed);

		JButton signInButton = new JButton("Sign in");

		buttonRow.add(loginButton);
		buttonRow.add(signInButton);

		LOGIN_PANEL.add(buttonRow, gbc);
	}

	// TODO: Do something when server not found / invalid server
	private void loginButtonPressed(ActionEvent e) {
		String username = emailField.getText();
		String password = String.valueOf(passwordField.getPassword());
		String server = serverField.getText().strip();

		String hashedPassword = DigestUtils.sha256Hex(password);
		Registry.host = server;

		AuthRequest authRequest = new AuthRequest(username, hashedPassword);

		AuthRequest.Response response = new APIRequest<>("/auth", "POST", authRequest, AuthRequest.Response.class)
				.getResponse();

		errorLabel.setVisible(true);
		if (response.token == null || response.token.isEmpty()) {
			errorLabel.setText(response.response);
			return;
		}

		errorLabel.setText("Login successful");
	}
}
