package history;

/**
 * Une interface déclarant un prototype public
 * (contrairement à Object qui possède une méthode clone mais qui est protégée)
 * @author davidroussel
 */
public interface Prototype<E>
{
	/**
	 * Création d'une copie (distincte mais égale)
	 * @return la copie de l'opjet à cloner
	 */
	public E clone();
}
