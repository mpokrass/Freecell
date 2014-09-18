import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates a new frame that keeps track of size and other attributes
 * 
 * @author Michelle Pokrass
 * 
 */
public class FreeCellMain extends JFrame
{
	private TablePanel tableArea;

	/**
	 * Creates a new frame
	 */
	public FreeCellMain()
	{
		// Creates the jframe
		super("Freecell");
		setResizable(false);
		// Position in the middle of the window
		setLocation(100, 100);
		// Add in an Icon - Ace of Spades
		setIconImage(new ImageIcon("images\\ace.png").getImage());
		// Add the TablePanel to the centre of the Frame
		setLayout(new BorderLayout());
		tableArea = new TablePanel(this);
		add(tableArea, BorderLayout.CENTER);
		// Add in the menus
		addMenus();
	}

	/**
	 * Adds the menus to the main frame Includes adding ActionListeners to
	 * respond to menu commands
	 */
	private void addMenus()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');

		JMenuItem newOption = new JMenuItem("New");
		newOption.addActionListener(new ActionListener() {
			/**
			 * Starts a new game
			 * 
			 * @param event
			 *            The event that selected this menu option
			 */
			public void actionPerformed(ActionEvent event)
			{
				tableArea.newGame();
			}
		});
		gameMenu.add(newOption);

		JMenuItem undo = new JMenuItem("Undo");

		undo.addActionListener(new ActionListener() {
			/**
			 * Undoes the last move
			 * 
			 * @param event
			 *            The event that selected this menu option
			 */
			public void actionPerformed(ActionEvent event)
			{
				tableArea.undoMove();
			}
		});
		gameMenu.add(undo);
		// Creates a new menu item for autocomplete
		JMenu autocompleteMenu = new JMenu("AutoComplete");
		final JRadioButtonMenuItem autoCompleteOn = new JRadioButtonMenuItem(
				"On", true);
		final JRadioButtonMenuItem autoCompleteOff = new JRadioButtonMenuItem(
				"Off", false);
		// When autocomplete is turned on, set it to on in the panel class and
		// de-select the "off" option
		autoCompleteOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				tableArea.autoOn();
				autoCompleteOff.setSelected(false);
			}
		});
		// When autocomplete is turned off, set it to off in the panel class and
		// de-select the "on" option
		autoCompleteOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				tableArea.autoOff();
				autoCompleteOn.setSelected(false);
			}
		});
		// Add all the options to the menu
		autocompleteMenu.add(autoCompleteOn);
		autocompleteMenu.add(autoCompleteOff);
		menuBar.add(gameMenu);
		menuBar.add(autocompleteMenu);
		setJMenuBar(menuBar);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Creates a new frame
		FreeCellMain frame = new FreeCellMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		// Creates a new panel within the frame
		TablePanel panel = new TablePanel(frame);

	}

}
