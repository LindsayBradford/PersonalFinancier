/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.model;

import java.awt.Color;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Observable;
import java.util.prefs.Preferences;

/**
 * A Singleton model that manages user preferences. It implements the {@link Observable} interface
 * allowing observers to react to user preference changes.
 * @author linds
 *
 */
public class PreferencesModel extends Observable {
  private static final String BASENODE = "blacksmyth/personalfinancier/prefs";
  private Preferences prefs = Preferences.userRoot().node(BASENODE);

  // built by an Aussie for an Aussie. Nuff said.
  private static final String DEFAULT_CURRENCY_CODE = "AUD";
  private static final String CURRENCY_CODE_KEY = "CurrencyCode";
  
  private static PreferencesModel instance = null;
  
  private static MathContext preferredMathContext;
  
  protected PreferencesModel() {
     // Exists only to defeat instantiation.
  }
  
  public static PreferencesModel getInstance() {
     if(instance == null) {
        instance = new PreferencesModel();
     }
     return instance;
  }

  private void setChangeAndNotifyObservers() {
    this.setChanged();
    this.notifyObservers();
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
    this.setChangeAndNotifyObservers();
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
    updatePreferredMathContext();
    this.setChangeAndNotifyObservers();
  }

  
  // this rounding mode introduces the least bias.
  private static final int DEFAULT_PRECISION = 6;
  private static final String PRECISION_KEY = "Precision";
  
  public int getPreferredPrecision() {
      return this.prefs.getInt(
          PRECISION_KEY, 
          DEFAULT_PRECISION
      );
  }
  
  public void setPreferredPrecision(int precision) {
    this.prefs.putInt(
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
    this.prefs.put(
        BUDGET_ACCOUNT_KEY,
        budgetAccount.getNickname()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final String DEFAULT_CASHFLOW_FREQUENCY = CashFlowFrequency.Daily.toString();
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
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_EVEN_ROW_COLOR = Color.BLACK.getRGB();
  private static final String EVEN_ROW_COLOR_KEY = "EvenRowColor";
  
  public Color getPreferredEvenRowColor() {
    return new Color(
        this.prefs.getInt(
            EVEN_ROW_COLOR_KEY, 
            DEFAULT_EVEN_ROW_COLOR
        )
    );
  }

  public void setPreferredEvenRowColor(Color color) {
    this.prefs.putInt(
        EVEN_ROW_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_ODD_ROW_COLOR = Color.DARK_GRAY.darker().getRGB();
  private static final String ODD_ROW_COLOR_KEY = "OddRowColor";
  
  public Color getPreferredOddRowColor() {
    return new Color(
        this.prefs.getInt(
            ODD_ROW_COLOR_KEY, 
            DEFAULT_ODD_ROW_COLOR
        )
    );
  }

  public void setPreferredOddRowColor(Color color) {
    this.prefs.putInt(
        ODD_ROW_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_EDITABLE_CELL_COLOR = Color.ORANGE.getRGB();
  private static final String EDITABLE_CELL_COLOR_KEY = "EditableCellColor";
  
  public Color getPreferredEditableCellColor() {
    return new Color(
        this.prefs.getInt(
            EDITABLE_CELL_COLOR_KEY, 
            DEFAULT_EDITABLE_CELL_COLOR
        )
    );
  }

  public void setPreferredEditableCellColor(Color color) {
    this.prefs.putInt(
        EDITABLE_CELL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_UNEDITABLE_CELL_COLOR = Color.GRAY.getRGB();
  private static final String UNEDITABLE_CELL_COLOR_KEY = "UnEditableCellColor";
  
  public Color getPreferredUnEditableCellColor() {
    return new Color(
        this.prefs.getInt(
            UNEDITABLE_CELL_COLOR_KEY, 
            DEFAULT_UNEDITABLE_CELL_COLOR
        )
    );
  }

  public void setPreferredUnEditableCellColor(Color color) {
    this.prefs.putInt(
        UNEDITABLE_CELL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final int DEFAULT_BUDGET_FREQUENCY_CELL_COLOR = Color.GRAY.brighter().brighter().getRGB();
  private static final String BUDGET_FREQUENCY_CELLL_COLOR_KEY = "BudgetFrequencyCellColor";
  
  public Color getPreferredBudgetFrequencyCellColor() {
    return new Color(
        this.prefs.getInt(
            BUDGET_FREQUENCY_CELLL_COLOR_KEY, 
            DEFAULT_BUDGET_FREQUENCY_CELL_COLOR
        )
    );
  }

  public void setPreferredBudgetFrequencyCellColor(Color color) {
    this.prefs.putInt(
        BUDGET_FREQUENCY_CELLL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }
  
  
  private static final int DEFAULT_SELECTED_CELL_COLOR = Color.GRAY.darker().getRGB();
  private static final String SELECTED_CELL_COLOR_KEY = "SelectedCellColor";
  
  public Color getPreferredSelectedCellColor() {
    return new Color(
        this.prefs.getInt(
            UNEDITABLE_CELL_COLOR_KEY, 
            DEFAULT_SELECTED_CELL_COLOR
        )
    );
  }

  public void setPreferredSelectedCellColor(Color color) {
    this.prefs.putInt(
        SELECTED_CELL_COLOR_KEY,
        color.getRGB()
    );
    this.setChangeAndNotifyObservers();
  }

  private static final String DEFAULT_LAST_USED_FILE_PATH = ".";
  private static final String LAST_USED_FILE_PATH = "LastUsedFilePath";

  public String getLastUsedFilePath() {
    return this.prefs.get(LAST_USED_FILE_PATH, DEFAULT_LAST_USED_FILE_PATH);
  }
  
  public void setLastUsedFilePath(String lastUsedPath) {
    this.prefs.put(
        LAST_USED_FILE_PATH,
        lastUsedPath
    );
    this.setChangeAndNotifyObservers();
  }

  private static final boolean DEFAULT_DERIVED_BUDGET_COLUMNS_VISIBLE = true;
  private static final String DERIVED_BUDGET_COLUMNS_VISIBLE = "DerivedBudgetColumnsVisible";
  
  public boolean getDerivedBudgetColumsVisibility() {
    return this.prefs.getBoolean(
      DERIVED_BUDGET_COLUMNS_VISIBLE, 
      DEFAULT_DERIVED_BUDGET_COLUMNS_VISIBLE
    );
  }
  
  public void toggleDerivedBudgetColumsVisibility() {
	boolean currentValue = this.getDerivedBudgetColumsVisibility();
    this.prefs.putBoolean(
    	DERIVED_BUDGET_COLUMNS_VISIBLE,
        !currentValue
    );
    this.setChangeAndNotifyObservers();
  }
}
