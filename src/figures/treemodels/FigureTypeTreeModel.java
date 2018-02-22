package figures.treemodels;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;

import figures.Drawing;
import figures.Figure;
import figures.enums.FigureType;

public class FigureTypeTreeModel extends AbstractFigureTreeModel {

	private EnumMap<FigureType, List<Figure>> map;

	public FigureTypeTreeModel(Drawing drawing, JTree tree) throws NullPointerException
	{
		super(drawing, tree, "Figure Types");
		map = new EnumMap<FigureType, List<Figure>>(FigureType.class);
		update(drawing, null);
	}

	@Override
	protected void finalize() throws Throwable
	{
		Set<FigureType> figureTypeSet = map.keySet();
		for (Iterator<FigureType> ftit = figureTypeSet.iterator(); ftit.hasNext();)
		{
			List<Figure> list = map.get(ftit.next());
			list.clear();
		}
		map.clear();
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
		if (parent == rootElement)
		{
			if ((index >= 0) && (index < map.size()))
			{
				Set<FigureType> keySet = map.keySet();
				int count = 0;
				FigureType currentFigureType = null;
				for (Iterator<FigureType> ftit = keySet.iterator(); ftit.hasNext()
				    && (count <= index);)
				{
					currentFigureType = ftit.next();
					count++;
				}

				return currentFigureType;
			}

			return null;
		}
		else if (parent instanceof FigureType)
		{
			FigureType ft = (FigureType) parent;
			List<Figure> typedFigures = map.get(ft);

			if (typedFigures != null)
			{
				if ((index >= 0) && (index < typedFigures.size()))
					return typedFigures.get(index);
			}

			return null;
		}
		else
			return null;
	}

	@Override
	public int getChildCount(Object parent) {
		// TODO 自动生成的方法存根
		if (parent == rootElement)
		{
			return map.size();
		}
		else if (parent instanceof FigureType)
		{
			FigureType ft = (FigureType) parent;
			List<Figure> typedFigures = map.get(ft);
			if (typedFigures != null)
				return typedFigures.size();
			else
				return 0;
		}
		else
			return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO 自动生成的方法存根
		if (parent == rootElement)
		{
			Set<FigureType> figureTypesSet = map.keySet();
			if (figureTypesSet.contains(child))
			{
				int index = 0;
				for (Iterator<FigureType> ftit = figureTypesSet.iterator(); ftit
				    .hasNext();)
				{
					if (ftit.next().equals(child))
						return index;
				}

				return -1;
			}
			else
				return -1;
		}
		else if (parent instanceof FigureType)
		{
			FigureType ft = (FigureType) parent;
			List<Figure> typedFigures = map.get(ft);
			if (typedFigures != null)
				return typedFigures.indexOf(child);
		}

		return -1;
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO 自动生成的方法存根
		if (node == rootElement)
		{
			return false;
		}
		else if (node instanceof FigureType)
		{
			return false;
		}
		else if (node instanceof Figure)
		{
			return true;
		}

		return true;
	}

}
