package figures.listeners.transform;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.listeners.AbstractFigureListener;
import figures.listeners.creation.AbstractCreationListener;
import history.HistoryManager;

/**
 * Listener permettant de transformer une figure
 * <ol>
 * <li>bouton 1 pressé et maintenu enfoncé</li>
 * <li>déplacement de la souris avec le bouton enfoncé</li>
 * <li>relachement du bouton</li>
 * </ol>
 * @author davidroussel
 */
public abstract class AbstractTransformShapeListener extends AbstractFigureListener
{
	/**
	 * La transformation initiale de la figure
	 */
	protected AffineTransform initialTransform;

	/**
	 * Indique si seules les figures sélectionnées sont transformables ou pas
	 */
	protected boolean onlySelected;

	/**
	 * Le centre de la figure sélectionnée (car on l'utilisera souvent)
	 */
	protected Point2D center;

	/**
	 * Le modificateur (Crtl, Shift, Alt, etc.) applicable lors du traitement
	 * des évènements souris
	 * @see InputEvent#SHIFT_DOWN_MASK
	 * @see InputEvent#CTRL_DOWN_MASK
	 * @see InputEvent#ALT_DOWN_MASK
	 * @see InputEvent#META_DOWN_MASK
	 */
	protected int keyMask;

	/**
	 * Valeur par défaut lorsqu'aucun key mask n'est requis
	 */
	protected static final int NoKeyMask = 0;

	/**
	 * Constructeur d'un listener à deux étapes: pressed->drag->release pour
	 * transformer les figures
	 * @param model le modèle de dessin à modifier par ce Listener
	 * @param history le gestionnaire d'historique
	 * @param tipLabel le label dans lequel afficher les conseils utilisateur
	 */
	public AbstractTransformShapeListener(Drawing model,
	                                      HistoryManager<Figure> history,
	                                      JLabel tipLabel)
	{
		super(model, history, tipLabel, 2);

		tips[0] = new String("Cliquez et maintenez enfoncé pour transformer la figure");
		tips[1] = new String("Relâchez pour terminer le déplacement");

		updateTip();

		System.out.println(getClass().getSimpleName() + " created");

		center = null;

		keyMask = NoKeyMask;
	}

	/**
	 * Vérifie que seul le {@link InputEvent#BUTTON1_MASK} ainsi que le
	 * {@link #keyMask} sont présents dans les modifiers renvoyés par
	 * {@link MouseEvent#getModifiers()} mais <b>aucun autre</b> modifier
	 * @param modifiers les modifiers à vérifier
	 * @return true si seuls {@link InputEvent#BUTTON1_MASK} et {@link #keyMask}
	 * sont présents dans les modifiers, false sinon
	 */
	public boolean checkModifiers(int modifiers)
	{
		return modifiers == (InputEvent.BUTTON1_MASK | keyMask);
	}

	/**
	 * Initialisation de la transformation de la figure. Détermine le point de
	 * départ de la transformation de la figure ({@link #startPoint}) ainsi que
	 * la figure sélectionnée qui peut éventuellement être nulle s'il n'y a pas
	 * de figures sélectionnées ou sous le curseur.
	 * A utiliser dans
	 * {@link MouseListener#mousePressed(MouseEvent)} ou bien dans
	 * {@link MouseListener#mouseClicked(MouseEvent)} suivant la figure à créer.
	 * @see #mousePressed(MouseEvent)
	 * @see #mouseClicked(MouseEvent)
	 */
	@Override
	public void startAction(MouseEvent e)
	{
		history.record();

		setStartPoint(e);

		currentFigure = drawingModel.getFigureAt(startPoint);
		if (currentFigure != null)
		{
			center = currentFigure.getCenter();

			init();

			nextStep();

			drawingModel.update(); // optionel
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::startAction : null figure");
		}
	}

	/**
	 * Initialisations particulières à l'initialisation du listener
	 * <ul>
	 * 	<li>Initialisation de transformation initiale</li>
	 * 	<li>...</li>
	 * </ul>
	 */
	public abstract void init();

	/**
	 * Terminaison du déplacement d'une figure. remet l'étape courante à 0,
	 * détermine la position du point de terminaison du déplacement de la figure
	 * ({@link #endPoint}), puis met à jour le dessin (
	 * {@link Drawing#update()}) et les conseils utilisateurs (
	 * {@link #updateTip()}). A utiliser dans un
	 * {@link MouseListener#mousePressed(MouseEvent)} ou bien dans un
	 * {@link MouseListener#mouseClicked(MouseEvent)} suivant la figure à créer.
	 * @param e l'évènement souris à utiliser lors de la terminaison d'un figure
	 */
	@Override
	public void endAction(MouseEvent e)
	{
		if (currentStep == 1)
		{
			// Remise à zéro de currentStep pour pouvoir réutiliser ce
			// listener sur une autre figure
			nextStep();

			setendPoint(e);

			currentFigure = null;

			drawingModel.update();
		}
	}

	/**
	 * Création d'une nouvelle figure rectangulaire de taille 0 au point de
	 * l'évènement souris, si le bouton appuyé est le bouton gauche.
	 * @param e l'évènement souris
	 * @see AbstractCreationListener#startAction(MouseEvent)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		currentFigure = drawingModel.getFigureAt(e.getPoint());

		if (currentFigure != null)
		{
			if (currentFigure.isSelected() &&
//				(e.getButton() == MouseEvent.BUTTON1) &&
				checkModifiers(e.getModifiers()))
			{
				startAction(e);
			}
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::mousePressed : null figure");
		}
	}

	/**
	 * Terminaison de la nouvelle figure rectangulaire si le bouton appuyé
	 * était le bouton gauche
	 * @param e l'évènement souris
	 * @see AbstractCreationListener#endAction(MouseEvent)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) // On se fiche du keymask pour terminer l'action
		{
			// System.out.println("TransformShapeListener ended...");
			endAction(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Rien
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Rien
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		// Rien
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		// Rien
	}

	/**
	 * Déplacement du point en bas à droite de la figure rectangulaire, si
	 * l'on se trouve à l'étape 1 (après initalisation du déplacement) et que
	 * le bouton enfoncé est bien le bouton gauche.
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (currentStep == 1)
		{
			if (currentFigure != null)
			{
				updateDrag(e);

				drawingModel.update();
			}
			else
			{
				System.err.println(getClass().getSimpleName() + "::mouseDragged : null figure");
			}
		}
	}

	/**
	 * Mise à jour de la transformation courante et application
	 * de la transformation initiale ({@link #initialTransformation} et
	 * de la transformation courante
	 * @param e évènement souris
	 */
	public abstract void updateDrag(MouseEvent e);

	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// Rien
	}
}
