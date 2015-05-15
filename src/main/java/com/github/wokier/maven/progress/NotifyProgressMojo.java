package com.github.wokier.maven.progress;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import notify.MessageType;
import notify.Notify;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Allows to notify the progress of a build in a reactor
 *
 * @author francois wauquier 'wokier'
 * @goal notify-progress
 * @phase validate
 * @threadSafe
 */
public class NotifyProgressMojo extends AbstractMojo {

    private static AtomicLong lastNotification = new AtomicLong();

    /**
     * The minimum interval that should have elapsed before a new progress notification is sent, default value is 0, i.e. instant notification. The final notification (100%) is always sent.
     *
     * @parameter expression="${progress.minimumNotifyMillis}" default-value="0"
     */
    String minimumNotifyMillis;

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

    Notify notify = Notify.getInstance();

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        int projectIndex = reactorProjects.indexOf(currentProject) + 1;
        int reactorProjectsCount = reactorProjects.size();
        File currentProjectBasedir = currentProject.getBasedir();

        boolean reactorFinished = projectIndex == reactorProjectsCount;
        long millisSinceLastNotification = System.currentTimeMillis() - lastNotification.get();

        if (reactorFinished || millisSinceLastNotification >= Long.valueOf(minimumNotifyMillis)) {
            notify.notify(MessageType.INFO, "Reactor Progress", ProgressUtils.progress(projectIndex, reactorProjectsCount, currentProjectBasedir));
            lastNotification.set(System.currentTimeMillis());
        }
        if (reactorFinished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new MojoExecutionException("Thread sleep error", e);
            }
        }
    }
}
