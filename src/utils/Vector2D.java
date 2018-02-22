package utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Vector 2D class relating two points
 * @author davidroussel
 */
public class Vector2D
{
	/**
	 * Vector's origin
	 */
	protected Point2D start;

	/**
	 * Vector's end
	 */
	protected Point2D end;

	/**
	 * Construtor from 1 point (the origin is supposed to be (0, 0)
	 * @param p the end point
	 */
	public Vector2D(Point2D p)
	{
		this(null, p);
	}

	/**
	 * Constructor from two points
	 * @param p1 the start point
	 * @param p2 the end point
	 */
	public Vector2D(Point2D p1, Point2D p2)
	{
		start = p1;
		end = p2;
	}

	/**
	 * Constructeur de copie
	 * @param vector le vecteur à copier
	 */
	public Vector2D(Vector2D vector)
	{
		start = vector.start;
		end = vector.end;
	}

	/**
	 * Start point getter
	 * @return the start
	 */
	public Point2D getStart()
	{
		if (start == null)
		{
			return new Point2D.Double(0.0, 0.0);
		}
		else
		{
			return start;
		}
	}

	/**
	 * Start point setter
	 * @param start the start to set
	 */
	public void setStart(Point2D start)
	{
		this.start = start;
	}

	/**
	 * End Point getter
	 * @return the end
	 */
	public Point2D getEnd()
	{
		return end;
	}

	/**
	 * End point setter
	 * @param end the end to set
	 */
	public void setEnd(Point2D end)
	{
		this.end = end;
	}

	/**
	 * Delta X of the vector
	 * @return The delta X of the vector
	 */
	protected double getX()
	{
		return end.getX() - (start == null ? 0.0 : start.getX());
	}

	/**
	 * Delta Y of the vector
	 * @return The delta Y of the vector
	 */
	protected double getY()
	{
		return end.getY() - (start == null ? 0.0 : start.getY());
	}

	/**
	 * Dot product with vector v
	 * @param v the vector to compute dot product with
	 * @return the value of the dot product
	 */
	public double dotProduc(Vector2D v)
	{
		return ((start == null ? 0.0 : start.getX()) * end.getX()) +
		       ((start == null ? 0.0 : start.getY()) * end.getY());
	}

	/**
	 * Cross product's norm
	 * @param v the vector to compute cross product's norm
	 * @return the value of the cross product's norm
	 */
	public double crossProductNorm(Vector2D v)
	{
		return (getX()*v.getY()) - (v.getX()*getY());
	}

	/**
	 * Vector's norm
	 * @return the vector's norm
	 */
	public double norm()
	{
		return Math.sqrt(dotProduc(this));
	}

	/**
	 * Compute normalized vector's
	 * @return normalized vector
	 */
	public Vector2D normalize()
	{
		double norm = norm();

		return new Vector2D(new Point2D.Double(getX()/ norm, getY() / norm));
	}

	/**
	 * Angle between vectors
	 * @param v the vector to compute angle with
	 * @return the angle between current vector and vector v
	 */
	public double angle(Vector2D v)
	{
		Vector2D vn1 = normalize();
		Vector2D vn2 = v.normalize();

		return Math.atan2(vn2.getY(),vn2.getX()) -
		       Math.atan2(vn1.getY(),vn1.getX());
	}

	/**
	 * The endPoint as in {@link #end} - {@link #start}
	 * @return the end point
	 */
	public Point2D toPoint2D()
	{
		return new Point2D.Double(end.getX() - start.getX(),
		                          end.getY() - start.getY());
	}

	/**
	 * Apply Affine transform to vector centered on {@link #start}
	 * @param transform the affine transform to apply
	 */
	public void transformEnd(AffineTransform transform)
	{
		Point2D pVector = toPoint2D();
		if (transform != null)
		{
			Point2D tPVector = new Point2D.Double();
			transform.transform(pVector, tPVector);

			setEnd(new Point2D.Double(start.getX() + tPVector.getX(),
			                          start.getY() + tPVector.getY()));
		}
	}
}
