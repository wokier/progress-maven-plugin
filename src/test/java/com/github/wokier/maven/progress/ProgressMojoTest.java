package com.github.wokier.maven.progress;

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class ProgressMojoTest extends AbstractMojoTestCase {

    public void testExecute() throws Exception {

	File testPom = new File("src/test/resources/test-1-pom.xml");

	Mojo mojo = lookupMojo("display-progress", testPom);

	assertNotNull(mojo);

    }

    public void testExecuteModules() throws Exception {

	File testPom = new File("src/test/resources/test-modules-pom.xml");

	Mojo mojo = lookupMojo("display-progress", testPom);

	assertNotNull(mojo);

    }

}
