package com.blakeharley.fileworker.utils;

import java.text.Normalizer;

/**
 * StringExt is an "extension" to the Java String class. The reason I say
 * "extension" is because Strings in Java are immutable. Instead, we will
 * use a delegation pattern.
 * 
 * This string will simply provide some nifty functionality that would be
 * nice for cleaning up those pesky ID3v2 tags.
 * 
 * @author Blake
 */
public class StringExt
{
	/**
	 * The string object itself.
	 */
	protected String delegate;
	
	/**
	 * Construct our delegate object.
	 * 
	 * @param str
	 */
	public StringExt(String str)
	{
		this.delegate = str;
	}
	
	/**
	 * Gets the length of the string.
	 * 
	 * @return String length
	 */
	public int length()
	{
		return this.delegate.length();
	}
	
	/**
	 * Converts articles in the string to their lower case forms. Doesn't
	 * touch articles at the beginning of the string.
	 */
	public void articlesToLower()
	{
		this.delegate = this.delegate.replaceAll(" A ", " a ");
		this.delegate = this.delegate.replaceAll(" An ", " an ");
		this.delegate = this.delegate.replaceAll(" The ", " the ");
		this.delegate = this.delegate.replaceAll(" Of ", " of ");
		this.delegate = this.delegate.replaceAll(" In ", " in ");
		this.delegate = this.delegate.replaceAll(" To ", " to ");
		this.delegate = this.delegate.replaceAll(" And ", " and ");
		this.delegate = this.delegate.replaceAll(" But ", " but ");
		this.delegate = this.delegate.replaceAll(" Or ", " or ");
		this.delegate = this.delegate.replaceAll(" Nor ", " nor ");
		this.delegate = this.delegate.replaceAll(" For ", " for ");
	}
	
	/**
	 * Prepares the string to be used in a file name. This will remove
	 * characters that are illegal in file names and some other OCD things
	 * I have.
	 */
	public void prepareFileName()
	{
		// Characters not allowed:
		// \ / : ? " < > |
		
		// Removes:
		//  * Illegal characters
		//  * Things wrapped in []
		this.delegate = this.delegate.replaceAll("(?:[\\/:<>\\?]| ?\\[.*?\\])", "");
		
		// Remove duplicate spacing
		this.delegate = this.delegate.replaceAll("  ", " ");
		
		// Remove non-ASCII characters with accents and stuffs
		this.delegate = Normalizer.normalize(this.delegate, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public boolean equals(StringExt str)
	{
		return this.equals(str.toString());
	}
	
	public boolean equals(String str)
	{
		return this.delegate.equals(str);
	}
	
	public String toString()
	{
		return this.delegate;
	}
}
