package widgets;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import figures.Drawing;
import figures.Figure;
import figures.enums.FigureType;
import figures.enums.LineType;
import figures.enums.PaintToType;
import figures.listeners.AbstractFigureListener;
import figures.listeners.SelectionFigureListener;
import figures.listeners.creation.AbstractCreationListener;
import figures.listeners.transform.AbstractTransformShapeListener;
import figures.listeners.transform.MoveShapeListener;
import figures.listeners.transform.RotateShapeListener;
import figures.listeners.transform.ScaleShapeListener;
import filters.EdgeColorFilter;
import filters.FillColorFilter;
import filters.LineFilter;
import filters.ShapeFilter;
import history.HistoryManager;
import history.Memento;
import utils.IconFactory;
import utils.PaintFactory;
import widgets.enums.OperationMode;
import javax.swing.JToggleButton;
import javax.swing.JTable;
import java.awt.Label;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;

/**
 * Classe de la fenêtre principale de l'éditeur de figures
 * @author davidroussel
 */
@SuppressWarnings("serial")
public class EditorFrame extends JFrame
{
	/**
	 * Le nom de l'éditeur
	 */
	protected static final String EditorName = "Figure Editor v4.0";

	/**
	 * Le modèle de dessin sous-jacent;
	 */
	protected Drawing drawingModel;

	/**
	 * Le gestionnaire d'historique pour les Undo/Redo
	 */
	protected HistoryManager<Figure> history;

	/**
	 * Taille de l'historique
	 */
	protected static final int historyLength = 32;

	/**
	 * Indique si l'éditeur est en mode Création de figures ou édition
	 * de figures (mode initial : création de figures)
	 */
	protected OperationMode operationMode = OperationMode.CREATION;

	/**
	 * La zone de dessin dans laquelle seront dessinées les figures.
	 * On a besoin d'une référence à la zone de dessin (contrairement aux
	 * autres widgets) car il faut lui affecter un xxxCreationListener en
	 * fonction de la figure choisie dans la liste des figures possibles.
	 */
	protected DrawingPanel drawingPanel;

	/**
	 * Le creationListener à mettre en place dans le drawingPanel en fonction
	 * du type de figure choisie;
	 */
	protected AbstractCreationListener creationListener;

	/**
	 * Le listener à mettre en place dans le drawingPanel lorsque l'on
	 * est en mode édition de figures pour déplacer les figures sélectionnées
	 */
	protected AbstractTransformShapeListener moveListener;

	/**
	 * Le listener à mettre en place dans le drawingPanel lorsque l'on
	 * est en mode édition de figures pour faire tourner les figures
	 * sélectionnées
	 */
	protected AbstractTransformShapeListener rotateListener;

	/**
	 * Le listener à mettre en place dans le drawingPanel lorsque l'on
	 * est en mode édition de figures pour changer l'échelle les figures
	 * sélectionnées
	 */
	protected AbstractTransformShapeListener scaleListener;

	/**
	 * Le listener de sélection des figures à mettre en place lorsque l'on
	 * est en mode édition.
	 */
	protected AbstractFigureListener selectionListener;

	/**
	 * Le label dans la barre d'état en bas dans lequel on affiche les
	 * conseils utilisateur pour créer une figure
	 */
	protected JLabel infoLabel;

	/**
	 * L'index de l'élément sélectionné par défaut pour le type de figure
	 */
	private final static int defaultFigureTypeIndex = 0;

	/**
	 * Les noms des couleurs de remplissage à utiliser pour remplir
	 * la [labeled]combobox des couleurs de remplissage
	 */
	protected final static String[] fillColorNames =
	    { "Black", "White", "Red", "Orange", "Yellow", "Green", "Cyan", "Blue",
	        "Magenta", "Others", "None" };

	/**
	 * Les couleurs de remplissage à utiliser en fonction de l'élément
	 * sélectionné dans la [labeled]combobox des couleurs de remplissage
	 */
	protected final static Paint[] fillPaints =
	    { Color.black, Color.white, Color.red, Color.orange, Color.yellow,
	        Color.green, Color.cyan, Color.blue, Color.magenta, null, // Color
	                                                                  // selected
	                                                                  // by a
	                                                                  // JColorChooser
	        null // No Color
		};

	/**
	 * L'index de l'élément sélectionné par défaut dans les couleurs de
	 * remplissage
	 */
	private final static int defaultFillColorIndex = 0; // black

	/**
	 * L'index de la couleur de remplissage à choisir avec un
	 * {@link JColorChooser} fournit par la {@link PaintFactory}
	 */
	private final static int specialFillColorIndex = 9;

	/**
	 * Les noms des couleurs de trait à utiliser pour remplir
	 * la [labeled]combobox des couleurs de trait
	 */
	protected final static String[] edgeColorNames = { "Magenta", "Red",
	    "Orange", "Yellow", "Green", "Cyan", "Blue", "Black", "Others" };

	/**
	 * Les couleurs de trait à utiliser en fonction de l'élément
	 * sélectionné dans la [labeled]combobox des couleurs de trait
	 */
	protected final static Paint[] edgePaints =
	    { Color.magenta, Color.red, Color.orange, Color.yellow, Color.green,
	        Color.cyan, Color.blue, Color.black, null // Color selected by a
	                                                  // JColorChooser
		};

	/**
	 * L'index de l'élément sélectionné par défaut dans les couleurs de
	 * trait
	 */
	private final static int defaultEdgeColorIndex = 6; // blue;

	/**
	 * L'index de la couleur de remplissage à choisir avec un
	 * {@link JColorChooser} fournit par la {@link PaintFactory}
	 */
	private final static int specialEdgeColorIndex = 8;

	/**
	 * L'index de l'élément sélectionné par défaut dans les types de
	 * trait
	 */
	private final static int defaultEdgeTypeIndex = 0; // none

	/**
	 * La largeur de trait par défaut
	 */
	private final static int defaultEdgeWidth = 4;

	/**
	 * Largeur de trait minimum
	 */
	private final static int minEdgeWidth = 1;

	/**
	 * Largeur de trait maximum
	 */
	private final static int maxEdgeWidth = 30;

	/**
	 * l'incrément entre deux largeurs de trait
	 */
	private final static int stepEdgeWidth = 1;

	/**
	 * Action déclenchée lorsque l'on clique sur le bouton quit ou sur l'item
	 * de menu quit
	 */
	private final Action quitAction = new QuitAction();

	/**
	 * Action déclenchée lorsque l'on clique sur le bouton undo ou sur l'item
	 * de menu undo
	 */
	private final Action undoAction = new UndoAction();

	/**
	 * Action réalisée lorsque l'on souhaite refaire une action qui vient
	 * d'être annulée
	 */
	private final Action redoAction = new RedoAction();

	/**
	 * Action déclenchée lorsque l'on clique sur le bouton clear ou sur l'item
	 * de menu clear
	 */
	private final Action clearAction = new ClearAction();

	/**
	 * Action déclenchée lorsque l'on clique sur le bouton about ou sur l'item
	 * de menu about
	 */
	private final Action aboutAction = new AboutAction();

	/**
	 * Action déclenchée lorsque l'on sélectionne de mode édition des figures
	 */
	private final Action toggleCreateEditAction = new ToggleCreateEditAction();

	/**
	 * Action déclenchée pour mettre filter ou non les figures
	 */
	private final Action filterAction = new FilterAction();

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des cercles
	 */
	private final Action circleFilterAction =
	    new ShapeFilterAction(FigureType.CIRCLE);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des ellipse
	 */
	private final Action ellipseFilterAction =
	    new ShapeFilterAction(FigureType.ELLIPSE);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des rectangles
	 */
	private final Action rectangleFilterAction =
	    new ShapeFilterAction(FigureType.RECTANGLE);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des rectangles arrondis
	 */
	private final Action rRectangleFilterAction =
	    new ShapeFilterAction(FigureType.ROUNDED_RECTANGLE);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des polygones
	 */
	private final Action polyFilterAction =
	    new ShapeFilterAction(FigureType.POLYGON);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des polygones réguliers
	 */
	private final Action ngonFilterAction =
	    new ShapeFilterAction(FigureType.NGON);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des type de lignes vides
	 */
	private final Action noneLineFilterAction =
	    new LineFilterAction(LineType.NONE);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des type de lignes pleines
	 */
	private final Action solidLineFilterAction =
	    new LineFilterAction(LineType.SOLID);

	/**
	 * Action déclenchée lorsque l'on clique sur l'item de menu de filtrage
	 * des type de lignes pointillées
	 */
	private final Action dashedLineFilterAction =
	    new LineFilterAction(LineType.DASHED);

	/**
	 * Action déclenchée pour mettre filter ou non les figures suivant
	 * la couleur de replissage courante
	 */
	private final Action fillColorFilterAction = new FillColorFilterAction();

	/**
	 * Action déclenchée pour mettre filter ou non les figures suivant
	 * la couleur de trait courante
	 */
	private final Action edgeColorFilterAction = new EdgeColorFilterAction();

	/**
	 * Action réalisée pour détruire les figures sélectionnées
	 */
	private final Action deleteAction = new DeleteAction();

	/**
	 * Action réalisée pour monter les figures sélectionnées en tête de liste
	 * des figures
	 */
	private final Action moveUpAction = new MoveUpAction();

	/**
	 * Action réalisée pour descendre les figures sélectionnées en fin de liste
	 * des figures
	 */
	private final Action moveDownAction = new MoveDownAction();

	/**
	 * Action réalisée pour appliquer le style courant (couleur de remplissage,
	 * couleur de trait et style de trait) aux figures sélectionnées
	 */
	private final Action styleAction = new StyleAction();
	private JTable table;

	/**
	 * Constructeur de la fenètre de l'éditeur.
	 * Construit les widgets et assigne les actions et autres listeners
	 * aux widgets
	 * @throws HeadlessException
	 */
	public EditorFrame() throws HeadlessException
	{
		drawingModel = new Drawing();
		history = new HistoryManager<Figure>(drawingModel, historyLength);
		operationMode = OperationMode.CREATION;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boolean isMacOS = System.getProperty("os.name").startsWith("Mac OS");

		/*
		 * Construire l'interface graphique en utilisant WindowBuilder:
		 * Menu Contextuel -> Open With -> WindowBuilder Editor puis
		 * aller dans l'onglet Design
		 */
		setPreferredSize(new Dimension(650, 450));
		creationListener = null;

		setTitle(EditorName);
		if (!isMacOS)
		{
			setIconImage(Toolkit.getDefaultToolkit()
			    .getImage(EditorFrame.class.getResource("/images/Logo.png")));
		}

		// --------------------------------------------------------------------
		// Toolbar en haut
		// --------------------------------------------------------------------
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton btnCancel = new JButton("Undo");
		btnCancel.setAction(undoAction);
		toolBar.add(btnCancel);

		JButton btnRedo = new JButton("Redo");
		btnRedo.setAction(redoAction);
		toolBar.add(btnRedo);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setAction(clearAction);
		toolBar.add(btnClear);
		
		JToggleButton tglbtnEdit = new JToggleButton("Edit");
		tglbtnEdit.setAction(toggleCreateEditAction);
		toolBar.add(tglbtnEdit);
		
		JButton btnUp = new JButton("Up");
		btnUp.setAction(moveUpAction);
		toolBar.add(btnUp);
		
		JButton btnDown = new JButton("Down");
		btnDown.setAction(moveDownAction);
		toolBar.add(btnDown);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setAction(deleteAction);
		toolBar.add(btnDelete);
		
		JButton btnStyle = new JButton("Style");
		btnStyle.setAction(styleAction);
		toolBar.add(btnStyle);

		Component toolBoxSpringer = Box.createHorizontalGlue();
		toolBar.add(toolBoxSpringer);

		JButton btnAbout = new JButton("About");
		btnAbout.setAction(aboutAction);
		toolBar.add(btnAbout);

		JButton btnClose = new JButton("Close");
		btnClose.setAction(quitAction);
		toolBar.add(btnClose);

		// --------------------------------------------------------------------
		// Barre d'état en bas
		// --------------------------------------------------------------------
		JPanel bottomPanel = new JPanel();
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		infoLabel = new JLabel(AbstractFigureListener.defaultTip);
		bottomPanel.add(infoLabel);

		Component horizontalGlue = Box.createHorizontalGlue();
		bottomPanel.add(horizontalGlue);

		JLabel coordsLabel = new JLabel(DrawingPanel.defaultCoordString);
		bottomPanel.add(coordsLabel);

		// --------------------------------------------------------------------
		// Panneau de contrôle à gauche
		// --------------------------------------------------------------------
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(220, 10));
		leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		getContentPane().add(leftPanel, BorderLayout.WEST);

		JLabeledComboBox figureTypeCombobox = new JLabeledComboBox("Shape",
		                                                           FigureType
		                                                               .stringValues(),
		                                                           defaultFigureTypeIndex,
		                                                           (ItemListener) null);
		figureTypeCombobox.setAlignmentX(Component.CENTER_ALIGNMENT);
		figureTypeCombobox.setPreferredSize(new Dimension(80, 32));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(figureTypeCombobox);
		
		JLabeledComboBox fillColorComboBox = new JLabeledComboBox("Fill Color", 
																fillColorNames, 
																0, 
																new ColorItemListener(fillPaints, 0, specialFillColorIndex, PaintToType.FILL));
		fillColorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		fillColorComboBox.setAlignmentY(Component.CENTER_ALIGNMENT);
		leftPanel.add(fillColorComboBox);
		
		JLabeledComboBox EdgeColorComboBox = new JLabeledComboBox(
						"Edge Color",  
						edgeColorNames, 
						0,  
						new ColorItemListener( 
								edgePaints,  
								0,  
								specialEdgeColorIndex,
								PaintToType.EDGE)); 
		EdgeColorComboBox.setAlignmentY(Component.CENTER_ALIGNMENT); 
		EdgeColorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT); 
		leftPanel.add(EdgeColorComboBox); 

		JLabeledComboBox lineTypeComboBox = 
				new JLabeledComboBox("LineType",
						new String[] {"None", "Solid", "Dashed"},
						0, 
						new EdgeTypeListener(LineType.NONE));
		leftPanel.add(lineTypeComboBox);
		lineTypeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		/*由于没有JLabeledSPinner,用网络布局使得“Line Width”和spinner出现在同一行*/
		//JPanel linepanel = new JPanel(new BorderLayout());
		//leftPanel.add(jpanel);
		JPanel linepanel = new JPanel();
		leftPanel.add(linepanel);
		linepanel.setLayout(new GridLayout(1,2,0,1));
		
		
		JLabel linewidthLabel = new JLabel("Line Width");
		linepanel.add(linewidthLabel);
		
		JSpinner spinner = new JSpinner();
		linepanel.add(spinner);
		spinner.setModel(new SpinnerNumberModel(defaultEdgeWidth, minEdgeWidth, maxEdgeWidth, stepEdgeWidth));
		EdgeWidthListener ewl = new EdgeWidthListener(defaultEdgeWidth);
		spinner.addChangeListener(ewl);

		/**/
		
		JPanel edgeWidthPanel = new JPanel();
		edgeWidthPanel.setPreferredSize(new Dimension(80, 32));
		leftPanel.add(edgeWidthPanel);
		edgeWidthPanel
		    .setLayout(new BoxLayout(edgeWidthPanel, BoxLayout.X_AXIS));
		SpinnerNumberModel snm =
		    new SpinnerNumberModel(defaultEdgeWidth,
		                           minEdgeWidth,
		                           maxEdgeWidth,
		                           stepEdgeWidth);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setAlignmentY(Component.TOP_ALIGNMENT);
		leftPanel.add(tabbedPane);
		
		TreesPanel treesPanel = new TreesPanel();
		treesPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		tabbedPane.addTab("Structure", new ImageIcon(EditorFrame.class.getResource("/images/Tree_small.png")), treesPanel, "Trees Structure");
		treesPanel.setDrawing(drawingModel);

		InfoPanel infoPanel = new InfoPanel();
		infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		tabbedPane.addTab("Info", IconFactory.getIcon("Details_small"), infoPanel, "Selected Figure");

		// --------------------------------------------------------------------
		// Zone de dessin
		// --------------------------------------------------------------------
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		drawingPanel = new DrawingPanel(drawingModel, coordsLabel, infoPanel);
		scrollPane.setViewportView(drawingPanel);

		// --------------------------------------------------------------------
		// Barre de menus
		// --------------------------------------------------------------------
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("Drawing");
		menuBar.add(mnFile);

		JMenuItem mntmCancel = new JMenuItem("Cancel");
		mntmCancel.setAction(undoAction);
		mnFile.add(mntmCancel);

		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.setAction(redoAction);
		mnFile.add(mntmRedo);

		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.setAction(clearAction);
		mnFile.add(mntmClear);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAction(quitAction);
		mnFile.add(mntmQuit);

		JMenu mnEdition = new JMenu("Edition");
		menuBar.add(mnEdition);
		
		JCheckBoxMenuItem chckbxmntmEdit = new JCheckBoxMenuItem("Editing");/*带文本的，最初为被选定的复选框菜单*/
		chckbxmntmEdit.setAction(toggleCreateEditAction);
		mnEdition.add(chckbxmntmEdit);

		JMenu mnFilter = new JMenu("Filter");
		menuBar.add(mnFilter);

		JCheckBoxMenuItem chckbxmntmFiltering =
		    new JCheckBoxMenuItem("Filtering");
		chckbxmntmFiltering.setAction(filterAction);
		mnFilter.add(chckbxmntmFiltering);

		JMenu mnFigures = new JMenu("Figures");
		// add 2017年5月22日
		JCheckBoxMenuItem chckbxmntmCircles = new JCheckBoxMenuItem("Circles");
		chckbxmntmCircles.setAction(circleFilterAction);
		mnFigures.add(chckbxmntmCircles);

		JCheckBoxMenuItem chckbxmntmEllipses = new JCheckBoxMenuItem("Ellipse");
		chckbxmntmEllipses.setAction(ellipseFilterAction);
		mnFigures.add(chckbxmntmEllipses);

		JCheckBoxMenuItem chckbxmntmRectangles = new JCheckBoxMenuItem("Rectangle");
		chckbxmntmRectangles.setAction(rectangleFilterAction);
		mnFigures.add(chckbxmntmRectangles);

		JCheckBoxMenuItem chckbxmntmRoundedRectangles = new JCheckBoxMenuItem("RoundedRectangle");
		chckbxmntmRoundedRectangles.setAction(rRectangleFilterAction);
		mnFigures.add(chckbxmntmRoundedRectangles);

		JCheckBoxMenuItem chckbxmntmPolygons = new JCheckBoxMenuItem("Polygon");
		chckbxmntmPolygons.setAction(polyFilterAction);
		mnFigures.add(chckbxmntmPolygons);

		JCheckBoxMenuItem chckbxmntmNgon = new JCheckBoxMenuItem("Ngon");
		chckbxmntmNgon.setAction(ngonFilterAction);
		mnFigures.add(chckbxmntmNgon);
		//
		mnFilter.add(mnFigures);

		JMenu mnColors = new JMenu("Colors");
		// add 2017年5月22日
		JCheckBoxMenuItem chckbxmntmFillColor = new JCheckBoxMenuItem("Fill Color");
		chckbxmntmFillColor.setAction(fillColorFilterAction);
		mnColors.add(chckbxmntmFillColor);

		JCheckBoxMenuItem chckbxmntmEdgeColor = new JCheckBoxMenuItem("Edge Color");
		chckbxmntmEdgeColor.setAction(edgeColorFilterAction);
		mnColors.add(chckbxmntmEdgeColor);
		//
		mnFilter.add(mnColors);

		JMenu mnStrokes = new JMenu("Strokes");
		// add 2017年5月22日
		JCheckBoxMenuItem chckbxmntmNone = new JCheckBoxMenuItem("None");
		chckbxmntmNone.setAction(noneLineFilterAction);
		chckbxmntmNone.setIcon(IconFactory.getIcon("None_small"));
		mnStrokes.add(chckbxmntmNone);

		JCheckBoxMenuItem chckbxmntmSolid = new JCheckBoxMenuItem("Solid");
		chckbxmntmSolid.setAction(solidLineFilterAction);
		chckbxmntmSolid.setIcon(IconFactory.getIcon("Solid_small"));
		mnStrokes.add(chckbxmntmSolid);

		JCheckBoxMenuItem chckbxmntmDashed = new JCheckBoxMenuItem("Dash");
		chckbxmntmDashed.setAction(dashedLineFilterAction);
		chckbxmntmDashed.setIcon(IconFactory.getIcon("Dashed_small"));
		mnStrokes.add(chckbxmntmDashed);
		//
		mnFilter.add(mnStrokes);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.setAction(aboutAction);
		mnHelp.add(mntmAbout);

		// --------------------------------------------------------------------
		// Ajout des contrôleurs aux widgets
		// pour connaître les Listeners applicable à un widget
		// dans WindowBuilder, sélectionnez un widger de l'UI puis Menu
		// Contextuel -> Add event handler
		// --------------------------------------------------------------------
		moveListener = new MoveShapeListener(drawingModel, history, infoLabel);
		scaleListener =  new ScaleShapeListener(drawingModel, history, infoLabel);
		rotateListener = new RotateShapeListener(drawingModel, history, infoLabel);
		selectionListener = new SelectionFigureListener(drawingModel, history, infoLabel);

		figureTypeCombobox.addItemListener(new ShapeItemListener(FigureType
		    .fromInteger(figureTypeCombobox.getSelectedIndex())));
	}

	/**
	 * Action pour quitter l'application
	 * @author davidroussel
	 */
	private class QuitAction extends AbstractAction // implements QuitHandler
	{
		/**
		 * Constructeur de l'action pour quitter l'application.
		 * Met en place le raccourci clavier, l'icône et la description
		 * de l'action
		 */
		public QuitAction()
		{
			putValue(NAME, "Quit");
			/*
			 * Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
			 * = InputEvent.CTRL_MASK on win/linux
			 * = InputEvent.META_MASK on mac os
			 */
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Quit"));
			putValue(SMALL_ICON, IconFactory.getIcon("Quit_small"));
			putValue(SHORT_DESCRIPTION, "Quits the application");
		}

		/**
		 * Opérations réalisées par l'action : Quitte l'application
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			doQuit();
		}

		/**
		 * Action réalisée pour quitter dans un {@link Action}
		 */
		private void doQuit()
		{
			/*
			 * Action à effectuer lorsque l'action "undo" est cliquée :
			 * sortir avec un System.exit() (pas très propre, mais fonctionne)
			 */
			System.exit(0);
		}
	}

	/**
	 * Action réalisée pour effacer la dernière action dans le dessin
	 */
	private class UndoAction extends AbstractAction
	{
		/**
		 * Constructeur de l'action effacer la dernière action sur le dessin
		 * Met en place le raccourci clavier, l'icône et la description
		 * de l'action
		 */
		public UndoAction()
		{
			putValue(NAME, "Undo");
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_Z,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Undo"));
			putValue(SMALL_ICON, IconFactory.getIcon("Undo_small"));
			putValue(SHORT_DESCRIPTION, "Undo last drawing");
		}

		/**
		 * Opérations réalisées par l'action : Mise en place du dernier
		 * Memento enregistré dans la pile des undo
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			drawingModel.setMemento(history.undo());
		}
	}

	/**
	 * Action réalisée pour refaire la dernière action (qui a été annulée)
	 * dans le dessin
	 */
	private class RedoAction extends AbstractAction
	{
		public RedoAction()
		{
			putValue(NAME, "Redo");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Redo"));
			putValue(SMALL_ICON, IconFactory.getIcon("Redo_small"));
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_Z,
			                                InputEvent.SHIFT_MASK
			                                    | Toolkit.getDefaultToolkit()
			                                        .getMenuShortcutKeyMask()));
			putValue(SHORT_DESCRIPTION, "Redo last drawing");
		}

		/**
		 * Opérations réalisées par l'action : Mise en place du dernier
		 * Memento enregistré dans la pile des redo
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			drawingModel.setMemento(history.redo());
		}
	}

	/**
	 * Action réalisée pour effacer toutes les figures du dessin
	 */
	private class ClearAction extends AbstractAction
	{
		/**
		 * Constructeur de l'action pour effacer toutes les figures du dessin
		 * Met en place le raccourci clavier, l'icône et la description
		 * de l'action
		 */
		public ClearAction()
		{
			putValue(NAME, "Clear");
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_X,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Clear"));
			putValue(SMALL_ICON, IconFactory.getIcon("Clear_small"));
			putValue(SHORT_DESCRIPTION, "Erase all drawings");
		}

		/**
		 * Opérations réalisées par l'action : Effacement de toutes les figures
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			/*
			 * Action à effectuer lorsque l'action "clear" est cliquée :
			 * Effacer toutes les figures du dessin
			 */
			drawingModel.clear();
		}
	}

	/**
	 * Action réalisée pour afficher la boite de dialogue "A propos ..."
	 */
	private class AboutAction extends AbstractAction // implements AboutHandler
	{
		/**
		 * Constructeur de l'action pour afficher la boite de dialogue
		 * "A propos ..." Met en place le raccourci clavier, l'icône et la
		 * description de l'action
		 */
		public AboutAction()
		{
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_I,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("About"));
			putValue(SMALL_ICON, IconFactory.getIcon("About_small"));
			putValue(NAME, "About");
			putValue(SHORT_DESCRIPTION, "App information");
		}

		/**
		 * Opérations réalisées par l'action : Affichage d'une boite de dialogue
		 * Affichant des infos sur l'application
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			doAbout(e);
		}
		
		
		/**
		 * Action réalisée pour "A propos" dans un {@link Action}
		 * @param e l'évènement ayant déclenché l'action
		 */
		private void doAbout(EventObject e)
		{
			/*
			 * Action à effectuer lorsque l'action "about" est cliquée :
			 * Ouvrir un MessageDialog (JOptionPane.showMessageDialog(...)) de
			 * type JOptionPane.INFORMATION_MESSAGE
			 */
			Object source = e.getSource();
			Component component =
			    source instanceof Component ? (Component) source : null;
			JOptionPane.showMessageDialog(component,
			                              EditorName,
			                              "About ...",
			                              JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Action réalisée lorsque l'on passe en mode édition des figures
	 */
	private class ToggleCreateEditAction extends AbstractAction
	{
		/**
		 * Liste des "boutons" pouvant déclencher cette action.
		 * De manière à ce que lorqu'un bouton déclenche l'action
		 * les autres boutons soient eux aussi mis dans l'état correspondant
		 * à l'action
		 */
		private List<AbstractButton> buttons;

		/**
		 * Constructeur de l'action pour mettre en place ou enlever un filtre
		 * pour filtrer les types de figures
		 */
		public ToggleCreateEditAction()
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.ALT_MASK));
			putValue(NAME, "Edition");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Edition"));
			putValue(SMALL_ICON, IconFactory.getIcon("Edition_small"));
			putValue(SHORT_DESCRIPTION, "Édition des figures");

			buttons = new ArrayList<AbstractButton>();
		}

		/**
		 * Ajout d'un bouton déclenchant cette action
		 * @param button le bouton à ajouter à la liste des boutons
		 * @return true si le bouton a été ajouté à la liste des boutons
		 * déclenchant cette action, false si le bouton était déjà présent
		 * dans la liste des actions et n'a pas été ajouté
		 */
		public boolean registerButton(AbstractButton button)
		{
			if (!buttons.contains(button))
			{
				return buttons.add(button);
			}
			return false;
		}

		/**
		 * Opérations réalisées par l'action : Changement de mode Création /
		 * Edition des figures
		 * @param event l'évènement déclenchant l'action. Peut provenir d'un
		 * bouton ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			AbstractButton button = (AbstractButton) event.getSource();
			boolean selected = button.getModel().isSelected();

			/*
			 * TODO Parcourir tous les "buttons" pour s'assurer qu'ils sont
			 * bien dans l'état voulu
			 */

			/*
			 * TODO
			 * Si on est en mode :
			 * 	- Creation : on met en place le creationListener courant dans
			 * 	drawingPanelpour créer la prochaine figure et on enlève de
			 * 	drawingPanel tous les listeners pour modifier les figures
			 * 	- Edition On retire le creationListener de drawingPanel puis on
			 * 	met en places les listeners dans drawingPanel pour
			 * 		- pouvoir sélectionner/désélectionner des figures
			 * 		- déplacer des figures
			 * 		- tourner des figrues
			 * 		- changer l'échelle des figures
			 *
			 */
			for (Iterator<AbstractButton> bit = buttons.iterator(); bit.hasNext();)
			{
				AbstractButton nextButton = bit.next();
				if (nextButton.isSelected() != selected)
					nextButton.setSelected(selected);
			}

			if (selected)
			{
				System.out.println("Switch to mode Edition");
				operationMode = OperationMode.TRANSFORMATION;

				drawingPanel.removeFigureListener(creationListener);
				drawingPanel.addFigureListener(selectionListener);
				drawingPanel.addFigureListener(moveListener);
				drawingPanel.addFigureListener(rotateListener);
				drawingPanel.addFigureListener(scaleListener);
			}
			else
			{
				System.out.println("Switch to mode Creation");
				operationMode = OperationMode.CREATION;

				drawingPanel.removeFigureListener(scaleListener);
				drawingPanel.removeFigureListener(rotateListener);
				drawingPanel.removeFigureListener(moveListener);
				drawingPanel.removeFigureListener(selectionListener);
				drawingPanel.addFigureListener(creationListener);
			}
		}
	}


	/**
	 * Action réalisée pour filtrer ou pas le flux de figures
	 */
	private class FilterAction extends AbstractAction
	{
		/**
		 * Constructeur de l'action pour mettre en place ou enlever un filtre
		 * pour filtrer les types de figures
		 */
		public FilterAction()
		{
			putValue(NAME, "Filter");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Filter"));
			putValue(SMALL_ICON, IconFactory.getIcon("Filter_small"));
			putValue(SHORT_DESCRIPTION, "Set/unset filtering");
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_F,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));

		}

		/**
		 * Opérations réalisées par l'action : Mise en place ou arrêt du
		 * filtrage des figures
		 * @param event l'évènement déclenchant l'action. Peut provenir d'un
		 * bouton ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			AbstractButton button = (AbstractButton) event.getSource();
			boolean selected = button.getModel().isSelected();

			// TODO Compléter ...
			drawingModel.setFiltering(selected);
		}
	}


	/**
	 * Action réalisée pour ajouter ou retirer un filtre de type de figure
	 */
	private class ShapeFilterAction extends AbstractAction
	{
		/**
		 * Le type de figure
		 */
		private FigureType type;

		/**
		 * Constructeur de l'action pour mettre en place ou enlever un filtre
		 * pour filtrer les types de figures
		 */
		public ShapeFilterAction(FigureType type)
		{
			this.type = type;
			String name = type.toString();
			putValue(LARGE_ICON_KEY, IconFactory.getIcon(name));
			putValue(SMALL_ICON, IconFactory.getIcon(name + "_small"));
			putValue(NAME, name);
			putValue(SHORT_DESCRIPTION, "Set/unset " + name + " filter");
		}

		/**
		 * Opérations réalisées par l'action : Ajout ou retrait d'un filtre
		 * concernant un type particulier de figure ({@link #type})
		 * @param event l'évènement déclenchant l'action. Peut provenir d'un
		 * bouton ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			AbstractButton button = (AbstractButton) event.getSource();
			boolean selected = button.getModel().isSelected();

			// TODO Compléter ...
			// After filter create
			ShapeFilter sf = new ShapeFilter(type);
			if (selected) drawingModel.addShapeFilter(sf);
			else drawingModel.removeShapeFilter(sf);
		}

		/**
		 * Action réalisée pour "A propos" dans un {@link Action}
		 * @param e l'évènement ayant déclenché l'action
		 */
		private void doAbout(EventObject e)
		{
			/*
			 * Action à effectuer lorsque l'action "about" est cliquée :
			 * Ouvrir un MessageDialog (JOptionPane.showMessageDialog(...)) de
			 * type JOptionPane.INFORMATION_MESSAGE
			 */
			Object source = e.getSource();
			Component component =
			    source instanceof Component ? (Component) source : null;
			JOptionPane.showMessageDialog(component,
			                              EditorName,
			                              "About ...",
			                              JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Action réalisée pour ajouter ou retirer un filtre de type trait de figure
	 */
	private class LineFilterAction extends AbstractAction
	{
		/**
		 * Le type de trait de la figure
		 */
		private LineType type;

		/**
		 * Constructeur de l'action pour mettre en place ou enlever un filtre
		 * pour filtrer les types de figures
		 */
		public LineFilterAction(LineType type)
		{
			this.type = type;
			String name = type.toString();
			putValue(LARGE_ICON_KEY, IconFactory.getIcon(name));
			putValue(SMALL_ICON, IconFactory.getIcon(name + "_small"));
			putValue(NAME, name);
			putValue(SHORT_DESCRIPTION, "Set/unset " + name + " filter");
		}

		/**
		 * Opérations réalisées par l'action : Ajout ou retrait d'un filtre
		 * concernant le type de trait des figures
		 * @param event l'évènement déclenchant l'action. Peut provenir d'un
		 * bouton ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			AbstractButton button = (AbstractButton) event.getSource();
			boolean selected = button.getModel().isSelected();

			// TODO Compléter ...
			LineFilter lf = new LineFilter(type);
			if (selected)
				drawingModel.addLineFilter(lf);
			else
				drawingModel.removeLineFilter(lf);
		}
	}

	/**
	 * Action pour mettre en place un filtre basé sur la couleur de remplissage
	 * courante
	 */
	private class FillColorFilterAction extends AbstractAction
	{
		/**
		 * Constructeur de l'action
		 * Met en place le raccourci clavier, l'icône et la description
		 * de l'action
		 */
		public FillColorFilterAction()
		{
			putValue(NAME, "Fill Color");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("FillColor"));
			putValue(SMALL_ICON, IconFactory.getIcon("FillColor_small"));
			putValue(SHORT_DESCRIPTION, "Set/Unset Fill Color Filter");
		}

		/**
		 * Opérations réalisées par l'action : Ajout ou retrait du filtre
		 * de couleur de remplissage en fonction de la couleur de remplissage
		 * courante.
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AbstractButton button = (AbstractButton) e.getSource();
			boolean selected = button.getModel().isSelected();

			// TODO Compléter ...
			if (selected)
			{
				drawingModel
				    .setFillColorFilter(new FillColorFilter(drawingModel
				        .getFillpaint()));
			}
			else
				drawingModel.setFillColorFilter(null);
		}
	}

	/**
	 * Action pour mettre en place un filtre basé sur la couleur de trait
	 * courante
	 */
	private class EdgeColorFilterAction extends AbstractAction
	{
		/**
		 * Constructeur de l'action
		 * Met en place le raccourci clavier, l'icône et la description
		 * de l'action
		 */
		public EdgeColorFilterAction()
		{
			putValue(NAME, "Edge Color");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("EdgeColor"));
			putValue(SMALL_ICON, IconFactory.getIcon("EdgeColor_small"));
			putValue(SHORT_DESCRIPTION, "Set/Unset edge color filter");
		}

		/**
		 * Opérations réalisées par l'action : Ajout ou retrait d'un filtre
		 * concernant la couleur de trait d'après la couleur de trait courante.
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AbstractButton button = (AbstractButton) e.getSource();
			boolean selected = button.getModel().isSelected();

			// TODO Compléter ...
			if (selected)
			{
				drawingModel
				    .setEdgeColorFilter(new EdgeColorFilter(drawingModel
				        .getEdgePaint()));
			}
			else
				drawingModel.setEdgeColorFilter(null);
		}
	}

	/**
	 * Action réalisée pour détruire les figures sélectionnées
	 * @author davidroussel
	 */
	private class DeleteAction extends AbstractAction
	{
		public DeleteAction()
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
			putValue(NAME, "Delete");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Delete"));
			putValue(SMALL_ICON, IconFactory.getIcon("Delete_small"));
			putValue(SHORT_DESCRIPTION, "Delete selected figures");
		}

		/**
		 * Opérations réalisées par l'action : Retrait des figures sélectionnées.
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Compléter ...
			drawingModel.deleteSelected();
		}
	}

	/**
	 * Action réalisée pour remonter les figures sélectionnées dans la liste
	 * des figures
	 */
	private class MoveUpAction extends AbstractAction
	{
		public MoveUpAction()
		{
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_UP,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));
			putValue(NAME, "Up");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("MoveUp"));
			putValue(SMALL_ICON, IconFactory.getIcon("MoveUp_small"));
			putValue(SHORT_DESCRIPTION, "Move selected figures up");
		}

		/**
		 * Opérations réalisées par l'action : Déplacement des figures
		 * sélectionnées en haut de la liste des figures.
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Compléter ...
			drawingModel.moveSelectedUp();
		}
	}

	/**
	 * Action réalisée pour descendre les figures sélectionnées dans la liste
	 * des figures
	 */
	private class MoveDownAction extends AbstractAction
	{
		public MoveDownAction()
		{
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));
			putValue(NAME, "Down");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("MoveDown"));
			putValue(SMALL_ICON, IconFactory.getIcon("MoveDown_small"));
			putValue(SHORT_DESCRIPTION, "Move selected figures down");
		}

		/**
		 * Opérations réalisées par l'action : Déplacement des figures
		 * sélectionnées en bas de la liste des figures.
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Compléter ...
//			drawingModel.
			drawingModel.moveSelectedDown();
		}
	}

	/**
	 * Action réalisée pour appliquer le style courant aux figures
	 * sélectionnées,
	 * A savoir :
	 * <ul>
	 * <li>La couleur de remplissage courante</li>
	 * <li>La couleur de trait courante</li>
	 * <li>Le type de trait courant (style et épaisseur)</li>
	 * </ul>
	 */
	private class StyleAction extends AbstractAction
	{
		public StyleAction()
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
			putValue(NAME, "Style");
			putValue(LARGE_ICON_KEY, IconFactory.getIcon("Style"));
			putValue(SMALL_ICON, IconFactory.getIcon("Style_small"));
			putValue(SHORT_DESCRIPTION,
			         "Apply current style to selected figures");
		}

		/**
		 * Opérations réalisées par l'action : Application du style courant (
		 * couleur de remplissage, couleur de trait, type de trait et épaisseur
		 * du trait) aux figures sélectionnées.
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Compléter ...
			Paint fill = drawingModel.getFillpaint();
			Paint edge = drawingModel.getEdgePaint();
			BasicStroke stroke = drawingModel.getStroke();

			drawingModel.applyStyleToSelected(fill, edge, stroke);
		}
	}

	/**
	 * Contrôleur d'évènement permettant de modifier le type de figures à
	 * dessiner.
	 * @note dépends de #drawingModel et #infoLabel qui doivent être non
	 * null avant instanciation
	 */
	private class ShapeItemListener implements ItemListener
	{
		/**
		 * Constructeur valué du contrôleur.
		 * Initialise le type de dessin dans {@link EditorFrame#drawingModel}
		 * et crée le {@link AbstractCreationListener} correspondant.
		 * @param initialIndex l'index du type de forme sélectionné afin de
		 * mettre en place le bon creationListener dans le
		 * {@link EditorFrame#drawingPanel}.
		 */
		public ShapeItemListener(FigureType type)
		{
			// Mise en place du type de figure
			drawingModel.setFigureType(type);

			// Mise en place du type de creationListener
			creationListener = type.getCreationListener(drawingModel,
			                                            history,
			                                            infoLabel);
			drawingPanel.addFigureListener(creationListener);
		}

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			JComboBox<?> items = (JComboBox<?>) e.getSource();
			int index = items.getSelectedIndex();
			int stateChange = e.getStateChange();
			FigureType figureType = FigureType.fromInteger(index);
			switch (stateChange)
			{
				case ItemEvent.SELECTED:
				{
					// Mise en place d'un nouveau type de figure
					drawingModel.setFigureType(figureType);
					AbstractCreationListener newCreationListener =
					    figureType.getCreationListener(drawingModel,
					                                   history,
					                                   infoLabel);
					if (operationMode == OperationMode.CREATION)
					{
						// Mise en place d'un nouveau type de creationListener
						// Après avoir retiré l'ancien dans le drawingPanel
						drawingPanel.removeFigureListener(creationListener);
						drawingPanel.addFigureListener(newCreationListener);
					}
					creationListener = newCreationListener;
					break;
				}
			}
		}
	}

	/**
	 * Contrôleur d'évènements permettant de modifier la couleur du trait.
	 * @note utilise #drawingModel qui doit être non null avant instanciation
	 * @note A associer comme listener au JJ[Labeled]ComboBox des couleurs de
	 * remplissage ou de trait
	 */
	private class ColorItemListener implements ItemListener
	{
		/**
		 * Ce à quoi s'applique la couleur choisie.
		 * Soit au rmplissage, soit au trait.
		 */
		private PaintToType applyTo;

		/**
		 * La dernière couleur choisie (pour le {@link JColorChooser})
		 */
		private Color lastColor;

		/**
		 * Le tableau des couleurs possibles
		 */
		private Paint[] colors;

		/**
		 * L'index de la couleur spéciale à choisir avec un
		 * {@link JColorChooser}
		 */
		private final int customColorIndex;

		/**
		 * L'index de la dernière couleur sélectionnée dans le combobox.
		 * Afin de pouvoir y revenir si jamais le {@link JColorChooser} est
		 * annulé.
		 */
		private int lastSelectedIndex;

		/**
		 * la couleur choisie
		 */
		private Paint paint;

		/**
		 * Constructeur du contrôleur d'évènements d'un combobox permettant
		 * de choisir la couleur de templissage
		 * @param colors le tableau des couleurs possibles
		 * @param selectedIndex l'index de l'élément actuellement sélectionné
		 * @param customColorIndex l'index de la couleur spéciale parmis les
		 * colors à définir à l'aide d'un {@link JColorChooser}.
		 * @param applyTo Ce à quoi s'applique la couleur (le remplissage ou
		 * bien le trait)
		 */
		public ColorItemListener(Paint[] colors,
		                         int selectedIndex,
		                         int customColorIndex,
		                         PaintToType applyTo)
		{
			this.colors = colors;
			lastSelectedIndex = selectedIndex;
			this.customColorIndex = customColorIndex;
			this.applyTo = applyTo;
			lastColor = (Color) colors[selectedIndex];
			paint = colors[selectedIndex];

			applyTo.applyPaintTo(paint, drawingModel);
		}

		/**
		 * Actions à réaliser lorsque l'élément sélectionné du combox change
		 * @param e l'évènement de changement d'item du combobox
		 */
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			JComboBox<?> combo = (JComboBox<?>) e.getSource();
			int index = combo.getSelectedIndex();

			if ((index >= 0) && (index < colors.length))
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					// New color has been selected
					if (index == customColorIndex) // Custom color from chooser
					{
						Paint chosenColor = PaintFactory
						    .getPaint(combo,
						              "Choose " + applyTo.toString() + " Color",
						              lastColor);
						if (chosenColor != null)
						{
							paint = chosenColor;
						}
						else
						{
							// ColorChooser has been cancelled we should go
							// back to last selected index
							combo.setSelectedIndex(lastSelectedIndex);

							// paint does not change
						}
					}
					else // regular color
					{
						paint = colors[index];
					}

					lastColor = (Color) paint;
					applyTo.applyPaintTo(paint, drawingModel);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED)
				{
					// Old color has been delesected
					if ((index >= 0) && (index < customColorIndex))
					{
						lastColor = (Color) edgePaints[index];
						lastSelectedIndex = index;
					}
				}
			}
			else
			{
				System.err.println("Unknown " + applyTo.toString()
				    + " color index : " + index);
			}
		}
	}

	/**
	 * Contrôleur d'évènements permettant de modifier le type de trait (normal,
	 * pointillé, sans trait)
	 * @note utilise #drawingModel qui doit être non null avant instanciation
	 * @note A associer comme listener au J[Labeled]Combobox des types de traits
	 */
	private class EdgeTypeListener implements ItemListener
	{
		/**
		 * Le type de trait à mettre en place
		 */
		private LineType edgeType;

		public EdgeTypeListener(LineType type)
		{
			edgeType = type;
			drawingModel.setEdgeType(edgeType);
		}

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			JComboBox<?> items = (JComboBox<?>) e.getSource();
			int index = items.getSelectedIndex();

			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				// actions à réaliser lorsque le type de trait change
				LineType type = LineType.fromInteger(index);
				drawingModel.setEdgeType(type);
			}
		}
	}

	/**
	 * Contrôleur d'évènement permettant de modifier la taille du trait
	 * en fonction des valeurs d'un {@link JSpinner}
	 * @note à associer comme listener au JSpinner de l'épaisseur de trait
	 */
	private class EdgeWidthListener implements ChangeListener
	{
		/**
		 * Constructeur du contrôleur d'évènements contrôlant l'épaisseur du
		 * trait
		 * @param initialValue la valeur initiale de la largeur du trait à
		 * appliquer au dessin (EditorFrame#drawingModel)
		 */
		public EdgeWidthListener(int initialValue)
		{
			drawingModel.setEdgeWidth(initialValue);
		}

		/**
		 * Actions à réaliser lorsque la valeur du spinner change
		 * @param e l'évènement de changement de valeur du spinner
		 */
		@Override
		public void stateChanged(ChangeEvent e)
		{
			JSpinner spinner = (JSpinner) e.getSource();
			SpinnerNumberModel spinnerModel =
			    (SpinnerNumberModel) spinner.getModel();

			drawingModel.setEdgeWidth(spinnerModel.getNumber().floatValue());
		}
	}

	/**
	 * Action pour ...
	 * @author davidroussel
	 */
	@SuppressWarnings("unused")
	private class EmptyAction extends AbstractAction
	{
		/**
		 * Constructeur de l'action pour ....
		 * Met en place le raccourci clavier, l'icône et la description
		 * de l'action
		 */
		public EmptyAction()
		{
			String name = "XXX";
			putValue(NAME, name);
			/*
			 * Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
			 * = InputEvent.CTRL_MASK on win/linux
			 * = InputEvent.META_MASK on mac os
			 */
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_X,
			                                Toolkit.getDefaultToolkit()
			                                    .getMenuShortcutKeyMask()));
			putValue(LARGE_ICON_KEY, IconFactory.getIcon(name));
			putValue(SMALL_ICON, IconFactory.getIcon(name + "_small"));
			putValue(SHORT_DESCRIPTION, "Description de l'action");
		}

		/**
		 * Opérations réalisées par l'action
		 * @param e l'évènement déclenchant l'action. Peut provenir d'un bouton
		 * ou d'un item de menu
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AbstractButton button = (AbstractButton) e.getSource();
			boolean selected = button.getModel().isSelected();

			// drawingModel.awesomeMethod(...)
		}
	}
}
