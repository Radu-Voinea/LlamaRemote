package org.jetbrains.plugins.template.panels;

import com.crazyllama.llama_remote.common.dto.rest.auth.AuthRequest;
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

		JPanel LOGIN_PANEL = new JPanel();
		LOGIN_PANEL.setLayout(new BoxLayout(LOGIN_PANEL, BoxLayout.Y_AXIS));
		LOGIN_PANEL.setPreferredSize(new java.awt.Dimension(300, 250)); // Width x Height

		this.add(LOGIN_PANEL);

		JLabel serverLabel = new JLabel("Server");
		serverField = new JTextField();
		JLabel emailLabel = new JLabel("email");
		emailField = new JTextField();
		JLabel passwordLabel = new JLabel("password");
		passwordField = new JPasswordField();
		errorLabel = new JLabel();
		errorLabel.setVisible(false);

		serverField.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
		emailField.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
		passwordField.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));

		LOGIN_PANEL.add(serverLabel);
		LOGIN_PANEL.add(serverField);
		LOGIN_PANEL.add(emailLabel);
		LOGIN_PANEL.add(emailField);
		LOGIN_PANEL.add(passwordLabel);
		LOGIN_PANEL.add(passwordField);
		LOGIN_PANEL.add(errorLabel);

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(this::loginButtonPressed);

		LOGIN_PANEL.add(loginButton);
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
