package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Stream;

import figures.enums.FigureType;
import figures.enums.LineType;
import filters.EdgeColorFilter;
import filters.FigureFilters;
import filters.FillColorFilter;
import filters.ShapeFilter;
import filters.LineFilter;
import history.Memento;
import history.Originator;
import utils.PaintFactory;
import utils.StrokeFactory;

/**
 * Classe contenant l'ensemble des figures à dessiner (LE MODELE)
 * @author davidroussel
 */
public class Drawing extends Observable implements Originator<Figure>
{
	/**
	 * Liste des figures à dessiner (protected pour que les classes du même
	 * package puissent y accéder)
	 */
	protected Vector<Figure> figures;

	/**
	 * Liste triée des indices (uniques) des figures sélectionnées
	 */
	protected SortedSet<Integer> selectionIndex;

	/**
	 * Figure située sous le curseur.
	 * Déterminé par {@link #getFigureAt(Point2D)}
	 */
	private Figure selectedFigure;

	/**
	 * Le type de figure à créer (pour la prochaine figure)
	 */
	private FigureType type;

	/**
	 * La couleur de remplissage courante (pour la prochaine figure)
	 */
	private Paint fillPaint;

	/**
	 * La couleur de trait courante (pour la prochaine figure)
	 */
	private Paint edgePaint;

	/**
	 * La largeur de trait courante (pour la prochaine figure)
	 */
	private float edgeWidth;

	/**
	 * Le type de trait courant (sans trait, trait plein, trait pointillé,
	 * pour la prochaine figure)
	 */
	private LineType edgeType;

	/**
	 * Les caractétistique à appliquer au trait en fonction de {@link #type} et
	 * {@link #edgeWidth}
	 */
	private BasicStroke stroke;

	/**
	 * Etat de filtrage des figures dans le flux de figures fournit par
	 * {@link #stream()}
	 * Lorsque {@link #filtering} est true le dessin des figures est filtré
	 * par l'ensemble des filtres présents dans {@link #shapeFilters},
	 * {@link #fillColorFilter}, {@link #edgeColorFilter} et
	 * {@link #lineFilters}.
	 * Lorsque {@link #filtering} est false, toutes les figures sont fournies
	 * dans le flux des figures.
	 * @see #stream()
	 */
	private boolean filtering;

	/**
	 * Filtres à appliquer au flux des figures pour sélectionner les types
	 * de figures à afficher
	 * @see #stream()
	 */
	private FigureFilters<FigureType> shapeFilters;

	/**
	 * Filtre à appliquer au flux des figures pour sélectionner les figures
	 * ayant une couleur particulière de remplissage
	 */
	private FillColorFilter fillColorFilter; // décommenter lorsque prêt

	/**
	 * Filtre à appliquer au flux des figures pour sélectionner les figures
	 * ayant une couleur particulière de trait
	 */
	private EdgeColorFilter edgeColorFilter; // décommenter lorsque prêt

	/**
	 * Filtres à applique au flux des figures pour sélectionner les figures
	 * ayant un type particulier de lignes
	 */
	private FigureFilters<LineType> lineFilters;

	/**
	 * Constructeur de modèle de dessin
	 */
	public Drawing()
	{
		figures = new Vector<Figure>();
		selectionIndex = new TreeSet<Integer>(Integer::compare);
		shapeFilters = new FigureFilters<FigureType>();

		fillColorFilter = null; // décommenter lorsque prêt
		edgeColorFilter = null; // décommenter lorsque prêt
		lineFilters = new FigureFilters<LineType>();

		fillPaint = null;
		edgePaint = null;
		edgeWidth = 1.0f;
		edgeType = LineType.SOLID;
		stroke = StrokeFactory.getStroke(edgeType, edgeWidth);
		filtering = false;
		selectedFigure = null;
		System.out.println("Drawing model created");
	}

	/**
	 * Nettoyage avant destruction
	 */
	@Override
	protected void finalize()
	{
		// Aide au GC
		figures.clear();
		figures = null;
		selectionIndex.clear();
		selectionIndex = null;
		fillPaint = null;
		edgePaint = null;
		edgeType = null;
		stroke = null;
		shapeFilters.clear();
		shapeFilters = null;
		fillColorFilter = null; // TODO décommenter lorsque prêt
		edgeColorFilter = null; // TODO décommenter lorsque prêt
		lineFilters.clear();
		lineFilters = null;
	}

	/**
	 * Mise à jour du ou des {@link Observer} qui observent ce modèle. On place
	 * le modèle dans un état "changé" puis on notifie les observateurs.
	 */
	public void update()
	{
		setChanged();
		notifyObservers(); // pour que les observateurs soient mis à jour
	}

	/**
	 * Mise en place d'un nouveau type de figure à générer
	 * @param type le nouveau type de figure
	 */
	public void setFigureType(FigureType type)
	{
		this.type = type;
	}

	/**
	 * Accesseur de la couleur de remplissage courante des figures
	 * @return la couleur de remplissage courante des figures
	 */
	public Paint getFillpaint()
	{
		return fillPaint;
	}

	/**
	 * Mise en place d'une nouvelle couleur de remplissage
	 * @param fillPaint la nouvelle couleur de remplissage
	 */
	public void setFillPaint(Paint fillPaint)
	{
		this.fillPaint = fillPaint;
	}

	/**
	 * Accesseur de la couleur de trait courante des figures
	 * @return la couleur de remplissage courante des figures
	 */
	public Paint getEdgePaint()
	{
		return edgePaint;
	}

	/**
	 * Mise en place d'une nouvelle couleur de trait
	 * @param edgePaint la nouvelle couleur de trait
	 */
	public void setEdgePaint(Paint edgePaint)
	{
		this.edgePaint = edgePaint;
	}

	/**
	 * Accesseur du trait courant des figures
	 * @return le trait courant des figures
	 */
	public BasicStroke getStroke()
	{
		return stroke;
	}

	/**
	 * Mise en place d'un nouvelle épaisseur de trait
	 * @param width la nouvelle épaisseur de trait
	 */
	public void setEdgeWidth(float width)
	{
		edgeWidth = width;
		/*
		 * Il faut regénérer le stroke
		 */
		stroke = StrokeFactory.getStroke(edgeType, edgeWidth);
	}

	/**
	 * Mise en place d'un nouvel état de ligne pointillée
	 * @param type le nouveau type de ligne
	 */
	public void setEdgeType(LineType type)
	{
		edgeType = type;
		/*
		 * Il faut regénérer le stroke
		 */
		stroke = StrokeFactory.getStroke(edgeType, edgeWidth);
	}

	/**
	 * Initialisation d'une figure de type {@link #type} au point p et ajout de
	 * cette figure à la liste des {@link #figures}
	 * @param p le point où initialiser la figure
	 * @return la nouvelle figure créée à x et y avec les paramètres courants
	 */
	public Figure initiateFigure(Point2D p)
	{
		/*
		 * TODO Maintenant que l'on s'apprête effectivement à créer une figure on
		 * ajoute/obtient les Paints et le Stroke des factories
		 */
		fillPaint = PaintFactory.getPaint(fillPaint);
		edgePaint = PaintFactory.getPaint(edgePaint);
		stroke = StrokeFactory.getStroke(edgeType, edgeWidth);
		/*
		 * TODO Obtention de la figure correspondant au type de figure choisi grâce à
		 * type.getFigure(...)
		 */
		Figure newFigure = type.getFigure(stroke, edgePaint, fillPaint, p);
		// TODO remplacer par type.getFigure(...)

		/*
		 * TODO Ajout de la figure à #figures
		 */
		if (newFigure != null)
			figures.add(newFigure);
		/* TODO Notification des observers */
		update();
		return newFigure;
	}

	/**
	 * Obtention de la dernière figure (implicitement celle qui est en cours de
	 * dessin)
	 * @return la dernière figure du dessin
	 */
	public Figure getLastFigure()
	{
		// TODO Remplacer par l'implémentation ...
		if (!figures.isEmpty()) return figures.get(figures.size() - 1);
		else
		{
			System.err.println("getLastFigure : fail");
			return null;
		}
	}

	/**
	 * Obtention de la dernière figure contenant le point p.
	 * @param p le point sous lequel on cherche une figure
	 * @return une référence vers la dernière figure contenant le point p ou à
	 * défaut null.
	 */
	public Figure getFigureAt(Point2D p)
	{
		selectedFigure = null;

		/*
		 * TODO Recherche dans le flux des figures de la DERNIERE figure
		 * contenant le point p.
		 */
		stream().forEach((Figure figure) -> {
			if (figure.contains(p))
				selectedFigure = figure;
		});

		return selectedFigure;
	}

	/**
	 * Retrait de la dernière figure
	 * @post le modèle de dessin a été mis à jour
	 */
	public void removeLastFigure()
	{
		// TODO Compléter ...
		if (!figures.isEmpty())
		{
			figures.remove(figures.size() - 1);
			update();
		}
	}

	/**
	 * Effacement de toutes les figures (sera déclenché par une action clear)
	 * @post le modèle de dessin a été mis à jour
	 */
	public void clear()
	{
		// TODO Compléter ...
		if (!figures.isEmpty())
		{
			figures.clear();
			// TODO use history instead.
			update();
		}
	}

	/**
	 * Accesseur de l'état de filtrage
	 * @return l'état courant de filtrage
	 */
	public boolean getFiltering()
	{
		return filtering;
	}

	/**
	 * Changement d'état du filtrage
	 * @param filtering le nouveau statut de filtrage
	 * @post le modèle de dessin a été mis à jour
	 */
	public void setFiltering(boolean filtering)
	{
		// TODO ... filtering ...
		this.filtering = filtering;
		// TODO history
		update();
	}

	/**
	 * Ajout d'un filtre pour filtrer les types de figures
	 * @param filter le filtre à ajouter
	 * @return true si le filtre n'était pas déjà présent dans l'ensemble des
	 * filtres fitrant les types de figures, false sinon
	 * @post si le filtre a été ajouté, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean addShapeFilter(ShapeFilter filter)
	{
		// TODO ... shapeFilters ...
		boolean added = shapeFilters.add(filter);

		// System.out.println(shapeFilters);

		if (added)
		{
			update();
		}

		return added;
	}

	/**
	 * Retrait d'un filtre filtrant les types de figures
	 * @param filter le filtre à retirer
	 * @return true si le filtre faisait partie des filtres filtrant les types
	 * de figure et a été retiré, false sinon.
	 * @post si le filtre a éré retiré, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean removeShapeFilter(ShapeFilter filter)
	{
		// TODO ... shapeFilters ...
		boolean removed = shapeFilters.remove(filter);

		if (removed)
		{
			update();
		}

		return removed;
	}

	/**
	 * Mise en place du filtre de couleur de remplissage
	 * @param filter le filtre de couleur de remplissage à appliquer
	 * @post le {@link #fillColorFilter} est mis en place et une mise à jour
	 * est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public void setFillColorFilter(FillColorFilter filter)
	{
		// TODO ... fillColorFilter ...
		fillColorFilter = filter;
		update();
	}

	/**
	 * Mise en place du filtre de couleur de trait
	 * @param filter le filtre de couleur de trait à appliquer
	 * @post le #edgeColorFilter est mis en place et une mise à jour
	 * est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public void setEdgeColorFilter(EdgeColorFilter filter)
	{
		// TODO ... edgeColorFilter ...
		edgeColorFilter = filter;
		update();
	}

	/**
	 * Ajout d'un filtre pour filtrer les types de ligne des figures
	 * @param filter le filtre à ajouter
	 * @return true si le filtre n'était pas déjà présent dans l'ensemble des
	 * filtres fitrant les types de lignes, false sinon
	 * @post si le filtre a été ajouté, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean addLineFilter(LineFilter filter)
	{
		// TODO ... lineFilters ...
		boolean added = lineFilters.add(filter);

		if (added)
		{
			update();
		}

		return added;
	}

	/**
	 * Retrait d'un filtre filtrant les types de lignes
	 * @param filter le filtre à retirer
	 * @return true si le filtre faisait partie des filtres filtrant les types
	 * de lignes et a été retiré, false sinon.
	 * @post si le filtre a éré retiré, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean removeLineFilter(LineFilter filter)
	{
		// TODO ... lineFilters ...
		boolean removed = lineFilters.remove(filter);

		if (removed)
		{
			update();
		}

		return removed;
	}

	/**
	 * Remise à l'état non sélectionné de toutes les figures
	 */
	public void clearSelection()
	{
		// TODO Compléter ...
		for (Iterator<Figure> fit = figures.iterator(); fit.hasNext();)
		{
			Figure figure = fit.next();
			if (figure.isSelected())
			{
				figure.setSelected(false);
			}
		}
	}

	/**
	 * Mise à jour des indices des figures sélectionnées dans {@link #selectionIndex}
	 * d'après l'interrogation de l'ensembles des figures (après filtrage).
	 */
	public void updateSelection()
	{
		// TODO Compléter ...
		selectionIndex.clear();

		stream().forEach((Figure figure) -> {
			if (figure.isSelected())
			{
				int index = figures.indexOf(figure);
				System.out.println("Figure #" + index + " : "
				    + figures.get(index) + " is selected");
				selectionIndex.add(new Integer(index));
			}
		});
		
		update();
	}

	/**
	 * Indique s'il existe des figures sélectionnées
	 * @return true s'il y a des figures sélectionnées
	 */
	public boolean hasSelection()
	{
		// TODO Remplacer par l'implémentation
		return selectionIndex.size() > 0;
	}

	/**
	 * Destruction des figures sélectionnées.
	 * Et incidemment nettoyage de {@link #selectionIndex}
	 */
	public void deleteSelected()
	{
		// TODO Compléter ...
		while (hasSelection())
		{
			Integer lastIndex = selectionIndex.last();
			figures.removeElementAt(lastIndex.intValue());
			selectionIndex.remove(lastIndex);
		}

		clearSelection();
		// TODO History
		update();
	}

	/**
	 * Applique un style particulier aux figure sélectionnées
	 * @param fill la couleur de remplissage à applique aux figures sélectionnées
	 * @param edge la couleur de trait à appliquer aux figures sélectionnées
	 * @param stroke le style de trait à appliquer aux figures sélectionnées
	 */
	public void applyStyleToSelected(Paint fill, Paint edge, BasicStroke stroke)
	{
		// TODO Compléter ...
		for (Iterator<Integer> indexIt = selectionIndex.iterator(); indexIt.hasNext();)
			{
				try
				{
					Figure selectedFigure = figures.get(indexIt.next());
					if (fill != null)
						selectedFigure.setFillPaint(fill);

					if (edge != null)
						selectedFigure.setEdgePaint(edge);

					if (stroke != null)
						selectedFigure.setStroke(stroke);

				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					System.err.println(getClass().getSimpleName()
					    + "::applyStyleToSelected : invalid index");
				}
			}
			update();
	}

	/**
	 * Déplacement des figures sélectionnées en haut de la liste des figures.
	 * En conservant l'ordre des figures sélectionnées
	 */
	public void moveSelectedUp()
	{
		// TODO Compléter ...
		Vector<Figure> newFigures = new Vector<Figure>();
		Iterator<Figure> it = figures.iterator();
		while (it.hasNext())
		{
			Figure f = it.next();
			if(f.isSelected())
			{
				newFigures.add(f);
				it.remove();
			}
		}
		figures.addAll(newFigures);
		updateSelection();
	}

	/**
	 * Accès aux figures dans un stream afin que l'on puisse y appliquer
	 * de filtres
	 * @return le flux des figures éventuellement filtrés par les différents
	 * filtres
	 */
	public Stream<Figure> stream()
	{
		Stream<Figure> figuresStream = figures.stream();
		if (filtering)
		{
			// TODO Compléter avec ...
			if (shapeFilters.size() > 0)
				figuresStream = figuresStream.filter(shapeFilters);

			if (fillColorFilter != null)
				figuresStream = figuresStream.filter(x->fillColorFilter.test(x));

			if (edgeColorFilter != null)
				figuresStream = figuresStream.filter(edgeColorFilter);

			if (lineFilters.size() > 0)
				figuresStream = figuresStream.filter(lineFilters);
		}

		return figuresStream;
	}

	/* (non-Javadoc)
	 * @see history.Originator#createMemento()
	 */
	@Override
	public Memento<Figure> createMemento()
	{
		return new Memento<Figure>(figures);
	}

	/* (non-Javadoc)
	 * @see history.Originator#setMemento(history.Memento)
	 */
	@Override
	public void setMemento(Memento<Figure> memento)
	{
		if (memento != null)
		{
			List<Figure> savedFigures = memento.getState();
			System.out.println("Drawing::setMemento(" + savedFigures + ")");

			figures.clear();
			for (Figure elt : savedFigures)
			{
				figures.add(elt.clone());
			}

			update();
		}
		else
		{
			System.err.println("Drawing::setMemento(null)");
		}
	}

	public void moveSelectedDown() {
		// TODO Auto-generated method stub
		Vector<Figure> newFigures = new Vector<Figure>();
		for (Iterator<Integer> indexIt = selectionIndex.iterator(); indexIt.hasNext();)
				newFigures.add(figures.get(indexIt.next()));
		
		for (int i = 0; i < figures.size(); i++)
		{
			if (!selectionIndex.contains(Integer.valueOf(i)))
				newFigures.add(figures.get(i));
		}
		
		figures.clear();
		figures = newFigures;

		// Mise à jour des index des figures sélectionnées & notif observers
		
		updateSelection();
	}
}
