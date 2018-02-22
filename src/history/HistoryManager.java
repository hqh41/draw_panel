package history;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe permettant de gérer les piles de Undo et de Redo de E
 * @param E l'état à sauvegarder dans les piles
 * @author davidroussel
 */
public class HistoryManager<E extends Prototype<E>>
{
	/**
	 * Le nombre maximum d'undo / redo
	 */
	private int size;

	/**
	 * L'originator dont on doit sauvegarder l'état.
	 * Permet de demander à l'originator de générer un memento ou de
	 * mettre en place un memento qu'on lui fournit
	 */
	private Originator<E> originator;

	/**
	 * La pile des Undo
	 * @note les {@link Deque} permettent d'empiler/dépiler en tête de liste
	 * mais aussi d'accéder au dernier élément pour garder des piles de taille
	 * inférieure ou égale à {@link #size}
	 */
	private Deque<Memento<E>> undoStack;

	/**
	 * La pile de Redo
	 */
	private Deque<Memento<E>> redoStack;

	/**
	 * Constructeur du manager de Undo/Redo
	 */
	public HistoryManager(Originator<E> origin, int size)
	{
		this.size = size;
		originator = origin;
		undoStack = new LinkedList<Memento<E>>();
		redoStack = new LinkedList<Memento<E>>();
	}

	@Override
	protected void finalize() throws Throwable
	{
		undoStack.clear();
		redoStack.clear();
		super.finalize();
	}

	/**
	 * Nombre d'éléments accumulés dans la pile de undo
	 * @return Le nombre d'éléments accumulés dans la pile de undo
	 */
	public int undoSize()
	{
		return undoStack.size();
	}

	/**
	 * Nombre d'éléments accumulés dans la pile de redo
	 * @return Le nombre d'éléments accumulés dans la pile de redo
	 */
	public int redoSize()
	{
		return redoStack.size();
	}

	/**
	 * Ajout d'un état dans la pile des undo
	 * @param state l'état à ajouter dans la pile des undo
	 * @return true si le memento était non null, différent du dernier
	 * Memento ajouté à la pile des undo et a été ajouté à la pile des undo
	 * @note si le nombre d'états dans la pile des undo dépasse {@link #size}
	 * alors le tout premier état empilé est supprimé de la pile
	 */
	private boolean pushUndo(Memento<E> state)
	{
		if (state != null)
		{
			/*
			 * 	- On vérifie que le memento que l'on cherche à ajouter est
			 * 	bien différent du dernier ajouté
			 * 	- On ajoute ce memento à la pile des undo
			 * 	- Si le nombre de mementos dans la pile dépasse #size alors
			 * 	on enlève le premier memento de manière à a garder au maximum
			 * 	#size mementos dans la pile
			 */
			if (!state.equals(undoStack.peek()))
			{
				if (undoSize()>size)
				{
					undoStack.removeFirst();
				}
				undoStack.push(state);
				return true;
			}
		}
		else
		{
			System.err.println("HistoryManager::pushUndo(null)");
		}
		return false;
	}

	/**
	 * Dépilage du dernier état empilé dans la pile des undo
	 * @return l'état qui était en haut de la pile des undo, ou bien null
	 * s'il n'y avait pas d'état en haut de la pile
	 */
	private Memento<E> popUndo()
	{
		Memento<E> state = undoStack.pop();

		/*
		 * dépiler le dernier memento empilé
		 */

		return state;
	}

	/**
	 * Ajout d'un état dans la pile des redo
	 * @param state l'état à ajouter dans la pile des redo
	 * @return true si le memento était non null, différent du dernier
	 * Memento ajouté à la pile des undo et a été ajouté à la pile des undo
	 * @note si le nombre d'états dans la pile des redo dépasse {@link #size}
	 * alors le tout premier état empilé est supprimé de la pile
	 */
	private boolean pushRedo(Memento<E> state)
	{
		if (state != null)
		{
			/*
			 * 
			 * 	- On vérifie que le memento que l'on cherche à ajouter est
			 * 	bien différent du dernier ajouté
			 * 	- On ajoute ce memento à la pile des redo
			 * 	- Si le nombre de mementos dans la pile dépasse #size alors
			 * 	on enlève le premier memento de manière à a garder au maximum
			 * 	#size mementos dans la pile
			 */
			if (!state.equals(redoStack.peek()))
			{
				if (redoSize()>size)
				{
					redoStack.removeFirst();
				}
				redoStack.push(state);
				return true;
			}
		}
		else
		{
			System.err.println("HistoryManager::pushRedo(null)");
		}
		return false;
	}

	/**
	 * Dépilage du dernier état empilé dans la pile des redo et donc réempilage
	 * de cet état dans la pile des undo
	 * @return l'état dépilé de la pile des redo
	 */
	private Memento<E> popRedo()
	{
		Memento<E> state = redoStack.pop();

		/*
		 * dépiler le dernier memento empilé
		 */

		return state;
	}

	/**
	 * Enregistre un {@link Memento} de l'{@link #originator} pour pouvoir
	 * le restituer par la suite.
	 */
	public void record()
	{
		/*
		 * 	- Demander à l'originator de créer un memento
		 * 	- Empiler ce memento dans la pile des undo
		 * 	- Effacer la pile des redo
		 */
		Memento<E> momento = originator.createMemento();
		pushUndo(momento);
		redoStack.clear();
	}

	/**
	 * Restitue le dernier Memento sauvegardé dans la pile des undo
	 * @return le dernier memento sauvegardé dans la pile des undo
	 * ({@link #undoStack}), ou bien null si celle-ci est vide.
	 * @post un {@link Memento} de l'{@link #originator} a été créé au préalable
	 * dans la pile des redo.
	 */
	public Memento<E> undo()
	{
		/*
		 * 
		 * 	- Dépiler un élément de la pile des undo (s'il y en a un)
		 * 	- Tout en sauvegardant l'état courant de l'originator dans la pile
		 * 	des redo.
		 */
		if (originator!=null)
		{
			pushRedo(originator.createMemento());
			return popUndo();
		}		
		return null;
	}

	/**
	 * Annule le dernier {@link Memento} enregistré dans la pile des undo.
	 * Lorsque l'action n'a pas modifié l'état (par exemple si elle a échoué)
	 */
	public void cancel()
	{
		popUndo();
	}

	/**
	 * Restitue de dernier Memento sauvegardé dans la pile des redo
	 * @return Le dernier Memento sauvegardé dans la pile des redo
	 * ({@link #redoStack}) ou bien null si celle-ci est vide.
	 * @post un {@link Memento} de l'{@link #originator} a été créé au préalable
	 * dans la pile des undo.
	 */
	public Memento<E> redo()
	{
		/*
		 * TODO
		 * 	- Dépiler un élément de la pile des redo (s'il y en a un)
		 * 	- Tout en sauvegardant l'état courant de l'originator dans la pile
		 * 	des undo
		 */
		if (originator!=null)
		{
			pushUndo(originator.createMemento());
			return popRedo();
		}
		return null;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(super.toString());
		sb.append("[" + String.valueOf(size) + "] :\nUndo = {");
		for (Iterator<Memento<E>> it = undoStack.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append("},\nRedo = {");
		for (Iterator<Memento<E>> it = redoStack.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
