package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

/**
 * Classe de Rectangle pour les {@link Figure}
 *
 * @author davidroussel
 */
public class Rectangle extends Figure
{
	/**
	 * Le compteur d'instance des cercles.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;

	/**
	 * Création d'un rectangle avec les points en haut à gauche et en bas à
	 * droite
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 * @param topLeft le point en haut à gauche
	 * @param bottomRight le point en bas à droite
	 */
	public Rectangle(BasicStroke stroke, Paint edge, Paint fill, Point2D topLeft,
			Point2D bottomRight)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double x = topLeft.getX();
		double y = topLeft.getY();
		double w = (bottomRight.getX() - x);
		double h = (bottomRight.getY() - y);

		shape = new Rectangle2D.Double(x, y, w, h);

		// System.out.println("Rectangle created");
	}

	/**
	 * Constructeur de copie assurant une copie distincte du rectangle
	 * @param rect le rectangle à copier
	 */
	public Rectangle(Rectangle rect)
	{
		super(rect);
		if (rect.getClass() == Rectangle.class)
		{
			Rectangle2D oldRectangle = (Rectangle2D) rect.shape;
			shape = new Rectangle2D.Double(oldRectangle.getMinX(),
			                               oldRectangle.getMinY(),
			                               oldRectangle.getWidth(),
			                               oldRectangle.getHeight());
		}
		else
		{
			System.out.println("Calling Rectangle(Rectangle) from another class");
		}
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		return new Rectangle(this);
	}

	/**
	 * Comparaison de deux figures
	 * @param Object o l'objet à comparer
	 * @return true si obj est une figure de même type et que son contenu est
	 * identique
	 */
	@Override
	public boolean equals(Object o)
	{
		if (super.equals(o))
		{
			Rectangle r = (Rectangle) o;
			RectangularShape r1 = (RectangularShape) shape;
			RectangularShape r2 = (RectangularShape) r.shape;

			return ((r1.getX() == r2.getX()) &&
			        (r1.getY() == r2.getY()) &&
			        (r1.getWidth() == r2.getWidth()) &&
			        (r1.getHeight() == r2.getHeight()));
		}

		return false;
	}

	/**
	 * Création d'un rectangle sans points (utilisé dans les classes filles
	 * pour initialiser seulement les couleur et le style de trait sans
	 * initialiser {@link #shape}.
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 */
	protected Rectangle(BasicStroke stroke, Paint edge, Paint fill)
	{
		super(stroke, edge, fill);

		shape = null;
	}

	/**
	 * Déplacement du point en bas à droite du rectangle à la position
	 * du point p
	 *
	 * @param p la nouvelle position du dernier point
	 * @see figures.Figure#setLastPoint(Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p)
	{
		if (shape != null)
		{
			Rectangle2D.Double rect = (Rectangle2D.Double) shape;
			double newWidth = p.getX() - rect.x;
			double newHeight = p.getY() - rect.y;
			rect.width = newWidth;
			rect.height = newHeight;
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter()
	{
		RectangularShape rect = (RectangularShape) shape;

		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize()
	{
		Point2D center = getCenter();
		double cx = center.getX();
		double cy = center.getY();
		RectangularShape rectangle = (RectangularShape) shape;
		translation.translate(cx, cy);
		rectangle.setFrame(rectangle.getX() - cx,
		                   rectangle.getY() - cy,
		                   rectangle.getWidth(),
		                   rectangle.getHeight());
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		return FigureType.RECTANGLE;
 	}
}
