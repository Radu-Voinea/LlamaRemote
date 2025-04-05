package org.jetbrains.plugins.template.panels;

import com.crazyllama.llama_remote.common.dto.rest.auth.AuthRequest;
import com.google.gson.Gson;
import com.raduvoinea.utils.message_builder.MessageBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

	// TODO: Move this all of this in a separate class
	// TODO: Do something when server not found / invalid server
	private void loginButtonPressed(ActionEvent e) {
		String username = emailField.getText();
		String password = String.valueOf(passwordField.getPassword());
		String server = serverField.getText();

		String hashedPassword = DigestUtils.sha256Hex(password);

		Gson gson = new Gson();
		AuthRequest authRequest = new AuthRequest(username, hashedPassword);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(new MessageBuilder("http://{server}/auth")
						.parse("server", server.strip())
						.parse()
				))
				.header("Content-Type", "application/json")
				.method("GET", HttpRequest.BodyPublishers.ofString(gson.toJson(authRequest)))
				.build();


		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ex) {
			throw new RuntimeException(ex);
		}

		AuthRequest.Response authResponse = gson.fromJson(response.body(), AuthRequest.Response.class);

		errorLabel.setVisible(true);
		if (authResponse.token == null || authResponse.token.isEmpty()) {
			errorLabel.setText(authResponse.response);
			return;
		}

		errorLabel.setText("Login successful");
	}
}
