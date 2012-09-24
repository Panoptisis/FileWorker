package com.blakeharley.fileworker.worker.mover;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.blakeharley.fileworker.utils.Cull;
import com.blakeharley.fileworker.utils.StringExt;
import com.blakeharley.fileworker.worker.AudioTagWorker;


/**
 * This worker moves all of the MP3 files from a given directory to a new one.
 * 
 * @author Blake Harley <blake@blakeharley.com>
 */
public class Mover extends AudioTagWorker
{
	
	/**
	 * The instance of the culler.
	 */
	protected Cull cull;
	
	/**
	 * The place to relocate all this wonderful music to.
	 */
	protected String newLocation;
	
	/**
	 * Creates a new instance of this worker.
	 * 
	 * @param path The location to look in for files
	 * @param newLocation The new place to put the discovered files
	 */
	public Mover(String path, String newLocation)
	{
		this.cull = new Cull(path);
		this.newLocation = newLocation;
	}
	
	@Override
	public void doWork()
	{
		log.log("Scanning directories for music...");
		this.progressString = "Scanning directories for music...";
		ArrayList<File> files = this.cull.claim();
		this.filesTotal = files.size();
		log.log("Starting file transfer...");
		this.progressString = null;
		
		this.filesDone = 0;
		for (File file : files)
		{
			try
			{
				this.filesDone++;
				// Get the audio and tag files
				AudioFile audioFile = this.getAudioFile(file);
				
				// Get the new absolute file name
				String newFileName = this.generateFileName(audioFile);
				
				// Copy the file into its new home
				File newFile = this.copyFile(file, newFileName);
				
				// If there was an error, move along
				if (newFile == null)
				{
					continue;
				}
				
				AudioFile newAudioFile = this.getAudioFile(newFile);
				
				// Update the ID3 tag information
				this.cleanMetadata(newAudioFile);
			}
			catch (Exception e)
			{
				log.log("Tag could not be read: " + file.getAbsolutePath());
			}
		}
		
		log.log("Done.");
	}
	
	/**
	 * Copys the given file to the given destination.
	 * 
	 * @param file The file to copy
	 * @param newLocation The new location of this file
	 * @return The new file
	 */
	protected File copyFile(File file, String newLocation)
	{
		File newFile = new File(newLocation);
		
		try
		{
			FileUtils.copyFile(file, newFile);
		}
		catch (Exception e)
		{
			System.out.println("Could not copy file: " + file.getAbsolutePath());
			return null;
		}
		
		return newFile;
	}
	
	/**
	 * Cleans up the audio file's metadata in whatever way I see fit. Eat my OCD.
	 * 
	 * @param file The file to clean
	 */
	protected void cleanMetadata(AudioFile file)
	{
		try
		{
			// Get the metadata
			Tag tag = file.getTag();
			
			// Correct articles in albums and track names
			StringExt album = new StringExt(tag.getFirst(FieldKey.ALBUM));
			StringExt title = new StringExt(tag.getFirst(FieldKey.TITLE));
			
			album.articlesToLower();
			title.articlesToLower();
			
			// Only commit if this data need changing
			if (!album.toString().equals(tag.getFirst(FieldKey.ALBUM)) || !title.toString().equals(tag.getFirst(FieldKey.TITLE)))
			{
				tag.setField(FieldKey.ALBUM, album.toString());
				tag.setField(FieldKey.TITLE, title.toString());
				
				file.commit();
			}
		}
		catch (Exception e)
		{
			System.out.println("Could update file metadata: " + file.getFile().getAbsolutePath());
		}
	}
	
	/**
	 * Prepares a new file name and location for this audio file based on the
	 * ID3v2 metadata.
	 * 
	 * @param file
	 * @return
	 */
	protected String generateFileName(AudioFile file)
	{
		// Get the metadata
		Tag tag = file.getTag();
		
		// Get the data we're going to be working with
		StringExt artist = new StringExt(tag.getFirst(FieldKey.ALBUM_ARTIST));
		StringExt album  = new StringExt(tag.getFirst(FieldKey.ALBUM));
		StringExt title  = new StringExt(tag.getFirst(FieldKey.TITLE));
		String disc      = tag.getFirst(FieldKey.DISC_NO);
		String track     = String.format("%02d", Integer.parseInt(tag.getFirst(FieldKey.TRACK)));
		
		// Make sure we actually got the artist
		if (artist.length() == 0)
		{
			artist = new StringExt(tag.getFirst(FieldKey.ARTIST));
		}
		
		// Drop invalid characters and other undesirables
		artist.prepareFileName();
		album.prepareFileName();
		title.prepareFileName();
		
		// TEMP xx
		album.articlesToLower();
		title.articlesToLower();
		// xx TEMP
		
		return this.newLocation + "\\" + artist + "\\" + album + "\\" + disc + "." + track + " - " + title + ".mp3";
	}
	
	@Override
	public String getName()
	{
		return "Mover";
	}
}
