package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import figures.enums.FigureType;

public class Circle extends Figure {

	public final static double DEFAULT_RADIUS = 2.0f;
	private static int counter = 0;

	public Circle(BasicStroke stroke, Paint edge, Paint fill, Point2D center,
			double rayon)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double width = rayon * 2.0f;
		double height = width;
		double x = center.getX() - rayon;
		double y = center.getY() -rayon;
		shape = new Ellipse2D.Double(x, y, width, height);
	}

	public Circle(Circle circle)
	{
		super(circle);
		Ellipse2D oldEllipse = (Ellipse2D) circle.shape;
		shape = new Ellipse2D.Double(oldEllipse.getMinX(),
									 oldEllipse.getMinY(),
									 oldEllipse.getWidth(),
									 oldEllipse.getHeight());
	}

	@Override
	public Figure clone() {
		// TODO 自动生成的方法存根
		return new Circle(this);
	}

	@Override
	public void setLastPoint(Point2D p) {
		// TODO 自动生成的方法存根
		if (shape != null)
		{
			Ellipse2D.Double ellipse = (Ellipse2D.Double) shape;
			Double newWidth = p.getX() - ellipse.x;
			Double newHeight = p.getY() - ellipse.y;
			Double dia = Math.min(Math.abs(newWidth), Math.abs(newHeight));
			ellipse.width = dia;
			ellipse.height = dia;
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
		
	}

	@Override
	public void normalize() {
		// TODO 自动生成的方法存根
		Point2D center = getCenter();
		double centerX = center.getX();
		double centerY = center.getY();
		Ellipse2D.Double circle = (Ellipse2D.Double) shape;
		translation.translate(centerX, centerY);
		circle.setFrame(circle.x - centerX,
						circle.y - centerY,
						circle.width,
						circle.height);
	}

	@Override
	public Point2D getCenter() {
		// TODO 自动生成的方法存根
		Ellipse2D ellipse = (Ellipse2D.Double) shape;
		Point2D center = new Point2D.Double(ellipse.getCenterX(), ellipse.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}

	@Override
	public FigureType getType() {
		// TODO 自动生成的方法存根
		return FigureType.CIRCLE;
	}

}
