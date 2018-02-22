package widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import utils.IconItem;

/**
 * Classe contenant un titre et une liste déroulante utilisant des JLabel avec
 * des icones pour les élements de la liste déroulante
 */
public class JLabeledComboBox extends JPanel
{
	/** Le titre de cette liste */
	private String title;

	/**
	 * Les textes et icônes pour les items
	 */
	private IconItem[] items;

	/**
	 * La combobox utilisée à l'intérieur pour pouvoir ajouter des listener
	 * par la suite
	 */
	private JComboBox<IconItem> combobox;

	/**
	 * Constructeur
	 * @param title le titre du panel
	 * @param captions les légendes des éléments de la liste
	 * @param selectedIndex l'élément sélectionné initialement
	 * @param listener le listener à appeller quand l'élement sélectionné de la
	 *            liste change
	 * @see #createImageIcon(String)
	 */
	public JLabeledComboBox(String title, String[] captions, int selectedIndex,
	        ItemListener listener)
	{
		setAlignmentX(Component.LEFT_ALIGNMENT);

		this.title = title;
		items = new IconItem[captions.length];

		for (int i = 0; i < captions.length; i++)
		{
			items[i] = new IconItem(captions[i]);
		}

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// Creates the title
		JLabel label = new JLabel((this.title != null ? this.title : "text"));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		add(label);

		// Creates the Combobox
		combobox = new JComboBox<IconItem>(items);
		combobox.setAlignmentX(Component.LEFT_ALIGNMENT);
		combobox.setEditable(false);
		int index;
		if ((selectedIndex < 0) || (selectedIndex > captions.length))
		{
			index = 0;
		}
		else
		{
			index = selectedIndex;
		}
		combobox.setSelectedIndex(index);
		combobox.addItemListener(listener);
		// Mise en place du renderer pour les élements de la liste
		JLabelRenderer renderer = new JLabelRenderer();
		renderer.setPreferredSize(new Dimension(100, 32));
		combobox.setRenderer(renderer);
		// Ajout de la liste
		add(combobox);
	}

	/**
	 * Ajout d'un nouveau listener déclenché lorsqu'un élément est sélectionné
	 * @param aListener le nouveau listener à ajouter.
	 */
 	public void addItemListener(ItemListener aListener)
 	{
 		if (combobox != null)
 		{
 			combobox.addItemListener(aListener);
 		}
 		else
 		{
 			System.err.println(getClass().getSimpleName() + "::addItemListener : null combobox");
 		}
 	}

 	/**
 	 * Obtention de l'index de l'élément sélectionné dans le combobox
 	 * @return l'index de l'élément sélectionné dans le combobox
 	 */
 	public int getSelectedIndex()
 	{
 		return combobox.getSelectedIndex();
 	}

	/**
	 * Renderer pour les Labels du combobox
	 */
	protected class JLabelRenderer extends JLabel
		implements ListCellRenderer<IconItem>
	{
		/** fonte pour les items à problèmes */
		private Font pbFont;

		/**
		 * Constructeur
		 */
		public JLabelRenderer()
		{
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
		 * .JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(
			JList<? extends IconItem> list, IconItem value, int index,
			boolean isSelected, boolean cellHasFocus)
		{
			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Mise en place de l'icone et du texte dans le label
			// Si l'icone est null afficher un label particulier avec
			// setPbText
			ImageIcon itemIcon = value.getIcon();
			String itemString = value.getCaption();
			setIcon(itemIcon);
			if (itemIcon != null)
			{
				setText(itemString);
				setFont(list.getFont());
			}
			else
			{
				setPbText(itemString + " (pas d'image)", list.getFont());
			}

			return this;
		}

		/**
		 * Mise en place du texte s'il y a un pb pour cet item
		 * @param pbText le texte à afficher
		 * @param normalFont la fonte à utiliser (italique)
		 */
		protected void setPbText(String pbText, Font normalFont)
		{
			if (pbFont == null)
			{ // lazily create this font
				pbFont = normalFont.deriveFont(Font.ITALIC);
			}
			setFont(pbFont);
			setText(pbText);
		}
	}
}
