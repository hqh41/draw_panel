package utils;

import java.awt.Color;
import java.awt.color.ColorSpace;

/**
 * Une Couleur comparable (pour pouvoir être utilisée dans un ensemble ou un
 * arbre trié)
 * @author davidroussel
 */
public class CColor extends Color implements Comparable<CColor>
{

	/**
	 * Instance statique particulière pour représenter pas de couleur
	 */
	public static final CColor NoColor = new CColor(255, 255, 255, 255);

	/**
	 * Constructeur à partir d'une couleur ordinaire
	 * @param c la couleur à convertir
	 */
	public CColor(Color c)
	{
		super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	/**
	 * Constructeur de copie
	 * @param c la couleur comparable à copier
	 */
	public CColor(CColor c)
	{
		this(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	/**
	 * Couleur à partir d'un entier
	 * @param rgb entier dont on utilise les 24 premiers bits pour fabriquer une
	 * couleur
	 */
	public CColor(int rgb)
	{
		super(rgb);
	}

	/**
	 * Constructeur à partir d'un entier
	 * @param rgba entier dont on utilise les 32 bits pour fabriquer une
	 * couleur
	 * @param hasalpha indique s'il faut utiliser les 8 dernier bits comme
	 * bits de transparence
	 */
	public CColor(int rgba, boolean hasalpha)
	{
		super(rgba, hasalpha);
	}

	/**
	 * Constructeur à partir des composantes R, G & B
	 * @param r la composante rouge
	 * @param g la composante verte
	 * @param b la compostante bleue
	 */
	public CColor(int r, int g, int b)
	{
		super(r, g, b);
	}

	/**
	 * Constructeur à partir des composantes R, G & B
	 * @param r la composante rouge
	 * @param g la composante verte
	 * @param b la compostante bleue
	 */
	public CColor(float r, float g, float b)
	{
		super(r, g, b);
	}

	/**
	 * Constructeur à partir de composantes dans un espace de couleur particulier
	 * @param cspace l'espace de couleurs utilisé
	 * @param components les composantes dans cet espace de couleur
	 * @param alpha la transparence
	 */
	public CColor(ColorSpace cspace, float[] components, float alpha)
	{
		super(cspace, components, alpha);
	}

	/**
	 * Constructeur à partir des composantes R, G & B et Alpha pour la
	 * transparence
	 * @param r la composante rouge
	 * @param g la composante verte
	 * @param b la compostante bleue
	 * @param a la composante alpha
	 */
	public CColor(int r, int g, int b, int a)
	{
		super(r, g, b, a);
	}

	/**
	 * Constructeur à partir des composantes R, G & B et Alpha pour la
	 * transparence
	 * @param r la composante rouge
	 * @param g la composante verte
	 * @param b la compostante bleue
	 * @param a la composante alpha
	 */
	public CColor(float r, float g, float b, float a)
	{
		super(r, g, b, a);
	}

	@Override
	public int compareTo(CColor o)
	{
		int red1 = getRed();
		int red2 = o.getRed();
		if (red1 < red2)
		{
			return -1;
		}
		else // red >= o.red
		{
			if (red1 > red2)
			{
				return 1;
			}
			else // red1 == red2
			{
				int green1 = getGreen();
				int green2 = o.getGreen();
				if (green1 < green2)
				{
					return -1;
				}
				else // green1 >= green2
				{
					if (green1 > green2)
					{
						return 1;
					}
					else // green1 == green2
					{
						int blue1 = getBlue();
						int blue2 = o.getBlue();
						if (blue1 < blue2)
						{
							return -1;
						}
						else // blue1 >= blue2
						{
							if (blue1 > blue2)
							{
								return 1;
							}
							else // blue1 == blue2
							{
								int alpha1 = getAlpha();
								int alpha2 = o.getAlpha();
								if (alpha1 < alpha2)
								{
									return -1;
								}
								else // alpha1 >= alpha2
								{
									if (alpha1 > alpha2)
									{
										return 1;
									}
									else
									{
										return 0;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Color#toString()
	 */
	@Override
	public String toString()
	{
		if (this == NoColor)
		{
			return new String("No Color");
		}
		if (super.equals(Color.BLACK))
		{
			return new String("Black");
		}
		if (super.equals(Color.BLUE))
		{
			return new String("Blue");
		}
		if (super.equals(Color.CYAN))
		{
			return new String("Cyan");
		}
		if (super.equals(Color.DARK_GRAY))
		{
			return new String("Dark Gray");
		}
		if (super.equals(Color.GRAY))
		{
			return new String("Gray");
		}
		if (super.equals(Color.GREEN))
		{
			return new String("Green");
		}
		if (super.equals(Color.LIGHT_GRAY))
		{
			return new String("Light Gray");
		}
		if (super.equals(Color.MAGENTA))
		{
			return new String("Magenta");
		}
		if (super.equals(Color.ORANGE))
		{
			return new String("Orange");
		}
		if (super.equals(Color.PINK))
		{
			return new String("Pink");
		}
		if (super.equals(Color.RED))
		{
			return new String("Red");
		}
		if (super.equals(Color.WHITE))
		{
			return new String("White");
		}
		if (super.equals(Color.YELLOW))
		{
			return new String("Yellow");
		}

		return new String("(" + String.valueOf(getRed()) +
		                  ", " + String.valueOf(getGreen()) +
		                  ", " + String.valueOf(getBlue()) + ")");
	}
}
