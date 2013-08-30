package com.github.wokier.maven.progress;

import java.io.File;
import java.util.List;

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
 */
public class NotifyProgressMojo extends AbstractMojo {

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
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		int projectIndex = reactorProjects.indexOf(currentProject) + 1;
		int reactorProjectsCount = reactorProjects.size();
		File currentProjectBasedir = currentProject.getBasedir();

		Notify.getInstance().notify(MessageType.INFO, "Reactor Progress", ProgressUtils.progress(projectIndex, reactorProjectsCount, currentProjectBasedir));

		if (projectIndex == reactorProjectsCount) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new MojoExecutionException("Thread sleep error", e);
			}
		}
	}
}
