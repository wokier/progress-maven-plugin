package com.github.wokier.maven.progress;

import java.io.File;
import java.util.Arrays;

import notify.MessageType;
import notify.Notify;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class DisplayProgressMojoTest extends AbstractMojoTestCase {

	public void testLookup() throws Exception {

		File testPom = new File("src/test/resources/test-1-pom.xml");

		Mojo mojo = lookupMojo("display-progress", testPom);

		assertNotNull(mojo);
	}


    public void testExecute() throws Exception {
        DisplayProgressMojo displayProgressMojo = spy(new DisplayProgressMojo());
        MavenProject singleProject = mock(MavenProject.class);
        displayProgressMojo.currentProject = singleProject;
        displayProgressMojo.reactorProjects = Arrays.asList(singleProject);
        when(displayProgressMojo.currentProject.getBasedir()).thenReturn(new File(new File(".").getAbsolutePath()));
        displayProgressMojo.execute();

        verify(displayProgressMojo).log(anyString());
    }


	public void testLookupModules() throws Exception {

		File testPom = new File("src/test/resources/test-modules-pom.xml");

		Mojo mojo = lookupMojo("display-progress", testPom);

		assertNotNull(mojo);
    }

}
