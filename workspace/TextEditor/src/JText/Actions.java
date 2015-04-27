package JText;

//import ../ui.;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
//import java.awt.*;
import javax.swing.JButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

public class Actions {
	protected UndoManager undo;
	String fileName;
	protected Action newFileAction;
	protected Action openAction;
	protected Action saveAction;
	private JTextArea copyOfDocument;
	private Caret copyOfCaret;
	protected Action autoCompleteAction;
	protected Action undoAction;
	protected Action redoAction;
	protected Action hideAllAction;
	protected Action findAction;
	protected JFrame findFrame;
	protected JTextField textField;
	protected JTextField replaceText;
	protected JTextField findText;
	protected Action replaceAction;
	String textBook;
	protected Action spellCheckAction;
	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();

	// protected Action searchAction;

	public Actions(JTextArea textArea, Caret c) {

		copyOfDocument = textArea;
		copyOfCaret = c;
		undo = new UndoManager();
		textArea.getDocument().addUndoableEditListener(new EditListener());
		openAction = new OpenAction();
		saveAction = new SaveAction();
		autoCompleteAction = new AutoCompleteAction();
		undoAction = new UndoAction();
		redoAction = new RedoAction();
		findAction = new FindAction();
		// searchAction = new SearchAction();
		hideAllAction = new HideAllAction();
		replaceAction = new ReplaceAction();
		fileName = null;
		textBook = null;
		InputMap ip = textField.getInputMap();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
				Event.CTRL_MASK);
		ip.put(key, hideAllAction);
		spellCheckAction = new SpellCheck();
	}

	private class EditListener implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
		}
	}

	private class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (Exception ex) {
			}
		}
	}

	private class RedoAction extends AbstractAction {
		public RedoAction() {
			super("Redo");
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (Exception ex) {
			}
		}
	}

	private class OpenAction extends AbstractAction {
		public OpenAction() {
			super("Open");
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser open = new JFileChooser();
			open.showOpenDialog(null);
			File file = open.getSelectedFile();
			try {
				TextEditor newOpener = new TextEditor();
				newOpener.actionManager.fileName = file.getPath();
				FileReader t = new FileReader(file);
				String entireFileText = new Scanner(new File(file.getPath()))
						.useDelimiter("\\A").next();
				// newOpener.editor.setTitle("JText: " +file.getPath());
				newOpener.style.textArea.setText(entireFileText);
				System.out.println(fileName + " Opened");
			} catch (Exception ex) {
				System.out.println("CannotOpenException");
			}
		}
	}

	private class SaveAction extends AbstractAction {
		public SaveAction() {
			super("Save");
		}

		public void actionPerformed(ActionEvent e) {
			if (fileName == null) {
				JFileChooser save = new JFileChooser();
				save.showSaveDialog(null);

				File file = save.getSelectedFile();
				if (file == null)
					return;
				fileName = file.getPath();

				saveFiles(fileName);
				// System.out.println(fileName);
			} else {
				saveFiles(fileName);
			}
		}

		private void saveFiles(String f) {
			try {
				FileWriter w = new FileWriter(f);
				copyOfDocument.write(w);
				w.close();
			} catch (Exception ex) {
				System.out.println("CannotSaveException");
			}
		}
	}

	protected class AutoCompleteAction extends AbstractAction {
		int caretOffset;

		public AutoCompleteAction() {
			super("AutoComplete");
		}

		public void actionPerformed(ActionEvent ev) {
			ActionListener menuListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try {
						Document doc = copyOfDocument.getDocument();
						doc.remove(
								copyOfCaret.caretPositionStart - caretOffset,
								caretOffset);

						doc.insertString(copyOfCaret.caretPositionStart,
								event.getActionCommand(), null);

					} catch (Exception ex) {
					}
				}
			};
			JMenuItem temp;
			JPopupMenu suggestionMenu = new JPopupMenu();
			String[] allWords;
			String text = copyOfDocument.getText();
			allWords = text.split("\\W+");
			TreeSet<String> autoCompleteMenuEntries = new TreeSet<String>();

			StringBuilder supportBuild = new StringBuilder();
			int i = copyOfCaret.caretPositionStart - 1;
			while (i > -1 && Character.isLetter(text.charAt(i))) {
				supportBuild.append(text.charAt(i));
				i--;
			}
			for (String s : allWords) {
				autoCompleteMenuEntries.add(s);
			}
			supportBuild.reverse();
			String present = supportBuild.toString();
			i = present.length();
			caretOffset = i;
			int numberOfMatches = 0;
			for (String s : autoCompleteMenuEntries) {
				try {
					if (present != null) {
						int match = 0;
						for (int j = 0; j < s.length(); j++) {
							if (s.charAt(j) == present.charAt(match))
								match++;
							if (match > i - 1) {
								System.out.println(s);
								temp = new JMenuItem(s);
								temp.addActionListener(menuListener);
								suggestionMenu.add(temp);
								numberOfMatches++;
								break;
							}
						}
					}
				} catch (Exception ex) {
				}
			}
			if (numberOfMatches != 0) {
				Point p = copyOfDocument.getCaret().getMagicCaretPosition();
				suggestionMenu.show(copyOfDocument, p.x, p.y);
			}
		}
	}

	private class FindAction extends AbstractAction {
		ArrayList<Integer> searchIndex = new ArrayList<Integer>();
		int lastIndex = 0;
		int u = copyOfCaret.caretPositionStart;
		int stringLength;

		public FindAction() {
			super("Find");
			textField = new JTextField(10);
			textField.setFont(new Font("SansSerif", Font.BOLD, 20));

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			searchAndHighlight();
		}

		private void searchAndHighlight() {
			findFrame = new JFrame();
			findFrame.setTitle("Search");
			findFrame.setSize(300, 70);
			findFrame.setLocationRelativeTo(null);

			JButton searchButton = new JButton("Search");
			JButton searchNext = new JButton("SearchNext");
			JPanel buttonPane = new JPanel();
			JPanel searchPane = new JPanel();

			searchPane.add(textField);
			buttonPane.add(searchButton);
			buttonPane.add(searchNext);
			findFrame.setTitle("SearchPanel");
			findFrame.getContentPane().add(searchPane, BorderLayout.CENTER);
			findFrame.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			findFrame.pack();
			findFrame.setVisible(true);

			searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = textField.getText();
					String fileText = copyOfDocument.getText();
					stringLength = searchText.length();
					if (stringLength != 0)
						while (true) {
							System.out.println(lastIndex);
							lastIndex = fileText.indexOf(searchText, lastIndex);
							searchIndex.add(lastIndex);
							if (lastIndex != -1) {
								lastIndex += searchText.length();
								continue;
							}
							break;
						}
					runHighlighter();
				}
			});
			searchNext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = textField.getText();
					String fileText = copyOfDocument.getText();
					stringLength = searchText.length();

					if (stringLength != 0)
						System.out.println(lastIndex);
					u = fileText.indexOf(searchText, u);
					if (u != -1) {
						Highlighter hl = copyOfDocument.getHighlighter();
						hl.removeAllHighlights();
						selectNext(u, searchText.length());

						u += searchText.length();
						copyOfDocument.requestFocus();

					} else {
						u = 0;
					}
				}
			});
		}

		private void selectNext(int a, int b) {
			copyOfDocument.select(a, a + b);
		}

		private void runHighlighter() {
			DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
					Color.YELLOW);
			Highlighter hl = copyOfDocument.getHighlighter();

			int l = searchIndex.size();
			for (int i = 0; i < l; i++) {
				try {
					hl.addHighlight(searchIndex.get(i), searchIndex.get(i)
							+ stringLength, highlightPainter);
				} catch (BadLocationException e) {
				}
			}
		}

	}

	private class ReplaceAction extends AbstractAction {
		ArrayList<Integer> searchIndex = new ArrayList<Integer>();
		int lastIndex = 0;
		int u = copyOfCaret.caretPositionStart;
		int stringLength;
		int i = u;

		public ReplaceAction() {
			super("Replace");
			textField = new JTextField(10);
			textField.setFont(new Font("SansSerif", Font.BOLD, 20));

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			searchAndHighlight();
		}

		private void searchAndHighlight() {
			findFrame = new JFrame();
			findFrame.setTitle("SearchAndReplace");
			findFrame.setSize(300, 70);
			findFrame.setLocationRelativeTo(null);
			JPanel searchPane = new JPanel();
			JPanel buttonPane = new JPanel();
			searchPane.setLayout(new GridLayout(2, 2));
			buttonPane.setLayout(new GridLayout(1, 4));
			JLabel findLabel = new JLabel("Find :");
			JLabel replaceLabel = new JLabel("Replace with:");
			findText = new JTextField(10);
			replaceText = new JTextField(10);

			JButton searchButton = new JButton("Search");
			JButton replaceNext = new JButton("ReplaceNext");
			JButton replaceAll = new JButton("ReplaceAll");
			JButton searchNext = new JButton("SearchNext");
			searchPane.add(findLabel);
			searchPane.add(findText);
			searchPane.add(replaceLabel);
			searchPane.add(replaceText);
			buttonPane.add(searchButton);
			buttonPane.add(replaceNext);
			buttonPane.add(replaceAll);
			buttonPane.add(searchNext);
			buttonPane.add(searchButton);
			findFrame.setTitle("SearchPanel");
			findFrame.getContentPane().add(searchPane, BorderLayout.CENTER);
			findFrame.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			findFrame.pack();
			findFrame.setVisible(true);
			replaceAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = findText.getText();
					String fileText = copyOfDocument.getText();
					String repText = replaceText.getText();

					stringLength = searchText.length();
					if (stringLength != 0) {
						int lastIndex = 0;
						while (true) {
							lastIndex = fileText.indexOf(searchText, lastIndex);
							if (lastIndex != -1) {
								int end = lastIndex + searchText.length();
								copyOfDocument.replaceRange(repText, lastIndex,
										end);
								lastIndex += repText.length();
								fileText = copyOfDocument.getText();
								/*
								 * lastIndex = fileText.indexOf(searchText,
								 * lastIndex); lastIndex += searchText.length();
								 */
								continue;
							}
							break;
						}
					}
				}
			});
			searchNext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = findText.getText();
					String fileText = copyOfDocument.getText();
					stringLength = searchText.length();

					i = fileText.indexOf(searchText, i);
					if (i != -1) {
						Highlighter hl = copyOfDocument.getHighlighter();
						hl.removeAllHighlights();
						selectNext(i, searchText.length());

						i += searchText.length();
						copyOfDocument.requestFocus();
						u = i - searchText.length();
					} else {
						i = 0;
					}
				}
			});

			searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = findText.getText();
					String fileText = copyOfDocument.getText();
					// System.out.println(searchText);
					stringLength = searchText.length();
					if (stringLength != 0)
						while (true) {
							System.out.println(lastIndex);
							lastIndex = fileText.indexOf(searchText, lastIndex);
							searchIndex.add(lastIndex);
							if (lastIndex != -1) {
								// copyOfDocument.select(lastIndex, lastIndex +
								// searchText.length());
								// copyOfDocument.requestFocusInWindow();
								lastIndex += searchText.length();
								// System.out.println();
								continue;
							}
							break;
						}
					// findFrame.setVisible(false);
					// findFrame.dispose();
					runHighlighter();
				}
			});
			replaceNext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String searchText = findText.getText();
					String fileText = copyOfDocument.getText();
					// System.out.println(searchText);
					stringLength = searchText.length();
					// u = copyOfCaret.caretPositionStart;
					u = fileText.indexOf(searchText, u);
					// searchIndex.add(lastIndex);
					System.out.println(u);
					if (u != -1) {

						try {
							copyOfDocument.replaceRange(replaceText.getText(),
									u, u + stringLength);
							copyOfDocument.setCaretPosition(u
									+ replaceText.getText().length());
						} catch (Exception ex) {
						}
						copyOfDocument.requestFocus();
					} else {
						u = 0;
					}
				}
			});
		}

		private void selectNext(int a, int b) {
			copyOfDocument.select(a, a + b);
		}

		private void runHighlighter() {
			DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
					Color.YELLOW);
			Highlighter hl = copyOfDocument.getHighlighter();
			int l = searchIndex.size();
			for (int i = 0; i < l; i++) {
				try {
					hl.addHighlight(searchIndex.get(i), searchIndex.get(i)
							+ stringLength, highlightPainter);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}

	}

	protected class HideAllAction extends AbstractAction {
		public HideAllAction() {
			super("hideAll");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Highlighter hl = copyOfDocument.getHighlighter();
			hl.removeAllHighlights();
			copyOfDocument.requestFocus();
			try {
				findFrame.setVisible(false);
				findFrame.dispose();
			} catch (Exception ex) {
				System.out.println("CannotHideException");
			}
		}
	}

	protected class SpellCheck extends AbstractAction {
		BufferedReader f;
		String curWord;

		public SpellCheck() {
			super("SpellCheck");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (textBook == null) {
				textBook = "big.txt";
				try {
					f = new BufferedReader(new FileReader(textBook));
					Pattern p = Pattern.compile("\\w+");
					for (String reader = ""; reader != null; reader = f
							.readLine()) {
						Matcher m = p.matcher(reader.toLowerCase());
						// System.out.println("boo");

						while (m.find()) {
							reader = m.group();
							if (dictionary.containsKey(reader)) {
								dictionary.put(reader,
										dictionary.get(reader) + 1);
							} else {
								dictionary.put(reader, 1);
							}
						}
					}
					f.close();
				} catch (IOException e) {
					System.out.println("UnableToOpenTextBook");
				}

			}

			String u = spellChecker();
			int i = copyOfCaret.caretPositionStart;
			try {
				copyOfDocument.replaceRange(u, i - curWord.length(), i);
				copyOfDocument.setCaretPosition(i + u.length());
			} catch (Exception ex) {
				// System.out.println(u+" "+i);

			}

		}

		private String spellChecker() {
			String text = copyOfDocument.getText();
			StringBuilder supportBuild = new StringBuilder();
			int i = copyOfCaret.caretPositionStart - 1;
			while (i > -1 && Character.isLetter(text.charAt(i))) {

				supportBuild.append(text.charAt(i));

				i--;
			}
			supportBuild.reverse();
			curWord = supportBuild.toString();
			if (dictionary.containsKey(curWord))
				return curWord;
			ArrayList<String> list = getEdits(curWord);
			HashMap<Integer, String> candidates = new HashMap<Integer, String>();
			for (String s : list)
				if (dictionary.containsKey(s))
					candidates.put(dictionary.get(s), s);
			if (candidates.size() > 0)
				return candidates.get(Collections.max(candidates.keySet()));
			for (String s : list)
				for (String w : getEdits(s))
					if (dictionary.containsKey(w))
						candidates.put(dictionary.get(w), w);
			return candidates.size() > 0 ? candidates.get(Collections
					.max(candidates.keySet())) : curWord;
		}

		private ArrayList<String> getEdits(String curWord) {
			ArrayList<String> result = new ArrayList<String>();
			for (int i = 0; i < curWord.length(); ++i)
				result.add(curWord.substring(0, i) + curWord.substring(i + 1));
			for (int i = 0; i < curWord.length() - 1; ++i)
				result.add(curWord.substring(0, i)
						+ curWord.substring(i + 1, i + 2)
						+ curWord.substring(i, i + 1)
						+ curWord.substring(i + 2));
			for (int i = 0; i < curWord.length(); ++i)
				for (char c = 'a'; c <= 'z'; ++c)
					result.add(curWord.substring(0, i) + String.valueOf(c)
							+ curWord.substring(i + 1));
			for (int i = 0; i <= curWord.length(); ++i)
				for (char c = 'a'; c <= 'z'; ++c)
					result.add(curWord.substring(0, i) + String.valueOf(c)
							+ curWord.substring(i));
			return result;
		}
	}
}