package org.jetbrains.plugins.template.toolWindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.plugins.template.MyBundle;
import org.jetbrains.plugins.template.services.MyProjectService;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            JBPanel<?> panel = new JBPanel<>();

            JBLabel label = new JBLabel(MyBundle.message("randomLabel", "?"));
            panel.add(label);

            JButton button = new JButton(MyBundle.message("shuffle"));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    label.setText(MyBundle.message("randomLabel", service.getRandomNumber()));
                }
            });

            panel.add(button);
            return panel;
        }
    }
}
