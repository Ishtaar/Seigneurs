package diceroll;

import java.util.logging.Level;
import java.util.logging.Logger;

import diceroll.view.DiceRoll_C;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DiceRoll extends Application
{
	private Stage primaryStage;
    private AnchorPane root;
    private DiceRoll_C controller;
    
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			this.primaryStage = primaryStage;
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DiceRoll.class.getResource("/diceroll/view/DiceRoll_V.fxml"));
			root = (AnchorPane) loader.load();
			controller = (DiceRoll_C) loader.getController();
			
			primaryStage.setTitle("Lanceur de dés - Seigneurs");
			primaryStage.getIcons().add(new Image("/diceroll/view/ressources/Seigneurs.png"));
			
			Scene diceRoll = new Scene(root);
			primaryStage.setScene(diceRoll);
			primaryStage.setResizable(false);
			
			diceRoll.setFill(null);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			
			primaryStage.show();
			
			new DiceRollThread(this);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			Logger.getLogger(DiceRoll.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public DiceRoll_C getController()
	{
		return controller;
	}

	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}