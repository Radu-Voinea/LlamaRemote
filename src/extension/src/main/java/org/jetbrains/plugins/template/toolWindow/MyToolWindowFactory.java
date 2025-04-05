package org.jetbrains.plugins.template.toolWindow;

import com.crazyllama.llama_remote.common.dto.rest.auth.AuthRequest;
import com.google.gson.Gson;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.raduvoinea.utils.message_builder.MessageBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.plugins.template.MyBundle;
import org.jetbrains.plugins.template.services.MyProjectService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MyToolWindowFactory implements ToolWindowFactory {

    private static final Logger LOG = Logger.getInstance(MyToolWindowFactory.class);

    private static JPanel CONTAINER_PANEL = null;
    private static JPanel LOGIN_PANEL = null;
    private static JTextField emailField = null;
    private static JTextField serverField = null;
    private static JLabel errorLabel = null;
    private static JPasswordField passwordField = null;

    public MyToolWindowFactory() {
        LOG.warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.");
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
        Content content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public boolean shouldBeAvailable(Project project) {
        return true;
    }

    private static class MyToolWindow {

        private final MyProjectService service;

        public MyToolWindow(ToolWindow toolWindow) {
            this.service = ServiceManager.getService(toolWindow.getProject(), MyProjectService.class);
        }

        public JPanel getContent() {
            if(CONTAINER_PANEL != null){
                return CONTAINER_PANEL;
            }

            CONTAINER_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50)); // Centered with some vertical padding
            LOGIN_PANEL = new JPanel();
            LOGIN_PANEL.setLayout(new BoxLayout(LOGIN_PANEL, BoxLayout.Y_AXIS));
            LOGIN_PANEL.setPreferredSize(new java.awt.Dimension(300, 250)); // Width x Height

            CONTAINER_PANEL.add(LOGIN_PANEL);

            JLabel serverLabel = new JLabel("Server");
            serverField = new JTextField();
            JLabel emailLabel = new JLabel("email");
            emailField = new JTextField();
            JLabel passwordLabel = new JLabel("password");
            passwordField = new JPasswordField();
            errorLabel= new JLabel();
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
            loginButton.addActionListener(MyToolWindowFactory::loginButtonPressed);

            LOGIN_PANEL.add(loginButton);
            return CONTAINER_PANEL;
        }
    }

    private static void loginButtonPressed(ActionEvent e){
        String username = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String server = serverField.getText();

        System.out.println("PASSWORD: " + password);

        String hashedPassword = DigestUtils.sha256Hex(password);

        String url = new MessageBuilder("http://{server}/get?username={username}&password={password}")
                .parse("server", server)
                .parse("username", username)
                .parse("password", hashedPassword)
                .parse();


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> stringResponse;
        try {
            stringResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }

        Gson gson = new Gson();
        AuthRequest.Response authResponse = gson.fromJson(stringResponse.body(), AuthRequest.Response.class);

        errorLabel.setVisible(true);
        if(authResponse.token == null || authResponse.token.isEmpty()){
            errorLabel.setText(authResponse.response);
            return;
        }

        errorLabel.setText("Login successful");
    }

    public static JTextField getTextFieldFromPanel(JPanel panel, String fieldName) {
        for (java.awt.Component component : panel.getComponents()) {
            if (component instanceof JTextField && component.getName().equals(fieldName)) {
                return (JTextField) component;
            }
        }
        return null; // Return null if no JTextField is found
    }
}
