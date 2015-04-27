package JText;

import java.awt.Event;
import java.awt.event.KeyEvent;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class KeyBindings{
	public KeyBindings(InputMap ip,Actions actionsManager){
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK);
        ip.put(key, actionsManager.undoAction);

        key = KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK);
        ip.put(key, actionsManager.redoAction);

        key = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
        ip.put(key, actionsManager.saveAction);

        key = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK);
        ip.put(key, actionsManager.openAction);
        /*A naive auto complete trigger*/
        key = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, Event.CTRL_MASK);
        ip.put(key, actionsManager.autoCompleteAction);	

        key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
        ip.put(key, actionsManager.findAction); 
        
        key = KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK);
        ip.put(key,actionsManager.replaceAction); 
        
        key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, Event.CTRL_MASK);
        ip.put(key,actionsManager.hideAllAction); 
        
        key = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.ALT_MASK);
        ip.put(key,actionsManager.spellCheckAction); 
	}
}