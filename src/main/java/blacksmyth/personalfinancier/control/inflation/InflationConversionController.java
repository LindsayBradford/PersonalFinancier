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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class InflationConversionController implements PropertyChangeListener  {
  
  private static Logger LOG = LogManager.getLogger(InflationConversionController .class);
  
  private InflationConversionModel model;
  
  private PropertyChangeSupport support;
  
  
  public InflationConversionController(InflationConversionModel model) {
    this.model = model;

    this.support = new PropertyChangeSupport(this);
    this.support.addPropertyChangeListener(model);
  }
  
  public void setInitialValue(String initialValueAsString) {
    Money initialValue = model.getInitialValue();
    
    Number valueAsDouble = 0;
    try {
      valueAsDouble = WidgetFactory.DECIMAL_FORMAT.parse(initialValueAsString);
    } catch (ParseException e) {
      LOG.warn("Initial value format {} not parsable", initialValue);
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
    
    LocalDate newInitialDate = LocalDate.now();
    try {
      newInitialDate = LocalDate.parse(initialDateAsString, WidgetFactory.DATE_FORMAT);
    } catch (DateTimeParseException e) {
      LOG.warn("Initial date format {} not parsable", initialDateAsString);
    }
    
    model.setInitialDate(newInitialDate);
  }

  
  public void setConversionValue(String conversionValueAsString) {
    Money conversionValue = model.getConversionValue();
    
    Number valueAsDouble = 0;
    try {
      valueAsDouble = WidgetFactory.DECIMAL_FORMAT.parse(conversionValueAsString);
    } catch (ParseException e) {
      LOG.warn("Conversion value format {} not parsable", conversionValue);
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
    
    LocalDate newConversionDate = LocalDate.now();
    try {
      newConversionDate = LocalDate.parse(conversionDateAsString, WidgetFactory.DATE_FORMAT);
    } catch (DateTimeParseException e) {
      LOG.warn("Conversion date format {} not parsable", conversionDateAsString);
    }
    model.setConversionDate(newConversionDate);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    support.firePropertyChange(evt);
  }
  
  public void addPropertyListener(PropertyChangeListener listener) {
    this.support.addPropertyChangeListener(listener);
  }
}
