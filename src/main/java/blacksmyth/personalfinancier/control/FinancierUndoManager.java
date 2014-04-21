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

import java.util.Observable;
import java.util.Observer;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class FinancierUndoManager extends UndoManager {
  
  /**
   * The FinancierUndoManager is observable indirectly by
   * relying on all Observable behaviour, delegated to
   * <tt>observableDelegate</tt>
   */
  private Observable observableDelegate;

  public FinancierUndoManager() {
    super();
    this.observableDelegate = new Observable() {
      @Override
      public void notifyObservers(Object arg) {
        // Small convenience tweak, forcing a notify
        // on all observers every time this method is invoked.
        this.setChanged();
        super.notifyObservers(arg);
      }
    };
  }
  
  public void addObserver(Observer o) {
    this.observableDelegate.addObserver(o);
    //Below: a quick and nasty way to sync observer state with current model state.
    this.observableDelegate.notifyObservers(this);
  }
  
  public void undo() {
    super.undo();
    this.observableDelegate.notifyObservers(this);
  }
  
  public void redo() {
    super.redo();
    this.observableDelegate.notifyObservers(this);
  }
  
  public boolean addEdit(UndoableEdit e) {
    boolean result = super.addEdit(e);
    this.observableDelegate.notifyObservers(this);
    return result;
  }
  
  public void discardAllEdits() {
    super.discardAllEdits();
    this.observableDelegate.notifyObservers(this);
  }
  
  public void undoableEditHappened(UndoableEditEvent e) {
    super.undoableEditHappened(e);
    this.observableDelegate.notifyObservers(this);
  }
}
