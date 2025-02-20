/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.undo.UndoManager;

/**
 * An <tt>JButton</tt> that reacts to the current undo/redo state of an
 * UndoManager. Override the <tt>handle*</tt> methods as appropriate to get
 * desired button behaviour.
 * 
 * @author linds
 */

@SuppressWarnings("serial")
public abstract class JUndoListeningButton extends JButton implements PropertyChangeListener {

  public JUndoListeningButton() {
    super();
  }

  public JUndoListeningButton(Action a) {
    super(a);
  }
  
  public void propertyChange(PropertyChangeEvent evt) {
    // TODO: replace with a reflective assert on arg being an UndoManager.
    if (evt.getSource() == null)
      return;

    handleUndoManagerState((UndoManager) evt.getSource());
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
   * Override this method in subclasses to support how the button reacts to not
   * being able to undo any edits. The base method does nothing.
   */
  protected void handleCantUndoState() {
  }

  /**
   * Override this method in subclasses to support how the button reacts to being
   * able to undo edits. The base method does nothing.
   */

  protected void handleCanUndoState() {
  }

  /**
   * Override this method in subclasses to support how the button reacts to not
   * being able to redo any edits. The base method does nothing.
   */

  protected void handleCantRedoState() {
  }

  /**
   * Override this method in subclasses to support how the button reacts to being
   * able to redo edits. The base method does nothing.
   */
  protected void handleCanRedoState() {
  }

}
