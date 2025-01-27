/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model.inflation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;

public class InflationConversionModel  implements PropertyChangeListener {
  
  private Money initialValue;
  private LocalDate initialDate;

  private Money conversionValue;
  private LocalDate conversionDate;
  
  private double inflationOverPeriod;
  private double inflationPerAnnum;
  
  private InflationProvider inflationProvider;
  
  private PropertyChangeSupport support;

  
  public InflationConversionModel(InflationProvider provider) {
    this.support = new PropertyChangeSupport(this);
    this.setInflationProvider(provider);
    
    this.initialValue = MoneyFactory.createAmount(0);
    this.initialDate = inflationProvider.getEarliestDate();
    
    this.conversionValue = MoneyFactory.createAmount(0);
    this.conversionDate = inflationProvider.getLatestDate();

    this.inflationOverPeriod = 0;
    this.inflationPerAnnum = 0;

    this.changeAndNotifyObservers();
  }
  
  public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
    this.support.firePropertyChange("Inflation Conversion Event", null, null);
  }

  public void setInflationProvider(InflationProvider inflationProvider) {
    this.inflationProvider = inflationProvider;

    inflationProvider.addListener(this);
  }

  public Money getInitialValue() {
    return initialValue;
  }

  public void setInitialValue(Money initialValue) {
    this.initialValue = initialValue;
    
    updateConversionValue();
    
    changeAndNotifyObservers();
  }

  public LocalDate getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(LocalDate initialDate) {
    this.initialDate = initialDate;

    updateConversionValue();

    changeAndNotifyObservers();
  }

  private void updateConversionValue() {
    this.conversionValue = 
        inflationProvider.computeComparisonValue(
          this.initialValue,
          this.initialDate,
          this.conversionDate
        );
  }
  
  public Money getConversionValue() {
    return conversionValue;
  }

  public void setConversionValue(Money conversionValue) {
    this.conversionValue = conversionValue;
    
    updateInitialValue();
    
    changeAndNotifyObservers();
  }

  public LocalDate getConversionDate() {
    return conversionDate;
  }

  public void setConversionDate(LocalDate conversionDate) {
    this.conversionDate = conversionDate;

    updateInitialValue();

    changeAndNotifyObservers();
  }
  
  private void updateInitialValue() {
    this.initialValue = 
        inflationProvider.computeComparisonValue(
          this.conversionValue,
          this.conversionDate,
          this.initialDate
        );
  }

  public double getInflationOverPeriod() {
    return inflationOverPeriod;
  }

  private void setInflationOverPeriod(double inflationOverPeriod) {
    this.inflationOverPeriod = inflationOverPeriod;
  }

  public double getInflationPerAnnum() {
    return inflationPerAnnum;
  }

  private void setInflationPerAnnum(double inflationPerAnnum) {
    this.inflationPerAnnum = inflationPerAnnum;
  }

  
  private void updateDerivedValues() {
    updateInflationOverPeriod();
    updateInflationPerAnnum();
  }
 
  private void updateInflationOverPeriod() {
    this.setInflationOverPeriod(
        inflationProvider.getInflationForDateRange(
            this.getInitialDate(), 
            this.getConversionDate()
        )
    );
  }

  private void updateInflationPerAnnum() {
    this.setInflationPerAnnum(
        inflationProvider.getInflationPerAnnum(
            this.getInitialDate(), 
            this.getConversionDate()
        )
    );
  }
  
  public LocalDate getEarliestDate() {
    return inflationProvider.getEarliestDate();
  }
  public LocalDate getLatestLatestDate() {
    return inflationProvider.getLatestDate();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    switch(evt.getPropertyName()) {
      case "Inflation Model Loaded":
        this.setInitialDate(inflationProvider.getEarliestDate());
        this.setConversionDate(inflationProvider.getLatestDate());
        this.setInitialValue(MoneyFactory.createAmount(1));
        updateConversionValue();
      case "Inflation Model Change":
        updateConversionValue();
    }
    changeAndNotifyObservers();
  }
  
  public void changeAndNotifyObservers() {
    updateDerivedValues();
    this.support.firePropertyChange("Inflation Conversion Event", null, null);
  }
}
