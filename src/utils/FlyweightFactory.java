package utils;

import java.util.HashMap;

/**
 * Flyweight gérant les différents éléments utilisés dans la zone de dessin.
 * Utilisable avec les {@link Paint} et avec les {@link BasicStroke} des figures
 * Gère les éléments dans une HashMap<Integer, T> dont la clé correspond au
 * hashCode de l'élément correspondant. Lorsque l'on demande un élément à la
 * Factory, celui ci le recherche dans sa table de hachage : Si l'élément n'est
 * pas déjà présent dans la table de hachage il est ajouté, puis renvoyé, s'il
 * est déjà présent dans la table de hachage il est directement renvoyé et celui
 * demandé est alors destructible par le garbage collector.
 *
 * @author davidroussel
 */
public class FlyweightFactory<T>
{
	/**
	 * La table de hachage contenant les différentes paires <hashcode,elt> et
	 * dont les clés sont les hashCode des différents élements.
	 */
	protected HashMap<Integer, T> map;

	/**
	 * Constructeur d'un FlyweightFactory.
	 * Initialise la {@link HashMap}
	 */
	public FlyweightFactory()
	{
		map = new HashMap<Integer, T>();
	}

	/**
	 * Obtention d'un élément à partir son hashcode plutôt que par l'élément
	 * lui même
	 * @param hash le hachage de l'élément demandé
	 * @return l'élément correspondant au hachage demandé ou bien null si aucun
	 * élément avec ce hachage n'est contenu dans la factory
	 * @note cette méthode est nécessaire lorsque l'on veut stocker dans la
	 * factory des éléments qui ne réimplémentent pas la méthode hashCode.
	 * Auquel cas on fournit soi même un code de hachage.
	 */
	protected T get(int hash)
	{
		Integer key = Integer.valueOf(hash);
		if (map.containsKey(key))
		{
			return map.get(key);
		}

		return null;
	}

	/**
	 * Ajout d'un élément à la factory en fournissant un hashcode particulier
	 * @param hash le hachage voulu pour cet élément
	 * @param element l'élément à ajouter
	 * @return true si aucun élément avec ce hachage n'était contenu dans la
	 * factory et que le couple hash/value a bien été ajouté à la factory
	 * @note cette méthode est nécessaire lorsque l'on veut stocker dans la
	 * factory des éléments qui ne réimplémentent pas la méthode hashCode.
	 * Auquel cas on fournit soi même un code de hachage.
	 */
	protected boolean put(int hash, T element)
	{
		Integer key = Integer.valueOf(hash);
		if (!map.containsKey(key))
		{
			if (element != null)
			{
				map.put(key, element);
//				System.out.println("Added " + element
//					+ " to the flyweight factory which contains "
//					+ map.size() + " elements");
				return true;
			}
			else
			{
				System.err.println("FlyweightFactory::put(...) : null element");
			}
		}
		return false;
	}

	/**
	 * Obtention d'un élément (nouveau ou pas) : Lorsque l'élément demandé est
	 * déjà présent dans la table on le renvoie directement sinon celui ci est
	 * ajouté à la table avant d'être renvoyé
	 * @param element l'élément demandé [celui ci pourra être détruit par le
	 * garbage collector si il en existe déjà un équivalent dans la table]
	 * @return l'élément demandé en provenance de la table
	 */
	public T get(T element)
	{
		if (element != null)
		{
			int hash = element.hashCode();
			T result = get(hash);
			if (result == null)
			{
				put(hash, element);
				result = get(hash);
			}
			return result;
		}
		return null;
	}

	/**
	 * Nettoyage de tous les éléments
	 */
	public void clear()
	{
		map.clear();
	}

	/**
	 * Nettoyage avant destruction de la factory
	 */
	@Override
	protected void finalize()
	{
		clear();
	}
}
