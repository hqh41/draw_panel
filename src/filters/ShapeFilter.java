package filters;

import figures.Figure;
import figures.enums.FigureType;

public class ShapeFilter extends FigureFilter<FigureType> {

	public ShapeFilter(FigureType ft)
	{
		super(ft);
	}

	@Override
	public boolean test(Figure f) {
		// TODO 自动生成的方法存根
		return f.getType() == element;
	}

}
