package figures.treemodels;

import java.util.EnumMap;
import java.util.List;

import javax.swing.JTree;

import figures.Drawing;
import figures.Figure;
import figures.enums.FigureType;

public class EdgeTypeTreeModel extends AbstractFigureTreeModel {
	private EnumMap<FigureType, List<Figure>> map;

	public EdgeTypeTreeModel(Drawing drawing, JTree tree) throws NullPointerException
	{
		super(drawing, tree, "Figure Types");
		map = new EnumMap<FigureType, List<Figure>>(FigureType.class);
		update(drawing, null);
	}

	@Override
	protected void updateFiguresFromDrawing(List<Figure> figures) {
		// TODO 自动生成的方法存根

	}

	@Override
	protected void updateSelectedFigures() {
		// TODO 自动生成的方法存根

	}

	@Override
	public Object getChild(Object parent, int index) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO 自动生成的方法存根
		return false;
	}

}
