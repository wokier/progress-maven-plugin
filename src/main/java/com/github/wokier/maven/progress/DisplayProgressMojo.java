package com.github.wokier.maven.progress;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Allows to display the progress of a build in a reactor
 * 
 * @author francois wauquier 'wokier'
 * @goal display-progress
 * @phase validate
 */
public class DisplayProgressMojo extends AbstractMojo {

	private static AtomicInteger counter = new AtomicInteger(0);

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

		int next = counter.incrementAndGet();
		int reactorProjectsCount = reactorProjects.size();
		File currentProjectBasedir = currentProject.getBasedir();

		getLog().info("Reactor Progress: " + ProgressUtils.progress(next, reactorProjectsCount, currentProjectBasedir));
	}

}
