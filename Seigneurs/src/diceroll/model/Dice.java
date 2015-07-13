package diceroll.model;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

@SuppressWarnings("unused")
public class Dice extends StackPane
{
	private Color _color;
	private String _value;
	
	private Polygon hexagon;
	private Text text; /* Text(double x, double y, String text) */
	
	public Dice ()
	{
		
	}
	
	public Dice(String color, String value)
	{
		_color = Color.web(color);
		_value = value;
		
		this.setScaleY(0.65); // Bricolage pour faire correspondre la hauteur du dé à celle de la grille
		this.setScaleX(0.65); // Conservation du ratio X/Y
		this.setTranslateX(-7.75); // Bricolage pour centrer les dés dans la grille
		
		hexagon = new Polygon();
		hexagon.getPoints().addAll(new Double[]
		{
			-50.0, 30.0,
			0.0, 60.0,
			50.0, 30.0,
			50.0, -30.0,
			0.0, -60.0,
			-50.0, -30.0
		});
		
		hexagon.setFill(_color);
		hexagon.setStrokeWidth(4);
		hexagon.setSmooth(true);
		
		text = new Text (_value);
		
		text.setFont(new Font("Impact",35)); /* Nom de la police, Taille de la police */
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setSmooth(true);
		
		if(_color.equals(Color.BLACK)) /* Case dice is black */
		{
			hexagon.setStroke(Color.WHITE); /* Couleur de la bordure */
			text.setFill(Color.WHITE); /* Couleur du numéro */
		}
		else
		{
			hexagon.setStroke(Color.BLACK);
			text.setFill(Color.BLACK);
		}
		
		this.getChildren().add(hexagon);
		this.getChildren().add(text);
	
//		this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY))); /* Test de visualisation du stackpane */
	}

	public Polygon getHexagon()
	{
		return hexagon;
	}

	public Color getColor()
	{
		return _color;
	}

	public Text getText()
	{
		return text;
	}

	public String getValue()
	{
		return _value;
	}
}
