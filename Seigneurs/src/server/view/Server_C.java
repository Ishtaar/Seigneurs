package server.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Server_C
{
	private double xOffset;
	private double yOffset;
	
	@FXML
	private AnchorPane pane;
	@FXML
	private TextArea console;
	
	public void setConsole(String text)
	{
		try
		{
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if (console != null)
                        {
                            if (console.getText().length() == 0)
                            {
                                console.setText(text);
                            }
                            else
                            {
                                console.selectEnd();
                                console.insertText(console.getText().length(),"\n");
                                console.insertText(console.getText().length(),text);
                            }
                        }
                    }
                    catch (final Throwable t)
                    {
                        System.out.println("Unable to append log to text area: "+ t.getMessage());
                    }
                }
            });
        }
		catch (final IllegalStateException e)
		{
            // ignore case when the platform hasn't yet been initialized
        }
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
                System.exit(0);
            }
        });
    }
    
	public String getIP() throws IOException
	{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

		String ip = in.readLine(); // Get IP as a String
		
		return ip;
	}
}
