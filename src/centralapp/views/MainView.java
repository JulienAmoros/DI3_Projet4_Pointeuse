package centralapp.views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import centralapp.controlers.CentralApp;

@SuppressWarnings("serial")
public class MainView extends JFrame {
	
	private JTabbedPane tabbedPane;
	
	public MainView(CentralApp controler) {
		//GTK theme
		/*try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {	}*/
		
		//Ensure to exit the program and not only the window
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(800, 600);
		
		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane);
		
		//Just to test
		//openCheckInOutTab();
		
		//Set up events
		addWindowListener(controler.new ExitEvent());
	}
	
	public void addTab(String name, JPanel tab){
		tabbedPane.addTab(name, tab);
	}
}