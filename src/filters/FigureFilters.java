package filters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Predicate;

import figures.Figure;

/**
 * Collection de filtres 
 * @author davidroussel
 */
public class FigureFilters<T> implements Collection<FigureFilter<T>>, Predicate<Figure>
{
	/**
	 * Vecteur de filtres
	 */
	private Vector<FigureFilter<T>> filters;
		
	/**
	 * Constructeur par défaut
	 */
	public FigureFilters()
	{
		filters = new Vector<FigureFilter<T>>();
	}

	/**
	 * Test du prédicat
	 * @param f la figure à tester
	 * @return true si l'un au moins des prédicats de la collection est vrai,
	 * false sinon 
	 * @see filters.FigureFilter#test(figures.Figure)
	 */
	@Override
	public boolean test(Figure f)
	{
		boolean result = false;
		
		for (FigureFilter<T> ff : this)
		{
			boolean thisResult = ff.test(f);
			// System.out.println(ff + (thisResult ? "passed": "denied"));
			result |= thisResult;
		}
		
		// System.out.println(this +  (result ? "passed": "denied"));
		
		return result;
	}

	/**
	 * Taille de la collection
	 * @return la taille de la collection
	 */
	@Override
	public int size()
	{
		return filters.size();
	}

	/**
	 * Teste si la collection est vide
	 * @return true si la collection est vide
	 */
	@Override
	public boolean isEmpty()
	{
		return filters.isEmpty();
	}

	/**
	 * Test de contenu d'un objet dans la collection de filtres
	 * @param o l'objet recherché dans la collection de filtres
	 * @return true si l'objet est contenu dans la collection de filtres
	 */
	@Override
	public boolean contains(Object o)
	{
		return filters.contains(o);
	}

	/**
	 * Itérateur de la collection de {@link FigureFilter}
	 * @return l'itérateur sur les filtres de la collection
	 */
	@Override
	public Iterator<FigureFilter<T>> iterator()
	{
		return filters.iterator();
	}

	/**
	 * Conversion en tableau d'objets
	 * @return un tableau d'objets contenant les éléments de la collection
	 */
	@Override
	public Object[] toArray()
	{
		return filters.toArray();
	}

	/**
	 * Conversion en tableau générique
	 * @param a un tableau générique spécimen
	 * @return un tableau générique contenant les éléments de la collection
	 */
	@Override
	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a)
	{
		return filters.toArray(a);
	}

	/**
	 * Ajout d'un nouveau filtre à la collection uniquement si celle ci ne 
	 * contient pas déjà ce filtre
	 * @param filter le filtre à ajouter
	 * @return true si le filtre à ajouté n'était pas déjà présent dans la 
	 * collection et qu'il a été ajouté
	 */
	@Override
	public boolean add(FigureFilter<T> filter)
	{
		if (!contains(filter))
		{
			return filters.add(filter);
		}
		else
		{
			return false;
		}
	}

	/**
	 * Retrait d'un objet de la collection
	 * @param o l'objet à retirer de la collection
	 * @return true si l'objet a été retiré de la collection
	 */
	@Override
	public boolean remove(Object o)
	{
		return filters.remove(o);
	}

	/**
	 * Test si une collection est entièrement contenue dans la collection
	 * @param c la collection à tester
	 * @return true si la collection c est entièrement contenue dans la 
	 * collection
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return filters.containsAll(c);
	}

	/**
	 * Ajout d'une collection de {@link FigureFilters} à la collection courante
	 * @param c la collection de {@link FigureFilter} à ajouter
	 * @return true si au moins un élément de la collection c a été ajouté
	 * à la collection courante
	 */
	@Override
	public boolean addAll(Collection<? extends FigureFilter<T>> c)
	{
		boolean added = false;
		for (FigureFilter<T> ff : c)
		{
			if (!contains(ff))
			{
				added |= add(ff);
			}
		}
		
		return added;
	}

	/**
	 * Retrait de tous les éléments d'une collection de la collection courante
	 * @param c la collection à retirer de la collection courante
	 * @return true si la collection courante a été modifiée par cette opération
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return filters.removeAll(c);
	}

	/**
	 * Conservation dans la collection courante uniquement des éléments présents
	 * dans la collection c
	 * @param c la collection qui détermine les éléments à conserver dans la 
	 * collection courante
	 * @return true si la collection courante a été modifiée par cette opération
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean retained = filters.retainAll(c);
		
		// remove doubles
		
		return retained;
	}

	/**
	 * Effacement de la collection
	 */
	@Override
	public void clear()
	{
		filters.clear();
	}

	/**
	 * Réprésentation de la collection de filtres
	 * @return une chaine de caractères représentantn tous les filtres
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(getClass().getSimpleName());
		sb.append("[");
		sb.append(filters.size());
		sb.append("]\n");
		for ( FigureFilter<T> ff : filters)
		{
			sb.append(ff.toString() + "\n");
		}
		
		return sb.toString();
	}
	
	
}
