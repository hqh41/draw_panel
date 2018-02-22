package widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import figures.Drawing;
import figures.Figure;
import figures.listeners.AbstractFigureListener;
import figures.listeners.creation.AbstractCreationListener;

/**
 * Panel de dessin des figures (Vue): mis à jour par modèle des figures (
 * {@link Drawing}) au travers d'un observateur. On attache des Listeners
 * (Controleurs) à ce Panel pour :
 * <dl>
 * <dt>Attachements statiques :</dt>
 * <dd>Mettre à jour les coordonnées du pointeur de la souris dans la barre
 * d'état : {@link #coordLabel}</dd>
 * <dd>Mettre à jour le panneau d'informations relatif aux figures située sous
 * le pointeur de la souris : {@link #infoPanel}.</dd>
 * <dt>Attachements dynamique :</dt>
 * <dd>Pour chaque type de figure à créer on attache un
 * {@link AbstractCreationListener} ou plus exactement un de ses descendants
 * pour traduire les évènements souris en instructions pour le modèle de dessin
 * lors de la création d'une nouvelle figure.
 * </dl>
 *
 * @author davidroussel
 */
public class DrawingPanel extends JPanel implements Observer, MouseListener,
		MouseMotionListener
{
	/**
	 * Taille effective du panel. Ce panel n'ayant pas de Layout Manager, il est
	 * important de conserver une taille effective qui puisse être renvoyée dans
	 * la méthode {@link #getPreferredSize()} et modifiée par un
	 * {@link java.awt.event.ComponentListener} tel que le
	 * {@link ResizeListener} ci-dessous.
	 */
	protected Dimension size;

	/**
	 * Contrôleur de changement de taille afin de mettre à jour
	 * {@link DrawingPanel#size} utilisé dans
	 * {@link DrawingPanel#getPreferredSize()}.
	 *
	 * @author davidroussel
	 */
	protected class ResizeListener extends ComponentAdapter
	{
		/**
		 * Action à réaliser lorsque le composant change de taille
		 */
		@Override
		public void componentResized(ComponentEvent e)
		{
			size = e.getComponent().getSize();
		}
	}

	/**
	 * Le modèle (les figures) à dessiner
	 */
	private Drawing drawingModel;

	/**
	 * Le label (qq part dans la GUI) dans lequel afficher les coordonnées du
	 * pointeur de la souris
	 */
	private JLabel coordLabel;

	/**
	 * L'{@link InfoPanel} dans lequel afficher les informations à propos de
	 * la figure sous le curseur.
	 */
	private InfoPanel infoPanel;

	/**
	 * Chaîne de caractère à afficher par défaut dans le {@link #coordLabel}
	 */
	public final static String defaultCoordString = new String("x: ___ y: ___");

	/**
	 * Le formatteur à utiliser pour formater les nombres dans le
	 * {@link #coordLabel} et dans l'{@link #infoPanel}
	 */
	private final static DecimalFormat coordFormat = new DecimalFormat("000");

	/**
	 * état indiquant s'il faut envoyer les coordonnées de la souris ou la
	 * figure au dessus de laquelle se trouve la souris. Lorsque le curseur sort
	 * du widget (mouseExited) on cesse d'envoyer les coordonnées de la souris
	 * et lorsqu'elle entre (mouseEntered) on recommence à envoyer les
	 * coordonnées de la souris.
	 */
	private boolean sendInfoState;

	/**
	 * Constructeur de la zone de dessin à partir d'un modèle de dessin.
	 *
	 * @param drawing le modèle de dessin
	 * @param coordLabel le label à mettre à jour avec les coordonnées du
	 *            curseur de la souris
	 * @param infoPanel le panneau d'information des figures à mettre à jour
	 *            avec les informations relative à la figure située sous le
	 *            curseur de la souris
	 */
	public DrawingPanel(Drawing drawing, JLabel coordLabel, InfoPanel infoPanel)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		size = new Dimension(800, 600);
		setPreferredSize(size);
		addComponentListener(new ResizeListener());

		setBackground(Color.WHITE);
		setLayout(null);
		setDoubleBuffered(true);

		drawingModel = drawing;
		if (drawing != null)
		{
			drawingModel.addObserver(this);
		}
		else
		{
			System.err.println("DrawingPanel caution: null drawing");
		}

		this.coordLabel = coordLabel;

		if (this.coordLabel != null)
		{
			this.coordLabel.setText(defaultCoordString);
		}
		else
		{
			System.err.println("DrawingPanel : null coordLabel");
		}

		this.infoPanel = infoPanel;

		if (this.infoPanel != null)
		{
			this.infoPanel.resetLabels();
		}
		else
		{
			System.err.println("DrawingPanel : null infoPanel");
		}

		// DrawingPanel est son propre listener d'évènements souris
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	protected void finalize() throws Throwable
	{
		drawingModel.deleteObserver(this);
		super.finalize();
	}

	/**
	 * Accès à la taille effective du panel qui peut changer si celui-ci est
	 * agrandi (avec la fenêtre dans lequel il est par exemple). Cette méthode
	 * permet d'ajuster les scrollbars d'un container qui contiendrait ce panel
	 * lorsque la taille de celui-ci change.
	 *
	 * @return la taille effective du panel de dessin
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return size;
	}

	/**
	 * Mise en place du modèle de dessin. Met en place un nouveau modèle et s'il
	 * est non null ajoute ce panel comme observateur du modèle
	 *
	 * @param drawing le modèle de dessin à mettre en place
	 */
	public void setDrawing(Drawing drawing)
	{
		// retrait du précédent modèle de dessin (s'il existe)
		if (drawingModel != null)
		{
			drawing.deleteObserver(this);
		}

		// Mise en place du nouveau modèle de dessin
		drawingModel = drawing;
		if (drawingModel != null)
		{
			drawingModel.addObserver(this);
		}
	}

	/**
	 * Mise en place du label dans lequel afficher les coordonnées du pointeur
	 * de la souris.
	 *
	 * @param coordLabel le label dans lequel afficher les coordonnées du
	 *            pointeur de la souris.
	 */
	public void setCoordLabel(JLabel coordLabel)
	{
		this.coordLabel = coordLabel;
	}

	/**
	 * Mise en place du panel d'information dans lequel afficher les infos sur
	 * la figure située sous le curseur
	 *
	 * @param infoPanel l'{@link InfoPanel} à mettre en place
	 */
	public void setInfoPanel(InfoPanel infoPanel)
	{
		this.infoPanel = infoPanel;
	}

	/**
	 * Dessin du panel. Effacement ce celui-ci puis dessin des figures.
	 * @param g le contexte graphique
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g); // Inutile

		// caractéristiques graphiques : mise en place de l'antialiasing
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                     RenderingHints.VALUE_ANTIALIAS_ON);

		// taille de la zone de dessin
		Dimension d = getSize();
		// on commence par effacer le fond
		g2D.setColor(getBackground());
		g2D.fillRect(0, 0, d.width, d.height);

		// Puis on dessine l'ensemble des figures
		if (drawingModel != null)
		{
			/*
			 * Application d'un Consumer<Figure> en tant que lambda expression
			 * sur le flux (éventuellement filtré) des figures permettant
			 * de dessiner les figures
			 */
			drawingModel.stream().forEach((Figure f) -> f.draw(g2D));

			/*
			 * Soulignement des figures sélectionnées (s'il y en a).
			 * Le soulignement est séparé du dessin des figures elles mêmes
			 * de manière à apparaître par dessus les figures dessinées
			 */
			if (drawingModel.hasSelection())
			{
				drawingModel.stream().forEach((Figure f) -> f.drawSelection(g2D));
			}
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::paintComponent : null model");
		}
	}

	/**
	 * Mise en place d'un nouveau listener de figure
	 *
	 * @param fl le nouveau listener
	 */
	public void addFigureListener(AbstractFigureListener fl)
	{
		if (fl != null)
		{
			addMouseListener(fl);
			addMouseMotionListener(fl);
			// System.out.println("CreationListener " + cl + " added");
		}
		else
		{
			System.err.println("DrawingPanel.addFigureListener(null)");
		}
	}

	/**
	 * Retrait d'un listener de figure
	 *
	 * @param fl le creationListener à retirer
	 */
	public void removeFigureListener(AbstractFigureListener fl)
	{
		if (fl != null)
		{
			removeMouseListener(fl);
			removeMouseMotionListener(fl);
			// System.out.println("CreationListener " + cl + " removed");
		}
	}

	/**
	 * Mise à jour déclenchée par un {@link Observable#notifyObservers()} : en
	 * l'occurence le modèle de dessin ({@link Drawing}) lorsque celui ci est
	 * modifié. Cette mise à jour déclenche une requête de redessin du panel.
	 *
	 * @param observable l'observable ayant déclenché cette MAJ
	 * @param data les données (evt) transmises par l'observable [non utilisé ici]
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object data)
	{
		if (observable instanceof Drawing)
		{
			// Le modèle à changé il faut redessiner les figures
			repaint();
		}
	}

	/**
	 * Rafraichissement des panneaux d'information lors du déplacement de la
	 * souris
	 *
	 * @param e l'évènement souris associé
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		// Déplacement de la souris (btn enfoncé) : MAJ des coordonnées
		// de la souris dans le coordLabel et infoPanel
		refreshCoordLabel(e.getPoint());
		refreshInfoPanel(e.getPoint());
	}

	/**
	 * Rafraichissement des panneaux d'information lors du déplacement (bouton
	 * enfoncé) de la souris
	 *
	 * @param e l'évènement souris associé
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		// Déplacement de la souris : MAJ des coordonnées
		// de la souris dans le coordLabel et infoPanel
		Point p = e.getPoint();
		refreshCoordLabel(p);
		refreshInfoPanel(p);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Rien
	}

	/**
	 * Reprise du rafraichissement des panneaux d'information lorsque la souris
	 * rentre dans ce panel.
	 *
	 * @param e l'évènement souris associé
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		sendInfoState = true;
		refreshCoordLabel(e.getPoint());
		refreshInfoPanel(e.getPoint());
	}

	/**
	 * Arrêt du rafraichissement des panneaux d'information et effacement de ces
	 * panneaux lorsque la souris sort du panel.
	 *
	 * @param e l'évènement souris associé
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		// Rien si ce n'est de remettre les coordonnés dans la barre d'état
		// à x = ___ y = ___
		sendInfoState = false;
		refreshCoordLabel(e.getPoint());
		infoPanel.resetLabels();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// Rien
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Rien
	}

	/**
	 * Rafraichissement du {@link #coordLabel} (s'il est non null) avec de
	 * nouvelles coordonnées ou bien avec la {@link #defaultCoordString} si l'on
	 * affiche pas les coordonnées
	 *
	 * @param x l'abcisse des coordonnées à afficher
	 * @param y l'ordonnée des coordonnées à afficher
	 */
	private void refreshCoordLabel(Point p)
	{
		if ((coordLabel != null) && (p != null))
		{
			if (sendInfoState)
			{
				String xs = coordFormat.format(p.getX());
				String ys = coordFormat.format(p.getY());
				coordLabel.setText("x : " + xs + " y : " + ys);
			}
			else
			{
				coordLabel.setText(defaultCoordString);
			}
		}
	}

	/**
	 * Rafraichissement du panneau d'information {@link #infoPanel}
	 *
	 * @param p la position du curseur pour déclencher la recherche de figures
	 *            sous ce curseur
	 */
	private void refreshInfoPanel(Point2D p)
	{
		if ((infoPanel != null) && sendInfoState)
		{
			Figure selectedFigure = drawingModel.getFigureAt(p);

			if (selectedFigure != null)
			{
				infoPanel.updateLabels(selectedFigure);
			}
			else
			{
				infoPanel.resetLabels();
			}
		}
	}
}
