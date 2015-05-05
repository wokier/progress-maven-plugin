
package com.github.wokier.maven.progress;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Called whenever a new progress update has been determined. Allows for the Mojos to have their own
 * specific update action.
 */
public interface ProgressUpdatedCallback
{

    public void progressUpdated(final ReactorProgress progress) throws MojoExecutionException;

}
