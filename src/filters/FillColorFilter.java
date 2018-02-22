package filters;

import java.awt.Paint;

import figures.Figure;

public class FillColorFilter extends FigureFilter<Paint> {

	public FillColorFilter(Paint paint)
	{
		super(paint);
	}

	@Override
	public boolean test(Figure f) {
		// TODO 自动生成的方法存根
		return f.getFillPaint().equals(element);
	}

}
