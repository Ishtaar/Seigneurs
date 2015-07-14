package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.EnumMap;

import diceroll.Arguments.ARGS;

public class DiceRollThread implements Runnable
{
	private Thread t; // Contiendra le thread du client
	private Socket _s; // Recevra le socket liant au client
	private ObjectOutputStream out; // Pour gestion du flux de sortie
	private ObjectInputStream in; // Pour gestion du flux d'entrée
	private DiceRollServerThread _serverThread; // Pour utilisation des méthodes de la classe principale
	private int numClient=0; // Contiendra le numéro de client géré par ce thread
	private Server _server;
	private EnumMap<ARGS,String> _arguments;
	
	DiceRollThread(Socket s, DiceRollServerThread serverThread, Server server)
	{
		_s = s;
		_serverThread = serverThread;
		_server = server;
		
		try
		{
			out = new ObjectOutputStream(_s.getOutputStream());
			in = new ObjectInputStream(_s.getInputStream());
			numClient = _serverThread.addClient(out);
		}
		catch (IOException e)
		{
			
		}
		
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run()
	{
		try
		{
			while(_s.getInputStream().read() != -1) // Vérification si la connexion est toujours établie
			{
				System.out.println("Ca rentre");
				_arguments = (EnumMap<ARGS, String>) in.readObject(); // Déclaration de la variable qui recevra les dés
				
				if(_arguments != null)
				{
					System.out.println(_arguments);
					_serverThread.sendAll(_arguments);
				}
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
		finally // Finally se produira le plus souvent lors de la deconnexion du client
	    {
			try
			{
				// On indique à la console la deconnexion du client
				_server.getController().setConsole("Déconnexion du client n°"+numClient);
				_serverThread.delClient(numClient); // On supprime le client de la liste
				_s.close(); // Fermeture du socket si il ne l'a pas déjà été (à cause de l'exception levée plus haut)
			}
			catch (IOException e)
			{
				
			}
	    }
	}
}
