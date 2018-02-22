package utils;

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Classe contenant une FlyweigtFactory pour les les icônes, afin de pouvoir
 * réutiliser une même icône (chargée à partir d'un fichier image contenu dans
 * le package "images") à plusieurs endroits de l'interface graphique.
 * @author davidroussel
 */
public class IconFactory
{
	/**
	 * le répertoire de base pour chercher les images
	 */
	private final static String ImageBase = "/images/";
	
	/**
	 * L'extension par défaut pour chercher les fichiers images
	 */
	private final static String ImageType = ".png";

	/**
	 * La factory stockant et fournissant les icônes
	 */
	static private FlyweightFactory<ImageIcon> iconFactory =
		new FlyweightFactory<ImageIcon>();

	/**
	 * Méthode d'obtention d'une icône pour un nom donné
	 * @param name le nom de l'icône que l'on recherche
	 * @return l'icône correspondant au nom demandé si un fichier avec ce nom
	 * est trouvé dans le package/répertoire "images" ou bien null si aucune
	 * image correspondant à ce nom n'est trouvée.
	 */
	static public ImageIcon getIcon(String name)
	{
		// checks if there is an icon with this name in the "images" directory
		if (name.length() > 0)
		{
			int hash = name.hashCode();
			ImageIcon icon = iconFactory.get(hash);
			if (icon == null)
			{
				URL url = IconFactory.class.getResource(ImageBase + name + ImageType);
				if (url != null)
				{
					icon = new ImageIcon(url);
					if (icon != null && 
						icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE)
					{
						icon.setDescription(name);
						iconFactory.put(hash, icon);
					}
				}
				else
				{
					System.err.println("IconFactory::getIcon(" + name
						+ ") : could'nt find file " + ImageBase + name
						+ ImageType);
				}
				
				return iconFactory.get(hash);
			}
			else
			{
				return icon;
			}
		}
		else
		{
			System.err.println("IconFactory::getIcon(<EMPTY NAME>)");
		}
		
		return null;
	}
}
