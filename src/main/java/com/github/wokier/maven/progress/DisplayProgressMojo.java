
package com.github.wokier.maven.progress;


/**
 * Allows to display the progress of a build in a reactor.
 *
 * @author francois wauquier 'wokier'
 * @goal display-progress
 */
public class DisplayProgressMojo extends AbstractProgressMojo {

    @Override
    public void progressUpdated(ReactorProgress progress) {

        getLog().info("Reactor Progress: " + progress.toString());

    }
}
