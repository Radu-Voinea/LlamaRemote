package org.jetbrains.plugins.template.toolWindow;

import com.crazyllama.llama_remote.common.dto.rest.auth.TokenCheckRequest;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.template.Registry;
import org.jetbrains.plugins.template.api.APIRequest;
import org.jetbrains.plugins.template.panels.IRefreshableComponent;
import org.jetbrains.plugins.template.panels.LoginPanel;
import org.jetbrains.plugins.template.panels.WorkspacesPanel;

import javax.swing.*;
import java.awt.*;

public class LLamaWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
	public static LLamaWindowFactory instance;

	public static Project project;
	public static ToolWindow toolWindow;
	public static LlamaWindow llamaWindow;

	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		LLamaWindowFactory.instance = this;
		LLamaWindowFactory.project = project;
		LLamaWindowFactory.toolWindow = toolWindow;

		LLamaWindowFactory.llamaWindow = new LlamaWindow(toolWindow);
		Content content = ContentFactory.getInstance().createContent(llamaWindow.getContent(), null, false);
		toolWindow.getContentManager().addContent(content);
	}

	public void updateToolWindowContent(JComponent component) {
		if(component == null){
			System.out.println("UPDATE COMPONENT IS NULL");
			return;
		}

		llamaWindow.setActiveComponent(component);

		toolWindow.getContentManager().removeAllContents(true);
		Content content = ContentFactory.getInstance().createContent(llamaWindow.getContent(), null, false);

		toolWindow.getContentManager().addContent(content);
	}

	@Override
	public boolean shouldBeAvailable(@NotNull Project project) {
		return true;
	}

	@Setter
	public static class LlamaWindow {
		private JComponent activeComponent;

		public LlamaWindow(ToolWindow toolWindow) {
			System.out.println("Verify registry");
			if (Registry.host == null || Registry.host.isEmpty() || Registry.token == null || Registry.token.isEmpty()) {
				System.out.println("One of them don't exist");
				this.activeComponent = new LoginPanel();
				return;
			}

			//TODO (HIGH): Not working as expected
			TokenCheckRequest.Response response = new APIRequest<>(
					"/token/check", "POST", null,
					TokenCheckRequest.Response.class)
					.getResponse();

			System.out.println(Registry.host);
			System.out.println(Registry.token);

			System.out.println("Response status: " + response.status);

			if (!response.status) {
				this.activeComponent = new LoginPanel();
				return;
			}

			this.activeComponent = new WorkspacesPanel();
		}

		public JComponent getContent() {
			JPanel mainPanel = new JPanel(new BorderLayout());

			if (activeComponent instanceof IRefreshableComponent activeRefreshableComponent) {
				JButton refreshButton = new JButton("Refresh");
				refreshButton.addActionListener(ignored -> activeRefreshableComponent.refresh());

				JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				topPanel.add(refreshButton);
				mainPanel.add(topPanel, BorderLayout.NORTH);
			}

			mainPanel.add(activeComponent, BorderLayout.CENTER);

			return mainPanel;
		}
	}
}
