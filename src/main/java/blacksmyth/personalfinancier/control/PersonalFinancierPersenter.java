/**
 * Copyright (c) 2015, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */
package blacksmyth.personalfinancier.control;

import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blacksmyth.personalfinancier.model.IPersomalFinancierModel;
import blacksmyth.personalfinancier.view.IPersonalFinancierView;
import blacksmyth.personalfinancier.view.IPersonalFinancierView.Events;

public class PersonalFinancierPersenter implements IPersoanalFinancierPresenter {

  private static final Logger LOG = LogManager.getLogger(PersonalFinancierPersenter.class);

  @SuppressWarnings("unused")
  private IPersomalFinancierModel model;

  public PersonalFinancierPersenter(IPersonalFinancierView view, IPersomalFinancierModel model) {
    addView(view);
    setModel(model);
  }

  @Override
  public void addView(IPersonalFinancierView view) {
    ((Observable) view).addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    processEvent((IPersonalFinancierView) o, (IPersonalFinancierView.Events) arg);
  }

  @Override
  public void processEvent(IPersonalFinancierView view, Events event) {

    LOG.info("Processing View Event [{}]", event.name());

    switch (event) {
    case ExitRequested:
      processExitRequest();
      break;
    }
  }

  private void processExitRequest() {
    // TODO: Move this out to the budget presenter.
    // UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
    // budgetFileController.save();
    LOG.info("Exiting Application");
    System.exit(0);
  }

  @Override
  public void setModel(IPersomalFinancierModel model) {
    this.model = model;
  }
}
