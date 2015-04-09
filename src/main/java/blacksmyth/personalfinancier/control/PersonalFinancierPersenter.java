package blacksmyth.personalfinancier.control;

import java.util.Observable;

import blacksmyth.general.RunnableQueueThread;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.view.IPersonalFinancierView;
import blacksmyth.personalfinancier.view.IPersonalFinancierView.Events;

public class PersonalFinancierPersenter implements IPersoanalFinancierPresenter {
  
  private IPersonalFinancierView view;
  
  public PersonalFinancierPersenter(IPersonalFinancierView view) {
    RunnableQueueThread.getInstance().start();
    
    setView(view);
    alignViewWithModel();
  }
  
  @Override
  public void setView(IPersonalFinancierView view) {
    ((Observable) view).addObserver(this);
    this.view = view;
  }
  
  private void alignViewWithModel() {
    view.setBounds(
        PreferencesModel.getInstance().getWindowBounds()
    );
  }
  
  @Override
  public void update(Observable o, Object arg) {
    processEvent((IPersonalFinancierView.Events) arg);
  }

  @Override
  public void processEvent(Events event) {
    switch(event) {
    
      case BoundsChanged:
        processBoundsChanged();
        break;

      case ExitRequested:
        processExitRequest();
        break;
    }
  }
  
  private void processBoundsChanged() {
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
