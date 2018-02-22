package figures.treemodels;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import figures.Drawing;
import figures.Figure;
/**
 * Classe abstraite de base de tous les arbres composés de figures
 * @author davidroussel
 */
public abstract class AbstractFigureTreeModel implements TreeModel, Observer, TreeSelectionListener
{
	/**
	 * L'élément racine de l'arbre (une simple chaine de caractères)
	 */
	protected String rootElement;

	/**
	 * Le modèle de dessin.
	 * On a besoin de garder une référence vers le modèlde de dessin lorsque
	 * la liste des figures sélectionnées dans l'arbre change afin que l'on
	 * puisse le notifier des changements
	 */
	protected Drawing drawing;

	/**
	 * Le JTree utilisé pour visualiser cet arbre.
	 * On a besoin de garder une référence vers cette vue afin de
	 * spécifier (programmatiquement) quels sont les noeuds sélectionnés
	 * en fonction des figures sélectionnées.
	 * @see #selectedFigures
	 */
	protected JTree treeView;

	/**
	 * Liste des figures sélectionnées dans l'arbre
	 */
	protected Set<TreePath> selectedFigures;

	/**
	 * La liste des listeners de ce modèle
	 */
	protected Vector<TreeModelListener> treeModelListeners;

	/**
	 * Indique si un évènement est généré à l'intérieur du TreeModel ou
	 * bien s'il provient de l'UI
	 */
	protected boolean selfEvent;

	/**
	 * Constructeur de l'arbre des figures
	 * @param drawing le modèle de dessin
	 * @param tree le {@link JTree} utilisé pour visualiser cet arbre
	 * @param rootName le nom du noeud racine
	 */
	public AbstractFigureTreeModel(Drawing drawing, JTree tree, String rootName)
	    throws NullPointerException
	{
		this.drawing = drawing;
		treeView = tree;

		rootElement = new String(rootName);
		selectedFigures = new HashSet<TreePath>();
		treeModelListeners = new Vector<TreeModelListener>();

		if (this.drawing != null)
		{
			this.drawing.addObserver(this);
		}
		else
		{
			throw new NullPointerException("AbstractFigureTreeModel(null drawing)");
		}

		if (treeView != null)
		{
			treeView.setModel(this);
			treeView.addTreeSelectionListener(this);
		}
		else
		{
			throw new NullPointerException("AbstractFigureTreeModel(null tree)");
		}

		selfEvent = false;
	}

	/**
	 * Nettoyage avant destruction
	 */
	@Override
	protected void finalize() throws Throwable
	{
		drawing.deleteObserver(this);
		rootElement = null;
		drawing = null;
		treeView.removeTreeSelectionListener(this);
		treeView = null;
		selectedFigures.clear();
		selectedFigures = null;
		treeModelListeners.clear();
		treeModelListeners = null;
		super.finalize();
	}

	/**
	 * Mise à jour par l'observable (en l'occurrence un {@link Drawing})
	 * @param observable le {@link Drawing}
	 * @param data les données à transmettre (non utilisé ici)
	 * @see Observer#update(Observable, Object)
	 */
	@Override
	public void update(Observable observable, Object data)
	{
		if (observable instanceof Drawing)
		{
			synchronized (observable)
			{
				drawing = (Drawing) observable;
				Stream<Figure> stream = drawing.stream();

				// Obtention d'une collection de figures à dessiner
				Vector<Figure> figures = stream.sequential()
				    .collect(Collectors.toCollection(Vector::new));

				// Effacement des chemins des figures sélectionnées
				selectedFigures.clear();

				// Mise à jour de l'arbre des figures
				updateFiguresFromDrawing(figures);

				// Mise à jour des chemins des figures sélectionnées
				updateSelectedFigures();

				// Mise à jour des figures sélectionnées dans le treeView
				updateSelectedPath();
			}
		}
		else
		{
			System.err.println("Observable is not an instance of Drawing");
		}
	}

	/**
	 * Mise à jour des figures de l'arbre en les comparant une par une aux
	 * figures du modèle de dessin.
	 * Permet l'enlever/ajouter les figures de l'arbre en fonction des
	 * modifications observées dans les figures du modèle de dessin.
	 * @param figures les figures du modèle de dessin
	 */
	protected abstract void updateFiguresFromDrawing(List<Figure> figures);

	/**
	 * Mise à jour de {@link #selectedFigures} d'après les figures de l'arbre
	 * sélectionnées.
	 */
	protected abstract void updateSelectedFigures();

	/**
	 * Mise à jour des noeuds sélectionnés dans le {@link #treeView} d'après
	 * les paths répertoriés dans {@link #selectedFigures}
	 */
	protected void updateSelectedPath()
	{
		if (treeView != null)
		{
			TreeSelectionModel tsm = treeView.getSelectionModel();
			if (tsm != null)
			{
				TreePath[] treePathes = selectedFigures.toArray(new TreePath[0]);
				if(treePathes.length == 0)
				{
					treePathes = null; // pour effacer la sélection
				}
				tsm.setSelectionPaths(treePathes);
			}
			else
			{
				System.err.println("AbstractFigureTreeModel::updateSelectedPath : null Selection Model");
			}
		}
		else
		{
			System.err.println("AbstractFigureTreeModel::updateSelectedPath : null TreeView");
		}
	}

	/**
	 * Méthode à utiliser lorsque la structure de l'arbre change.
	 * Tous les éléments situés en dessous de path sont mis à jour
	 * @param path le chemin en dessous duquel l'arbre a changé
	 */
	protected synchronized void fireTreeStructureChanged(TreePath path)
	{
		if (treeModelListeners.size() > 0)
		{
			/*
			 * Used to create an event when the node structure has changed in
			 * some way, identifying the path to the root of the modified
			 * subtree as a TreePath object.
			 */
			TreeModelEvent e = new TreeModelEvent(this, path);
			for (TreeModelListener tml : treeModelListeners)
			{
				selfEvent = true;
//				System.out.println("fireTreeStructureChanged(" + e + " to " + tml);
				tml.treeStructureChanged(e);
			}
		}
	}

	/**
	 * Méthode à utliser lorsqu'un ou plusieurs noeuds sont ajoutés à
	 * l'arbre
	 * @param path the path to the parent of inserted node(s)
	 * @param newchildIndices an array of the indices of the new inserted nodes
	 * @param newNodes an array of the new inserted nodes (Optional)
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(TreeModelEvent)
	 */
	protected synchronized void fireTreeNodesInserted(TreePath path,
	                                     int[] newchildIndices,
	                                     Object[] newNodes)
	{
		if (treeModelListeners.size() > 0)
		{
			TreeModelEvent e =
			    new TreeModelEvent(this, path, newchildIndices, newNodes);
			for (TreeModelListener tml : treeModelListeners)
			{
				selfEvent = true;
//				System.out.println("fireTreeNodesInserted(" + e + " to " + tml);
				tml.treeNodesInserted(e);
			}
		}
	}

	/**
	 * Méthode à utiliser lorsqu'un ou plusieurs noeuds sont retirés de l'arbre
	 * @param path the path to the former parent of deleted node
	 * @param oldChildIndices an array of indices (in ascending order) where
	 * the removed nodes used to be
	 * @note if a subtree is removed from the tree, this method may only be
	 * invoked once for the root of the removed subtree, not once for
	 * each individual set of siblings removed.
	 */
	protected synchronized void fireTreeNodesRemoved(TreePath path,
	                                int[] oldChildIndices,
	                                Object[] oldNodes)
	{
		if (treeModelListeners.size() > 0)
		{
			TreeModelEvent e = new TreeModelEvent(this,
			                                      path,
			                                      oldChildIndices,
			                                      oldNodes);
			for (TreeModelListener tml : treeModelListeners)
			{
				selfEvent = true;
//				System.out.println("fireTreeNodesRemoved(" + e + " to " + tml);
				tml.treeNodesRemoved(e);
			}
		}
	}

	/**
	 * Méthode à utiliser lorsqu'un ou plusieurs noeuds sont changés (par
	 * exemple s'il sont sélectionnés programmatiquement)
	 * @param treePathes l'ensemble des {@link TreePath} des noeuds changés
	 */
	protected synchronized void fireNodesChanged(TreePath[] treePathes)
	{
		for (int i = 0; i < treePathes.length; i++)
		{
			for (TreeModelListener tml : treeModelListeners)
			{
				selfEvent = true;
//				System.out.println("fireNodesChanged(" + treePathes[i] + " to " + tml);
				tml.treeNodesChanged(new TreeModelEvent(this, treePathes[i]));
			}
		}
	}

	/**
	 * Accès au noeud d'index index fils du noeud de parent
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 * @param parent le noeud parent du noeud recherché
	 * @param index l'index du noeud enfant recherché
	 * @return le noeud recherché ou bien null s'il n'existe pas.
	 */
	@Override
	public abstract Object getChild(Object parent, int index);

	/**
	 * Nombre d'enfants d'un noeud
	 * @param parent le noeud dont on veut connaitre le nombre d'enfants.
	 * @return le nombre d'enfants du noeud ou bien 0 si ce noeud n'a pas
	 * d'enfants ou est une feuille de l'arbre.
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public abstract int getChildCount(Object parent);

	/**
	 * Index d'un enfant particulier à partir d'un noeud parent
	 * @param parent le noeud parent
	 * @param child le noeud enfant
	 * @return l'index du noeud enfant dans le noeud parent. si parent ou
	 * child sont null, ou si l'un des deux n'est pas un noeud de cet arbre
	 * renvoie -1.
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public abstract int getIndexOfChild(Object parent, Object child);

	/**
	 * Accesseur à la racine de l'arbre
	 * @return la racine de l'arbre
	 */
	@Override
	public Object getRoot()
	{
		return rootElement;
	}

	/**
	 * Indique si un noeud est une feuille de l'arbre
	 * @param node le noeud dont on veut savoir s'il est une feuille
	 * @return true si le noeud est une feuille de l'arbre, false autrement.
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public abstract boolean isLeaf(Object node);

	/**
	 * Méthode déclenchée lorsqu'un utilisateur a altéré la valeur d'un item
	 * identifié par path avec la nouvelle valeur newValue. Si newValue est
	 * effectivement une nouvelle valeur, alors on doit déclencher un
	 * treeNodesChanged event [Non utilisé ici]
	 * @param path le chemin du noeud modifié
	 * @param newValue la nouvelle valeur du noeud
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		System.out.println("*** valueForPathChanged : " + path + " --> " +
		                   newValue);
	}

	/**
	 * Ajout d'un listener à ce modèle d'abre
	 * @param l le listener à ajouter
	 */
	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		if ((l != null) && !treeModelListeners.contains(l))
		{
			treeModelListeners.add(l);
		}
	}

	/**
	 * Retrait d'n listener à ce modèle d'arbre
	 * @param l le listener à retirer
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		if (treeModelListeners.contains(l))
		{
			treeModelListeners.remove(l);
		}
	}

	/**
	 * Callback déclenché lorsqu'un noeud est sélectionné dans le {@link #treeView}
	 * @param e l'évènement de sélection dans le {@link JTree}
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 * @note doit être réimplémenté dans les classes filles si l'arbre est plus
	 * complexe qu'une racine et de figures en dessous.
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
					drawing.stream().forEach((Figure f ) ->
					{
						f.setSelected(true);
					});
				}
				if (node instanceof Figure) // Select one figure
				{
					Figure figure = (Figure) node;
					figure.setSelected(true);
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
}
