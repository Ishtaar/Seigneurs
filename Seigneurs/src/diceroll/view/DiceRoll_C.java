package diceroll.view;

import java.util.Random;

import diceroll.model.Dice;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class DiceRoll_C
{
	public Dice dice;
	public StackPane[] tab_dice; // Keep track of generated dices
	
	private double xOffset;
	private double yOffset;
	
    @FXML
    private ColorPicker box_couleur;
    @FXML
    private ComboBox<String> box_de;
    @FXML
    private Button button_launch;
    @FXML
    private Slider slider;
    @FXML
    private AnchorPane pane;
    @FXML
    private GridPane gridpane;
    
    public DiceRoll_C ()
    {
    	
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {
    	box_de.getItems().addAll(
    		"20",
    		"100");
    	
    	tab_dice = new StackPane[10];
    	
    	xOffset = 0.0;
    	yOffset = 0.0;
    }
    
    /**
     * Called when the user clicks on the launch button.
     */
    @FXML
    private void roll()
    {
    	if(box_de.getValue() != null)
		{
    		/*
    		 * Generating dice
    		 */
    		dice = new Dice(box_couleur.getValue(),randInt(1,Integer.parseInt(box_de.getValue())));
    			
    		addDice(dice);
    		
    		/**
    		 * Disable launch button and set timer for x seconds to re-enable it.
    		 */
    		button_launch.setDisable(true);
    		
    		final Timeline wait = new Timeline(
    	    	new KeyFrame(Duration.seconds(1),
    	        new EventHandler<ActionEvent>()
    	        {
    	    		@Override
    	    		public void handle(ActionEvent actionEvent)
    	    		{
    	    			button_launch.setDisable(false);
    	    		}
    	        }));
    		wait.setCycleCount(1);
    		wait.play();
		}
    }
    
	public void addDice(Dice dice)
	{
		this.dice = dice;
		
		/*
		 * Checking if grid is empty
		 */
		if(gridpane.getChildren().isEmpty())
		{
			tab_dice[0]=dice;
			gridpane.add(dice, 0, 0); // gridpane.add(child, columnIndex, rowIndex)
		}
		else
		{
			/*
			 * If grid is not empty
			 * Shift dices to the right
			 */
			for(int i=8; i>=0; i--) 
			{
				if(tab_dice[i] != null)
				{
					tab_dice[i+1]=tab_dice[i];
					gridpane.getChildren().remove(tab_dice[i]);
					gridpane.add(tab_dice[i+1], i+1, 0);
				}
			}
			
			tab_dice[0]=dice;
			gridpane.add(dice, 0, 0);
		}
	}
    
    /**
     * Called when the user clicks on the clear button.
     * Removes items in table and gridpane
     */
    @FXML
    private void clear()
    {
    	for(int i=0; i<10; i++)
		{
    		tab_dice[i] = null;
		}
    	
    	gridpane.getChildren().clear();
    }
    
    @FXML
    public void pressed(MouseEvent event)
    {
    	Stage stage = (Stage) pane.getScene().getWindow();
    	
    	xOffset = stage.getX() - event.getScreenX();
    	yOffset = stage.getY() - event.getScreenY();
    }
    
    @FXML
    public void dragged(MouseEvent event)
    {
    	Stage stage = (Stage) pane.getScene().getWindow();
    	
    	stage.setX(event.getScreenX() + xOffset);
    	stage.setY(event.getScreenY() + yOffset);
    }
    
    @FXML
    private void reduce()
    {
    	Stage stage = (Stage) pane.getScene().getWindow();
    	
    	if (!Platform.isFxApplicationThread()) // Ensure on correct thread else hangs X under Unbuntu
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                	stage.setIconified(true);
                }
            });
        }
    	else
        {
        	stage.setIconified(true);
        }
    }
    
    @FXML
    private void close()
    {
    	Stage stage = (Stage) pane.getScene().getWindow();
    	
    	Platform.runLater(new Runnable()
    	{
            @Override
            public void run()
            {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        });
    }
    
    /**
     * Set window on top
     */
    @FXML
    private void top()
    {
    	Stage stage = (Stage) pane.getScene().getWindow();
    	
    	if(stage.isAlwaysOnTop())
    	{
    		stage.setAlwaysOnTop(false);
    	}
    	else
    	{
    		stage.setAlwaysOnTop(true);
    	}
    }
    
    @FXML
    private void opacity()
    {
    	slider.valueProperty().addListener(new ChangeListener<Number>()
    	{
    		@Override
    	   	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
    	    {
    			pane.setOpacity((double) newValue);
    	    }
    	});
    }
    
    /**
     * Generates a random number between min & max
     */
    private String randInt(int min, int max)
    {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return ""+randomNum;
    }
} 