package JText;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.DefaultEditorKit;

public class Menu {
	protected JMenuBar menuBar;
	private Actions copyOfActions;
	protected JMenu fileMenu;
	private JMenu editMenu;

	protected JMenuItem openFile, saveFile, newFile;
	protected JMenuItem undoEdit, redoEdit, selectAll, cutSelection, copySelection, paste;

	protected Menu(Actions actionManager) {
		copyOfActions = actionManager;
		menuBar = new JMenuBar();
		createFileMenu();
		createEditMenu();
	}

	private void createFileMenu() {
		fileMenu = new JMenu("File");

		/*newFile = new JMenuItem("New");

		saveFile = new JMenuItem("Save");

		openFile = new JMenuItem("Open");
*/
		fileMenu.add(copyOfActions.newFileAction);
		fileMenu.add(copyOfActions.saveAction);
		fileMenu.add(copyOfActions.openAction);
		menuBar.add(fileMenu);
	}
	private void createEditMenu() {
		editMenu = new JMenu("Edit");
/*
		undoEdit = new JMenuItem("Undo");

		redoEdit = new JMenuItem("Redo");

		cutSelection = new JMenuItem("Cut");

		copySelection = new JMenuItem("Copy");

		paste = new JMenuItem("Paste");

		selectAll = new JMenuItem("SelectAll");*/

		editMenu.add(copyOfActions.undoAction);
		editMenu.add(copyOfActions.redoAction);
		editMenu.add(DefaultEditorKit.copyAction).setText("Copy");
		editMenu.add(DefaultEditorKit.cutAction).setText("Cut");
		editMenu.add(DefaultEditorKit.pasteAction).setText("Paste");
/*		editMenu.add(cutSelection);
		editMenu.add(copySelection);
		editMenu.add(paste);
		editMenu.add(selectAll);
*/		menuBar.add(editMenu);
	}
}