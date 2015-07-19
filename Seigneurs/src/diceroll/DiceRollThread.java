package diceroll;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.EnumMap;
import java.util.Properties;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private String properties_file = "client.properties"; // Fichier Permettant d'�diter l'IP et le Port pour la connexion au serveur
	
	DiceRollThread(DiceRoll diceroll) throws IOException
	{	
		_controller = diceroll.getController();
		_controller.setDiceRollThread(this);
		
		// R�cup�ration des informations du fichier properties
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
			
			Alert alert = new Alert(AlertType.ERROR); // Affichage d'une fen�tre d'erreur dans le cas o� la connexion �choue
			alert.setTitle("Lanceur de d�s - Seigneurs - Erreur");
			alert.setHeaderText("La connexion au serveur a �chou�e.");
			alert.setContentText("V�rifiez les param�tres de connexion.");

			alert.showAndWait();
			
			System.exit(0);
		}
		
		t = new Thread(this);
		t.start();Platform.setImplicitExit(false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				_arguments = (EnumMap<ARGS, String>) in.readObject(); // D�claration de la variable qui recevra les arguments pour cr�er le d�
				System.out.println("(Client)Recu: "+_arguments);
				Platform.runLater(() ->_controller.addDice(_arguments)); // FX application thread
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
	
	public void send(EnumMap<ARGS,String> arguments)
	{
		try
		{
			System.out.println("(Client)Envoy�: "+arguments);
			out.writeObject(arguments);
			out.flush();
			out.reset();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
