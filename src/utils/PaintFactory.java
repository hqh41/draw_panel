package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JColorChooser;

/**
 * Classe contenant une FlyweightFactory pour les {@link Paint} afin de pouvoir
 * réutiliser un même {@link Paint} à plusieurs endroits du programme
 * @author davidroussel
 */
public class PaintFactory
{
	/**
	 * Map associant des noms de couleurs standard à des {@link Paint} standards
	 */
	private static final Map<String, Paint> standardPaints = fillStandardPaints();

	/**
	 * Construction de la map des {@link Paint} standards
	 * @return une map contenant les {@link Paint} standards
	 */
	private static Map<String, Paint> fillStandardPaints()
	{
		Map<String, Paint> map = new HashMap<String, Paint>();
		map.put("Black", Color.black);
		map.put("Blue", Color.blue);
		map.put("Cyan", Color.cyan);
		map.put("Green", Color.green);
		map.put("Magenta", Color.magenta);
		map.put("None", null);
		map.put("Orange", Color.orange);
		map.put("Pink", Color.pink);
		map.put("Red", Color.red);
		map.put("White", Color.white);
		map.put("Yellow", Color.yellow);
		
		return map;
	}
	
	/**
	 * Flyweight factory stockant tous les {@link Paint} déjà requis
	 */
	private static FlyweightFactory<Paint> paintFactory =
		new FlyweightFactory<Paint>();

	/**
	 * Obtention d'un {@link Paint} de la factory
	 * @param paint le paint recherché
	 * @return le paint recherché extrait de la factory
	 */
	public static Paint getPaint(Paint paint)
	{
		if (paint != null)
		{
			return paintFactory.get(paint);
		}
		
		return null;
	}
	
	/**
	 * Obtention d'un paint de la factory par son nom en le recherchant dans les
	 * {@link #standardPaints}
	 * @param paintName le nom de la couleur requise
	 * @return le paint recherché extrait de la factory
	 */
	public static Paint getPaint(String paintName)
	{
		if (paintName.length() > 0)
		{
			if (standardPaints.containsKey(paintName))
			{
				return paintFactory.get(standardPaints.get(paintName));
			}
		}
		
		return null;
	}
	
	/**
	 * Obtention d'un paint de la factory en déclenchant une boite de dialogue
	 * de choix d'une couleur.
	 * @param component le composant AWT à l'origine de la boite de dialogue
	 * @param title le titre de la boite de dialogue
	 * @param initialColor la couleur initiale de la boite de dialogue de choix 
	 * de couleus
	 * @return
	 */
	public static Paint getPaint(Component component, 
	                             String title, 
	                             Color initialColor)
	{
		if (component != null)
		{
			Color color = JColorChooser.showDialog(component, title, initialColor);
			if (color != null)
			{
				return paintFactory.get(color);
			}
		}
		
		return null;
	}
}
