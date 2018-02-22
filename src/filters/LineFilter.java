package filters;

import java.awt.Paint;

import figures.Figure;
import figures.enums.LineType;

public class LineFilter extends FigureFilter<LineType> {

	public LineFilter(LineType line)
	{
		super(line);
	}

	@Override
	public boolean test(Figure f) {
		// TODO 自动生成的方法存根
		return LineType.fromStroke(f.getStroke()) == element;
	}

}
