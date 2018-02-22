package figures.listeners.transform;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;
import utils.Vector2D;

public class RotateShapeListener extends AbstractTransformShapeListener {
	
	private Vector2D initialVector;
	private Vector2D currentVector;
	
	public RotateShapeListener(Drawing model,
            HistoryManager<Figure> history, JLabel tipLabel)
	{
		super(model, history, tipLabel);
		keyMask = InputEvent.SHIFT_MASK;
	}
	
	@Override
	public void init() {
		// TODO 自动生成的方法存根
		if ((currentFigure != null) && (center != null))
		{
			initialVector = new Vector2D(center, startPoint);
			currentVector = new Vector2D(initialVector);
			initialTransform = currentFigure.getRotation();
		}
		else
			System.err.println(getClass().getSimpleName() + "init : null figure or center");
		
	}

	@Override
	public void updateDrag(MouseEvent e) {
		// TODO 自动生成的方法存根
		Point2D currentPoint = e.getPoint();
		currentVector.setEnd(currentPoint);
		if (currentFigure != null)
		{
			double angle = initialVector.angle(currentVector);
			// System.out.println("Rotate " + ((angle/Math.PI)*180.0) + "°");
			AffineTransform rotate = AffineTransform.getRotateInstance(angle);
			rotate.concatenate(initialTransform);
			currentFigure.setRotation(rotate);
		}
		else
			System.err.println(getClass().getSimpleName() + "updateDrag : null figure");
	}

}
