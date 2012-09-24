package com.blakeharley.fileworker.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

/**
 * This "logger" is designed to communicate with the text area on the main
 * pane to communicate messages from the worker to the user.
 * 
 * @author Blake Harley <blake@blakeharley.com>
 */
public class Logger
{
	/**
	 * The text area on the main pane.
	 */
	protected JTextArea area;
	
	/**
	 * Creates a new instance of this class
	 * 
	 * @param area The text area on the main pane
	 */
	public Logger(JTextArea area)
	{
		this.area = area;
	}
	
	/**
	 * Logs the given event. A timestamp will be appended to the beginning
	 * of the log event.
	 * 
	 * @param str The event to log
	 */
	public void log(String str)
	{
		this.area.append(this.getTimestamp() + str + "\n");
	}
	
	/**
	 * Creates the timestamp string that gets appended to log events.
	 * 
	 * @return The timestamp
	 */
	protected String getTimestamp()
	{
		DateFormat format = new SimpleDateFormat("[HH:mm:ss] ");
		Date date = new Date();
		
		return format.format(date);
	}
}
