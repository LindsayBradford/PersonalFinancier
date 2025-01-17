/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Observable;
import java.util.prefs.Preferences;

import blacksmyth.personalfinancier.BasePreferences;

/**
 * A Singleton model that manages user preferences. It implements the {@link Observable} interface
 * allowing observers to react to user preference changes. Constructed as a singleton so that observers 
 * can subscribe to updates via an object instance reference to PreferencesModel.
 */
public class ModelPreferences extends BasePreferences {

  private static final Preferences ModelUserPrefs = BaseUserPrefs.node("model");
  
  // built by an Aussie for an Aussie. Nuff said.
  private static final String DEFAULT_CURRENCY_CODE = "AUD";
  private static final String CURRENCY_CODE_KEY = "CurrencyCode";
  
  private static ModelPreferences instance = null;
  
  private static MathContext preferredMathContext;
  
  protected ModelPreferences() {
    support = new PropertyChangeSupport(this);
  }
  
  public static ModelPreferences getInstance() {
     if(instance == null) {
        instance = new ModelPreferences();
     }
     return instance;
  }
  
  public Currency getPreferredCurrency() {
    return Currency.getInstance(
        getPreferredCurrencyCode()    
    );
  }
  
  public void setPreferredCurrency(Currency currency) {
    ModelUserPrefs.put(
        CURRENCY_CODE_KEY, 
        currency.getCurrencyCode()
    );
    this.setChangeAndNotifyObservers();
  }
  
  private String getPreferredCurrencyCode() {
    return ModelUserPrefs.get(
        CURRENCY_CODE_KEY, 
        DEFAULT_CURRENCY_CODE
    );
  }

  // this rounding mode introduces the least bias.
  private static final String DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN.toString();
  private static final String ROUNDING_MODE_KEY = "RoundingMode";
  
  public RoundingMode getPreferredRoundingMode() {
    return RoundingMode.valueOf(
        ModelUserPrefs.get(
            ROUNDING_MODE_KEY, 
            DEFAULT_ROUNDING_MODE
        )
    );
  }
  
  public void setPreferredRoundingMode(RoundingMode roundingMode) {
    ModelUserPrefs.put(
        ROUNDING_MODE_KEY,
        roundingMode.toString()
    );
    updatePreferredMathContext();
    this.setChangeAndNotifyObservers();
  }

  
  // this rounding mode introduces the least bias.
  private static final int DEFAULT_PRECISION = 6;
  private static final String PRECISION_KEY = "Precision";
  
  public int getPreferredPrecision() {
      return ModelUserPrefs.getInt(
          PRECISION_KEY, 
          DEFAULT_PRECISION
      );
  }
  
  public void setPreferredPrecision(int precision) {
    ModelUserPrefs.putInt(
        PRECISION_KEY,
        precision
    );
    updatePreferredMathContext();
    this.setChangeAndNotifyObservers();
  }

  private void updatePreferredMathContext() {
    preferredMathContext = new MathContext(
        getPreferredPrecision(), 
        getPreferredRoundingMode()
    );
  }
  
  public MathContext getPreferredMathContext() {
    if (preferredMathContext == null) {
      updatePreferredMathContext();
    }
    return preferredMathContext;
  }

  // TODO: Build out Account support.
  @SuppressWarnings("unused")
  private static final String DEFAULT_BUDGET_ACCOUNT = "Default";
  private static final String BUDGET_ACCOUNT_KEY = "BudgetAccount";
  
  public Account getPreferredBudgetAccount() {
    // TODO: introduce account model.
    return Account.DEFAULT;
//    return this.prefs.get(
//       BUDGET_ACCOUNT_KEY, 
//       DEFAULT_BUDGET_ACCOUNT
//    );
  }
  
  public void setPreferredBudgetAccount(Account budgetAccount) {
    ModelUserPrefs.put(
        BUDGET_ACCOUNT_KEY,
        budgetAccount.getNickname()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final String DEFAULT_CASHFLOW_FREQUENCY = CashFlowFrequency.Daily.toString();
  private static final String CASHFLOW_FREQUENCY_KEY = "CashflowFrequency";
  
  public CashFlowFrequency getPreferredCashflowFrequency() {
    return CashFlowFrequency.valueOf(
        ModelUserPrefs.get(
            CASHFLOW_FREQUENCY_KEY, 
            DEFAULT_CASHFLOW_FREQUENCY
        )
    );
  }
  
  public void setPreferredCashflowFrequency(CashFlowFrequency frequency) {
    ModelUserPrefs.put(
        CASHFLOW_FREQUENCY_KEY,
        frequency.toString()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final String DEFAULT_LAST_USED_BUDGET_FILE_PATH = ".";
  public static final String LAST_USED_BUDGET_FILE_PATH = "LastUsedBudgetFilePath";

  public String getLastUsedBudgetFilePath() {
    return ModelUserPrefs.get(
        LAST_USED_BUDGET_FILE_PATH, 
        DEFAULT_LAST_USED_BUDGET_FILE_PATH
    );
  }
  
  public void setLastUsedBudgetFilePath(String lastUsedPath) {
    ModelUserPrefs.put(
        LAST_USED_BUDGET_FILE_PATH,
        lastUsedPath
    );
    this.setChangeAndNotifyObservers();
  }

  private static final String DEFAULT_LAST_USED_INFLATION_FILE_PATH = ".";
  public  static final String LAST_USED_INFLATION_FILE_PATH = "LastUsedInflationFilePath";

  public String getLastUsedInflationFilePath() {
    return ModelUserPrefs.get(
        LAST_USED_INFLATION_FILE_PATH, 
        DEFAULT_LAST_USED_INFLATION_FILE_PATH
    );
  }
  
  public void setLastUsedInflationFilePath(String lastUsedPath) {
    ModelUserPrefs.put(
        LAST_USED_INFLATION_FILE_PATH,
        lastUsedPath
    );
    this.setChangeAndNotifyObservers();
  }
  
  public void addObserver(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

}
