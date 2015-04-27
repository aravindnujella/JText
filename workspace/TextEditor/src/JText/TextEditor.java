/*Contains main*/

package JText;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.AbstractAction;
 @SuppressWarnings("serial")
public class TextEditor implements ActionListener {

	protected JFrame editor;
	protected Style style;
	protected Caret caret;
	protected Actions actionManager;
	protected Menu menu;
	/*public static void main(String[] args) {
		new TextEditor();
	}*/
	public TextEditor() {
		
		editor = new JFrame();
//		editor.setExtendedState(Frame.MAXIMIZED_BOTH);
//		editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		style = new Style(editor);
		caret = new Caret(style.textArea);
		actionManager = new Actions(style.textArea,caret);
		editor.setTitle("untitled");
		createMenuBar(actionManager);
		KeyBindings k = new KeyBindings(style.textArea.getInputMap(), actionManager);
//		editor.setVisible(true);
		editor.pack();
	}

	public void createMenuBar(Actions actionManager) {
		menu = new Menu(actionManager);
		editor.setJMenuBar(menu.menuBar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println("boo");
	}
}