package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Vector;

import diceroll.model.Dice;

public class DiceRollServerThread implements Runnable
{
	private Vector<ObjectOutputStream> tabClients = new Vector<ObjectOutputStream>(); // Contiendra tous les flux de sortie vers les clients
	
	private Thread t;
	private ServerSocket _ss;
	
	public DiceRollServerThread(ServerSocket ss) throws IOException
	{
		_ss = ss;

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
				new DiceRollThread(_ss.accept(), this);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	synchronized public void sendAll(Dice dice) throws IOException
	{
		ObjectOutputStream out;
		
		for (int i = 0; i < tabClients.size(); i++) // Parcours de la table des connectés
	    {
			out = (ObjectOutputStream) tabClients.elementAt(i); // Extraction de l'élément courant (type ObjectOutputStream)
			if (out != null) // Sécurité, l'élément ne doit pas être vide
			{
				out.writeObject(dice);
				out.flush(); // Envoi dans le flux de sortie
			}
	    }
	}
	
	synchronized public void delClient(int i)
	{
		if (tabClients.elementAt(i) != null) // Si l'élément existe ...
		{
			tabClients.removeElementAt(i); // ... on le supprime
		}
	}
	
	synchronized public int addClient(ObjectOutputStream out)
	{
		tabClients.addElement(out); // On ajoute le nouveau flux de sortie au tableau
		
		return tabClients.size()-1; // On retourne le numéro du client ajouté (size-1)
	}
}
