package filters;

import java.util.function.Predicate;

import figures.Figure;

/**
 * Prédicat permettant de filtrer les figures à partir d'un élément de type T.
 * T pourra être instancié avec divers types dans les classes filles pour
 * filtrer :
 * <ul>
 * 	<li>le type de figures: {@link figures.enums.FigureType}</li>
 *	<li>la couleur de remplissage ou de trait: {@link java.awt.Paint}</li>
 *	<li>le type de trait: {@link figures.enums.LineType}</li>
 * </ul>
 * @author davidroussel
 */
public abstract class FigureFilter<T> implements Predicate<Figure>
{
	/**
	 * L'élément sur lequel filter les figures
	 */
	protected T element;

	/**
	 * Constructeur par défaut
	 */
	public FigureFilter()
	{
		element = null;
	}

	/**
	 * Constructeur d'un figure filter
	 * @param element l'élément de référence du prédicat
	 */
	public FigureFilter(T element)
	{
		this.element = element;
	}

	/**
	 * Accesseur à l'élément du filtre
	 * @return l'élément du filtre
	 */
	public T getElement()
	{
		return element;
	}

	/**
	 * Test du prédicat
	 * @param f la figure à tester
	 * @return vrai si un élément de la figure f correspond à l'élément contenu
	 * dans ce prédicat (par exemple figure.getType() == element pour filtrer
	 * les types de figures)
	 * @see java.util.function.Predicate#test(java.lang.Object)
	 */
	@Override
	public abstract boolean test(Figure f);

	/**
	 * Caomparaison avec un autr objet
	 * @param obj l'objet à comparer
	 * @erturn true si l'autre objet est un filtre sur le même type d'élément
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return  false;
		}

		if (obj == this)
		{
			return true;
		}

		if (obj instanceof FigureFilter<?>)
		{
			FigureFilter<?> ff = (FigureFilter<?>) obj;
			if ((ff.element != null) && (element != null))
			{
				if (ff.element.getClass() == element.getClass())
				{
					@SuppressWarnings("unchecked")
					FigureFilter<T> fft = (FigureFilter<T>)ff;
					return element.equals(fft.element);
				}
				else
				{
					if ((element != null) || (ff.element != null))
					{
						return false;
					}
					else
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Chaîne de caractères représentant le filtre
	 * @return une chaine de caractère représentantn le filtre
	 */
	@Override
	public String toString()
	{
		return new String(getClass().getSimpleName() + "<"
			+ (element != null ? element.getClass().getSimpleName() : "null")
			+ ">(" + (element != null ? element.toString() : "") + ")");
	}
}
