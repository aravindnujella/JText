package JText;

import javax.swing.JTextArea;
import javax.swing.event.*;

//import javax.swing.event.EventListener;
public class Caret{
	protected int caretPositionStart;
	protected int caretPositionEnd;
//	protected int magicPositionX;
//	protected int magicPositionY;
	private CaretListenerLabel caret;
	public Caret(JTextArea textArea){
		caret = new CaretListenerLabel();
		textArea.addCaretListener(caret);
	}
	private class CaretListenerLabel implements CaretListener{
		public CaretListenerLabel(){
			super();
		}

		public void caretUpdate(CaretEvent e){
			caretPositionStart = e.getDot();
			caretPositionEnd = e.getMark();
		}
	}
}