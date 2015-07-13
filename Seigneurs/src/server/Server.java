package server;

import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;
import server.view.Server_C;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Server extends Application
{
	private AnchorPane root;
	Server_C controller;

	private Properties properties = new Properties();
	private String properties_file = "server.properties"; // Fichier Permettant d'éditer l'IP et le Port du serveur
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Server.class.getResource("/server/view/Server_V.fxml"));
		root = (AnchorPane) loader.load();
		controller = (Server_C) loader.getController();
		
		primaryStage.setTitle("Serveur - Seigneurs");
		primaryStage.getIcons().add(new Image("/server/view/ressources/Seigneurs.png"));
		
		Scene server = new Scene(root);
		primaryStage.setScene(server);
		primaryStage.setResizable(false);
		
		server.setFill(null);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		primaryStage.show();
		
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
		
		ServerSocket diceSS = new ServerSocket(Integer.parseInt(port)); // Ouverture d'un socket serveur sur "port"
		
		new DiceRollServerThread(diceSS);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
	
	public Server_C getController()
	{
		return controller;
	}
}
