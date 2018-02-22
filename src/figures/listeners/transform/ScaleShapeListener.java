package figures.listeners.transform;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;

public class ScaleShapeListener extends AbstractTransformShapeListener {

	private Double initialDistance;
	private Double currentDistance;
	
	public ScaleShapeListener(Drawing model,
            HistoryManager<Figure> history, JLabel tipLabel)
	{
		super(model, history, tipLabel);

		keyMask = InputEvent.ALT_MASK;
	}
	
	@Override
	public void init() {
		// TODO 自动生成的方法存根
		if ((currentFigure != null) && (center != null))
		{
			initialDistance = center.distance(startPoint);
			currentDistance = initialDistance;
			initialTransform = currentFigure.getScale();
		}
		else
			System.err.println("ScaleShapeListener::init : null figure or center");
	}

	@Override
	public void updateDrag(MouseEvent e) {
		// TODO 自动生成的方法存根
		Point2D currentPoint = e.getPoint();
		if ((currentFigure != null) && (center != null))
		{
			currentDistance = center.distance(currentPoint);
			double scale = currentDistance / initialDistance;
			AffineTransform scaleT = AffineTransform.getScaleInstance(scale, scale);
			scaleT.concatenate(initialTransform);
			currentFigure.setScale(scaleT);
		}
		else
			System.err.println(getClass().getSimpleName() + "::updateDrag : null figure or center");
	}

}
