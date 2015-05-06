
package com.github.wokier.maven.progress;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Called whenever a new progress update has been determined. Allows for the Mojos to have their own
 * specific update action.
 */
public interface ProgressUpdatedCallback
{

    /**
     * Contains the updated progress.
     *
     * @param progress the updated progress.
     * @return <code>true</code> when a progress notification was sent, <code>false</code> otherwise
     * @throws MojoExecutionException whenever processing the progress update failed
     */
    public boolean progressUpdated(final ReactorProgress progress) throws MojoExecutionException;

}
