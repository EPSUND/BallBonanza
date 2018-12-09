package sound;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

import utils.Helpers;

import java.util.HashMap;

public class SoundPlayer {
	/*Table of sound clips*/
	private HashMap<String, Clip> clips;
	private boolean muted;
	
	public SoundPlayer()
	{
		clips = new HashMap<String, Clip>();
		muted = false;
	}
	
	public void loadClip(String fileName, String name)
	{
		try
		{
			URL soundURL = Helpers.getResourceURL(this, fileName);
			
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundURL);
			clip.open(inputStream);
			
			clips.put(name, clip);
		}
		catch(UnsupportedAudioFileException e)
		{
			System.err.println("BallBonanza: Unsupported audio file");
			e.printStackTrace();
			return;
		}
		catch (IOException e) 
		{
			System.err.println("BallBonanza: Could not load audioclip");
			e.printStackTrace();
			return;
		} 
		catch (LineUnavailableException e) 
		{
			System.err.println("BallBonanza: Line unavailable");
			e.printStackTrace();
			return;
		}
		catch(Exception e)
		{
			System.err.println("BallBonanza: An exception occured");
			e.printStackTrace();
			return;
		}
	}
	
	public void playClip(String clipName)
	{
		Clip clip;
		
		//Don't play the clip if the sound has been muted
		if(muted)
		{
			return;
		}
		
		//Check if the clip exists
		if(!clips.containsKey(clipName))
		{
			System.err.println("BallBonanza: No clip called " + clipName + " exists");
			return;
		}
		
		try {
			clip = clips.get(clipName);
			
			//Stop the clip if it is already running
			if(clip.isRunning())
			{
				clip.stop();
			}
			
			//Rewind the clip and play it
			clip.setFramePosition(0);
			clip.start();
		} catch (Exception e) {
			System.err.println("BallBonanza: Could not play clip " + clipName);
			e.printStackTrace();
		}
	}
	
	public void disposePlayer()
	{
		//Close all sound clips
		for(Clip clip : clips.values())
		{
			clip.stop();
			clip.close();
		}
		//Clear the clip collection
		clips.clear();
	}
	
	public boolean isMuted()
	{
		return muted;
	}
	
	public void mute()
	{
		muted = true;
	}
	
	public void unmute()
	{
		muted = false;
	}
}
