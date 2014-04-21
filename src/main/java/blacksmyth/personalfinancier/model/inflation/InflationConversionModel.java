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

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;

public class InflationConversionModel extends Observable implements Observer {
  
  private Money initialValue;
  private Calendar initialDate;

  private Money conversionValue;
  private Calendar conversionDate;
  
  private double inflationOverPeriod;
  private double inflationPerAnnum;
  
  private InflationProvider inflationProvider;

  public void setInflationProvider(InflationProvider inflationProvider) {
    this.inflationProvider = inflationProvider;

    Observable providerAsObservable = (Observable) inflationProvider;
    providerAsObservable.addObserver(this);
  }
  
  public InflationConversionModel(InflationProvider provider) {
    this.setInflationProvider(provider);
    this.initialValue = MoneyFactory.createAmount(0);
    this.initialDate = inflationProvider.getEarliestDate();
    
    this.conversionValue = MoneyFactory.createAmount(0);
    this.conversionDate = inflationProvider.getLatestDate();

    this.inflationOverPeriod = 0;
    this.inflationPerAnnum = 0;

    this.changeAndNotifyObservers();
  }

  public Money getInitialValue() {
    return initialValue;
  }

  public void setInitialValue(Money initialValue) {
    this.initialValue = initialValue;
    
    updateConversionValue();
    
    changeAndNotifyObservers();
  }

  public Calendar getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(Calendar initialDate) {
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

  public Calendar getConversionDate() {
    return conversionDate;
  }

  public void setConversionDate(Calendar conversionDate) {
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

  public void changeAndNotifyObservers() {
    this.setChanged();
    updateDerivedValues();
    this.notifyObservers();
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
  
  public Calendar getEarliestDate() {
    return inflationProvider.getEarliestDate();
  }
  public Calendar getLatestLatestDate() {
    return inflationProvider.getLatestDate();
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    updateConversionValue();
    changeAndNotifyObservers();
  }
}
