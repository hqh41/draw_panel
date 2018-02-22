package figures.enums;

import java.awt.Paint;

import figures.Drawing;

/**
 * Enumeration de ce à quoi s'applique une couleur ({@link Paint}) à utiliser
 * dans le {@link widgets.EditorFrame.ColoItemListener}
 *
 * @author davidroussel
 */
public enum PaintToType
{
	/**
	 * La couleur s'applique au remplissage
	 */
	FILL,
	/**
	 * La couleur s'applique au trait
	 */
	EDGE;

	/**
	 * Application d'une couleur au modèle de dessin en fonction de la valeur de
	 * l'enum
	 *
	 * @param paint la couleur à appliquer
	 * @param drawing le modèle de dessin sur lequel appliquer la couleur
	 * @throws AssertionError si le type de l'enum est inconnu
	 */
	public void applyPaintTo(Paint paint, Drawing drawing)
			throws AssertionError
	{
		switch (this)
		{
			case FILL:
				drawing.setFillPaint(paint);
				break;
			case EDGE:
				drawing.setEdgePaint(paint);
				break;
			default:
				throw new AssertionError(
						"PaintApplicationType unknown assertion " + this);
		}
	}

	/**
	 * Représentation sous forme de chaine de caractères
	 *
	 * @return une chaine de caractères représentant la valeur de cet enum
	 */
	@Override
	public String toString() throws AssertionError
	{
		switch (this)
		{
			case FILL:
				return new String("Fill");
			case EDGE:
				return new String("Edge");
		}

		throw new AssertionError("PaintApplicationType Unknown assertion "
				+ this);
	}

}
