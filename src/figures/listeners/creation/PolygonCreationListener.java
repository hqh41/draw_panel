package figures.listeners.creation;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.Polygon;
import history.HistoryManager;

public class PolygonCreationListener extends AbstractCreationListener {

	public PolygonCreationListener(Drawing model,
            HistoryManager<Figure> history, JLabel tipLabel)
	{
		super(model, history, tipLabel, 2);

		tips[0] = new String("Clic gauche pour commencer le polygone");
		tips[1] = new String("clic gauche pour ajouter / droit pour terminer");

		updateTip();

		System.out.println("PolygonCreationListener created");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		Point p = e.getPoint();

		if (currentStep == 0)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				// On initie le polygone
				startAction(e);
//				drawingModel.setStatus(Status.ADDED);
				System.out.println("initating polygon");
			}
		}
		else
		{
			// Polygon poly = (Polygon) drawingModel.getLastFigure();
			Polygon poly = (Polygon) currentFigure;

			switch (e.getButton())
			{
				case MouseEvent.BUTTON1:
					// On ajoute un point au polygone
					poly.addPoint(p.x, p.y);
					poly.printPoints();
					break;
				case MouseEvent.BUTTON2:
					// On supprime le dernier point
					poly.removeLastPoint();
					break;
				case MouseEvent.BUTTON3:
					// On termine le polygone
					endAction(e);
					poly.printPoints();
					break;
			}
		}

		drawingModel.update();
		updateTip();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自动生成的方法存根
		if (currentStep > 0)
		{
			// AbstractFigure figure = drawingModel.getLastFigure();
			if (currentFigure != null)
			{
				currentFigure.setLastPoint(e.getPoint());
			}
			else
			{
				System.err.println(getClass().getSimpleName() + "::mouseMoved : null figure");
			}
			drawingModel.update();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO 自动生成的方法存根

	}

}
