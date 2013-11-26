package com.github.wokier.maven.progress;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import notify.MessageType;
import notify.Notify;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Allows to notify the progress of a build in a reactor
 * @author francois wauquier 'wokier'
 * @goal notify-progress
 * @phase validate
 */
public class NotifyProgressMojo extends AbstractMojo
{

   /**
    * Reactor Sorted projects; provided by Maven
    * @parameter expression="${reactorProjects}"
    */
   List<MavenProject> reactorProjects;

   /**
    * A list of every project in this reactor; provided by Maven
    * @parameter expression="${project}"
    */
   MavenProject currentProject;

   /**
    * @see org.apache.maven.plugin.AbstractMojo#execute()
    */
   public void execute() throws MojoExecutionException, MojoFailureException
   {

      final int projectIndex = reactorProjects.indexOf(currentProject) + 1;
      final int reactorProjectsCount = reactorProjects.size();
      final File currentProjectBasedir = currentProject.getBasedir();
      final String message = ProgressUtils.progress(projectIndex, reactorProjectsCount, currentProjectBasedir);

      ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
      singleThreadExecutor.execute(new Runnable()
      {
         public void run()
         {
            Notify.getInstance().notify(MessageType.INFO, "Reactor Progress", message);
         }
      });
      try
      {
         singleThreadExecutor.awaitTermination(1, TimeUnit.SECONDS);

         if (projectIndex == reactorProjectsCount)
         {
            Thread.sleep(1000);
         }
      }
      catch (InterruptedException e)
      {
         throw new MojoExecutionException("Thread sleep error", e);
      }
   }
}
