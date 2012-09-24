package com.blakeharley.fileworker.worker.decommenter;

import java.io.File;
import java.util.ArrayList;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.blakeharley.fileworker.utils.Cull;
import com.blakeharley.fileworker.worker.AudioTagWorker;


public class Decommenter extends AudioTagWorker
{
	/**
	 * The instance of the culler.
	 */
	protected Cull cull;
	
	/**
	 * Creates a new instance of this worker.
	 * 
	 * @param path The location to look in for files
	 * @param newLocation The new place to put the discovered files
	 */
	public Decommenter(String path)
	{
		this.cull = new Cull(path);
	}

	@Override
	public void doWork()
	{
		log.log("Scanning directories for music...");
		this.progressString = "Scanning directories for music...";
		ArrayList<File> files = this.cull.claim();
		this.filesTotal = files.size();
		log.log("Erasing IDv3 comments...");
		this.progressString = null;
		
		this.filesDone = 0;
		for (File file : files)
		{
			try
			{
				this.filesDone++;
				// Get the audio and tag files
				AudioFile audioFile = this.getAudioFile(file);
				Tag tag = audioFile.getTag();
				if (tag.hasField(FieldKey.COMMENT) || tag.hasField("Comments"))
				{
    				tag.deleteField(FieldKey.COMMENT);
    				tag.deleteField("Comments");
    				audioFile.commit();
				}
			}
			catch (Exception e)
			{
				log.log("Tag could not be read: " + file.getAbsolutePath());
			}
		}
		
		log.log("Done.");
	}

	@Override
	public String getName()
	{
		return "Decommenter";
	}
}
