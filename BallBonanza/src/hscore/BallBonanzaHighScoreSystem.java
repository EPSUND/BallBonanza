package hscore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import utils.Helpers;

public class BallBonanzaHighScoreSystem implements HighScoreSystem {
	
	/**
	 * The URL to the high score service
	 */
	private static final String HIGH_SCORE_SERVICE_URL = "http://highscoresystemes86.appspot.com/highscoresystem";
	
	private HighScoreListDialog highScoreDialog;
	
	public BallBonanzaHighScoreSystem(HighScoreListDialog highScoreListDialog)
	{
		this.highScoreDialog = highScoreListDialog;
	}
	
	public void registerScore(Object[] args)
	{
		int score = (Integer)args[0];
		int balls = (Integer)args[1];
		Date startTime = (Date)args[2];
		
		String name = (String)JOptionPane.showInputDialog(
                null,
                "Please enter your name for the highscore list:",
                "High score",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                "");
		
		if(name != null)
		{
			try
			{
				URLConnection hscoreConn = getHighScoreConnection(score, balls, startTime, name);
				
				if(hscoreConn != null)
				{
					hscoreConn.connect();
					showHighScoreList(hscoreConn);
				}
			}
			catch(IOException e)
			{
				System.err.println("BallBonanza: Could not connect to the highscore service");
				e.printStackTrace();
			}
		}
	}
	
	private URLConnection getHighScoreConnection(int score, int balls, Date startTime, String name)
	{	
		try 
		{
			URL registerHighScoreURL = new URL(HIGH_SCORE_SERVICE_URL + "?highScoreList=ballbonanza" + 
											   "&name=" + name + 
											   "&score=" + Integer.toString(score) + 
											   "&balls=" + Integer.toString(balls) +
											   "&time=" + Long.toString(Helpers.getTimeSpan(startTime, new Date())) +
											   "&date=" + Helpers.getCurrentTimeUTC());
			
			return registerHighScoreURL.openConnection();
			
		} catch (IOException e) {
			System.err.println("BallBonanza: Could not get a connection to the high score service");
			e.printStackTrace();
			return null;
		}
	}
	
	private URLConnection getHighScoreConnection()
	{	
		try 
		{
			URL registerHighScoreURL = new URL(HIGH_SCORE_SERVICE_URL + "?highScoreList=ballbonanza");	
			return registerHighScoreURL.openConnection();	
		} catch (IOException e) {
			System.err.println("BallBonanza: Could not get a connection to the high score service");
			e.printStackTrace();
			return null;
		} 
	}
	
	public void showHighScoreList(URLConnection highScoreServiceConn)
	{
		InputStream highScoreStream = null;
		
		try {
			highScoreStream = highScoreServiceConn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(highScoreStream));
			
			String line;
			ArrayList<Object[]> highScoreList = new ArrayList<Object[]>();
			
			while((line = reader.readLine()) != null)
			{
				String[] highScoreData = line.split(",");
				Object[] objRow = new Object[5];
				
				for(String highScoreItem : highScoreData)
				{
					String[] components = highScoreItem.split("=");
					
					if(components[0].equals("name"))
					{
						objRow[0] = components.length < 2 ? "" : components[1];//Must handle if the user didn't enter a name
					}
					else if(components[0].equals("score"))
					{
						objRow[1] = components[1];
					}
					else if(components[0].equals("balls"))
					{
						objRow[2] = components[1];
					}
					else if(components[0].equals("time"))
					{
						objRow[3] = Helpers.getTimeSpanString(Long.parseLong(components[1]));
					}
					else if(components[0].equals("date"))
					{
						objRow[4] = Helpers.utcToLocalTime(components[1]);
					}
					
				}
				
				highScoreList.add(objRow);
			}
			
			highScoreDialog.setHighScoreList(highScoreList);
			highScoreDialog.setVisible(true);
		} catch (IOException e) {
			System.err.println("BallBonanza: Could not show high score list");
			e.printStackTrace();
		}
		finally
		{
			try {
				highScoreStream.close();
			} catch (IOException e) {
				System.err.println("BallBonanza: Could not close highscore stream");
				e.printStackTrace();
			}
		}
	}
	
	public void showHighScoreList()
	{
		URLConnection highScoreConn = getHighScoreConnection();
		
		if(highScoreConn != null)
			showHighScoreList(highScoreConn);
	}
}
