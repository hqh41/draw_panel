package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import figures.enums.FigureType;

/**
 * Une classe représentant les polygones réguliers (triangle équilatéral, carré,
 * pentagone, etc...)
 * @author davidroussel
 */
public class NGon extends Figure
{
	/**
	 * Le compteur d'instance des NGons
	 */
	private static int counter = 0;

	/**
	 * Nombre minimum de côtés
	 */
	public static int minNbSides = 3;

	/**
	 * Nombre maximum de côtés
	 */
	public static int maxNbSides = 33;

	/**
	 * Centre du polygone
	 */
	private Point2D center;

	/**
	 * Nombre de côtés du polygone
	 */
	private int nbSides;

	/**
	 * Le rayon de ce polygône régulier
	 */
	private double radius;

	/**
	 * Constructeur valué d'un polygone régulier à partir d'un point seul
	 * (son centre)
	 * @param stroke le style de la ligne
	 * @param edgeColor la couleur de la ligne
	 * @param fillColor la couleur de remplissage
	 * @param p le centre du polygone régulier
	 */
	public NGon(BasicStroke stroke, Paint edge, Paint fill, Point2D p)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;

		Polygon poly = new Polygon();
		center = new Point2D.Double(p.getX(), p.getY());
		nbSides = 5;
		radius = 0.0;
		for(int i = 0; i < nbSides; i++)
		{
			// la taille est nulle pour l'instant
			poly.addPoint(0, 0);
		}

		shape = poly;
	}

	/**
	 * Constructeur assurant une copie distincte du NGon
	 * @param ngon le NGone à copier
	 */
	public NGon(NGon ngon)
	{
		super(ngon);
		center = new Point2D.Double(ngon.center.getX(), ngon.center.getY());
		nbSides = ngon.nbSides;
		radius = ngon.radius;
		Polygon oldPoly = (Polygon) ngon.shape;
		int npoints = oldPoly.npoints;
		int [] xpoints = new int[npoints];
		int [] ypoints = new int[npoints];

		for (int i = 0; i < npoints; i++)
		{
			xpoints[i] = oldPoly.xpoints[i];
			ypoints[i] = oldPoly.ypoints[i];
		}

		shape = new Polygon(xpoints, ypoints, npoints);
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		return new NGon(this);
	}

	/**
	 * Accesseur du nombre de côtés du polygone
	 * @return le nombre de côtés du polygone
	 */
	public int getNbSides()
	{
		return nbSides;
	}

	/**
	 * Accesseur du rayon du polygone
	 * @return le rayon du polygone
	 */
	public double getRadius()
	{
		return radius;
	}

	/**
	 * Mise en place d'un nouveau nombre de côtés
	 * @param nbSides
	 */
	public void setNbSides(int nbSides)
	{
		System.out.print("Ngon set nb sides with " + nbSides);
		if (nbSides > maxNbSides)
		{
			nbSides = maxNbSides;
			System.out.println(" adjusted to " + nbSides);
		}
		else
		{
			System.out.println();
		}

		if (nbSides < minNbSides)
		{
			nbSides = minNbSides;
			System.out.println(" adjusted to " + nbSides);
		}
		else
		{
			System.out.println();
		}

		if (nbSides != this.nbSides)
		{
			this.nbSides = nbSides;
			Polygon poly = (Polygon) shape;
			poly.reset();

			for (int i = 0; i < this.nbSides; i++)
			{
				poly.addPoint(0, 0);
			}

			recomputePoints();
		}
	}

	/**
	 * Mise en place d'un nouveau nombre de côtés
	 * @param nbSides
	 */
	public void incrementNbSides(int deltaSides)
	{
		int oldNbSides = nbSides;
		System.out.print("Ngon set nb sides with " + nbSides);
		if ((nbSides + deltaSides) > maxNbSides)
		{
			nbSides = maxNbSides;
			System.out.println(" adjusted to " + nbSides);
		}
		else
		{
			System.out.println();
		}

		if ((nbSides + deltaSides) < minNbSides)
		{
			nbSides = minNbSides;
			System.out.println(" adjusted to " + nbSides);
		}
		else
		{
			System.out.println();
		}

		if (nbSides != oldNbSides)
		{
			Polygon poly = (Polygon) shape;
			poly.reset();

			for (int i = 0; i < nbSides; i++)
			{
				poly.addPoint(0, 0);
			}

			recomputePoints();
		}
	}

	/**
	 * Mise en place d'un nouveau rayon pour le polygone régulier
	 * @param radius le nouveau rayon
	 */
	public void setRadius(double radius)
	{
		if (radius >= 0.0)
		{
			this.radius = radius;

			recomputePoints();
		}
		else
		{
			System.err.println("NGon::setRadius(" + radius + ") : invalid radius");
		}
	}

	/**
	 * Changement de taille du polygone régulier. en fonction de la distance
	 * entre le centre du polygone et du point p
	 * @param p la position déterminant la taille du polygone
	 * @see figures.Figure#setLastPoint(java.awt.geom.Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p)
	{
		// computing radius
		double dist = center.distance(p);

		setRadius(dist);
	}

	/* (non-Javadoc)
	 * @see figures.Figure#getCenter()
	 */
	@Override
	public Point2D getCenter()
	{
		return center;
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
		translation.translate(0, 0);
		center.setLocation(cx, cy);
	}

	/* (non-Javadoc)
	 * @see figures.Figure#getType()
	 */
	@Override
	public FigureType getType()
	{
		return FigureType.NGON;
	}

	/**
	 * Calcule un ensemble de "nbSides" points autour du centre du polygone
	 * régulier à une distance 1.0 du centre
	 * @param nbSides nombre de côtés du polygone
	 * @pre le nombre de côtés doit être déjà dans l'intervalle
	 * [{@link #minNbSides} ... {@link #maxNbSides}] car aucune vérification
	 * n'est faire dans cette méthode
	 * @return un tableau de "nbSides" points répartis régulièrement autour du
	 * centre en commençant par le point au dessus du centre
	 */
	protected Point2D[] unitPoints(int nbSides)
	{
		Point2D[] points = null;
		points = new Point2D[nbSides];

		double cx = center.getX();
		double cy = center.getY();
		AffineTransform rotation = AffineTransform.getRotateInstance(
			(Math.PI * 2.0) / nbSides, cx, cy);

		points[0] = new Point2D.Double(cx, cy - 1.0);
		for (int i = 1; i < points.length; i++)
		{
			points[i] = new Point2D.Double();
			rotation.transform(points[i-1], points[i]);
		}

		return points;
	}

	/**
	 * Recalcule l'ensemble des points quand un paramètre du polygone a changé
	 * comme le rayon ou le nombre de points.
	 * @pre le tableau de points du polygone a été réalloué
	 * @pre le nombre de côtés doit être déjà dans l'intervalle
	 * [{@link #minNbSides} ... {@link #maxNbSides}] car aucune vérification
	 * n'est faire dans cette méthode.
	 */
	protected void recomputePoints()
	{
		// getting nbSides points
		Point2D[] points = unitPoints(nbSides);

		double cx = center.getX();
		double cy = center.getY();

		Polygon poly = (java.awt.Polygon) shape;

		for (int i = 0; i < points.length; i++)
		{
			// Application de l'échelle aux points
			double ix = ((points[i].getX() - cx) * radius) + cx;
			double iy = ((points[i].getY() - cy) * radius) + cy;

			// Mise à jour des points du polygone
			poly.xpoints[i] = Double.valueOf(ix).intValue();
			poly.ypoints[i] = Double.valueOf(iy).intValue();
		}
	}
}
