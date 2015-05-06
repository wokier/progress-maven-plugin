
package com.github.wokier.maven.progress;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Abstract Mojo for reporting progress of a build in a reactor
 *
 * @author francois wauquier 'wokier'
 * @phase validate
 */
public abstract class AbstractProgressMojo extends AbstractMojo implements ProgressUpdatedCallback {

    /**
     * Reactor start time in millis is added to file name so it is possible to run multiple builds together.
     */
    static final String PROPERTIES_FILE_NAME_FORMAT = "maven-progress-%s.properties";

    /**
     * Reactor Sorted projects; provided by Maven
     *
     * @parameter expression="${reactorProjects}"
     */
    List<MavenProject> reactorProjects;

    /**
     * A list of every project in this reactor; provided by Maven
     *
     * @parameter expression="${project}"
     */
    MavenProject currentProject;

    /**
     * The current session; provided by Maven
     *
     * @parameter expression="${session}"
     */
    MavenSession session;

    /**
     * The minimum interval that should have elapsed before a new progress notification is sent,
     * default value is 0, i.e. instant notification. The final notification (100%) is always sent.
     *
     * @parameter expression="${progress.minimumNotifyMillis}" default-value="0"
     */
    String minimumNotifyMillis;

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        Date reactorStartTime = session.getStartTime();
        int currentReactorSize = reactorProjects.size();

        File currentProjectBasedir = currentProject.getBasedir();
        String fileName = String.format(PROPERTIES_FILE_NAME_FORMAT, reactorStartTime.getTime());

        ProgressUpdater updater = new ProgressUpdater(System.getProperty("java.io.tmpdir"), fileName);
        updater.updateProgress(currentReactorSize, currentProjectBasedir, this);

    }

    public abstract boolean progressUpdated(final ReactorProgress progress) throws MojoExecutionException;

}
