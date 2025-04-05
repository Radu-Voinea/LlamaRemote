package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CreateHost implements ToolWindowFactory {

	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
		Content content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false);
		toolWindow.getContentManager().addContent(content);
	}

	@Override
	public boolean shouldBeAvailable(@NotNull Project project) {
		return true;
	}

	private static class MyToolWindow {


		public MyToolWindow(ToolWindow toolWindow) {
		}

		public JPanel getContent() {
			return new JPanel();
		}
	}
}
