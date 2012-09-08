package com.github.wokier.maven.progress;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * Allows to display the progress of a build in a reactor
 * 
 * @author francois wauquier 'wokier'
 * @goal display-progress
 * @phase validate
 */
public class ProgressMojo extends AbstractMojo {

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

    getLog().info(
        "Reactor Progress: " + projectIndex + "/" + reactorProjectsCount + " ("
            + StringUtils.left((projectIndex * 1.0 / reactorProjectsCount) * 100 + "", 5) + "%) "
            + currentProject.getBasedir());
  }

}
