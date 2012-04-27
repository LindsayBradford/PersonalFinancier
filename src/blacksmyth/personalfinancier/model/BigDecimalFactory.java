package blacksmyth.personalfinancier.model;

import java.math.BigDecimal;

public class BigDecimalFactory {
    public static BigDecimal create(int value) {
      return new BigDecimal(
          value, 
          PreferencesModel.getInstance().getPreferredMathContext()
      );
    }
    
    public static BigDecimal create(double value) {
      return new BigDecimal(
          value, 
          PreferencesModel.getInstance().getPreferredMathContext()
      );
    }
    
}
