package figures.listeners.creation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.listeners.AbstractFigureListener;
import history.HistoryManager;

/**
 * Listener (incomplet) des évènements souris pour créer une figure. Chaque
 * figure (Cercle, Ellipse, Rectangle, etc) est graphiquement construite par une
 * suite de pressed/drag/release ou de clicks qui peut être différente pour
 * chaque type de figure. Aussi les classes filles devront implémenter leur
 * propre xxxCreationListener assurant la gestion de la création d'une nouvelle
 * figure.
 * @author davidroussel
 */
public abstract class AbstractCreationListener extends AbstractFigureListener
    implements MouseListener, MouseMotionListener
{
	/**
	 * Constructeur protégé (destiné à être utilisé par les classes filles)
	 * @param model le modèle de dessin à modifier par ce creationListener
	 * @param history le gestionnaire d'historique pour les Undo/Redo
	 * @param infoLabel le label dans lequel afficher les conseils d'utilisation
	 * @param nbSteps le nombres d'étapes de création de la figure
	 */
	protected AbstractCreationListener(Drawing model,
	                                   HistoryManager<Figure> history,
	                                   JLabel infoLabel,
	                                   int nbSteps)
	{
		super(model, history, infoLabel, nbSteps);
	}

	/**
	 * Initialisation de la création d'une nouvelle figure. détermine le point
	 * de départ de la figure ({@link #startPoint}), initie une nouvelle figure
	 * à la position de l'évènement ({@link Drawing#initiateFigure(Point2D)}),
	 * met à jour le dessin {@link Drawing#update()}, puis passe à l'étape
	 * suivante en mettant à jour les conseils utilisateurs (
	 * {@link #updateTip()}). Pour la plupart des figures la création commence
	 * par un appui sur le bouton gauche de la souris. A utiliser dans
	 * {@link MouseListener#mousePressed(MouseEvent)} ou bien dans
	 * {@link MouseListener#mouseClicked(MouseEvent)} suivant la figure à créer.
	 * @param e l'évènement souris à utiliser pour initier la création d'une
	 * nouvelle figure à la position de cet évènement
	 */
	@Override
	public void startAction(MouseEvent e)
	{
		history.record();
		setStartPoint(e);
		currentFigure = drawingModel.initiateFigure(e.getPoint());

		nextStep();

		drawingModel.update();
	}

	/**
	 * Terminaison de la création d'une figure. remet l'étape courante à 0,
	 * détermine la position du point de terminaison de la figure (
	 * {@link #endPoint}), vérifie que la figure ainsi terminée n'est pas de
	 * taille 0 ({@link #checkZeroSizeFigure()}), puis met à jour le dessin (
	 * {@link Drawing#update()}) et les conseils utilisateurs (
	 * {@link #updateTip()}). A utiliser dans un
	 * {@link MouseListener#mousePressed(MouseEvent)} ou bien dans un
	 * {@link MouseListener#mouseClicked(MouseEvent)} suivant la figure à créer.
	 * @param e l'évènement souris à utiliser lors de la terminaison d'un figure
	 */
	@Override
	public void endAction(MouseEvent e)
	{
		// Remise à zéro de currentStep pour pouvoir réutiliser ce
		// listener sur une autre figure
		nextStep();

		setendPoint(e);

		// à la fin de la figure on la normalise pour qu'elle soit centrée
		// sur son barycentre et la position du barycentre dans la translation
		if (currentFigure != null)
		{
			currentFigure.normalize();
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::endAction : null figure");
		}

		if (checkZeroSizeFigure())
		{
			// cancel last memento
			history.cancel();
		}

		drawingModel.update();

		updateTip();
	}

	/**
	 * Contrôle de la taille de la figure créée à effectuer à la fin de la
	 * création afin d'éliminer les figures de taille 0;
	 * @return true si une figure de petite taille a été retirée
	 * @see #startPoint
	 * @see #endPoint
	 */
	protected boolean checkZeroSizeFigure()
	{
		if (startPoint.distance(endPoint) < 1.0)
		{
			drawingModel.removeLastFigure();
			System.err.println("Removed zero sized figure");
			return true;
		}

		return false;
	}
}
