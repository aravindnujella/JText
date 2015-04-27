package JText;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Style {
	private JScrollPane scroll;
	protected JTextArea textArea;
	
	public Style(JFrame f) {
		textArea = new JTextArea();
		textArea.setFont(new Font("SansSerif",0,15));
		textArea.setTabSize(2);
		scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea.setLineWrap(true);
		textArea.setCaretPosition(0);
		f.add(scroll,BorderLayout.CENTER);
	}
}