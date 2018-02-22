package figures;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import figures.enums.FigureType;
import figures.enums.LineType;
import history.Prototype;
import utils.CColor;
import utils.PaintFactory;
import utils.StrokeFactory;

/**
 * Classe commune à toutes les sortes de figures
 *
 * @author davidroussel
 */
public abstract class Figure implements Prototype<Figure>
{
	/**
	 * La forme à dessiner
	 */
	protected Shape shape;

	/**
	 * Couleur du bord de la figure
	 */
	protected Paint edge;

	/**
	 * Couleur du bord de la sélection
	 */
	protected static final Paint selectedEdge =
	    PaintFactory.getPaint(Color.LIGHT_GRAY);

	/**
	 * Couleur de remplissage de la figure
	 */
	protected Paint fill;

	/**
	 * Caractéristiques de la bordure des figure : épaisseur, forme des
	 * extremités et [evt] forme des jointures
	 */
	protected BasicStroke stroke;

	/**
	 * Caractéristique de la bordure des figures sélectionnées
	 */
	protected static final BasicStroke selectedStroke =
	    StrokeFactory.getStroke(LineType.DASHED, 2.0f);

	/**
	 * La translation à appliquer à cet objet
	 * @note sert à déplacer cet objet, pour ce faire il faut
	 * avant de dessiner cet objet appliquer cette transformation ainsi que
	 * sa rotation et son facteur d'échelle puis les retirer après le dessin.
	 */
	protected AffineTransform translation;

	/**
	 * La rotation à appliquer à cet objet
	 * @note sert à tourner cet objet, pour ce faire il faut
	 * avant de dessiner cet objet appliquer cette transformation ainsi que
	 * sa translation et son facteur d'échelle puis les retirer après le dessin.
	 */
	protected AffineTransform rotation;

	/**
	 * Le facteur d'échelle à applique à cet objet
	 * @note sert à changer l'échelle cet objet, pour ce faire il faut
	 * avant de dessiner cet objet appliquer cette transformation ainsi que
	 * sa translation et sa rotation puis les retirer après le dessin.
	 */
	protected AffineTransform scale;

	/**
	 * Le numéro d'instance de cette figure.
	 * 1 si c'est la première figure de ce type, etc.
	 */
	protected int instanceNumber;

	/**
	 * Indique si la figure fait partie des figrues sélectionnées
	 */
	protected boolean selected;

	/**
	 * Constructeur d'une figure abstraite à partir d'un style de ligne d'une
	 * couleur de bordure et d'une couleur de remplissage. Les styles de lignes
	 * et les couleurs étant souvent les même entre les différentes figures ils
	 * devront être fournis par un flyweight. Le stroke, le edge et le fill
	 * peuvent chacun être null.
	 *
	 * @param stroke caractéristiques de la ligne de bordure
	 * @param edge couleur de la ligne de bordure
	 * @param fill couleur (ou gradient de couleurs) de remplissage
	 */
	protected Figure(BasicStroke stroke, Paint edge, Paint fill)
	{
		this.stroke = stroke;
		this.edge = edge;
		this.fill = fill;
		shape = null;
		translation = new AffineTransform();
		translation.setToIdentity();
		rotation = new AffineTransform();
		rotation.setToIdentity();
		scale = new AffineTransform();
		scale.setToIdentity();
		selected = false;
	}

	/**
	 * Constructeur de copie assurant une copie distincte de la figure
	 * @param f la figure à copier
	 */
	protected Figure(Figure f)
	{
		shape = null; // Shapes must be copied in subclasses
		edge = PaintFactory.getPaint(f.edge);
		fill = PaintFactory.getPaint(f.fill);
		stroke = StrokeFactory.getStroke(f.stroke);
		translation = new AffineTransform(f.translation);
		rotation = new AffineTransform(f.rotation);
		scale = new AffineTransform(f.scale);
		instanceNumber = f.instanceNumber;
		selected = f.selected;
	}

	/**
	 * Création d'une copie distincte de la figure
	 */
	@Override
	public abstract Figure clone();

	/**
	 * Comparaison de deux figures
	 * @param Object o l'objet à comparer
	 * @return true si obj est une figure de même type et que son contenu est
	 * identique
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}

		if (o == this)
		{
			return true;
		}

		if (getClass() == o.getClass())
		{
			Figure f = (Figure) o;

			if (getType().equals(f.getType()))
			{
				if (instanceNumber == f.instanceNumber)
				{
					// boolean edgeTest = (edge == null ? f.edge == null : edge.equals(f.edge));
					/*
					 * Les egde sont fournies par une PaintFactory donc elles sont uniques
					 */
					boolean edgeTest = (edge == f.edge);
					if (edgeTest)
					{
						// boolean fillTest = (fill == null ? f.fill == null : fill.equals(f.fill));
						/*
						 * Les fill sont fournies par une PaintFactory donc elles sont uniques
						 */
						boolean fillTest = (fill == f.fill);
						if (fillTest)
						{
							// boolean strokeTest = (stroke == null ?
							//                       f.stroke == null :
							//                      stroke.equals(f.stroke));
							/*
							 * Les stroke sont fournies par une StrokeFactory donc elles sont uniques
							 */
							boolean strokeTest = (stroke == f.stroke);
							if (strokeTest)
							{
								if (translation.equals(f.translation))
								{
									if (rotation.equals(f.rotation))
									{
										if (scale.equals(f.scale))
										{
											if (getCenter()
											    .equals(f.getCenter()))
											{
												if (getBounds2D()
												    .equals(f.getBounds2D()))
												{
													return true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Déplacement du dernier point de la figure (utilisé lors du dessin d'une
	 * figure tant que l'on déplace le dernier point)
	 *
	 * @param p la nouvelle position du dernier point
	 */
	public abstract void setLastPoint(Point2D p);

	/**
	 * Dessin de la figure dans un contexte graphique fournit par le système.
	 * Met en place le stroke et les couleur, puis dessine la forme géométrique
	 * correspondant à la figure (figure remplie d'abord si le fill est non
	 * null, puis bordure si le edge est non null)
	 *
	 * @param g2D le contexte graphique
	 */
	public final void draw(Graphics2D g2D)
	{
		// Get the current transform
		 AffineTransform savedT = g2D.getTransform();

		 // Perform transformations
		 g2D.transform(getTransform());

		 // Render
		if (fill != null)
		{
			g2D.setPaint(fill);
			g2D.fill(shape);
		}
		if ((edge != null) && (stroke != null))
		{
			g2D.setStroke(stroke);
			g2D.setPaint(edge);
			g2D.draw(shape);
		}

		// Restore original transform
		g2D.setTransform(savedT);
	}

	/**
	 * Dessin de la sélection de la figure (son soulignement) dans un contexte
	 * graphique fournit par le système.
	 * @note le dessin de la sélection doit être séparé du dessin des figures
	 * de manière à ce que les sélection apparaissent par dessus les figures
	 * @param g2D le contexte graphique
	 */
	public final void drawSelection(Graphics2D g2D)
	{
		if (selected)
		{
			g2D.setPaint(selectedEdge);
			g2D.setStroke(selectedStroke);
			g2D.draw(getBounds2D()); // getBounds uses current transform
		}
	}

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	public abstract void normalize();

	/**
	 * Accesseur en lecture de la translation courante
	 * @return la translation courante
	 */
	public AffineTransform getTranslation()
	{
		return translation;
	}

	/**
	 * Accesseur en lecture de la rotation courante
	 * @return la rotation courante
	 */
	public AffineTransform getRotation()
	{
		return rotation;
	}

	/**
	 * Accesseur en lecture de l'échelle courante
	 * @return l'échelle courante
	 */
	public AffineTransform getScale()
	{
		return scale;
	}

	/**
	 * Produit la transformation complète de cet objet
	 * (facteur d'échelle)*(rotation)*(translation)
	 * @return la transformation combinant le facteur d'échelle, la rotation et
	 * la translation de cette figure.
	 */
	public AffineTransform getTransform()
	{
		AffineTransform transform = (AffineTransform) translation.clone();
		transform.concatenate(scale);
		transform.concatenate(rotation);

		return transform;
	}

	/**
	 * Mise en place d'une translation
	 * @param translation la translation à mettre en place
	 */
	public void setTranslation(AffineTransform translation)
	{
		this.translation = translation;
	}

	/**
	 * Déplace la figure de dx et dy
	 * @param dx la variation d'abcisse de la figure
	 * @param dy la variation d'ordonnée de la figure
	 */
	public void translate(double dx, double dy)
	{
		translation.translate(dx, dy);
	}

	/**
	 * Mise en place d'une rotation
	 * @param rotation la rotation à mettre en place
	 */
	public void setRotation(AffineTransform rotation)
	{
		this.rotation = rotation;
	}

	/**
	 * Fait tourner la figure d'un certain angle autour de son barycentre
	 * @param deltaAngle l'angle de rotation de la figure
	 */
	public void rotate(double deltaAngle)
	{
		rotation.rotate(deltaAngle);
	}

	/**
	 * Mise en place d'un facteur d'échelle
	 * @param scale le facteur d'échelle à mettre en place
	 */
	public void setScale(AffineTransform scale)
	{
		this.scale = scale;
	}

	/**
	 * Change l'échelle de la figure
	 * @param deltaScale le facteur d'échelle àç appliquer à la figure
	 */
	public void scale(double deltaScale)
	{
		scale.scale(deltaScale, deltaScale);
	}

	/**
	 * Obtention du rectangle englobant de la figure.
	 * Obtenu grâce au {@link Shape#getBounds2D()}
	 * @return le rectangle englobant de la figure
	 */
	public Rectangle2D getBounds2D()
	{
		/*
		 * Attention, il faut appliquer la transformation affine courante
		 * au Rectangle2D résultant de l'appel à shape.getBounds2D();
		 */
		Rectangle2D bounds = shape.getBounds2D();
		double minX = bounds.getMinX();
		double minY = bounds.getMinY();
		double maxX = bounds.getMaxX();
		double maxY = bounds.getMaxY();
		Point2D[] corners = new Point2D[] {
			new Point2D.Double(minX, minY),
			new Point2D.Double(maxX, minY),
			new Point2D.Double(maxX, maxY),
			new Point2D.Double(minX, maxY)
		};
		Point2D[] tCorners = new Point2D[4];
		for (int i = 0; i < 4; i++)
		{
			tCorners[i] = new Point2D.Double();
		}

		getTransform().transform(corners, 0, tCorners, 0, corners.length);

		double x = tCorners[0].getX();
		double y = tCorners[0].getY();
		double w = 0.0;
		double h = 0.0;

		for (int i = 0; i < 4; i++)
		{
			double tx = tCorners[i].getX();
			x = (x < tx ? x : tx);

			double ty = tCorners[i].getY();
			y = (y < ty ? y : ty);
		}

		for (int i = 0; i < 4; i++)
		{
			double tw = tCorners[i].getX() - x;
			w = (tw > w ? tw : w);

			double th = tCorners[i].getY() - y;
			h = (th > h ? th : h);
		}

		bounds.setFrame(x, y, w, h);

		return bounds;
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	public abstract Point2D getCenter();

	/**
	 * Teste si le point p est contenu dans cette figure.
	 * Utilise {@link Shape#contains(Point2D)}
	 * @param p le point dont on veut tester s'il est contenu dans la figure
	 * @return true si le point p est contenu dans la figure, false sinon
	 */
	public boolean contains(Point2D p)
	{
		/*
		 * TODO Ce point p doit subir la transformation inverse
		 * de celle subie par la figure pour déterminer si le point p fait
		 * partie de la figure
		 */
		try
		{
			Point2D transformedPoint = new Point2D.Double();
			getTransform().inverseTransform(p, transformedPoint);
			return shape.contains(transformedPoint);
		}
		catch (NoninvertibleTransformException e)
		{
			return false;
		}
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	public abstract FigureType getType();

	/**
	 * Accesseur en lecture du {@link Paint} du contour
	 * @return le {@link Paint} du contour
	 */
	public Paint getEdgePaint()
	{
		return edge;
	}

	/**
	 * Accesseur en lecture de la couleur comparable du contour
	 * @return la couleur comparable du contour
	 */
	public CColor getEdgeCColor()
	{
		if (edge != null)
		{
			if (edge instanceof Color)
			{
				return new CColor((Color)edge);
			}
		}

		return CColor.NoColor;
	}

	/**
	 * Mutateur du {@link Paint} du contour
	 * @param edge le nouveau {@link Paint} à mettre dans {@link #edge}
	 */
	public void setEdgePaint(Paint edge)
	{
		if (edge != null)
		{
			this.edge = edge;
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setEdgePaint : null paint");
		}
	}

	/**
	 * Accesseur en lecture du {@link Paint} du remplissage
	 * @return le {@link Paint} du remplissage
	 */
	public Paint getFillPaint()
	{
		return fill;
	}

	/**
	 * Accesseur en lecture de la couleur comparable de remplissage
	 * @return la couleur comparable du remplissage
	 */
	public CColor getFillCColor()
	{
		if (fill != null)
		{
			if (fill instanceof Color)
			{
				return new CColor((Color)fill);
			}
		}

		return CColor.NoColor;
	}

	/**
	 * Mutateur du {@link Paint} du contour
	 * @param fill le nouveau {@link Paint} à mettre dans {@link #fill}
	 */
	public void setFillPaint(Paint fill)
	{
		if (fill != null)
		{
			this.fill = fill;
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setFillPaint : null paint");
		}
	}

	/**
	 * Accesseur en lecture du type de ligne ({@link LineType}) en fonction
	 * du {@link #stroke}
	 * @return le type de ligne actuel d'après le {@link #stroke}.
	 * @see LineType#fromStroke(BasicStroke)
	 */
	public LineType getLineType()
	{
		return LineType.fromStroke(stroke);
	}

	/**
	 * Accesseur en lecture du {@link BasicStroke} du contour
	 * @return le {@link BasicStroke} du contour
	 */
	public BasicStroke getStroke()
	{
		return stroke;
	}

	/**
	 * Mutateur du {@link BasicStroke} du contour
	 * @param stroke le nouveau {@link BasicStroke} à mettre dans {@link #stroke}
	 */
	public void setStroke(BasicStroke stroke)
	{
		if (stroke != null)
		{
			this.stroke = stroke;
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setStroke : null stroke");
		}
	}

	/**
	 * Accesseur de la propriété {@link #selected}
	 * @return la valeur de {@link #selected}
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * Mutateur de la propriété {@link #selected}
	 * @param selected la nouvelle valeur de selected
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " " + String.valueOf(instanceNumber);
	}
}
