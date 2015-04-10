package blacksmyth.personalfinancier.control;

import java.util.Observable;

import blacksmyth.general.RunnableQueueThread;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.view.IPersonalFinancierView;
import blacksmyth.personalfinancier.view.IPersonalFinancierView.Events;

public class PersonalFinancierPersenter implements IPersoanalFinancierPresenter {
  
  public PersonalFinancierPersenter(IPersonalFinancierView view) {
    RunnableQueueThread.getInstance().start();
    
    addView(view);
    alignViewWithModel(view);
  }
  
  @Override
  public void addView(IPersonalFinancierView view) {
    ((Observable) view).addObserver(this);
  }
  
  private void alignViewWithModel(IPersonalFinancierView view) {
    view.setBounds(
        PreferencesModel.getInstance().getWindowBounds()
    );
  }
  
  @Override
  public void update(Observable o, Object arg) {
    processEvent(
        (IPersonalFinancierView) o,
        (IPersonalFinancierView.Events) arg);
  }

  @Override
  public void processEvent(IPersonalFinancierView view, Events event) {
    switch(event) {
    
      case BoundsChanged:
        processBoundsChanged(view);
        break;

      case ExitRequested:
        processExitRequest();
        break;
    }
  }
  
  private void processBoundsChanged(IPersonalFinancierView view) {
    PreferencesModel.getInstance().setWindowBounds(
        view.getBounds()
    );
  }
  
  private void processExitRequest() {
    UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
    //UIComponents.budgetFileController.save();
    System.exit(0);
  }

}
