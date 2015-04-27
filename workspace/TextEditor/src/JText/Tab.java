package JText;

import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class Tab {
	private JFrame mainFrame;
	private JTabbedPane tabbedPane;

	public static void main(String[] args) {
		new Tab();
	}

	public Tab() {
		mainFrame = new JFrame();
		mainFrame.setTitle("JEdit");
		tabbedPane = new JTabbedPane();
		TextEditor t = new TextEditor();
//		tabbedPane.setUI(new MyTabbedPaneUI());
		t.menu.fileMenu.add(new NewTabAction());
		t.menu.fileMenu.add(new CloseTabAction());
		t.menu.fileMenu.add(new OpenTabAction());
		tabbedPane.add("untitled", t.editor.getComponent(0));
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainFrame.add(tabbedPane);

		// t.editor.getComponent(0).requestFocus();
		// t.style.textArea.getComponent(0).requestFocus();
		// t.style.textArea.setCaretPosition(0);
		addKeyBindings(t);
		mainFrame.setVisible(true);
		mainFrame.pack();
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
	}

	class MyTabbedPaneUI extends BasicTabbedPaneUI {

		@Override
		protected void paintTab(Graphics g, int tabPlacement,
				Rectangle[] rects, int tabIndex, Rectangle iconRect,
				Rectangle textRect) {
			Color savedColor = g.getColor();
			g.setColor(Color.GRAY);
			g.fillRect(rects[tabIndex].x, rects[tabIndex].y,
					rects[tabIndex].width, rects[tabIndex].height);
			g.setColor(Color.WHITE);
			g.drawRect(rects[tabIndex].x, rects[tabIndex].y,
					rects[tabIndex].width, rects[tabIndex].height);
			g.setColor(savedColor);
		}
		
	}

	private class NewTabAction extends AbstractAction {
		public NewTabAction() {
			super("NewTab");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			TextEditor t = new TextEditor();
			t.menu.fileMenu.add(new NewTabAction());
			t.menu.fileMenu.add(new CloseTabAction());
			t.menu.fileMenu.add(new OpenTabAction());
			addKeyBindings(t);
			// t.editor.getComponent(0).requestFocus();
			tabbedPane.add("untitled", t.editor.getComponent(0));

			// t.style.textArea.getComponent(0).requestFocus();
			// int i = tabbedPane.indexOfComponent(t.editor.getComponent(0));
			// tabbedPane.setSelectedComponent(t.editor.getComponent(0));
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		}
	}

	private class CloseTabAction extends AbstractAction {
		public CloseTabAction() {
			super("CloseTab");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Component selected = tabbedPane.getSelectedComponent();

			if (selected != null) {
				try {
					tabbedPane.remove(selected);
				} catch (Exception ex) {
					System.out.println("CannotCloseTabException");
				}
			}
		}
	}

	private class OpenTabAction extends AbstractAction {
		public OpenTabAction() {
			super("OpenTab");
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser open = new JFileChooser();
			open.showOpenDialog(null);
			File file = open.getSelectedFile();
			try {
				TextEditor newOpener = new TextEditor();
				newOpener.actionManager.fileName = file.getPath();
				FileReader t = new FileReader(file);
				// newOpener.editor.setTitle("JText: " +file.getPath());
				newOpener.style.textArea.read(t, null);
				newOpener.menu.fileMenu.add(new NewTabAction());
				newOpener.menu.fileMenu.add(new CloseTabAction());
				newOpener.menu.fileMenu.add(new OpenTabAction());
				newOpener.menu.fileMenu.add(new OpenTabAction());
				newOpener.actionManager = new Actions(newOpener.style.textArea,
						newOpener.caret);
				tabbedPane
						.add(file.getPath(), newOpener.editor.getComponent(0));
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
				// System.out.println(fileName + " Opened");
			} catch (Exception ex) {
				System.out.println("CannotOpenException");
			}
		}
	}

	private void addKeyBindings(TextEditor t) {
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK);
		InputMap ip = t.style.textArea.getInputMap();
		ip.put(key, new NewTabAction());
		key = KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK);
		ip.put(key, new CloseTabAction());
	}
}