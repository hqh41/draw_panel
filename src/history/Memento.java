package history;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Un état constitué d'une liste de d'éléments de type E constituant
 * l'état à sauvegarder dans le Memento.
 * @note les élements doivent dériver de {@link Prototype} pour pouvoir
 * être effectivement clonés (Deep Copy) dans l'état du Memento.
 * @author davidroussel
 */
public class Memento<E extends Prototype<E>>
{
	/**
	 * La liste d'élément de type E qui constitue l'état à sauvegarder
	 */
	private List<E> state;

	/**
	 * Constructeur par défaut d'un état
	 */
	public Memento(List<E> things)
	{
		this.state = new ArrayList<E>();
		for (E elt : things)
		{
			this.state.add(elt.clone());
		}
	}

	/**
	 * Accesseur à l'état du memento
	 * @return l'état stocké dans le memento
	 */
	public List<E> getState()
	{
		return state;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int hash = 1;
		for (E elt : state)
		{
			hash += (prime * hash) + (elt != null ? elt.hashCode() : 0);
		}
		return hash;
	}

	/**
	 * Comparaison entre deux memento.
	 * Permet de vérifier que les memento stockés dans l'History manager
	 * ne sont pas identiques
	 * @param obj l'objet à comparer
	 * @return true si les deux memento sont identiques en terme de contenu
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj == this)
		{
			return true;
		}

		if (obj instanceof Memento<?>)
		{
			Memento<?> as = (Memento<?>) obj;
			if (!as.state.isEmpty() && !state.isEmpty())
			{
				if (state.get(0).getClass() == as.state.get(0).getClass())
				{
					@SuppressWarnings("unchecked")
					Memento<E> s = (Memento<E>) obj;
					Iterator<E> it1 = state.iterator();
					Iterator<E> it2 = s.state.iterator();

					for(; it1.hasNext() && it2.hasNext();)
					{
						if (!it1.next().equals(it2.next()))
						{
							return false;
						}
					}

					return it1.hasNext() == it2.hasNext();
				}
			}
			else
			{
				if (as.state.isEmpty() && state.isEmpty())
				{
					return true;
				}
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append('[');
		for (Iterator<E> it = state.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append(']');

		return sb.toString();
	}
}
