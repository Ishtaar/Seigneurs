package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.EnumMap;
import java.util.Properties;
import java.util.Vector;

import server.view.Server_C;
import diceroll.Arguments.ARGS;

public class DiceRollServerThread implements Runnable
{
	private Server _server;
	private Server_C controller;
	private Vector<DiceRollThread> tabClients = new Vector<DiceRollThread>(); // Contiendra tous les flux de sortie vers les clients
	private Thread t;
	private ServerSocket listener;
	private Properties properties = new Properties();
	private String properties_file = "server.properties"; // Fichier Permettant d'éditer le port du serveur
	
	public DiceRollServerThread(Server server) throws IOException
	{
		_server = server;
		controller = _server.getController();
		
		// Récupération des informations du fichier properties
		InputStream input = Server.class.getResourceAsStream(properties_file);
		properties.load(input);
		
		String port = properties.getProperty("Port");
		String ip;
		if(properties.getProperty("IP").equalsIgnoreCase("*")) // Récupération automatique de l'adresse IP
		{
			ip = controller.getIP();
		}
		else
		{
			ip = properties.getProperty("IP");
		}
		
		controller.setConsole("IP : "+ip+"\n"+"Port : "+port);
		
		listener = new ServerSocket(Integer.parseInt(port), 0, InetAddress.getLocalHost()); // Ouverture d'un socket serveur sur "port" et IP locale

		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run()
	{
		while (true) // attente en boucle de connexion (bloquant sur ss.accept)
		{
			try
			{
				new DiceRollThread(listener.accept(), this);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	synchronized public void sendAll(EnumMap<ARGS, String> arguments) throws IOException
	{
		for(DiceRollThread client : tabClients)
		{
			client.send(arguments);
		}
	}
	
	synchronized public void delClient(int i)
	{
		if (tabClients.elementAt(i) != null) // Si l'élément existe ...
		{
			tabClients.removeElementAt(i); // ... on le supprime
		}
	}
	
	synchronized public int addClient(DiceRollThread client)
	{
		tabClients.addElement(client); // On ajoute le nouveau flux de sortie au tableau	
		return tabClients.size()-1; // On retourne le numéro du client ajouté (size-1)
	}
	
	public Server get_server()
	{
		return _server;
	}
}
