package figures.listeners.creation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;

public class StarCreationListener extends AbstractCreationListener {

	public StarCreationListener(Drawing model,
            HistoryManager<Figure> history, JLabel tipLabel)
	{
		super(model, history, tipLabel, 2);
		tips[0] = new String("Bouton gauche + drag pour commencer l'étoile");
		tips[1] = new String("Relâchez pour terminer l'étoile");

		updateTip();

		System.out.println("StarCreationListener created");
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根

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
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 0))
		{
			startAction(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 1))
		{
			endAction(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自动生成的方法存根
		if (currentStep == 1)
		{
			// AbstractFigure figure = drawingModel.getLastFigure();
			if (currentFigure != null)
			{
				currentFigure.setLastPoint(e.getPoint());
			}
			else
			{
				System.err.println(getClass().getSimpleName() + "::mouseDragged : null figure");
			}

			drawingModel.update();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO 自动生成的方法存根

	}

}
