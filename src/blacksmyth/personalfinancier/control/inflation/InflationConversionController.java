package blacksmyth.personalfinancier.control.inflation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class InflationConversionController  {
  
  private InflationConversionModel model;
  
  public InflationConversionController(InflationConversionModel model) {
    this.model = model;
  }
  
  public void setInitialValue(String initialValueAsString) {
    Money initialValue = model.getInitialValue();
    
    BigDecimal newInitialValue= BigDecimal.valueOf(
        Double.valueOf(initialValueAsString)
    );
    
    initialValue.setTotal(
        newInitialValue
    );
    
    model.setInitialValue(initialValue);
  }

  public void setInitialDate(String initialDateAsString) {
    
    GregorianCalendar newInitialDate;
    try {
      newInitialDate = new GregorianCalendar();
      newInitialDate.setTime(
        WidgetFactory.DATE_FORMAT.parse(initialDateAsString)
      );
    } catch (ParseException e) {
      newInitialDate= new GregorianCalendar();
      newInitialDate.setTime(
          new Date()
      );
    }
    
    model.setInitialDate(newInitialDate);
  }

  
  public void setConversionValue(String conversionValueAsString) {
    Money conversionValue = model.getConversionValue();
    
    BigDecimal newConversionValue= BigDecimal.valueOf(
        Double.valueOf(conversionValueAsString)
    );
    
    conversionValue.setTotal(
        newConversionValue
    );
    
    model.setConversionValue(conversionValue);
  }

  public void setConversionDate(String conversionDateAsString) {
    
    GregorianCalendar newConversionDate;
    try {
      newConversionDate = new GregorianCalendar();
      newConversionDate.setTime(
        WidgetFactory.DATE_FORMAT.parse(conversionDateAsString)
      );
    } catch (ParseException e) {
      newConversionDate= new GregorianCalendar();
      newConversionDate.setTime(
          new Date()
      );
    }
    model.setConversionDate(newConversionDate);
  }
}
