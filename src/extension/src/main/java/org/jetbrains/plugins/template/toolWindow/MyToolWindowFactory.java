package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.plugins.template.panels.LoginPanel;
import org.jetbrains.plugins.template.services.MyProjectService;

import javax.swing.*;

public class MyToolWindowFactory implements ToolWindowFactory {

	private static final Logger LOG = Logger.getInstance(MyToolWindowFactory.class);

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
			return new LoginPanel();
		}
	}
}
