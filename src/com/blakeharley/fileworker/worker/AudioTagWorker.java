package com.blakeharley.fileworker.worker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import com.blakeharley.fileworker.utils.Logger;


abstract public class AudioTagWorker implements Worker
{
	/**
	 * The number of files moved so far.
	 */
	protected int filesDone = 0;
	
	/**
	 * The total amount of files to move for the purpose of the progress bar.
	 */
	protected int filesTotal = 0;
	
	/**
	 * This will track the current.
	 */
	protected String progressString = "Starting worker...";
	
	/**
	 * The logger instance.
	 */
	protected Logger log;
	
	/**
	 * Alternate print steam for hiding warnings.
	 */
	protected PrintStream errorStream;
	
	@Override
	public float getPercentDone()
	{
		return ((float) filesDone) / filesTotal;
	}

	@Override
	public String getProgressString()
	{
		return this.progressString;
	}

	@Override
	public void setLogger(Logger logger)
	{
		this.log = logger;
	}
	
	/**
	 * Gets an audio file while suppressing warning messages.
	 * 
	 * @param file The file to get audio information for
	 * @return The audio file
	 * @throws Exception
	 */
	protected AudioFile getAudioFile(File file) throws Exception
	{
		PrintStream oldErr = System.err;
		System.setErr(this.getAlternateErrorSteam());
		AudioFile audioFile = AudioFileIO.read(file);
		System.setErr(oldErr);
		
		return audioFile;
	}
	
	/**
	 * Lazy loads an error print steam.
	 * 
	 * @return
	 */
	protected PrintStream getAlternateErrorSteam()
	{
		if (this.errorStream == null)
		{
			this.errorStream = new PrintStream(new ByteArrayOutputStream());
		}
		
		return this.errorStream;
	}
}
