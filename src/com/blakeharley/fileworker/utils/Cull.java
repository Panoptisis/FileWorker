package com.blakeharley.fileworker.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Scans an entire directory and all subdirectories for files matching the given
 * extension.
 * 
 * @author Blake Harley <blake@blakeharley.com>
 */
public class Cull
{
	/**
	 * The directory to cull.
	 */
	protected File directory;
	
	/**
	 * The extension filter.
	 */
	protected FileFilter filter;
	
	/**
	 * Creates a new instance of this with the given settings.
	 * 
	 * @param directory The directory to recursively scan
	 * @param extension The files to match against will have these extensions
	 */
	public Cull(File directory, String[] extensions)
	{
		if (!directory.isDirectory())
		{
			throw new IllegalArgumentException("Paramter is expected to be a directory");
		}

		this.directory = directory;
		this.filter = new ExtensionFileFilter(extensions);
	}
	
	/**
	 * Creates a new instance of this with the given settings.
	 * 
	 * @param directory The directory to recursively scan
	 * @param extension The files to match against will have these extensions
	 */
	public Cull(String directory, String[] extensions)
	{
		this(new File(directory), extensions);
	}
	
	/**
	 * Creates a new instance of this with the given settings.
	 * 
	 * @param directory The directory to recursively scan
	 * @param extension The files to match against will have this extension
	 */
	public Cull(File directory, String extension)
	{
		this(directory, new String[] { extension });
	}
	
	/**
	 * Creates a new instance of this with the given settings.
	 * 
	 * @param directory The directory to recursively scan
	 * @param extension The files to match against will have this extension
	 */
	public Cull(String directory, String extension)
	{
		this(new File(directory), extension);
	}
	
	/**
	 * Creates a new instance of this that scans for MP3 files in the given directory.
	 * 
	 * @param directory The directory to recursively scan
	 */
	public Cull(File directory)
	{
		this(directory, "mp3");
	}
	
	/**
	 * Creates a new instance of this that scans for MP3 files in the given directory.
	 * 
	 * @param directory The directory to recursively scan
	 */
	public Cull(String directory)
	{
		this(new File(directory));
	}
	
	/**
	 * Claims all of the files that fall under the domain of the culling call. Yes, I can
	 * certainly be more cryptic.
	 * 
	 * @return The list of files that made the cut
	 */
	public ArrayList<File> claim()
	{
		return this.recursiveCull(this.directory);
	}
	
	/**
	 * Recursive scans directories until a suitable list is built.
	 * 
	 * @param dir The directory to cull
	 * @return The resulting files
	 */
	protected ArrayList<File> recursiveCull(File dir)
	{
		if (!dir.isDirectory())
		{
			throw new IllegalArgumentException("Cull expects the parameter to be a directory");
		}

		File files[] = dir.listFiles(this.filter);

		ArrayList<File> results = new ArrayList<File>();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				results.addAll(this.recursiveCull(file));
			} else
			{
				results.add(file);
			}
		}

		return results;
	}
	
	/**
	 * The directory to be culled.
	 * 
	 * @return A directory
	 */
	public File getDirectory()
	{
		return this.directory;
	}
}

class ExtensionFileFilter implements FileFilter
{
	protected ArrayList<String> extensions;

	public ExtensionFileFilter(String[] extensions)
	{
		ArrayList<String> list = new ArrayList<String>();

		for (String item : extensions)
		{
			list.add(item);
		}

		this.setExtensions(list);
	}

	public ExtensionFileFilter(ArrayList<String> list)
	{
		this.setExtensions(list);
	}

	/**
	 * Adds the given extensions to this filter. Makes sure that the extensions
	 * are in lower case.
	 * 
	 * @param extensions
	 */
	public void setExtensions(ArrayList<String> extensions)
	{
		for (int i = 0; i < extensions.size(); i++)
		{
			extensions.set(i, extensions.get(i).toLowerCase());
		}

		this.extensions = extensions;
	}
	
	public boolean accept(File file)
	{
		// Directories get a pass
		if (file.isDirectory())
		{
			return true;
		}

		// Parse the extension
		String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);

		if (extensions.contains(ext.toLowerCase()))
		{
			return true;
		}

		return false;
	}
}