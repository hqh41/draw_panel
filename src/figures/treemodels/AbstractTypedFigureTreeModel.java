package figures.treemodels;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;

import figures.Drawing;
import figures.Figure;
import filters.FigureFilter;

/**
 * Figure TreeModel dans lequel les noeuds de niveau 1 sont une caractéristique
 * de figures et les noeuds de niveau 2 les figures elles mêmes.
 * Exemple :
 * Titre de l'arbre
 * + Type 1
 * | + Figure de Type 1 1
 * | + Figure de Type 1 2
 * + Type 2
 * | + Figure de Type 2 1
 * + Type 3
 *   + Figure de Type 3 1
 * @author davidroussel
 */
public abstract class AbstractTypedFigureTreeModel<E> extends AbstractFigureTreeModel
{
	/**
	 * Le Dictionnaire des figures
	 * - Les clefs sont une caractéristique des figures
	 * - Les valeurs des listes de figures correspondant à cette caractéristique
	 * Cette map doit être concurrente ET triée :
	 * 	- Concurrente car la méthode {@link #updateFiguresFromDrawing(List)}
	 * 	risque d'être appellée de manière récursive à chaque fireXXXEvent
	 * 	- Et triée de manière à ce que les clés restent toujours dans le même
	 * 	ordre
	 */
	protected ConcurrentMap<E, List<Figure>> map;

	/**
	 * L'instance de la classe {@link Class} correspondant aux éléments de
	 * type E de manière à pouvoir comparer les types en utilisant cet attribut
	 */
	protected Class<E> elementType;

	/**
	 * Constructeur de l'arbre des types de figures
	 * @param drawing le modèle de dessin
	 * @param tree le JTree utilisé pour visualiser cet arbre
	 * @param title le nom de la racine de cet arbre
	 */
	public AbstractTypedFigureTreeModel(Class<E> elementType,
	                                   Drawing drawing,
	                                   JTree tree,
	                                   String title)
	    throws NullPointerException
	{
		super(drawing, tree, title);
		map = new ConcurrentSkipListMap<E, List<Figure>>(); // Triée & concurrente

		if (elementType != null)
		{
			this.elementType = elementType;
		}
		else
		{
			throw new NullPointerException("AbstractTypeFigureTreeModel : null element type");
		}

		update(drawing, null); // force Tree build
	}

	/**
	 * Nettoyage avant destruction
	 */
	@Override
	protected void finalize() throws Throwable
	{
		if (map != null)
		{
			Set<E> keySet = map.keySet();
			for (Iterator<E> keyIt = keySet.iterator(); keyIt.hasNext();)
			{
				List<Figure> keyFigures = map.get(keyIt.next());
				keyFigures.clear();
			}
			map.clear();
		}
	}

	/**
	 * Récupère la valeur de type E d'une figure utilisée pour les noeuds
	 * de niveau 1 de l'arbre
	 * @param f la figure à interroger
	 * @return la valeur de type E contenue dans cette figure en utilisant
	 * l'accesseur adéquat.
	 */
	public abstract E getValueFrom(Figure f);

	/**
	 * Obtention d'un filtre filtrant les figures possédant la même
	 * caractéristique de type E que la figure f
	 * @param l'élément de type E à utiliser pour le filtre.
	 * @return le filtre correspondant à la caractéristique de type E de la
	 * figure f
	 */
	public abstract FigureFilter<E> getFilter(E element);

	/**
	 * Mise à jour des figures de l'arbre en les comparant une par une aux
	 * figures du modèle de dessin.
	 * Permet l'enlever/ajouter les figures de l'arbre en fonction des
	 * modifications observées dans les figures du modèle de dessin.
	 * @param figures les figures du modèle de dessin
	 */
	@Override
	protected void updateFiguresFromDrawing(List<Figure> figures)
	{
		/*
		 * Tant que map n'est pas construit on update pas.
		 */
		if (map == null)
		{
			return;
		}

		/*
		 * Construction d'une map du même type que celle utilisée
		 * dans ce treemodel avec les figures du modèle passées en argument
		 */
		Map<E, List<Figure>> dmap = new TreeMap<E, List<Figure>>();
		for (Iterator<Figure> drawIt = figures.iterator(); drawIt.hasNext();)
		{
			Figure figure = drawIt.next();
			E type = getValueFrom(figure);
			List<Figure> keyFigure = dmap.get(type);
			if (keyFigure == null)
			{
				dmap.put(type, new Vector<Figure>());
				keyFigure = dmap.get(type);
			}
			keyFigure.add(figure);
		}

		TreePath rootPath = new TreePath(new Object[] { rootElement });

		/*
		 * Comparaison des figures du tree avec les figures du modèle
		 * en vue de déterminer
		 * 	- les noeuds de l'arbre à supprimer
		 * 	- les noeuds à ajouter à l'arbre
		 */
		// --------------------------------------------------------------------
		// Comparaison Tree --> Draw : Types, en vue d'enlever des types
		//---------------------------------------------------------------------
		List<Integer> removeNodes1IndexList = new Vector<Integer>();
		List<Object> removeNodes1ObjectList = new Vector<Object>();
		Set<E> treeKeySet = map.keySet();
		synchronized (map)
		{
			int nbNodesInitialBeforeRemove1 = map.size();
			int typeIndex = 0;
			for(Iterator<E> treeKeyIt = treeKeySet.iterator(); treeKeyIt.hasNext(); )
			{
				E treeType = treeKeyIt.next();
				if (!dmap.containsKey(treeType))
				{
					// Retrait de ce type de la map
					map.remove(treeType);
					// Index & Object pour la MAJ Listeners
					removeNodes1IndexList.add(new Integer(typeIndex));
					removeNodes1ObjectList.add(treeType);
				}
				typeIndex++;
			}

			// Notification noeuds 1 supprimés
			int nbNodes1Removed = removeNodes1IndexList.size();
			if(nbNodes1Removed > 0)
			{
				int [] removeNodes1Index = new int[nbNodes1Removed];
				for (int i = 0; i < nbNodes1Removed; i++)
				{
					removeNodes1Index[i] = removeNodes1IndexList.get(i).intValue();
				}
				if (nbNodes1Removed < nbNodesInitialBeforeRemove1)
				{
					fireTreeNodesRemoved(rootPath,
					                     removeNodes1Index,
					                     removeNodes1ObjectList.toArray());
				}
				else
				{
					fireTreeStructureChanged(rootPath);
				}
			}
		}

		//---------------------------------------------------------------------
		// Comparaison Tree --> Draw : figures, en vue de retirer des figures
		//---------------------------------------------------------------------
		treeKeySet = map.keySet();
		for (Iterator<E> treeKeyIt = treeKeySet.iterator(); treeKeyIt.hasNext();)
		{
			E type = treeKeyIt.next();
			List<Figure> treeKeyFigures = map.get(type);
			List<Figure> drawKeyFigures = dmap.get(type);
			List<Integer> removeNodes2IndexList = new Vector<Integer>();
			List<Object> removeNodes2ObjectList = new Vector<Object>();

			synchronized (map)
			{
				int nbNodesInitialBeforeRemove2 = treeKeyFigures.size();
				int figureIndex = 0;
				for (Iterator<Figure> treeIt = treeKeyFigures.iterator(); treeIt.hasNext();)
				{
					Figure figure = treeIt.next();
					if (drawKeyFigures.indexOf(figure) != figureIndex)
					{
						// Cette figure doit être enlevée
						treeIt.remove();
						// Index & Object pour la MAJ Listeners
						removeNodes2IndexList.add(new Integer(figureIndex));
						removeNodes2ObjectList.add(figure);
					}
					figureIndex++;
				}

				int nbRemoved = removeNodes2IndexList.size();
				if (nbRemoved > 0)
				{
					TreePath parentPath = new TreePath(new Object[]{rootElement, type});
					int[] removeNodes2Index = new int[nbRemoved];
					for (int i = 0; i < nbRemoved; i++)
					{
						removeNodes2Index[i] = removeNodes2IndexList.get(i).intValue();
					}

					if (nbRemoved < nbNodesInitialBeforeRemove2)
					{
						fireTreeNodesRemoved(parentPath,
						                     removeNodes2Index,
						                     removeNodes2ObjectList.toArray());
					}
					else
					{
						fireTreeStructureChanged(parentPath);
					}
				}
			}
		}

		//---------------------------------------------------------------------
		// Comparaison Draw --> Tree : Types en vue d'ajouter des types
		//---------------------------------------------------------------------
		Set<E> drawKeySet = dmap.keySet();
		List<Integer> addNodes1IndexList = new Vector<Integer>();
		List<Object> addNodes1ObjectList = new Vector<Object>();
		synchronized (map)
		{
			int nbNodesInitialBeforeAdd1 = map.size();
			int typeIndex = 0;
			for (Iterator<E> drawKeyIt = drawKeySet.iterator(); drawKeyIt.hasNext(); )
			{
				E drawType = drawKeyIt.next();
				if (!map.containsKey(drawType))
				{
					// Ajout de ce type à map
					map.put(drawType, new Vector<Figure>());
					// Index & Object pour la MAJ Listeners
					addNodes1IndexList.add(new Integer(typeIndex));
					addNodes1ObjectList.add(drawType);
				}
				typeIndex++;
			}

			// Notification noeuds 1 ajoutés
			int nbNodes1Added = addNodes1IndexList.size();
			if(nbNodes1Added > 0)
			{
				int [] addNodes1Index = new int[nbNodes1Added];
				for (int i = 0; i < nbNodes1Added; i++)
				{
					addNodes1Index[i] = addNodes1IndexList.get(i).intValue();
				}
				if (nbNodesInitialBeforeAdd1 > 0)
				{
					fireTreeNodesInserted(rootPath,
					                      addNodes1Index,
					                      addNodes1ObjectList.toArray());
				}
				else
				{
					fireTreeStructureChanged(rootPath);
				}
			}
		}

		//---------------------------------------------------------------------
		// Comparaison Draw --> Tree : Figures, en vue d'ajouter des figures
		//---------------------------------------------------------------------
		for (Iterator<E> drawKeyIt = drawKeySet.iterator(); drawKeyIt.hasNext();)
		{
			E type = drawKeyIt.next();
			List<Figure> drawKeyFigures = dmap.get(type);
			List<Figure> treeKeyFigures = map.get(type);
			List<Integer> addNodes2IndexList = new Vector<Integer>();
			List<Object> addNodes2ObjectList = new Vector<Object>();
			synchronized (map)
			{
				int nbNodesInitialBeforeAdd2 = treeKeyFigures.size();
				int figureIndex = 0;
				for (Iterator<Figure> drawIt = drawKeyFigures.iterator(); drawIt.hasNext();)
				{
					Figure figure = drawIt.next();
					if (treeKeyFigures.indexOf(figure) != figureIndex)
					{
						// Cette figure doit être insérée
						treeKeyFigures.add(figureIndex, figure);
						// Index & Object pour la MAJ Listeners
						addNodes2IndexList.add(new Integer(figureIndex));
						addNodes2ObjectList.add(figure);
					}
					figureIndex++;
				}

				int nbAdded = addNodes2IndexList.size();
				if (nbAdded > 0)
				{
					TreePath parentPath = new TreePath(new Object[]{rootElement, type});
					int[] addNodes2Index = new int[nbAdded];
					for (int i = 0; i < nbAdded; i++)
					{
						addNodes2Index[i] = addNodes2IndexList.get(i).intValue();
					}

					if (nbNodesInitialBeforeAdd2 > 0)
					{
						fireTreeNodesInserted(parentPath,
						                      addNodes2Index,
						                      addNodes2ObjectList.toArray());
					}
					else
					{
						fireTreeStructureChanged(parentPath);
					}
				}
			}
		}
	}

	/**
	 * Mise à jour de {@link #selectedFigures} d'après les figures de l'arbre
	 * sélectionnées.
	 */
	@Override
	protected void updateSelectedFigures()
	{
		if (map != null)
		{
			Set<E> keySet = map.keySet();
			for (Iterator<E> keyIt = keySet.iterator(); keyIt.hasNext();)
			{
				E type = keyIt.next();
				List<Figure> keyFigures = map.get(type);
				for (Iterator<Figure> figIt = keyFigures.iterator(); figIt.hasNext();)
				{
					Figure figure = figIt.next();
					if (figure.isSelected())
					{
						TreePath selectedPath = new TreePath(new Object[]{
							rootElement,
							type,
							figure
						});
						selectedFigures.add(selectedPath);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object parent, int index)
	{
		if (map != null)
		{
			if (parent == rootElement)
			{
				if ((index >= 0) && (index < map.size()))
				{
					Set<E> keySet = map.keySet();
					int count = 0;
					E currentKey = null;
					for (Iterator<E> keyIt = keySet.iterator();
						keyIt.hasNext() && (count <= index); count++)
					{
						currentKey = keyIt.next();
					}

					return currentKey;
				}
			}
			else if (elementType.isInstance(parent)) // (parent instanceof E)
			{
				@SuppressWarnings("unchecked")
				E type = (E) parent;
				List<Figure> keyFigures = map.get(type);

				if (keyFigures != null)
				{
					if ((index >= 0) && (index < keyFigures.size()))
					{
						return keyFigures.get(index);
					}
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent)
	{
		if (map != null)
		{
			if (parent == rootElement)
			{
				return map.size();
			}
			else if (elementType.isInstance(parent)) // (parent instanceof E)
			{
				@SuppressWarnings("unchecked")
				E type = (E) parent;
				List<Figure> keyFigures = map.get(type);
				if (keyFigures != null)
				{
					return keyFigures.size();
				}
			}
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object node)
	{
		if (node instanceof Figure)
		{
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		if (map != null)
		{
			if (parent == rootElement)
			{
				// searching in Types for child
				Set<E> keySet = map.keySet();
				int index = 0;
				for (Iterator<E> keyIt = keySet.iterator(); keyIt.hasNext();)
				{
					if (keyIt.next().equals(child))
					{
						return index;
					}
					index++;
				}
			}
			else if (elementType.isInstance(parent)) // (parent instanceof E)
			{
				// searching in Typed Figures for child
				@SuppressWarnings("unchecked")
				E type = (E) parent;
				List<Figure> keyFigures = map.get(type);
				if (keyFigures != null)
				{
					return keyFigures.indexOf(child);
				}
			}
		}

		return -1;
	}

	/**
	 * Callback déclenché lorsqu'un noeud est sélectionné dans le
	 * {@link #treeView}
	 * @param e l'évènement de sélection dans le {@link JTree}
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		JTree tree = (JTree) e.getSource();
		int count = tree.getSelectionCount();
		TreePath[] paths = tree.getSelectionPaths();

		if (!selfEvent)
		{
			drawing.clearSelection();

			for (int i = 0; i < count; i++)
			{
				Object[] objPath = paths[i].getPath();
				int pathSize = paths[i].getPathCount();
				Object node = objPath[pathSize - 1];
				if (node == rootElement) // select all figures
				{
					drawing.stream().forEach((Figure f) -> {
						f.setSelected(true);
					});
				}
				if (elementType.isInstance(node)) // select all figures of this type
				{
					@SuppressWarnings("unchecked")
					E type = (E) node;
					drawing.stream()
					       .filter(getFilter(type))
					       .forEach((Figure f) ->
					       {
					    	   f.setSelected(true);
					       });
				}
				if (node instanceof Figure) // Select one figure
				{
					Figure figure = (Figure) node;
					// figure.setSelected(true);
					drawing.stream().forEach((Figure f) ->
					{
						if (f.equals(figure))
						{
							f.setSelected(true);
						}
					});
				}
			}

			drawing.updateSelection();
		}

		selfEvent = false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(rootElement).append("\n");

		if (map != null)
		{
			Set<E> keySet = map.keySet();
			for (Iterator<E> keyIt = keySet.iterator(); keyIt.hasNext();)
			{
				E type = keyIt.next();
				sb.append("+--").append(type.toString()).append('s')
				    .append("\n");

				List<Figure> keyFigures = map.get(type);
				for (Iterator<Figure> figureIt =
				    keyFigures.iterator(); figureIt.hasNext();)
				{
					sb.append("   +--").append(figureIt.next().toString())
					    .append("\n");
				}
			}
		}

		return sb.toString();
	}
}
