package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.template.panels.CreateHostPanel;
import org.jetbrains.plugins.template.panels.IRefreshableComponent;

import javax.swing.*;
import java.awt.*;

public class LLamaWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {

	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		LlamaWindow llamaWindow = new LlamaWindow(toolWindow);
		Content content = ContentFactory.getInstance().createContent(llamaWindow.getContent(), null, false);
		toolWindow.getContentManager().addContent(content);
	}

	@Override
	public boolean shouldBeAvailable(@NotNull Project project) {
		return true;
	}

	private static class LlamaWindow {
		private final JComponent activeComponent;

		public LlamaWindow(ToolWindow toolWindow) {
//			this.activeComponent = new WorkspacesPanel();
			this.activeComponent = new CreateHostPanel();
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
