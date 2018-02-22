package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Point;

import java.awt.geom.Point2D;

import figures.enums.FigureType;

public class Polygon extends Figure {
	private static int counter = 0;

	public Polygon(BasicStroke stroke, Paint edgeColor, Paint fillColor,
		Point point1, Point point2)
	{
		super(stroke, edgeColor, fillColor);
		instanceNumber = ++counter;

		java.awt.Polygon polygon = new java.awt.Polygon();
		polygon.addPoint(point1.x, point1.y); 
		polygon.addPoint(point2.x, point2.y);
		shape = polygon;
	}

	public Polygon(Polygon polygon)
	{
		super(polygon);
		java.awt.Polygon oldPoly = (java.awt.Polygon) polygon.shape;
		int npoints = oldPoly.npoints;
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];

		for (int i = 0; i < npoints; i++)
		{
			xpoints[i] = oldPoly.xpoints[i];
			ypoints[i] = oldPoly.ypoints[i];
		}
		shape = new java.awt.Polygon(xpoints, ypoints, npoints);
	}

	@Override
	public Figure clone() {
		// TODO 自动生成的方法存根
		return new Polygon(this);
	}

	public void addPoint(int x, int y)
	{
		java.awt.Polygon polygon = (java.awt.Polygon) shape;
		polygon.addPoint(x, y);
	}

	public void removeLastPoint()
	{
		java.awt.Polygon polygon = (java.awt.Polygon) shape;

		if (polygon.npoints > 1)
		{
			int[] xs = new int[polygon.npoints-1];
			int[] ys = new int[polygon.npoints-1];
			for (int i = 0; i < xs.length; i++)
			{
				xs[i] = polygon.xpoints[i];
				ys[i] = polygon.ypoints[i];
			}

			polygon.reset();

			for (int i = 0; i < xs.length; i++)
			{
				polygon.addPoint(xs[i], ys[i]);
			}
		}
	}

	@Override
	public void setLastPoint(Point2D p) {
		// TODO 自动生成的方法存根
		if (shape != null)
		{
			java.awt.Polygon polygon = (java.awt.Polygon) shape;
			int lastIndex = polygon.npoints - 1;
			if (lastIndex >= 0)
			{
				polygon.xpoints[lastIndex] = Double.valueOf(p.getX()).intValue();
				polygon.ypoints[lastIndex] = Double.valueOf(p.getY()).intValue();
			}
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
	}

	@Override
	public void normalize() {
		// TODO 自动生成的方法存根
		System.out.println("Polygon Normalize");
		Point2D center = computeCenter();
		double cx = center.getX();
		double cy = center.getY();
		translation.setToTranslation(cx, cy);
		java.awt.Polygon polygon = (java.awt.Polygon) shape;
		if (polygon.npoints > 0)
		{
			int[] newX = new int[polygon.npoints];
			int[] newY = new int[polygon.npoints];

			for (int i = 0; i < polygon.npoints; i++)
			{
				newX[i] = polygon.xpoints[i] - Double.valueOf(cx).intValue();
				newY[i] = polygon.ypoints[i] - Double.valueOf(cy).intValue();
			}

			polygon.reset();

			for (int i = 0; i < newX.length; i++)
			{
				polygon.addPoint(newX[i], newY[i]);
			}
		}
	}

	protected Point2D computeCenter()
	{
		java.awt.Polygon polygon = (java.awt.Polygon) shape;

		double[] center = {0.0, 0.0};

		if (polygon.npoints > 0)
		{
			for (int i = 0; i < polygon.npoints; i++)
			{
				center[0] += polygon.xpoints[i];
				center[1] += polygon.ypoints[i];
			}

			center[0] /= polygon.npoints;
			center[1] /= polygon.npoints;
		}

		return new Point2D.Double(center[0], center[1]);
	}

	@Override
	public Point2D getCenter() {
		// TODO 自动生成的方法存根
		Point2D center = computeCenter();
		Point2D tCenter = new Point2D.Double();

		getTransform().transform(center,tCenter);

		return tCenter;
	}

	@Override
	public FigureType getType() {
		// TODO 自动生成的方法存根
		return FigureType.POLYGON;
	}

	public void printPoints()
	{
		System.out.print(this + " ");
		java.awt.Polygon polygon = (java.awt.Polygon) shape;
		if (polygon.npoints > 0)
		{
			for (int i = 0; i < polygon.npoints; i++)
			{
				System.out.print("(" + polygon.xpoints[i] + ", " + polygon.ypoints[i] + ")");
			}
		}
		System.out.print("[" + computeCenter() + "]");
 		System.out.println();
	}

}
