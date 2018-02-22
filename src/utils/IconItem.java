package utils;

import javax.swing.ImageIcon;

/**
 * Class defining an item Name associated to an Icon
 * @author davidroussel
 */
public class IconItem
{
	/**
	 * Combobox item name
	 */
	private String caption;
	
	/**
	 * Combobox item icon
	 * @note typically reflects the item name in a file named <caption>.png
	 */
	private ImageIcon icon;
	
	/**
	 * Constructor from caption only
	 * @param caption the caption of this item
	 */
	public IconItem(String caption)
	{
		this.caption = caption;
		icon = IconFactory.getIcon(caption);
		if (icon == null)
		{
			System.err.println("IconItem(" + caption
				+ ") : could not find corresponding icon");
		}
	}

	/**
	 * Caption accessor
	 * @return the caption of this item
	 */
	public String getCaption()
	{
		return caption;
	}

	/**
	 * Icon accessor
	 * @return the icon of this item
	 */
	public ImageIcon getIcon()
	{
		return icon;
	}
}
