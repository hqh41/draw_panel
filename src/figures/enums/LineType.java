package figures.enums;

import java.awt.BasicStroke;

/**
 * Le type de trait des lignes (continu, pointillé, ou sans trait)
 * @author davidroussel
 */
public enum LineType
{
	/**
	 * Pas de trait
	 */
	NONE,
	/**
	 * Trait plein
	 */
	SOLID,
	/**
	 * Trait pointillé
	 */
	DASHED;
	
	/**
	 * Le nombre de type de lignes (à changer si l'on rajoute un type de ligne)
	 */
	public static final int NbLineTypes = 3;

	/**
	 * Conversion d'un entier vers un {@link LineType}.
	 * A utiliser pour convertir l'index de l'élément sélectionné d'un combobox
	 * dans le type de ligne correspondant
	 * @param i l'entier à convertir
	 * @return le LineType correspondant
	 */
	public static LineType fromInteger(int i)
	{
		switch (i)
		{
			case 0:
				return NONE;
			case 1:
				return SOLID;
			case 2:
				return DASHED;
			default:
				return NONE;
		}
	}
	
	/**
	 * Conversion d'un {@link BasicStroke} en type de ligne
	 * @param stroke le stroke à examiner
	 * @return le type de ligne correspondant (NONE si le stroke est nul,
	 * SOLID si le stroke ne contient pas de dash array, DASHED si le stroke
	 * contient un dash array.
	 */
	public static LineType fromStroke(BasicStroke stroke)
	{
		if (stroke == null)
		{
			return LineType.NONE;
		}
		else
		{
			float[] dashArray = stroke.getDashArray();
			if (dashArray == null)
			{
				return LineType.SOLID;
			}
			else
			{
				return LineType.DASHED;
			}
		}
	}

	/**
	 * Représentation sous forme de chaine de caractères
	 * @return une chaine de caractères représentant la valeur de cet enum
	 */
	@Override
	public String toString() throws AssertionError
	{
		switch (this)
		{
			case NONE:
				return new String("None");
			case SOLID:
				return new String("Solid");
			case DASHED:
				return new String("Dashed");
		}

		throw new AssertionError("LineType Unknown assertion " + this);
	}

	/**
	 * Obtention d'un tableau de string contenant tous les noms des types.
	 * A utiliser lors de la création d'un combobox avec :
	 * LineType.stringValues()
	 * @return un tableau de string contenant tous les noms des types
	 */
	public static String[] stringValues()
	{
		LineType[] values = LineType.values();
		String[] stringValues = new String[values.length];
		for (int i = 0; i < values.length; i++)
		{
			stringValues[i] = values[i].toString();
		}

		return stringValues;
	}
}
