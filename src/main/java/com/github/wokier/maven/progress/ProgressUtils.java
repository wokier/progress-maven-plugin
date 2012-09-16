package com.github.wokier.maven.progress;

import java.io.File;

import org.codehaus.plexus.util.StringUtils;

public class ProgressUtils {

    public static String progress(int projectIndex, int reactorProjectsCount, File currentProjectBasedir) {
	return projectIndex + "/" + reactorProjectsCount + " (" + StringUtils.left((projectIndex * 1.0 / reactorProjectsCount) * 100 + "", 5) + "%) " + currentProjectBasedir;
    }

}
