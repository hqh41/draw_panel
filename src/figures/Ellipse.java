package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import figures.enums.FigureType;

public class Ellipse extends Figure {
	private static int counter = 0;

	public Ellipse(BasicStroke stroke, Paint edge, Paint fill, Point2D topLeft,
			Point2D bottomRight)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double x = topLeft.getX();
		double y = topLeft.getY();
		double w = bottomRight.getX() - x;
		double h = bottomRight.getY() - y;

		shape = new Ellipse2D.Double(x, y, w, h);
	}

	public Ellipse(Ellipse ellipse)
	{
		super(ellipse);
		Ellipse2D oldEllipse = (Ellipse2D) ellipse.shape;
		shape = new Ellipse2D.Double(oldEllipse.getMinX(),
									 oldEllipse.getMinY(),
									 oldEllipse.getWidth(),
									 oldEllipse.getHeight());
	}

	@Override
	public Figure clone() {
		// TODO 自动生成的方法存根
		return new Ellipse(this);
	}

	@Override
	public void setLastPoint(Point2D p) {
		// TODO 自动生成的方法存根
		if (shape != null)
		{
			Ellipse2D.Double ellipse = (Ellipse2D.Double) shape;
			double newWidth = p.getX() - ellipse.x;
			double newHeight = p.getY() - ellipse.y;
			ellipse.width = newWidth;
			ellipse.height = newHeight;
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
		double cx = center.getX();
		double cy = center.getY();
		Ellipse2D.Double ellipse = (Ellipse2D.Double) shape;
		translation.translate(cx, cy);
		ellipse.setFrame(ellipse.x - cx,
						 ellipse.y - cy,
						 ellipse.width,
						 ellipse.height);
	}

	@Override
	public Point2D getCenter() {
		// TODO 自动生成的方法存根
		Ellipse2D.Double ellipse = (Ellipse2D.Double) shape;
		Point2D center = new Point2D.Double(ellipse.getCenterX(), ellipse.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}

	@Override
	public FigureType getType() {
		// TODO 自动生成的方法存根
		return FigureType.ELLIPSE;
	}

}
