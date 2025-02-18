package blacksmyth.personalfinancier.control;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import blacksmyth.general.ApplicationMessagePresenter;

/**
 * The AbstractUndoManager is observable indirectly by relying on all PropertyChangeListener
 * behaviour, delegated to <tt>support</tt>
 */

@SuppressWarnings("serial")
public abstract class AbstractUndoManager extends UndoManager {

  protected PropertyChangeSupport observableDelegate;

  private static final ApplicationMessagePresenter MSG_PRESENTER = ApplicationMessagePresenter.getInstance();
  
  public AbstractUndoManager() {
    super();
    this.observableDelegate = new PropertyChangeSupport(this);
  }

  public void addObserver(PropertyChangeListener o) {
    this.observableDelegate.addPropertyChangeListener(o);
    // Below: a quick and nasty way to sync observer state with current model state.
    fireUndoableEvent();
  }
  
  public void undo() {
    sendMessage(this.getUndoPresentationName());
    super.undo();
    fireUndoableEvent();
  }

  public void redo() {
    sendMessage(this.getRedoPresentationName());
    super.redo();
    fireUndoableEvent();
  }

  public boolean addEdit(UndoableEdit e) {
    sendMessage(e.getPresentationName());
    boolean result = super.addEdit(e);
    fireUndoableEvent();
    return result;
  }
  
  public void discardAllEdits() {
    sendMessage("Discarding all edits");
    super.discardAllEdits();
    fireUndoableEvent();
  }
  
  public void undoableEditHappened(UndoableEditEvent e) {
    super.undoableEditHappened(e);
    fireUndoableEvent();
  }

  protected abstract void fireUndoableEvent();
  
  protected void sendMessage(String commandMessage) {
    MSG_PRESENTER.setMessage(commandMessage);
  }
}
