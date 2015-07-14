package diceroll;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.EnumMap;
import java.util.Properties;

import diceroll.Arguments.ARGS;
import diceroll.view.DiceRoll_C;

public class DiceRollThread implements Runnable
{
	private DiceRoll_C _controller;
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread t;
	private EnumMap<ARGS,String> _arguments;
	
	private Properties properties = new Properties();
	private String properties_file = "client.properties"; // Fichier Permettant d'éditer l'IP et le Port pour la connexion au serveur
	
	DiceRollThread(DiceRoll diceroll) throws IOException
	{	
		_controller = diceroll.getController();
		_controller.setDiceRollThread(this);
		
		// Récupération des informations du fichier properties
		InputStream input = DiceRollThread.class.getResourceAsStream(properties_file);
		properties.load(input);
				
		String port = properties.getProperty("Port");
		String ip = properties.getProperty("IP");
		
		try
		{
			s = new Socket(InetAddress.getByName(ip),Integer.parseInt(port));
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		t = new Thread();
		t.start();
	}
	
	@Override
	public void run()
	{
		try
		{
			_arguments = (EnumMap<ARGS, String>) in.readObject(); // Déclaration de la variable qui recevra les arguments pour créer le dé
			
			if(_arguments != null)
			{
				_controller.addDice(_arguments);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				s.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void test()
	{
		System.out.println("TEST");
	}
	
	public void send(EnumMap<ARGS,String> arguments)
	{
		try
		{
			out.writeObject(arguments);
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
