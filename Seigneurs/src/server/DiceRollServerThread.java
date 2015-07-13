package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
	private Vector<ObjectOutputStream> tabClients = new Vector<ObjectOutputStream>(); // Contiendra tous les flux de sortie vers les clients
	private Thread t;
	private ServerSocket diceSS;
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
		
		diceSS = new ServerSocket(Integer.parseInt(port), 0, InetAddress.getByName(null)); // Ouverture d'un socket serveur sur "port"

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
				new DiceRollThread(diceSS.accept(), this, _server);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	synchronized public void sendAll(EnumMap<ARGS, String> arguments) throws IOException
	{
		ObjectOutputStream out;
		
		for (int i = 0; i < tabClients.size(); i++) // Parcours de la table des connectés
	    {
			out = (ObjectOutputStream) tabClients.elementAt(i); // Extraction de l'élément courant (type ObjectOutputStream)
			if (out != null) // Sécurité, l'élément ne doit pas être vide
			{
				out.writeObject(arguments);
				out.flush(); // Envoi dans le flux de sortie
			}
	    }
	}
	
	synchronized public void delClient(int i)
	{
		if (tabClients.elementAt(i) != null) // Si l'élément existe ...
		{
			tabClients.removeElementAt(i); // ... on le supprime
			System.out.println("delClient");
		}
	}
	
	synchronized public int addClient(ObjectOutputStream out)
	{
		tabClients.addElement(out); // On ajoute le nouveau flux de sortie au tableau
		System.out.println("addClient");	
		return tabClients.size()-1; // On retourne le numéro du client ajouté (size-1)
	}
}
