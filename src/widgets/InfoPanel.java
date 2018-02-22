package widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import figures.Figure;
import figures.enums.FigureType;
import figures.enums.LineType;
import utils.IconFactory;
import utils.PaintFactory;

public class InfoPanel extends JPanel
{
	/**
	 * Une chaine vide pour remplir les champs lorsque la souris n'est au dessus
	 * d'aucune figure
	 */
	private static final String emptyString = new String();

	/**
	 * Une icône vide pour remplir les chanmps avec icône lorsque la souris
	 * n'est au dessus d'aucune figure
	 */
	private static final ImageIcon emptyIcon = IconFactory.getIcon("None");

	/**
	 * Le formatteur à utiliser pour formater les coordonnés
	 */
	private final static DecimalFormat coordFormat = new DecimalFormat("000");

	/**
	 * Le label contenant le nom de la figure
	 */
	private JLabel lblFigureName;

	/**
	 * Le label contenant l'icône correspondant à la figure
	 */
	private JLabel lblTypeicon;

	/**
	 * La map contenant les différentes icônes des types de figures
	 */
	private Map<FigureType, ImageIcon> figureIcons;

	/**
	 * Le label contenant l'icône de la couleur de remplissage
	 */
	private JLabel lblFillcolor;

	/**
	 * Le label contenant l'icône de la couleur du contour
	 */
	private JLabel lblEdgecolor;

	/**
	 * Map contenant les icônes relatives aux différentes couleurs (de contour
	 * ou de remplissage)
	 */
	private Map<Paint, ImageIcon> paintIcons;

	/**
	 * Le label contenant le type de contour
	 */
	private JLabel lblStroketype;

	/**
	 * Map contenant les icônes relatives au différents types de traits de
	 * contour
	 */
	private Map<LineType, ImageIcon> lineTypeIcons;

	/**
	 * Le label contenant l'abcisse du point en haut à gauche de la figure
	 */
	private JLabel lblTlx;

	/**
	 * Le label contenant l'ordonnée du point en haut à gauche de la figure
	 */
	private JLabel lblTly;

	/**
	 * Le label contenant l'abcisse du point en bas à droite de la figure
	 */
	private JLabel lblBrx;

	/**
	 * Le label contenant l'ordonnée du point en bas à droite de la figure
	 */
	private JLabel lblBry;

	/**
	 * Le label contenant la largeur de la figure
	 */
	private JLabel lblDx;

	/**
	 * Le label contenant la hauteur de la figure
	 */
	private JLabel lblDy;

	/**
	 * Le label contenant l'abcisse du barycentre de la figure
	 */
	private JLabel lblCx;

	/**
	 * Le label contenant l'ordonnée du barycentre de la figure
	 */
	private JLabel lblCy;

	/**
	 * Create the panel.
	 */
	public InfoPanel()
	{
		// --------------------------------------------------------------------
		// Initialisation des maps
		// --------------------------------------------------------------------
		figureIcons = new HashMap<FigureType, ImageIcon>();
		for (int i = 0; i < FigureType.NbFigureTypes; i++)
		{
			FigureType type = FigureType.fromInteger(i);
			figureIcons.put(type, IconFactory.getIcon(type.toString()));
		}

		paintIcons = new HashMap<Paint, ImageIcon>();
		String[] colorStrings = {
			"Black",
			"Blue",
			"Cyan",
			"Green",
			"Magenta",
			"None",
			"Orange",
			"Others",
			"Red",
			"White",
			"Yellow"
		};

		for (int i = 0; i < colorStrings.length; i++)
		{
			Paint paint = PaintFactory.getPaint(colorStrings[i]);
			if (paint != null)
			{
				paintIcons.put(paint, IconFactory.getIcon(colorStrings[i]));
			}
		}

		lineTypeIcons = new HashMap<LineType, ImageIcon>();
		for (int i = 0; i < LineType.NbLineTypes; i++)
		{
			LineType type = LineType.fromInteger(i);
			lineTypeIcons.put(type,  IconFactory.getIcon(type.toString()));
		}

		// --------------------------------------------------------------------
		// Création de l'UI
		// --------------------------------------------------------------------
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {80, 60, 60};
		gridBagLayout.rowHeights = new int[] {30, 32, 32, 32, 20, 20, 20, 20, 20};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);

		lblFigureName = new JLabel("Figure Name");
		lblFigureName.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblFigureName = new GridBagConstraints();
		gbc_lblFigureName.insets = new Insets(5, 5, 5, 0);
		gbc_lblFigureName.gridwidth = 3;
		gbc_lblFigureName.gridx = 0;
		gbc_lblFigureName.gridy = 0;
		add(lblFigureName, gbc_lblFigureName);

		JLabel lblType = new JLabel("type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 1;
		add(lblType, gbc_lblType);

		lblTypeicon = new JLabel(IconFactory.getIcon("Polygon"));
		lblTypeicon.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblTypeicon = new GridBagConstraints();
		gbc_lblTypeicon.insets = new Insets(0, 0, 5, 0);
		gbc_lblTypeicon.gridwidth = 2;
		gbc_lblTypeicon.gridx = 1;
		gbc_lblTypeicon.gridy = 1;
		add(lblTypeicon, gbc_lblTypeicon);

		JLabel lblFill = new JLabel("fill");
		GridBagConstraints gbc_lblFill = new GridBagConstraints();
		gbc_lblFill.anchor = GridBagConstraints.EAST;
		gbc_lblFill.insets = new Insets(0, 0, 5, 5);
		gbc_lblFill.gridx = 0;
		gbc_lblFill.gridy = 2;
		add(lblFill, gbc_lblFill);

		lblFillcolor = new JLabel(IconFactory.getIcon("White"));
		GridBagConstraints gbc_lblFillcolor = new GridBagConstraints();
		gbc_lblFillcolor.gridwidth = 2;
		gbc_lblFillcolor.insets = new Insets(0, 0, 5, 0);
		gbc_lblFillcolor.gridx = 1;
		gbc_lblFillcolor.gridy = 2;
		add(lblFillcolor, gbc_lblFillcolor);

		JLabel lblStroke = new JLabel("stroke");
		GridBagConstraints gbc_lblStroke = new GridBagConstraints();
		gbc_lblStroke.anchor = GridBagConstraints.EAST;
		gbc_lblStroke.insets = new Insets(0, 0, 5, 5);
		gbc_lblStroke.gridx = 0;
		gbc_lblStroke.gridy = 3;
		add(lblStroke, gbc_lblStroke);

		lblEdgecolor = new JLabel(IconFactory.getIcon("Black"));
		GridBagConstraints gbc_lblStrokecolor = new GridBagConstraints();
		gbc_lblStrokecolor.insets = new Insets(0, 0, 5, 5);
		gbc_lblStrokecolor.gridx = 1;
		gbc_lblStrokecolor.gridy = 3;
		add(lblEdgecolor, gbc_lblStrokecolor);

		lblStroketype = new JLabel(IconFactory.getIcon("Solid"));
		GridBagConstraints gbc_lblStroketype = new GridBagConstraints();
		gbc_lblStroketype.insets = new Insets(0, 0, 5, 0);
		gbc_lblStroketype.gridx = 2;
		gbc_lblStroketype.gridy = 3;
		add(lblStroketype, gbc_lblStroketype);

		JLabel lblX = new JLabel("x");
		lblX.setFont(lblX.getFont().deriveFont(lblX.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.gridx = 1;
		gbc_lblX.gridy = 4;
		add(lblX, gbc_lblX);

		JLabel lblY = new JLabel("y");
		lblY.setFont(lblY.getFont().deriveFont(lblY.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.insets = new Insets(0, 0, 5, 0);
		gbc_lblY.gridx = 2;
		gbc_lblY.gridy = 4;
		add(lblY, gbc_lblY);

		JLabel lblTopLeft = new JLabel("top left");
		lblTopLeft.setFont(lblTopLeft.getFont().deriveFont(lblTopLeft.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblTopLeft = new GridBagConstraints();
		gbc_lblTopLeft.anchor = GridBagConstraints.EAST;
		gbc_lblTopLeft.insets = new Insets(0, 0, 5, 5);
		gbc_lblTopLeft.gridx = 0;
		gbc_lblTopLeft.gridy = 5;
		add(lblTopLeft, gbc_lblTopLeft);

		lblTlx = new JLabel("tlx");
		lblTlx.setFont(lblTlx.getFont().deriveFont(lblTlx.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblTlx = new GridBagConstraints();
		gbc_lblTlx.insets = new Insets(0, 0, 5, 5);
		gbc_lblTlx.gridx = 1;
		gbc_lblTlx.gridy = 5;
		add(lblTlx, gbc_lblTlx);

		lblTly = new JLabel("tly");
		lblTly.setFont(lblTly.getFont().deriveFont(lblTly.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblTly = new GridBagConstraints();
		gbc_lblTly.insets = new Insets(0, 0, 5, 0);
		gbc_lblTly.gridx = 2;
		gbc_lblTly.gridy = 5;
		add(lblTly, gbc_lblTly);

		JLabel lblBottomRight = new JLabel("bottom right");
		lblBottomRight.setFont(lblBottomRight.getFont().deriveFont(lblBottomRight.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblBottomRight = new GridBagConstraints();
		gbc_lblBottomRight.anchor = GridBagConstraints.EAST;
		gbc_lblBottomRight.insets = new Insets(0, 0, 5, 5);
		gbc_lblBottomRight.gridx = 0;
		gbc_lblBottomRight.gridy = 6;
		add(lblBottomRight, gbc_lblBottomRight);

		lblBrx = new JLabel("brx");
		lblBrx.setFont(lblBrx.getFont().deriveFont(lblBrx.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblBrx = new GridBagConstraints();
		gbc_lblBrx.insets = new Insets(0, 0, 5, 5);
		gbc_lblBrx.gridx = 1;
		gbc_lblBrx.gridy = 6;
		add(lblBrx, gbc_lblBrx);

		lblBry = new JLabel("bry");
		lblBry.setFont(lblBry.getFont().deriveFont(lblBry.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblBry = new GridBagConstraints();
		gbc_lblBry.insets = new Insets(0, 0, 5, 0);
		gbc_lblBry.gridx = 2;
		gbc_lblBry.gridy = 6;
		add(lblBry, gbc_lblBry);

		JLabel lblDimensions = new JLabel("dimensions");
		lblDimensions.setFont(lblDimensions.getFont().deriveFont(lblDimensions.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblDimensions = new GridBagConstraints();
		gbc_lblDimensions.anchor = GridBagConstraints.EAST;
		gbc_lblDimensions.insets = new Insets(0, 0, 5, 5);
		gbc_lblDimensions.gridx = 0;
		gbc_lblDimensions.gridy = 7;
		add(lblDimensions, gbc_lblDimensions);

		lblDx = new JLabel("dx");
		lblDx.setFont(lblDx.getFont().deriveFont(lblDx.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblDx = new GridBagConstraints();
		gbc_lblDx.insets = new Insets(0, 0, 5, 5);
		gbc_lblDx.gridx = 1;
		gbc_lblDx.gridy = 7;
		add(lblDx, gbc_lblDx);

		lblDy = new JLabel("dy");
		lblDy.setFont(lblDy.getFont().deriveFont(lblDy.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblDy = new GridBagConstraints();
		gbc_lblDy.insets = new Insets(0, 0, 5, 0);
		gbc_lblDy.gridx = 2;
		gbc_lblDy.gridy = 7;
		add(lblDy, gbc_lblDy);

		JLabel lblCenter = new JLabel("center");
		lblCenter.setFont(lblCenter.getFont().deriveFont(lblCenter.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblCenter = new GridBagConstraints();
		gbc_lblCenter.anchor = GridBagConstraints.EAST;
		gbc_lblCenter.insets = new Insets(0, 0, 0, 5);
		gbc_lblCenter.gridx = 0;
		gbc_lblCenter.gridy = 8;
		add(lblCenter, gbc_lblCenter);

		lblCx = new JLabel("cx");
		lblCx.setFont(lblCx.getFont().deriveFont(lblCx.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblCx = new GridBagConstraints();
		gbc_lblCx.insets = new Insets(0, 0, 0, 5);
		gbc_lblCx.gridx = 1;
		gbc_lblCx.gridy = 8;
		add(lblCx, gbc_lblCx);

		lblCy = new JLabel("cy");
		lblCy.setFont(lblCy.getFont().deriveFont(lblCy.getFont().getSize() - 3f));
		GridBagConstraints gbc_lblCy = new GridBagConstraints();
		gbc_lblCy.gridx = 2;
		gbc_lblCy.gridy = 8;
		add(lblCy, gbc_lblCy);

	}

	/**
	 * Mise à jour de tous les labels avec les informations de figure
	 * @param figure la figure dont il faut extraire les informations
	 */
	public void updateLabels(Figure figure)
	{
		// titre de la figure
		lblFigureName.setText(figure.toString());

		// Icône du type de figure
		lblTypeicon.setIcon(figureIcons.get(figure.getType()));

		// Icône de la couleur de remplissage
		ImageIcon fillColorIcon = paintIcons.get(figure.getFillPaint());
		if (fillColorIcon == null)
		{
			fillColorIcon = IconFactory.getIcon("Others");
		}
		lblFillcolor.setIcon(fillColorIcon);

		// Icône de la couleur de trait
		ImageIcon edgeColorIcon = paintIcons.get(figure.getEdgePaint());
		if (edgeColorIcon == null)
		{
			edgeColorIcon = IconFactory.getIcon("Others");
		}
		lblEdgecolor.setIcon(edgeColorIcon);

		// Icône du type de trait
		BasicStroke stroke = figure.getStroke();
		ImageIcon lineTypeIcon = null;
		if (stroke == null)
		{
			lineTypeIcon = lineTypeIcons.get(LineType.NONE);
		}
		else
		{
			float[] dashArray = stroke.getDashArray();
			if (dashArray == null)
			{
				lineTypeIcon = lineTypeIcons.get(LineType.SOLID);
			}
			else
			{
				lineTypeIcon = lineTypeIcons.get(LineType.DASHED);
			}
		}
		lblStroketype.setIcon(lineTypeIcon);

		// Données numériques
		Rectangle2D bounds = figure.getBounds2D();
		Point2D center = figure.getCenter();

		double minX = bounds.getMinX();
		double maxX = bounds.getMaxX();
		double minY = bounds.getMinY();
		double maxY = bounds.getMaxY();
		double width = maxX - minX;
		double height = maxY - minY;

		lblTlx.setText(coordFormat.format(minX));
		lblTly.setText(coordFormat.format(minY));
		lblBrx.setText(coordFormat.format(maxX));
		lblBry.setText(coordFormat.format(maxY));

		lblDx.setText(coordFormat.format(width));
		lblDy.setText(coordFormat.format(height));

		lblCx.setText(coordFormat.format(center.getX()));
		lblCy.setText(coordFormat.format(center.getY()));
	}

	/**
	 * Effacement de tous les labels
	 */
	public void resetLabels()
	{
		// titre de la figure
		lblFigureName.setText(emptyString);

		// Icône du type de figure
		lblTypeicon.setIcon(emptyIcon);

		// Icône de la couleur de remplissage
		lblFillcolor.setIcon(emptyIcon);

		// Icône de la couleur de trait
		lblEdgecolor.setIcon(emptyIcon);

		// Icône du type de trait
		lblStroketype.setIcon(emptyIcon);

		// Données numériques
		lblTlx.setText(emptyString);
		lblTly.setText(emptyString);
		lblBrx.setText(emptyString);
		lblBry.setText(emptyString);

		lblDx.setText(emptyString);
		lblDy.setText(emptyString);

		lblCx.setText(emptyString);
		lblCy.setText(emptyString);
	}
}
