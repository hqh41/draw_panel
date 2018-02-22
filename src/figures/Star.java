package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import figures.enums.FigureType;

public class Star extends Figure {

	private static int counter = 0;
	
	private double radius = 10.0;
	private Point2D center;
	
	public Star(BasicStroke stroke, Paint edge, Paint fill, Point2D p)
	{		
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		radius = 0.0;
		center = new Point2D.Double(p.getX(), p.getY());
		java.awt.Polygon polygon = new Polygon();
		shape = polygon;
		
		System.out.println("Star created");
	}
	
	public Star(Star star)
	{
		super(star);
		radius=star.radius;
		center = new Point2D.Double(star.center.getX(), star.center.getY());
		Polygon oldPoly = (Polygon) star.shape;
		int npoints = oldPoly.npoints;
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];

		for (int i = 0; i < npoints; i++)
		{
			xpoints[i] = oldPoly.xpoints[i];
			ypoints[i] = oldPoly.ypoints[i];
		}
		shape = new Polygon(xpoints, ypoints, npoints);
	}
	
	@Override
	public Figure clone() {
		// TODO 自动生成的方法存根
		return new Star(this);
	}

	public double getRadius()
	{
		return radius;
	}
	
	protected void recomputePoints()
	{	int x0 = (int) getCenter().getX();
		int y0 = (int) getCenter().getY();
		double ch=72*Math.PI/180;
		double x1=x0,
		x2=(double)(x0-Math.sin(ch)*radius),
		x3=(double)(x0+Math.sin(ch)*radius),
		x4=(double)(x0-Math.sin(ch/2)*radius),
		x5=(double)(x0+Math.sin(ch/2)*radius);
		double y1=y0-radius,
		    y2=(double)(y0-Math.cos(ch)*radius),
		    y3=y2,
		    y4=(double)(y0+Math.cos(ch/2)*radius),
		    y5=y4; 
		
		Polygon poly = (Polygon) shape;
	    poly.reset();
	    poly.addPoint((int)x1,(int)y1);
		poly.addPoint((int)x4,(int)y4);
		poly.addPoint((int)x3,(int)y3);
		poly.addPoint((int)x2,(int)y2);
		poly.addPoint((int)x5,(int)y5);
	}
	
	public void setRadius(double radius)
	{
		if (radius >= 0.0)
		{
			this.radius = radius;
			recomputePoints();
		}
		else
		{
			System.err.println("NGon::setRadius(" + radius + ") fail: invalid radius");
		}
	}
	
	@Override
	public void setLastPoint(Point2D p) {
		// TODO 自动生成的方法存根
		double dist = center.distance(p);
		setRadius(dist);
	}

	@Override
	public void normalize() {
		// TODO 自动生成的方法存根
		Point2D center = getCenter();
		double cX = center.getX();
		double cY = center.getY();
		translation.translate(0, 0);
		center.setLocation(cX, cY);
	}


	@Override
	public Point2D getCenter() {
		// TODO 自动生成的方法存根
		return center;
	}

	@Override
	public FigureType getType() {
		// TODO 自动生成的方法存根
		return FigureType.STAR;
	}

}
