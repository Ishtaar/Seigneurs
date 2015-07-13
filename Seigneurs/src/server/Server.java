package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Server.class.getResource("/server/view/Server_V.fxml"));
		root = (AnchorPane) loader.load();
		Server_C controller = (Server_C) loader.getController();
		
		primaryStage.setTitle("Serveur - Seigneurs");
		primaryStage.getIcons().add(new Image("/server/view/ressources/Seigneurs.png"));
		
		Scene server = new Scene(root);
		primaryStage.setScene(server);
		primaryStage.setResizable(false);
		
		server.setFill(null);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		primaryStage.show();
		
		controller.setConsole("IP : "+getIP());
		controller.setConsole("Port : 1099"); // Default port
	}

	public String getIP() throws IOException
	{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

		String ip = in.readLine(); // Get IP as a String
		
		return ip;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
