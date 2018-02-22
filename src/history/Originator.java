package history;

/**
 * Interface pour les classes créant et récupérant des Memento de leur état
 * @author davidroussel
 */
public interface Originator<E extends Prototype<E>>
{
	/**
	 * Création d'un Memento
	 * @return le Memento contenant l'état de l'Originator
	 */
	public abstract Memento<E> createMemento();

	/**
	 * Remplacement de l'état courant par celui contenu dans le Memento
	 * @param memento le memento contenant l'état à mettre en place
	 * @post l'état contenu dans le Memento a remplacé l'état courant,
	 * SAUF si le memento est null
	 */
	public abstract void setMemento(Memento<E> memento);

}
