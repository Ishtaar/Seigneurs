package server;

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
		
		new DiceRollServerThread(this);
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
