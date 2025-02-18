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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;
import java.util.prefs.Preferences;

import blacksmyth.personalfinancier.BasePreferences;

/**
 * A Singleton model that manages user preferences. It implements the {@link Observable} interface
 * allowing observers to react to user preference changes. Constructed as a singleton so that observers 
 * can subscribe to updates via an object instance reference to PreferencesModel.
 */
public class ViewPreferences extends BasePreferences {
  private static final Preferences ViewUserPrefs = BaseUserPrefs.node("view");

//  // built by an Aussie for an Aussie. Nuff said.

  private static ViewPreferences instance = null;

  protected ViewPreferences() {
    support = new PropertyChangeSupport(this);
  }
  
  public static ViewPreferences getInstance() {
     if(instance == null) {
        instance = new ViewPreferences();
     }
     return instance;
  }

  private static final int DEFAULT_EVEN_ROW_COLOR = Color.BLACK.getRGB();
  private static final String EVEN_ROW_COLOR_KEY = "EvenRowColor";
  
  public Color getPreferredEvenRowColor() {
    return new Color(
        ViewUserPrefs.getInt(
            EVEN_ROW_COLOR_KEY, 
            DEFAULT_EVEN_ROW_COLOR
        )
    );
  }

  public void setPreferredEvenRowColor(Color color) {
    ViewUserPrefs.putInt(
        EVEN_ROW_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_ODD_ROW_COLOR = Color.DARK_GRAY.darker().darker().darker().getRGB();
  private static final String ODD_ROW_COLOR_KEY = "OddRowColor";
  
  public Color getPreferredOddRowColor() {
    return new Color(
        ViewUserPrefs.getInt(
            ODD_ROW_COLOR_KEY, 
            DEFAULT_ODD_ROW_COLOR
        )
    );
  }

  public void setPreferredOddRowColor(Color color) {
    ViewUserPrefs.putInt(
        ODD_ROW_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_EDITABLE_CELL_COLOR = Color.ORANGE.getRGB();
  private static final String EDITABLE_CELL_COLOR_KEY = "EditableCellColor";
  
  public Color getPreferredEditableCellColor() {
    return new Color(
        ViewUserPrefs.getInt(
            EDITABLE_CELL_COLOR_KEY, 
            DEFAULT_EDITABLE_CELL_COLOR
        )
    );
  }

  public void setPreferredEditableCellColor(Color color) {
    ViewUserPrefs.putInt(
        EDITABLE_CELL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_UNEDITABLE_CELL_COLOR = Color.GRAY.getRGB();
  private static final String UNEDITABLE_CELL_COLOR_KEY = "UnEditableCellColor";
  
  public Color getPreferredUnEditableCellColor() {
    return new Color(
        ViewUserPrefs.getInt(
            UNEDITABLE_CELL_COLOR_KEY, 
            DEFAULT_UNEDITABLE_CELL_COLOR
        )
    );
  }

  public void setPreferredUnEditableCellColor(Color color) {
    ViewUserPrefs.putInt(
        UNEDITABLE_CELL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  
  private static final int DEFAULT_SELECTED_CELL_COLOR = Color.GRAY.darker().getRGB();
  private static final String SELECTED_CELL_COLOR_KEY = "SelectedCellColor";
  
  public Color getPreferredSelectedCellColor() {
    return new Color(
        ViewUserPrefs.getInt(
            SELECTED_CELL_COLOR_KEY, 
            DEFAULT_SELECTED_CELL_COLOR
        )
    );
  }

  public void setPreferredSelectedCellColor(Color color) {
    ViewUserPrefs.putInt(
        SELECTED_CELL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }
  

  private static final int DEFAULT_POSITIVE_CASHFLOW_COLOR = Color.GREEN.getRGB();
  private static final String POSITIVE_CASHFLOW_COLOR_KEY = "PositiveCashflowColor";
  
  public Color getPreferredPositiveCashFlowColor() {
    return new Color(
        ViewUserPrefs.getInt(
            POSITIVE_CASHFLOW_COLOR_KEY, 
            DEFAULT_POSITIVE_CASHFLOW_COLOR
        )
    );
  }

  public void setPreferredPositiveCashFlowColor(Color color) {
    ViewUserPrefs.putInt(
        POSITIVE_CASHFLOW_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  
  private static final int DEFAULT_NEGATIVE_CASHFLOW_COLOR = Color.RED.getRGB();
  private static final String NEGATIVE_CASHFLOW_COLOR_KEY = "NegativeCashflowColor";
  
  public Color getPreferredNegativeCashFlowColor() {
    return new Color(
        ViewUserPrefs.getInt(
            NEGATIVE_CASHFLOW_COLOR_KEY, 
            DEFAULT_NEGATIVE_CASHFLOW_COLOR
        )
    );
  }

  public void setPreferredNegativeCashFlowColor(Color color) {
    ViewUserPrefs.putInt(
        NEGATIVE_CASHFLOW_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  
  private static final int DEFAULT_WINDOW_BOUNDS_X      = Toolkit.getDefaultToolkit().getScreenSize().width/8;
  private static final int DEFAULT_WINDOW_BOUNDS_Y      = Toolkit.getDefaultToolkit().getScreenSize().height/8;
  private static final int DEFAULT_WINDOW_BOUNDS_WIDTH  = Toolkit.getDefaultToolkit().getScreenSize().width/4*3;
  private static final int DEFAULT_WINDOW_BOUNDS_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height/4*3;
  
  private static final String WINDOW_BOUNDS_X      = "WindowBounds-X";
  private static final String WINDOW_BOUNDS_Y      = "WindowBounds-Y";
  private static final String WINDOW_BOUNDS_WIDTH  = "WindowBounds-Width";
  private static final String WINDOW_BOUNDS_HEIGHT = "WindowBounds-Height";

  public Rectangle getWindowBounds() {
    int x      =  ViewUserPrefs.getInt(WINDOW_BOUNDS_X, DEFAULT_WINDOW_BOUNDS_X); 
    int y      =  ViewUserPrefs.getInt(WINDOW_BOUNDS_Y, DEFAULT_WINDOW_BOUNDS_Y); 
    int width  =  ViewUserPrefs.getInt(WINDOW_BOUNDS_WIDTH, DEFAULT_WINDOW_BOUNDS_WIDTH); 
    int height =  ViewUserPrefs.getInt(WINDOW_BOUNDS_HEIGHT, DEFAULT_WINDOW_BOUNDS_HEIGHT);
	
    return new Rectangle(x,y,width,height);
  }
	  
  public void setWindowBounds(Rectangle bounds) {
  	ViewUserPrefs.putInt(
  	  WINDOW_BOUNDS_X,
  	  bounds.x
  	);
  	
  	ViewUserPrefs.putInt(
  	  WINDOW_BOUNDS_Y,
  	  bounds.y
      );
  	
  	ViewUserPrefs.putInt(
  	  WINDOW_BOUNDS_WIDTH,
  	  bounds.width
      );
  	
  	ViewUserPrefs.putInt(
  	  WINDOW_BOUNDS_HEIGHT,
  	  bounds.height
    );
  	
  	this.setChangeAndNotifyObservers();
  }
  
  // TODO: Relocate to Budget View preferences.
  private static final boolean DEFAULT_DERIVED_BUDGET_COLUMNS_VISIBLE = true;
  private static final String DERIVED_BUDGET_COLUMNS_VISIBLE = "DerivedBudgetColumnsVisible";
  
  public boolean getDerivedBudgetColumsVisibility() {
    return ViewUserPrefs.getBoolean(
      DERIVED_BUDGET_COLUMNS_VISIBLE, 
      DEFAULT_DERIVED_BUDGET_COLUMNS_VISIBLE
    );
  }
  
  public void toggleDerivedBudgetColumsVisibility() {
  boolean currentValue = this.getDerivedBudgetColumsVisibility();
    ViewUserPrefs.putBoolean(
      DERIVED_BUDGET_COLUMNS_VISIBLE,
        !currentValue
    );
    this.setChangeAndNotifyObservers();
  }

  // TODO: Relocate to Budget View preferences.
  private static final boolean DEFAULT_BUDGET_ITEM_BUTTONS_VISIBLE = true;
  private static final String BUDGET_ITEM_BUTTONS_VISIBLE = "BudgetItemButtonsVisible";
  
  public boolean getBudgetItemButtonsVisibility() {
    return ViewUserPrefs.getBoolean(
        BUDGET_ITEM_BUTTONS_VISIBLE, 
        DEFAULT_BUDGET_ITEM_BUTTONS_VISIBLE
    );
  }
  
  public void toggleBudgetItemButtonsVisibility() {
  boolean currentValue = this.getBudgetItemButtonsVisibility();
    ViewUserPrefs.putBoolean(
        BUDGET_ITEM_BUTTONS_VISIBLE,
        !currentValue
    );
    this.setChangeAndNotifyObservers();
  }
  
  // TODO: Relocate to Budget View preferences.
  private static final int DEFAULT_BUDGET_FREQUENCY_CELL_COLOR = Color.GRAY.brighter().brighter().getRGB();
  private static final String BUDGET_FREQUENCY_CELLL_COLOR_KEY = "BudgetFrequencyCellColor";
  
  public Color getPreferredBudgetFrequencyCellColor() {
    return new Color(
        ViewUserPrefs.getInt(
            BUDGET_FREQUENCY_CELLL_COLOR_KEY, 
            DEFAULT_BUDGET_FREQUENCY_CELL_COLOR
        )
    );
  }

  public void setPreferredBudgetFrequencyCellColor(Color color) {
    ViewUserPrefs.putInt(
        BUDGET_FREQUENCY_CELLL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }
  
  public void addObserver(PropertyChangeListener listener) {
      support.addPropertyChangeListener(listener);
  }
}
