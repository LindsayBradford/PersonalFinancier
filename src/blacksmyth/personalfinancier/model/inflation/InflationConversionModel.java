/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.model.inflation;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;

public class InflationConversionModel extends Observable implements Observer {
  
  private Money initialValue;
  private Date initialDate;

  private Money conversionValue;
  private Date conversionDate;
  
  private double inflationOverPeriod;
  private double inflationPerAnnum;
  
  private InflationProvider inflationProvider;

  public void setInflationProvider(InflationProvider inflationProvider) {
    this.inflationProvider = inflationProvider;
    if (ReflectionUtilities.classImplements(inflationProvider.getClass(), Observable.class)) {
      Observable providerAsObservable = (Observable) inflationProvider;
      providerAsObservable.addObserver(this);
    }
  }
  
  public InflationConversionModel() {
    this.setInitialValue(MoneyFactory.createAmount(0));
    this.setInitialDate(inflationProvider.getEarliestDate());
    
    this.setConversionValue(MoneyFactory.createAmount(0));
    this.setConversionDate(inflationProvider.getLatestDate());

    this.setInflationOverPeriod(0);
    this.setInflationPerAnnum(0);

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

  public Date getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(Date initialDate) {
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

  public Date getConversionDate() {
    return conversionDate;
  }

  public void setConversionDate(Date conversionDate) {
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

  @Override
  public void update(Observable arg0, Object arg1) {
    updateConversionValue();
    changeAndNotifyObservers();
  }
}
