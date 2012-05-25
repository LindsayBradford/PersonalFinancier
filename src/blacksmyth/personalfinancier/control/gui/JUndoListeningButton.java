package blacksmyth.personalfinancier.control.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.undo.UndoManager;

/**
 * An <tt>JButton</tt> that reacts to the current undo/redo state of an UndoManager.
 * Override the <tt>handle*</tt> methods as appropriate to get desired button behaviour.
 * @author linds
 */
public abstract class JUndoListeningButton extends JButton implements Observer {
  
  public JUndoListeningButton() {
    super();
  }
  
  public JUndoListeningButton(Action a) {
    super(a);
  }
  
  public final void update(Observable o, Object arg) {
    // TODO: replace with a reflective assert on arg being an UndoManager.
    if (arg == null) return; 

    handleUndoManagerState(
        (UndoManager) arg
    );
  }
  
  private void handleUndoManagerState(UndoManager manager) {
    if (!manager.canUndo()) {
      handleCantUndoState();
    } else {
      handleCanUndoState();
    }
    
    if (!manager.canRedo()) {
      handleCantRedoState();
    } else {
      handleCanRedoState();
    }
  }

  /**
   * Override this method in subclasses to support
   * how the button reacts to not being able to undo
   * any edits.  The base method does nothing.
   */
  protected void handleCantUndoState() {}

  /**
   * Override this method in subclasses to support
   * how the button reacts to being able to undo
   * edits.  The base method does nothing.
   */

  protected void handleCanUndoState() {}

  /**
   * Override this method in subclasses to support
   * how the button reacts to not being able to redo
   * any edits.  The base method does nothing.
   */

  protected void handleCantRedoState() {}
  
  /**
   * Override this method in subclasses to support
   * how the button reacts to being able to redo
   * edits.  The base method does nothing.
   */
  protected void handleCanRedoState() {}
  
}
