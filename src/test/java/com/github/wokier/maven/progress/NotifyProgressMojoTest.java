package com.github.wokier.maven.progress;

import java.io.File;
import java.util.Arrays;

import notify.MessageType;
import notify.Notify;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class NotifyProgressMojoTest extends AbstractMojoTestCase {

	public void testLookup() throws Exception {

		File testPom = new File("src/test/resources/test-1-pom.xml");

		Mojo mojo = lookupMojo("notify-progress", testPom);

		assertNotNull(mojo);

	}

   public void testExecute() throws Exception {
       NotifyProgressMojo notifyProgressMojo = new NotifyProgressMojo();
       MavenProject singleProject = mock(MavenProject.class);
       notifyProgressMojo.currentProject = singleProject;
       notifyProgressMojo.reactorProjects = Arrays.asList(singleProject);
       notifyProgressMojo.notify = mock(Notify.class);
       notifyProgressMojo.execute();

       verify(notifyProgressMojo.notify).notify(eq(MessageType.INFO),eq("Reactor Progress"), anyString());
   }


	public void testLookupModules() throws Exception {

		File testPom = new File("src/test/resources/test-modules-pom.xml");

		Mojo mojo = lookupMojo("notify-progress", testPom);

		assertNotNull(mojo);

	}

    public void testExecuteModules() throws Exception {
        NotifyProgressMojo notifyProgressMojo = new NotifyProgressMojo();
        MavenProject project0 = mock(MavenProject.class);
        MavenProject project1 = mock(MavenProject.class);
        MavenProject project2 = mock(MavenProject.class);
        notifyProgressMojo.reactorProjects = Arrays.asList(project0, project1, project2);
        notifyProgressMojo.minimumNotifyMillis = "0";
        notifyProgressMojo.notify = mock(Notify.class);

        notifyProgressMojo.currentProject = project0;
        notifyProgressMojo.execute();

        notifyProgressMojo.currentProject = project1;
        notifyProgressMojo.execute();

        notifyProgressMojo.currentProject = project2;
        notifyProgressMojo.execute();


        verify(notifyProgressMojo.notify, times(3)).notify(eq(MessageType.INFO),eq("Reactor Progress"), anyString());
    }

    public void testExecuteModulesAvoidSpam() throws Exception {
        NotifyProgressMojo notifyProgressMojo = new NotifyProgressMojo();
        MavenProject project0 = mock(MavenProject.class);
        MavenProject project1 = mock(MavenProject.class);
        MavenProject project2 = mock(MavenProject.class);
        notifyProgressMojo.reactorProjects = Arrays.asList(project0, project1, project2);
        notifyProgressMojo.minimumNotifyMillis = "1000";
        notifyProgressMojo.notify = mock(Notify.class);

        notifyProgressMojo.currentProject = project0;
        notifyProgressMojo.execute();

        notifyProgressMojo.currentProject = project1;
        notifyProgressMojo.execute();

        notifyProgressMojo.currentProject = project2;
        notifyProgressMojo.execute();


        verify(notifyProgressMojo.notify, times(2)).notify(eq(MessageType.INFO),eq("Reactor Progress"), anyString());
        verifyNoMoreInteractions(notifyProgressMojo.notify);
    }

}
