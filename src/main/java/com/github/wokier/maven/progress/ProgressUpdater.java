
package com.github.wokier.maven.progress;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Updates the progress of a reactor by storing and retrieving the progress from a properties file.
 * Because the plugin can run multithreaded in several classloaders a file is used to record state.
 *
 * The file is not placed in a target directory because it sometimes would get deleted when the
 * reactor is run with a 'clean' goal in combination with '--resume-from'. Also it is not clear what
 * this directory would be when running with '--resume-from' because there is no execution root.
 */
public class ProgressUpdater
{

    private File file = null;

    private FileChannel channel = null;

    private FileLock lock = null;

    public ProgressUpdater(String directoryPath, String fileName) throws MojoExecutionException
    {

        createPropertiesFile(directoryPath, fileName);
    }

    private void createPropertiesFile(String directoryPath, String fileName)
            throws MojoExecutionException
    {

        File directory = new File(directoryPath);
        file = new File(directory, fileName);

        try
        {
            directory.mkdirs();
            file.createNewFile();
            file.deleteOnExit(); // Make sure the file gets deleted also if the build fails.
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Failed to create new properties file: " + file, e);
        }

    }

    /**
     * Obtains a lock of the properties file, updates the progress, saves the progress, calls the
     * callback with the updated {@link ReactorProgress} and finally releases the lock.
     *
     * @param currentReactorSize the size of the reactor
     * @param currentProjectBasedir the current directory from which the plugin is run
     * @param callback the callback to invoke when the progress has been updated
     * @throws MojoExecutionException when operations on the properties file fail
     */
    public void updateProgress(int currentReactorSize, File currentProjectBasedir,
            ProgressUpdatedCallback callback) throws MojoExecutionException
    {

        ReactorProgress progress = null;

        try
        {
            obtainLock();

            // Update the progress based on earlier progress recorded in the properties file.
            progress = ReactorProgress.loadFromPropertiesFile(file);
            progress.updateProgress(currentReactorSize, currentProjectBasedir);

            progress.saveToPropertiesFile(file);

            // Let the callback handle the updated progress before releasing the lock.
            // This way progress is guaranteed to increase over time in a parallel build.
            callback.progressUpdated(progress);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Updating properties file failed", e);
        }
        finally
        {

            try
            {
                releaseLock();
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Closing lock/channel file failed", e);
            }

        }
    }

    /**
     * Obtains a read/write lock of the properties file.
     */
    private void obtainLock() throws MojoExecutionException
    {
        while (true)
        {
            try
            {

                channel = new RandomAccessFile(file, "rw").getChannel();
                // Blocking wait until file lock is obtained
                lock = channel.lock();
                // The write lock has been obtained so the file can be updated
                break;

            }
            catch (IOException e)
            {
                throw new MojoExecutionException("I/O error while locking properties file", e);
            }
            catch (OverlappingFileLockException e)
            {
                // This is expected behavior because there may be multiple instances of this
                // plugin (in the same JVM) trying to update the file. Synchronizing on an
                // object won't help here because Maven can run another instance of the plugin
                // in a different classloader.
                try
                {
                    Thread.sleep(100L);
                }
                catch (InterruptedException ie)
                {
                    throw new MojoExecutionException("Thread sleep error", ie);
                }
                continue;
            }
        }
    }

    /**
     * Releases the read/write lock of the properties file.
     */
    private void releaseLock() throws IOException
    {
        if (lock != null)
        {
            lock.release();
        }
        if (channel != null)
        {
            channel.close();
        }
    }
}
