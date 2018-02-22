package widgets.enums;

/**
 * Différents modes de fonctionnement de l'UI
 * @author davidroussel
 */
public enum OperationMode
{
	/**
	 * Creation mode dans le quel on crée de nouvelles figures
	 */
	CREATION,

	/**
	 * Transformation mode dans lequel on effectue des transformations
	 * géométriques (déplacement, rotation, facteur d'échelle) sur
	 * les figures sélectionnées
	 */
	TRANSFORMATION;

	/**
	 * Nombre d'éléments dans cet enum
	 */
	public static final int NbOperationModes = 2;

	/**
	 * Conversion d'un entier en {@link OperationMode}
	 *
	 * @param i l'entier à convertir en {@link OperationMode}
	 * @return l'OperationMode correspondant à l'entier
	 */
	public static OperationMode fromInteger(int i)
	{
		switch (i)
		{
			case 0:
				return CREATION;
			case 1:
				return TRANSFORMATION;
			default:
				return CREATION;
		}
	}

	/**
	 * Index du mode
	 * @return l'index du mode
	 * @throws AssertionError si le mode est inconnu
	 */
	public int toInteger() throws AssertionError
	{
		switch (this)
		{
			case CREATION:
				return 0;
			case TRANSFORMATION:
				return 1;
		}

		throw new AssertionError("OperationMode Unknown assertion " + this);
	}

	/**
	 * Représentation sous forme de chaine de caractères
	 * @return une chaine de caractères représentant la valeur de cet enum
	 * @throws AssertionError si le mode est inconnu
	 */
	@Override
	public String toString() throws AssertionError
	{
		switch (this)
		{
			case CREATION:
				return new String("Creation");
			case TRANSFORMATION:
				return new String("Edition");
		}

		throw new AssertionError("OperationMode Unknown assertion " + this);
	}


	/**
	 * Mode suivant dans l'ordre des modes
	 * @return le mode suivant le mode courant
	 * @throws AssertionError si le mode est inconnu
	 */
	public OperationMode nextMode() throws AssertionError
	{
		switch (this)
		{
			case CREATION:
				return TRANSFORMATION;
			case TRANSFORMATION:
				return CREATION;
		}

		throw new AssertionError("OperationMode Unknown assertion " + this);
	}

}

