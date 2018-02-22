package widgets;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreeModel;

import figures.Drawing;
import figures.treemodels.EdgeColorTreeModel;
import figures.treemodels.EdgeTypeTreeModel;
import figures.treemodels.FigureTreeModel;
import figures.treemodels.FigureTypeTreeModel;
import figures.treemodels.FillColorTreeModel;
import widgets.enums.TreeType;

public class TreesPanel extends JPanel
{
	/**
	 * Le type d'arbre que l'on veut utiliser
	 * @see TreeType
	 */
	private TreeType treeType;

	/**
	 * Le modèle d'arbre à créer en fonction du {@link #treeType}
	 */
	private TreeModel model;

	/**
	 * Le modèle de dessin
	 */
	private Drawing drawing;

	/**
	 * Le {@link JTree} à utiliser pour visualiser l'arbre
	 */
	private JTree tree;

	/**
	 * Change le type d'arbre et crée le TreeModel associé
	 * @param treeType the treeType to set
	 */
	public void setTreeType(TreeType treeType)
	{
		System.out.println("setTreeType(" + treeType + ")");
		this.treeType = treeType;

		if (model != null)
		{
			drawing.deleteObserver((Observer) model);
			model = null;
		}

		if ((drawing != null) && (tree != null))
		{
			switch (this.treeType)
			{
				case FIGURE:
					model = new FigureTreeModel(drawing, tree);
					break;
				case FIGURE_TYPE:
					// model = null; // TODO 
					model = new FigureTypeTreeModel(drawing, tree);
					break;
				case FILL_COLOR:
					// model = null; // TODO 
					model = new FillColorTreeModel(drawing, tree);
					break;
				case EDGE_COLOR:
					// model = null; // TODO 
					model = new EdgeColorTreeModel(drawing, tree);
					break;
				case EDGE_TYPE:
					// model = null; // TODO 
					model = new EdgeTypeTreeModel(drawing, tree);
					break;
				default:
					model = null;
					break;
			}
		}
		else
		{
			System.out.println("FigureTypeTreeModel not set up because "
			    + "null drawing or null JTree");
		}
	}

	/**
	 * Sets the drawing
	 * @param drawing the drawing to set
	 */
	public void setDrawing(Drawing drawing)
	{
		// System.out.println("Setting up Drawing" + drawing + " in
		// TreesPanel");
		this.drawing = drawing;
		if (drawing != null)
		{
			setTreeType(treeType);
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setDrawing : null drawing");
		}
	}

	/**
	 * Create the panel.
	 */
	public TreesPanel()
	{
		int treeTypeIndex = 0;
		treeType = TreeType.fromInteger(treeTypeIndex);
		model = null;
		setLayout(new BorderLayout(0, 0));

		JPanel treeModePanel = new JPanel();
		add(treeModePanel, BorderLayout.NORTH);
		treeModePanel.setLayout(new BorderLayout(0, 0));

		JLabel lblTreeMode = new JLabel("Tree mode");
		treeModePanel.add(lblTreeMode, BorderLayout.WEST);

		JComboBox<TreeType> treeComboBox = new JComboBox<TreeType>();
		treeComboBox.setMaximumRowCount(TreeType.NbTreeTypes);
		treeComboBox
		    .setModel(new DefaultComboBoxModel<TreeType>(TreeType.values()));
		treeComboBox.setSelectedIndex(treeTypeIndex);
		treeComboBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				@SuppressWarnings("unchecked")
				JComboBox<TreeType> combo = (JComboBox<TreeType>) e.getSource();
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					Object selectedItem = combo.getSelectedItem();
					if (selectedItem instanceof TreeType)
					{
						setTreeType((TreeType) selectedItem);

						System.out.println("Setting tree type to " +
						selectedItem);
					}
				}
			}
		});
		treeModePanel.add(treeComboBox);

		JScrollPane treeScrollPane = new JScrollPane();
		treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(treeScrollPane, BorderLayout.CENTER);

		tree = new JTree();
		treeScrollPane.setViewportView(tree);
	}
}
