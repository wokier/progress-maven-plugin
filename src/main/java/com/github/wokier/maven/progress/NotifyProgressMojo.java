
package com.github.wokier.maven.progress;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import notify.MessageType;
import notify.Notify;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Allows to notify the progress of a build in a reactor.
 *
 * @author francois wauquier 'wokier'
 * @goal notify-progress
 */
public class NotifyProgressMojo extends AbstractProgressMojo
{

    @Override
    public boolean progressUpdated(final ReactorProgress progress) throws MojoExecutionException {

        boolean reactorFinished = progress.getReactorIndex() == progress.getReactorSize();
        long millisSinceLastUpdate = System.currentTimeMillis() - progress.getLastNotified().getTime();

        boolean shouldNotify = reactorFinished || millisSinceLastUpdate >= Long.valueOf(minimumNotifyMillis);

        if (!shouldNotify)
        {
            return false;
        }

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable()
        {
            public void run()
            {
                Notify.getInstance().notify(MessageType.INFO, "Reactor Progress", progress.toString());
            }
        });

        try
        {
            singleThreadExecutor.awaitTermination(1, TimeUnit.SECONDS);

            if (reactorFinished)
            {
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e)
        {
            throw new MojoExecutionException("Thread sleep error", e);
        }

        return true;
    }

}
