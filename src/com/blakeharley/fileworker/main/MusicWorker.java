package com.blakeharley.fileworker.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.blakeharley.fileworker.utils.Logger;
import com.blakeharley.fileworker.worker.Worker;
import com.blakeharley.fileworker.worker.decommenter.Decommenter;
import com.blakeharley.fileworker.worker.mover.Mover;


/**
 * Music Worker
 * 
 * This is the main entry point for this application. The purpose of this application is varied
 * and depends on the worker that is currently plugged in.
 * 
 * @author Blake Harley <blake@blakeharley.com>
 */
@SuppressWarnings("serial")
public class MusicWorker extends JFrame
{
	protected JPanel pane;
	protected JProgressBar progressBar;
	protected final int progressBarMax = 10000;
	protected JTextArea textArea;
	
	/**
	 * Starts a new swing pane and starts the cogs moving.
	 * 
	 * @param worker The worker that will be run
	 */
	public MusicWorker(final Worker worker)
	{
		// Send the window title to the parent and construct
		super("Music Worker v1.0");
		
		// Set the general window settings 
		int width = 800;
		int height = 800;
		this.setBounds(0, 0, width, height);
		this.setLocationRelativeTo(getRootPane()); // Little tweak to center window
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set up the pane
		this.pane = new JPanel();
		
		// Set up the progress bar
		this.progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);
		this.progressBar.setIndeterminate(true);
		this.progressBar.setPreferredSize(new Dimension(width - 20, 20));
		this.progressBar.setMaximum(this.progressBarMax);
		
		// Set up the text area
		this.textArea = new JTextArea();
		this.textArea.setPreferredSize(new Dimension(width - 20, height - 65));
		this.textArea.setEditable(false);
		Font font = new Font("Consolas", Font.PLAIN, 13);
		this.textArea.setFont(font);
		
		// Set up the worker
		Logger logger = new Logger(this.textArea);
		worker.setLogger(logger);
		
		// Stitch it all together
		Container con = this.getContentPane();
		con.add(pane);
		this.pane.add(this.progressBar);
		this.pane.add(this.textArea);
		this.setVisible(true);
		
		// This thread will handle updating the progress bar
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Run to infinity and beyond
				while (1 == 1)
				{
					final String str = worker.getProgressString();
					
					// If we don't have a string to show, display the progress
					if (str == null)
					{
						SwingUtilities.invokeLater(new Runnable()
						{
							@Override
							public void run()
							{
								updateProgressBar((int) (progressBarMax * worker.getPercentDone()));
							}
						});
					}
					// Looks like we have a string to display
					else
					{
						SwingUtilities.invokeLater(new Runnable()
						{
							@Override
							public void run()
							{
								updateProgressBarText(str);
							}
						});
					}
					
					// Sleep for a wee bit
					try { Thread.sleep(500); } catch (InterruptedException e) {}
				}
			}
		}).start();
		
		// Finally, start the worker
		worker.doWork();
	}
	
	/**
	 * Updates the progress bar text. If the progress bar isn't indeterminate,
	 * it will be set into the indeterminate mode.
	 * 
	 * @param str The string to apply to the progress bar
	 */
	protected void updateProgressBarText(String str)
	{
		if (!this.progressBar.isIndeterminate())
		{
			this.progressBar.setIndeterminate(true);
		}
		
		this.progressBar.setString(str);
	}
	
	/**
	 * Updates the current value of the progress bar. If the progress bar is in
	 * indeterminate mode, it will be kicked out of it.
	 * 
	 * @param val The value to update the progress bar to
	 */
	protected void updateProgressBar(int val)
	{
		// If we're indeterminate, return to our normal state
		if (this.progressBar.isIndeterminate())
		{
			this.progressBar.setIndeterminate(false);
			this.progressBar.setString(null);
		}
		
		this.progressBar.setValue(val);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//Worker worker = new Mover("D:\\Music", "D:\\Music-new");
		Worker worker = new Decommenter("D:\\Music-new");
		
		new MusicWorker(worker);
	}
}
