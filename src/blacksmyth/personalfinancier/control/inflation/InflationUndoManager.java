package blacksmyth.personalfinancier.control.inflation;

import java.util.Observable;
import java.util.Observer;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class InflationUndoManager extends UndoManager {
  
  //TODO: Refactor this and BudgetUndoManager via DRY.
  
  private static InflationUndoManager instance;

  /**
   * The BudgetUndoManager is observable indirectly by
   * relying on all Observable behaviour, delegated to
   * <tt>observableDelegate</tt>
   */
  private Observable observableDelegate;

  protected InflationUndoManager() {
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
  
  public static InflationUndoManager getInstance() {
    if (instance == null) {
      instance = new InflationUndoManager();
    }
    return instance;
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
