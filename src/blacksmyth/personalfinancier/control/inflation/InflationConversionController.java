/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

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
    
    Number valueAsDouble = 0;
    try {
      valueAsDouble = WidgetFactory.DECIMAL_FORMAT.parse(initialValueAsString);
    } catch (ParseException e) {
      // deliberately do nothing.
    }
    
    BigDecimal newInitialValue = BigDecimal.valueOf(
        valueAsDouble.doubleValue()
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
    
    Number valueAsDouble = 0;
    try {
      valueAsDouble = WidgetFactory.DECIMAL_FORMAT.parse(conversionValueAsString);
    } catch (ParseException e) {
      // deliberately do nothing.
    }
    
    BigDecimal newValue = BigDecimal.valueOf(
        valueAsDouble.doubleValue()
    );
    
    conversionValue.setTotal(
        newValue
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
