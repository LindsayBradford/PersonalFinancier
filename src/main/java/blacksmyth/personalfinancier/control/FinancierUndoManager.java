/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

@SuppressWarnings("serial")
public class FinancierUndoManager extends UndoManager {

  /**
   * The FinancierUndoManager is observable indirectly by relying on all
   * Observable behaviour, delegated to <tt>observableDelegate</tt>
   */
  private PropertyChangeSupport observableDelegate;

  public FinancierUndoManager() {
    super();
    this.observableDelegate = new PropertyChangeSupport(this);
  }

  public void addObserver(PropertyChangeListener o) {
    this.observableDelegate.addPropertyChangeListener(o);
    // Below: a quick and nasty way to sync observer state with current model state.
    fireUndoableEvent();
  }

  public void undo() {
    super.undo();
    fireUndoableEvent();
  }

  public void redo() {
    super.redo();
    fireUndoableEvent();
  }

  public boolean addEdit(UndoableEdit e) {
    boolean result = super.addEdit(e);
    fireUndoableEvent();
    return result;
  }

  public void discardAllEdits() {
    super.discardAllEdits();
    fireUndoableEvent();
  }

  public void undoableEditHappened(UndoableEditEvent e) {
    super.undoableEditHappened(e);
    fireUndoableEvent();
  }
  
  private void fireUndoableEvent() {
    this.observableDelegate.firePropertyChange("Undoable Financier Change",null,null);
  }

}
