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
import javax.swing.LookAndFeel;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.core.config.Configurator;

import blacksmyth.general.ManifestBridge;
import blacksmyth.personalfinancier.control.PersonalFinancierPersenter;
import blacksmyth.personalfinancier.model.PersonalFinancierModel;
import blacksmyth.personalfinancier.view.PersonalFinancierView;

/**
 * The bootstrap "main" class for the PersonalFinancier.  It 
 * sets the application's look & feel, but delegates construction
 * of the application GUI dependency graph to specialist factories. 
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
    
    logNameAndVersion();
    
    try {
      LookAndFeel thisLookAndFeel = new HiFiLookAndFeel();
      LOG.debug("Setting Swing look and feel to [{}]",thisLookAndFeel.getID());
      UIManager.setLookAndFeel(thisLookAndFeel);
    } catch (Exception e) {
      LOG.warn(e);
    }

    createView().display();
  }
  
  private static void logNameAndVersion() {
    String title = ManifestBridge.getInstance().getAttribute("Implementation-Title");
    String version = ManifestBridge.getInstance().getAttribute("Implementation-Version");
    
    LOG.info("Bootstrapping [{}]",title + " version " + version);
  }
  
  private static PersonalFinancierView createView() {
    
    final PersonalFinancierView pfView = new PersonalFinancierView();
    
    new PersonalFinancierPersenter(
        pfView, 
        new PersonalFinancierModel()
    );
    
    pfView.addComponentView(
        BudgetUIFactory.createComponent(pfView)
    );

    pfView.addComponentView(
        InflationUIFactory.createComponent(pfView)
      );
    
    return pfView;
  }
}

