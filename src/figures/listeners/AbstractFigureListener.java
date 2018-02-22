package figures.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;

/**
 * Listener (incomplet) des évènements souris pour agir sur lesfigures.
 * Chaque action sur les figures (création ou transformation) est graphiquement
 * construite par une suite de pressed/drag/release ou de clicks qui peut être
 * différente pour chaque type d'action. Aussi les classes filles devront
 * implémenter leur propre xxxFigureListener assurant la gestion des évènement
 * souris.
 * @author davidroussel
 */
public abstract class AbstractFigureListener
    implements MouseListener, MouseMotionListener, MouseWheelListener
{
	/**
	 * Le drawing model à modifier par ce creationListener. Celui ci contient
	 * tous les élements nécessaires à la modification du dessin par les
	 * évènements souris.
	 */
	protected Drawing drawingModel;

	/**
	 * L'History manager qui gère les historiques d'Undo et de Redo
	 */
	protected HistoryManager<Figure> history;

	/**
	 * La figure en cours de dessin. Obtenue avec
	 * {@link Drawing#initiateFigure(java.awt.geom.Point2D)}. Evite d'avoir à
	 * appeler {@link Drawing#getLastFigure()} à chaque fois que la figure en
	 * cours de construction est modifiée.
	 */
	protected Figure currentFigure;

	/**
	 * Le label dans lequel afficher les instructions nécessaires à la
	 * complétion de la figure
	 */
	protected JLabel tipLabel;

	/**
	 * Le point de départ de la création de la figure. Utilisé pour comparer le
	 * point de départ et le point terminal pour élminier les figures de taille
	 * 0;
	 */
	protected Point2D startPoint;

	/**
	 * Le point terminal de la création de la figure. Utilisé pour comparer le
	 * point de départ et le point terminal pour élminier les figures de taille
	 * 0;
	 */
	protected Point2D endPoint;

	/**
	 * le conseil par défaut à afficher dans le {@link #tipLabel}
	 */
	public static final String defaultTip =
	    new String("Cliquez pour initier une figure");

	/**
	 * Le tableau de chaines de caractères contenant les conseils à
	 * l'utilisateur pour chacune des étapes de la création. Par exemple [0] :
	 * cliquez et maintenez enfoncé pour initier la figure [1] : relâchez pour
	 * terminer la figure
	 */
	protected String[] tips;

	/**
	 * Le nombre d'étapes (typiquement click->drag->release) nécessaires à la
	 * création de la figure
	 */
	protected final int nbSteps;

	/**
	 * L'étape actuelle de création de la figure
	 */
	protected int currentStep;

	/**
	 * Constructeur protégé (destiné à être utilisé par les classes filles)
	 * @param model le modèle de dessin à modifier par ce listener
	 * @param history le gestionnaire d'historique pour créer des sauvegardes
	 * de l'état courant des figures avant toute modification des figures
	 * @param infoLabel le label dans lequel afficher les conseils d'utilisation
	 * @param nbSteps le nombres d'étapes de l'action à réaliser
	 */
	protected AbstractFigureListener(Drawing model,
	                                 HistoryManager<Figure> history,
	                                 JLabel infoLabel,
	                                 int nbSteps)
	{
		drawingModel = model;
		this.history = history;
		currentFigure = null;
		tipLabel = infoLabel;
		this.nbSteps = nbSteps;
		currentStep = 0;

		// Allocation du nombres de conseils utilisateurs nécessaires
		tips = new String[(nbSteps > 0 ? nbSteps : 0)];

		if (drawingModel == null)
		{
			System.err.println("AbstractFigureListener caution null "
			    + "drawing model");
		}

		if (history == null)
		{
			System.err.println("AbstractFigureListener caution null "
				+ "history manager");
		}

		if (tipLabel == null)
		{
			System.err.println("AbstractFigureListener caution null "
			    + "tip label");
		}
	}

	/**
	 * Initialisation de l'action :
	 * Détermine le point de départ ({@link #startPoint})
	 * Les classes filles devront réutiliser cette méthode pour récupérer le
	 * point de départ de l'action. Puis elles devront initier l'action
	 * et enfin passer à l'étape suivante (éventuellement en mettant à jour
	 * le modèle dessin.
	 * Passe à l'étape suivante avec {@link #nextStep()} ce qui met à jour
	 * le {@link #tipLabel}.
	 * Met à jour le modèle de dessin avec {@link Drawing#update()}
	 * A utiliser dans {@link MouseListener#mousePressed(MouseEvent)} ou bien
	 * dans {@link MouseListener#mouseClicked(MouseEvent)} suivant l'action à
	 * réaliser.
	 * @param e l'évènement souris à utiliser pour initier la création d'une
	 * nouvelle figure à la position de cet évènement
	 */
	public abstract void startAction(MouseEvent e);

	/**
	 * Terminaison de l'action sur une figure:
	 * remet l'étape courante à 0 en passant à l'étape suivante (ce qui met à
	 * jour le {@link #tipLabel} avec {@link #updateTip()},
	 * détermine la position du point de terminaison de la figure
	 * ({@link #endPoint}), puis met à jour le dessin ({@link Drawing#update()}).
	 * A utiliser dans un {@link MouseListener#mousePressed(MouseEvent)} ou bien
	 * dans un
	 * {@link MouseListener#mouseClicked(MouseEvent)} suivant la figure à créer.
	 * @param e l'évènement souris à utiliser lors de la terminaison d'un figure
	 */
	public abstract void endAction(MouseEvent e);

	/**
	 * Récupération du point de départ de l'action
	 * @param e l'évènement souris d'où l'on veut récupérer le point de départ
	 */
	public void setStartPoint(MouseEvent e)
	{
		startPoint = e.getPoint();
	}

	/**
	 * Récupération du point de terminaison de l'action
	 * @param e l'évènement souris d'où l'on veut récupérer le point de terminaison
	 */
	public void setendPoint(MouseEvent e)
	{
		endPoint = e.getPoint();
	}

	/**
	 * Passage à l'étape suivante et mise à jours des conseils utilisateurs
	 * relatifs à l'étape suivante.
	 * Lorsque le passage à l'étape suivante dépasse le nombre d'étapes prévues
	 * l'étape courante est remise à 0.
	 * @see #currentStep
	 * @see #updateTip()
	 */
	protected void nextStep()
	{
		if (currentStep < (nbSteps - 1))
		{
			currentStep++;
		}
		else
		{
			currentStep = 0;
		}

//		System.out.println(getClass().getSimpleName() + " nextStep to step "
//		    + currentStep);

		updateTip();
	}

	/**
	 * Mise à jour du conseil dans le {@link #tipLabel} en fonction de l'étape
	 * courante
	 */
	protected void updateTip()
	{
		if (tipLabel != null)
		{
			tipLabel.setText(tips[currentStep]);
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::updateTip : null tipLabel");
		}
	}
}
