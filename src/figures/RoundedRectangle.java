package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import figures.enums.FigureType;

public class RoundedRectangle extends Figure {
	private static int counter = 0;

	public RoundedRectangle(BasicStroke stroke, Paint edge, Paint fill, Point2D topleft,
			Point2D bottomRight, int arc) {
				super(stroke, edge, fill);
				instanceNumber = ++counter;
				double x = topleft.getX();
				double y = topleft.getY();
				double w = (bottomRight.getX() - x);
				double h = (bottomRight.getY() - y);

				double minofLen = (w < h ? w : h) / 2.0f;
				double realArc =  arc < minofLen ? arc : minofLen;
				
				shape = new RoundRectangle2D.Double(x, y, w, h, 50, 50);
				System.out.println("Rounded Rectangle created");
	}

	public RoundedRectangle(RoundedRectangle rRect) {
		super(rRect);
		RoundRectangle2D oldRect = (RoundRectangle2D) rRect.shape;
		shape = new RoundRectangle2D.Double(oldRect.getMinX(),
											oldRect.getMinY(),
											oldRect.getWidth(),
											oldRect.getHeight(),
											oldRect.getArcWidth(),
											oldRect.getArcHeight());
	}

	@Override
	public Figure clone() {
		// TODO 自动生成的方法存根
		return new RoundedRectangle(this);
	}

	@Override
	public void setLastPoint(Point2D p) {
		// TODO 自动生成的方法存根
		if (shape != null)
		{
			RoundRectangle2D.Double rect = (RoundRectangle2D.Double) shape;
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

	@Override
	public void normalize() {
		// TODO 自动生成的方法存根
		Point2D center = getCenter();
		double cx = center.getX();
		double cy = center.getY();
		RectangularShape rect = (RectangularShape) shape;
		translation.translate(cx, cy);
		rect.setFrame(rect.getX() - cx,
					  rect.getY() - cy,
					  rect.getWidth(),
					  rect.getHeight());
	}

	@Override
	public Point2D getCenter() {
		// TODO 自动生成的方法存根
		RectangularShape rect = (RectangularShape) shape;

		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}
	
	public void setArc(Point2D p)
	{
		RoundRectangle2D.Double rect = (RoundRectangle2D.Double)shape;

		double bottomRightX = rect.getMaxX();
		double bottomRightY = rect.getMaxY();
		double x = p.getX();
		double y = p.getY();

		if (x > bottomRightX)
		{
			if (y < bottomRightY)
			{
				rect.arcwidth = bottomRightY - y;
				rect.archeight = rect.arcwidth;
			}
			else
			{
				rect.arcwidth = 0;
				rect.archeight = 0;
			}
		}
		else // x <= bottomRightX
		{
			if (y > bottomRightY)
			{
				rect.arcwidth = bottomRightX - x;
				rect.archeight = rect.arcwidth;
			}
			else
			{
				rect.arcwidth = 0;
				rect.archeight = 0;
			}
		}
	}

	@Override
	public FigureType getType() {
		// TODO 自动生成的方法存根
		return FigureType.ROUNDED_RECTANGLE;
	}

}
