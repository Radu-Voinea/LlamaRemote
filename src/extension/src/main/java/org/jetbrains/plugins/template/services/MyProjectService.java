package org.jetbrains.plugins.template.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.template.MyBundle;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Service(Service.Level.PROJECT)
public final class MyProjectService {

    private static final Logger LOG = Logger.getInstance(MyProjectService.class);
    private final Random random = new Random();

    public MyProjectService(@NotNull Project project) {
        LOG.info(MyBundle.message("projectService", project.getName()));
        LOG.warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.");
    }

    public int getRandomNumber() {
        return random.nextInt(100) + 1; // Generates a number between 1 and 100
    }
}
