package com.blakeharley.fileworker.worker;

import com.blakeharley.fileworker.utils.Logger;

/**
 * Workers are "units" of code that are designed to perform specific tasks. Considering
 * the name of this program, these are generally tasks relating to the management of my music library.
 * 
 * @author Blake Harley <blake@blakeharley.com>
 */
public interface Worker
{
	/**
	 * This is the "main" function of this unit of work. This is the method that will
	 * be called after the main window has been set up.
	 */
	public void doWork();
	
	/**
	 * This value will be returned to the progress bar for updating. If this value isn't currently
	 * being displayed, anything can be returned.
	 * 
	 * @return The percent done this worker is
	 */
	public float getPercentDone();
	
	/**
	 * This string will be displayed on the progress bar. If a value is returned, the progress bar
	 * will be placed into indeterminate mode if it isn't already. If you want to should progress
	 * instead, return null.
	 * 
	 * @return The string to be displayed on the progress bar
	 */
	public String getProgressString();
	
	/**
	 * The worker can use the logger to communicate messages back to the user via the text area.
	 * 
	 * @param logger The current logger instance
	 */
	public void setLogger(Logger logger);
	
	/**
	 * This use to have a purpose during prototyping. It might be useful again in the future,
	 * so I'll just leave this here.
	 * 
	 * @return The worker's name
	 */
	public String getName();
}
