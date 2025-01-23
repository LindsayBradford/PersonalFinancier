/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */


package blacksmyth.personalfinancier;

import javax.swing.UIManager;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.core.config.Configurator;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;

import blacksmyth.personalfinancier.control.PersonalFinancierPersenter;
import blacksmyth.personalfinancier.model.PersonalFinancierModel;
import blacksmyth.personalfinancier.view.PersonalFinancierView;

/**
 * The bootstrap "main" class for the PersonalFinancier.  It 
 * sets the application's look & feel, but delegates construction
 * of the application GUI dependency graph to the factory 
 * @author linds
 */
public final class PersonalFinancier {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;
  private static Logger LOG = LogManager.getLogger(PersonalFinancier.class);

  /**
   * Bootstrap method for the PersonalFinancier.
   * @param args
   */
  public static void main(String[] args) {

    Configurator.initialize("blacksmyth.personalfinancier", "resources/log4j2.xml");

    LOG.info("Initialising view.");

    
    try {
      LOG.debug("Setting look and feel.");
      UIManager.setLookAndFeel(
          new HiFiLookAndFeel()
        );
    } catch (Exception e) {
      LOG.error(e);
    }

    createView().display();
  }

  
  private static PersonalFinancierView createView() {
    
    final PersonalFinancierView view = new PersonalFinancierView();
    
    new PersonalFinancierPersenter(
        view, 
        new PersonalFinancierModel()
    );
    
    view.addComponentView(
        BudgetUIFactory.createBudgetComponent(view)
    );

    view.addComponentView(
        InflationUIFactory.createInflationComponent(view)
      );
    
    return view;
  }
}

