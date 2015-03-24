package com.github.wokier.maven.progress;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class DisplayProgressParallelMojoTest extends AbstractMojoTestCase {

	public void testExecute() throws Exception {

		File testPom = new File("src/test/resources/test-1-pom.xml");

		Mojo mojo = lookupMojo("display-progress-parallel", testPom);

		assertNotNull(mojo);

	}

	public void testExecuteModules() throws Exception {

		File testPom = new File("src/test/resources/test-modules-pom.xml");

		Mojo mojo = lookupMojo("display-progress-parallel", testPom);

		assertNotNull(mojo);

    }

}
