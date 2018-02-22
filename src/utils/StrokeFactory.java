package utils;

import java.awt.BasicStroke;

import figures.enums.LineType;

/**
 * Classe contenant une FlyweightFactory pour les {@link BasicStroke} afin de pouvoir
 * réutiliser un même {@link BasicStroke} à plusieurs endroits du programme
 * @author davidroussel
 */
public class StrokeFactory
{
	/**
	 * Flyweight factory stockant tous les {@link BasicStroke} déjà requis
	 */
	private static FlyweightFactory<BasicStroke> strokeFactory =
		new FlyweightFactory<BasicStroke>();

	/**
	 * Obtention d'un {@link BasicStroke} de la factory
	 * @param stroke le paint recherché
	 * @return le stroke recherché
	 */
	public static BasicStroke getStroke(BasicStroke stroke)
	{
		if (stroke != null)
		{
			return strokeFactory.get(stroke);
		}

		return null;
	}
	/**
	 * Obtention d'un {@link BasicStroke} à partir d'un type de trait et
	 * d'une épaisseur de trait
	 * @param type le type de trait (NONE, SOLID ou DASHED)
	 * @param width l'épaisseur du trait
	 * @return une {@link BasicStroke} correspondant au type et à l'épaisseur
	 * de trait en provenance de la factory
	 */
	public static BasicStroke getStroke(LineType type, float width)
	{
		switch (type)
		{
			default:
			case NONE:
				return null;
			case SOLID:
				return getStroke(new BasicStroke(width,
				                                 BasicStroke.CAP_ROUND,
				                                 BasicStroke.JOIN_ROUND));
			case DASHED:
				final float dash1[] = { 2 * width };
				return getStroke(new BasicStroke(width,
				                                 BasicStroke.CAP_ROUND,
				                                 BasicStroke.JOIN_ROUND,
				                                 width, dash1, 0.0f));
		}
	}
}
