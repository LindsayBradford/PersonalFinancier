/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model;

import java.math.RoundingMode;
import java.util.Currency;
import java.util.Observable;
import java.util.prefs.Preferences;

public class PreferencesModel extends Observable {
  private static final String BASENODE = "blacksmyth/personalfinancier/prefs";
  private Preferences prefs = Preferences.userRoot().node(BASENODE);

  // built by an Aussie for an Aussie. Nuff said.
  private static final String DEFAULT_CURRENCY_CODE = "AUD";
  private static final String CURRENCY_CODE_KEY = "CurrencyCode";
  
  private static PreferencesModel instance = null;
  
  protected PreferencesModel() {
     // Exists only to defeat instantiation.
  }
  
  public static PreferencesModel getInstance() {
     if(instance == null) {
        instance = new PreferencesModel();
     }
     return instance;
  }
  
  public Currency getPreferredCurrency() {
    return Currency.getInstance(
        getPreferredCurrencyCode()    
    );
  }
  
  public void setPreferredCurrency(Currency currency) {
    this.prefs.put(
        CURRENCY_CODE_KEY, 
        currency.getCurrencyCode()
    );
    this.notifyObservers();
  }
  
  private String getPreferredCurrencyCode() {
    return prefs.get(
        CURRENCY_CODE_KEY, 
        DEFAULT_CURRENCY_CODE
    );
  }

  // this rounding mode introduces the least bias.
  private static final String DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN.toString();
  private static final String ROUNDING_MODE_KEY = "RoundingMode";
  
  public RoundingMode getPreferredRoundingMode() {
    return RoundingMode.valueOf(
        this.prefs.get(
            ROUNDING_MODE_KEY, 
            DEFAULT_ROUNDING_MODE
        )
    );
  }
  
  public void setPreferredRoundingMode(RoundingMode roundingMode) {
    this.prefs.put(
        ROUNDING_MODE_KEY,
        roundingMode.toString()
    );
    this.notifyObservers();
  }

  // TODO: Switch from String to Account objects
  private static final String DEFAULT_BUDGET_ACCOUNT = "Default";
  private static final String BUDGET_ACCOUNT_KEY = "BudgetAccount";
  
  public String getPreferredBudgetAccount() {
    return this.prefs.get(
       BUDGET_ACCOUNT_KEY, 
       DEFAULT_BUDGET_ACCOUNT
    );
  }
  
  public void setPreferredBudgetAccount(String budgetAccount) {
    this.prefs.put(
        BUDGET_ACCOUNT_KEY,
        budgetAccount
    );
    this.notifyObservers();
  }

  private static final String DEFAULT_CASHFLOW_FREQUENCY = CashFlowFrequency.DAILY.toString();
  private static final String CASHFLOW_FREQUENCY_KEY = "CashflowFrequency";
  
  public CashFlowFrequency getPreferredCashflowFrequency() {
    return CashFlowFrequency.valueOf(
        this.prefs.get(
            CASHFLOW_FREQUENCY_KEY, 
            DEFAULT_CASHFLOW_FREQUENCY
        )
    );
  }
  
  public void setPreferredCashflowFrequency(CashFlowFrequency frequency) {
    this.prefs.put(
        CASHFLOW_FREQUENCY_KEY,
        frequency.toString()
    );
    this.notifyObservers();
  }

  
}
