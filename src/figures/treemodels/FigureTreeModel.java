package figures.treemodels;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import figures.Drawing;
import figures.Figure;

/**
 * Figure TreeModel dans lequel les noeuds de niveau 1 sont les figures,
 * Il n'y a pas de noeuds de niveau 2.
 * @author davidroussel
 */
public class FigureTreeModel extends AbstractFigureTreeModel
{
	/**
	 * La liste des figure dans l'arbre
	 */
	private List<Figure> figures;

	/**
	 * Constructeur de l'arbre des types de figures
	 * @param drawing le modèle de dessin
	 * @param tree le JTree utilisé pour visualiser cet arbre
	 */
	public FigureTreeModel(Drawing drawing, JTree tree) throws NullPointerException
	{
		super(drawing, tree, "Figures");
		figures = new Vector<Figure>();
		update(drawing, null); // force Tree build
	}

	/**
	 * Mise à jour des figures de l'arbre en les comparant une par une aux
	 * figures du modèle de dessin.
	 * Permet l'enlever/ajouter les figures de l'arbre en fonction des
	 * modifications observées dans les figures du modèle de dessin.
	 * @param figures les figures du modèle de dessin
	 */
	@Override
	protected synchronized void updateFiguresFromDrawing(List<Figure> figures)
	{
		/*
		 * Tant que this.figures n'est pas construit on update pas.
		 */
		if (this.figures == null)
		{
			return;
		}

		TreePath parentPath = new TreePath(new Object[] { rootElement });

		/*
		 * Comparaison des figures du tree avec les figures du modèle
		 * en vue de déterminer
		 * 	- les noeuds de l'arbre à supprimer : comparaison tree --> model
		 * 	- les noeuds à ajouter à l'arbre : comparaison model --> tree
		 */
		// Comparaison Tree --> Model : noeuds à enlever
		List<Integer> removeChildIndexList = new Vector<Integer>(this.figures.size());
		List<Object> removeNodesList = new Vector<Object>(this.figures.size());
		int nbNodesInitial = this.figures.size();
		int figureIndex = 0;
		for (Iterator<Figure> treeIt = this.figures.iterator(); treeIt.hasNext();)
		{
			Figure figure = treeIt.next();
			if (figures.indexOf(figure) != figureIndex)
			{
				// Cette figure doit être enlevée
				treeIt.remove();
				// Index & Object pour la MAJ Listeners
				removeChildIndexList.add(new Integer(figureIndex));
				removeNodesList.add(figure);
			}
			figureIndex++;
		}

		int nbRemoved = removeChildIndexList.size();
		if (nbRemoved > 0)
		{
			int[] removeChildIndex = new int[nbRemoved];
			for (int i = 0; i < nbRemoved; i++)
			{
				removeChildIndex[i] = removeChildIndexList.get(i).intValue();
			}

			if (nbRemoved < nbNodesInitial)
			{
				fireTreeNodesRemoved(parentPath,
				                     removeChildIndex,
				                     removeNodesList.toArray());
			}
			else
			{
				fireTreeStructureChanged(parentPath);
			}
		}

		// Comparaison Model --> Tree : noeuds à ajouter
		List<Integer> addChildIndexList = new Vector<Integer>(figures.size());
		List<Object> addNodesList = new Vector<Object>(figures.size());
		nbNodesInitial = this.figures.size();
		figureIndex = 0;
		for (Iterator<Figure> drawIt = figures.iterator(); drawIt.hasNext();)
		{
			Figure figure = drawIt.next();
			if (this.figures.indexOf(figure) != figureIndex)
			{
				// Cette figure doit être ajoutée
				this.figures.add(figureIndex, figure);
				// Index & Object pour la MAJ Listeners
				addChildIndexList.add(new Integer(figureIndex));
				addNodesList.add(figure);
			}
			figureIndex++;
		}

		int nbAdded = addChildIndexList.size();
		if (nbAdded > 0)
		{
			int[] addChildIndex = new int[nbAdded];
			for (int i = 0; i < nbAdded; i++)
			{
				addChildIndex[i] = addChildIndexList.get(i).intValue();
			}

			if (nbNodesInitial > 0)
			{
				fireTreeNodesInserted(parentPath,
				                      addChildIndex,
				                      addNodesList.toArray());
			}
			else
			{
				fireTreeStructureChanged(parentPath);
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
		if (figures != null)
		{
			// Mise à jour des figures sélectionnées
			for (Iterator<Figure> treeIt = figures.iterator(); treeIt.hasNext();)
			{
				Figure figure = treeIt.next();
				if (figure.isSelected())
				{
					TreePath selectedPath = new TreePath(new Object[]{
						rootElement,
						figure
					});
					selectedFigures.add(selectedPath);
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

		if (parent == rootElement)
		{
			if (figures != null)
			{
				if ((index >= 0) && (index < figures.size()))
				{
					return figures.get(index);
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
		if (parent == rootElement)
		{
			if (figures != null)
			{
				return figures.size();
			}
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		if (parent == rootElement)
		{
			if (figures != null)
			{
				return figures.indexOf(child);
			}
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object node)
	{
		if (node == rootElement)
		{
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(rootElement + "\n");

		if (figures != null)
		{
			for (Figure figure : figures)
			{
				sb.append("+--").append(figure.toString()).append('\n');
			}
		}

		return sb.toString();
	}
}
