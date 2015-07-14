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
	private ObjectInputStream in; // Pour gestion du flux d'entr�e
	private DiceRollServerThread _serverThread; // Pour utilisation des m�thodes de la classe principale
	private int numClient=0; // Contiendra le num�ro de client g�r� par ce thread
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
			while(_s.getInputStream().read() != -1) // V�rification si la connexion est toujours �tablie
			{
				System.out.println("Ca rentre");
				_arguments = (EnumMap<ARGS, String>) in.readObject(); // D�claration de la variable qui recevra les d�s
				
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
				// On indique � la console la deconnexion du client
				_server.getController().setConsole("D�connexion du client n�"+numClient);
				_serverThread.delClient(numClient); // On supprime le client de la liste
				_s.close(); // Fermeture du socket si il ne l'a pas d�j� �t� (� cause de l'exception lev�e plus haut)
			}
			catch (IOException e)
			{
				
			}
	    }
	}
}
