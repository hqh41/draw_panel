package widgets.enums;

/**
 * Les types d'arbre pour représenter les figures dans un {@link javax.swing.JTree}
 * @author davidroussel
 */
public enum TreeType
{
	/**
	 * Simple liste de figures
	 */
	FIGURE,
	/**
	 * Groupement des figures par type de figure
	 */
	FIGURE_TYPE,
	/**
	 * Groupement des figures par type de couleur de remplissage
	 */
	FILL_COLOR,
	/**
	 * Groupement des figures par type de couleur de trait
	 */
	EDGE_COLOR,
	/**
	 * Groupement des figures par type de trait
	 */
	EDGE_TYPE;


	/**
	 * Nombre d'éléments dans cet enum
	 */
	public static final int NbTreeTypes = 5;

	/**
	 * Conversion d'un entier en {@link TreeType}
	 *
	 * @param i l'entier à convertir en TreeType
	 * @return le TreeType correspondant à l'entier
	 */
	public static TreeType fromInteger(int i)
	{
		switch (i)
		{
			case 0:
				return FIGURE;
			case 1:
				return FIGURE_TYPE;
			case 2:
				return FILL_COLOR;
			case 3:
				return EDGE_COLOR;
			case 4:
				return EDGE_TYPE;
			default:
				return FIGURE;
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
			case FIGURE:
				return new String("Figure");
			case FIGURE_TYPE:
				return new String("Figure Type");
			case FILL_COLOR:
				return new String("Fill Color");
			case EDGE_COLOR:
				return new String("Edge Color");
			case EDGE_TYPE:
				return new String("Edge Type");
		}

		throw new AssertionError("TreeType Unknown assertion " + this);
	}
}
