package figures.listeners.creation;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.NGon;
import history.HistoryManager;

/**
 * @author davidroussel
 *
 */
public class NGonCreationListener extends AbstractCreationListener
{
	/**
	 * Point pù est relachée la souris pour déterminer ensuite le nombre de
	 * côtés du polygone.
	 */
	private Point releasedPoint;

	/**
	 * Le nombre de côtés du polygone avant que l'on ne commence à changer
	 * le nombre de côtés
	 */
	private int initialNbSides;

	/**
	 * Variation d'une coordonnée entrainant l'incrémentation du nombre de
	 * côtés du polygone de 1
	 */
	private final static int incrementDelta = 20;

	/**
	 * Constructeur d'un polygon régulier
	 * @param model
	 * @param infoLabel
	 * @param nbSteps
	 */
	public NGonCreationListener(Drawing model,HistoryManager<Figure> history, JLabel infoLabel)
	{
		super(model, history, infoLabel, 3);
		tips[0] = new String("Bouton gauche + drag pour commencer le polygone régulier");
		tips[1] = new String("Relâchez pour terminer la taille du polygon");
		tips[2] = new String("Utilisez la roulette pour nbre de côtés, puis click pour terminer");

		updateTip();

		System.out.println("NGonCreationListener created");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 2))
		{
			endAction(e);
		}
	}

	/**
	 * Initiation de la création d'un polygone régulier,
	 * @param e l'évènement souris associé
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 0))
		{
			startAction(e);
			initialNbSides = ((NGon) currentFigure).getNbSides();
		}
	}

	/**
	 * Initiation de la création d'un polygone régulier,
	 * @param e l'évènement souris associé
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Terminaison de la taille mais pas encore du nombre de côtés
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 1))
		{
			releasedPoint = e.getPoint();
			nextStep();
		}

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Nothing
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		// Nothing
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (currentStep == 1)
		{
			// Agrandissement du polygone régulier
			currentFigure.setLastPoint(e.getPoint());

			drawingModel.update();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		/*
		 * Après le drag and release les mouvements verticaux de la souris
		 * définissent le nombre de côtés du polygone
		 */
		if (currentStep == 2)
		{
			NGon ngon = (NGon) currentFigure;
			Point point = e.getPoint();
			int delta = point.y - Double.valueOf(releasedPoint.getY()).intValue();
			int nbSidesDelta = delta / incrementDelta;
			int newnbSides = initialNbSides + nbSidesDelta;
			System.out.print("Delta = " + delta);
			System.out.print(", Delta nb sides = " + nbSidesDelta);
			System.out.println(", new nb sides = " + newnbSides);

			if ((nbSidesDelta != 0) &&
				(newnbSides >= NGon.minNbSides) &&
				(newnbSides <= NGon.maxNbSides))
			{
				ngon.setNbSides(newnbSides);

//				if (ngon.getNbSides() != newnbSides)
//				{
//					releasedPoint.setLocation(point.x, point.y);;
//				}

				drawingModel.update();
			}

		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		System.out.println("Scrolling : " + e.getUnitsToScroll());

	}
}
