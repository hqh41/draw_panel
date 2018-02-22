package figures.listeners.creation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.RoundedRectangle;
import history.HistoryManager;
public class RoundedRectangleCreationListener extends AbstractCreationListener {

	public RoundedRectangleCreationListener(Drawing model,
            HistoryManager<Figure> history, JLabel tipLabel)
	{
		super(model, history, tipLabel, 3);
		tips[0] = new String("Bouton gauche + drag pour commencer le regtangle");
		tips[1] = new String("Relâchez pour terminer le rectangle");
		tips[2] = new String("Clic gauche pour terminer l'arrondi du rectangle");

		updateTip();

		System.out.println("RoundedRectangleCreationListener created");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 2))
		{
			endAction(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
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
			nextStep();
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		// TODO 自动生成的方法存根
		if (currentStep == 1)
		{
			if (currentFigure != null)
			{
				currentFigure.setLastPoint(event.getPoint());
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
		if (currentStep == 2)
		{
			RoundedRectangle rect = (RoundedRectangle) currentFigure;
			rect.setArc(e.getPoint());

			drawingModel.update();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO 自动生成的方法存根

	}

}
