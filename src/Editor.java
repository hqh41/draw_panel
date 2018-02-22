import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import widgets.EditorFrame;

/**
 * Programme principal lançant la fenêtre {@link EditorFrame}
 * @author davidroussel
 */
public class Editor
{

	/**
	 * Programme principal
	 * @param args arguments : le nom du look and feel à utiliser
	 */
	public static void main(String[] args)
	{
		/*
		 * Mise ne place du look and feel du système, ou celui fourni en
		 * argument du programme
		 */
		try
		{
			if (args.length == 0)
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			else
			{
				String lookAndFeelName = args[0];
				LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
				for (LookAndFeelInfo lafi : lafis)
				{
					if (lafi.getName().toLowerCase().equals(lookAndFeelName.toLowerCase()))
					{
						UIManager.setLookAndFeel(lafi.getClassName());
						break;
					}
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Look and feel could not be found");
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			System.err.println("new instance of the class couldn't be created");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			System.err.println("Look and feel class or initializer isn't accessible");
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			System.err.println("isSupportedLookAndFeel() is false");
			e.printStackTrace();
		}
		catch (ClassCastException e)
		{
			System.err.println("className does not identify a class that extends LookAndFeel");
			e.printStackTrace();
		}

		// Mise en place spécifique à Mac OS X
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Mac OS"))
		{
			macOSSettings();
		}

		/*
		 * Création de la fenêtre
		 */
		final EditorFrame frame = new EditorFrame();

		/*
		 * Insertion de la fenêtre dans la file des évènements GUI
		 */
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					frame.pack();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Mise en place des options spécifiques à MacOS.
	 * A virer si votre système n'est pas MacOS car com.apple.... risque
	 * de ne pas exister
	 */
	private static void macOSSettings()
	{
		// Remettre les menus au bon endroit (dans la barre en haut)
		System.setProperty("apple.laf.useScreenMenuBar", "true");

//		ImageIcon imageIcon = new ImageIcon(
//				Editor.class.getResource("/images/Logo.png"));
//		if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE)
//		{
			// Titre de l'application
			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name",
					"Figure Editor");
//			// Chargement d'une icône pour le dock
//			com.apple.eawt.Application.getApplication().setDockIconImage(
//					imageIcon.getImage());
//		}
	}
}

