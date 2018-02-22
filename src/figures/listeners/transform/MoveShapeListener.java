package figures.listeners.transform;

import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;

/**
 * Listener permettant de déplacer une figure
 * <ol>
 * 	<li>bouton 1 pressé et maintenu enfoncé</li>
 * 	<li>déplacement de la souris avec le bouton enfoncé</li>
 * 	<li>relachement du bouton</li>
 * </ol>
 * @author davidroussel
 */
public class MoveShapeListener extends AbstractTransformShapeListener
{
	/**
	 * Le dernier point
	 * @note Utilisé pour calculer le déplacement entre l'évènement courant
	 * et l'évènement précédent.
	 * @note modifié dans {@link #mouseDragged(MouseEvent)}
	 */
	private Point2D lastPoint;

	/**
	 * Constructeur d'un listener à deux étapes: pressed->drag->release pour
	 * déplacer toutes les figures
	 * @param model le modèle de dessin à modifier par ce Listener
	 * @param history le gestionnaire d'historique pour les Undo/Redo
	 * @param tipLabel le label dans lequel afficher les conseils utilisateur
	 */
	public MoveShapeListener(Drawing model,
	                         HistoryManager<Figure> history,
	                         JLabel tipLabel)
	{
		super(model, history, tipLabel);
	}

	/* (non-Javadoc)
	 * @see figures.listeners.transform.AbstractTransformShapeListener#init()
	 */
	@Override
	public void init()
	{
		lastPoint = startPoint;
		if (currentFigure != null)
		{
			initialTransform = currentFigure.getTranslation();
			// System.out.println("MoveShapeListener2 initialized");
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::init : null figure");
		}
	}

	/* (non-Javadoc)
	 * @see figures.listeners.transform.AbstractTransformShapeListener#updateDrag(java.awt.event.MouseEvent)
	 */
	@Override
	public void updateDrag(MouseEvent e)
	{
		// System.out.println("MoveShapeListener2 dragged");
		Point2D currentPoint = e.getPoint();
		double dx = currentPoint.getX() - lastPoint.getX();
		double dy = currentPoint.getY() - lastPoint.getY();
		AffineTransform translate = AffineTransform.getTranslateInstance(dx, dy);
		translate.concatenate(initialTransform);
		currentFigure.setTranslation(translate);
	}
}
