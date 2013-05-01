/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.model.inflation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Observable;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.general.SortedArrayList;
import blacksmyth.personalfinancier.control.budget.IBudgetController;
import blacksmyth.personalfinancier.control.inflation.IInflationController;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;

public class InflationModel extends Observable implements InflationProvider {
  
  private static final String CONTROLLER_ASSERT_MSG = "Caller does not implement IInflationController.";
  private static final String VIEWER_ASSERT_MSG = "Caller does not implement IInflationObserver.";
  
  //Note: SortedSet of CPIEntries are to be sorted earliest to latest in date order.
  private static SortedArrayList<InflationEntry> inflationList;

  public InflationModel() {
    inflationList = new SortedArrayList<InflationEntry>();
  }
  
  public SortedArrayList<InflationEntry> getInflationList() {
    return InflationModel.inflationList;
  }
  
  public void setInflationList(SortedArrayList<InflationEntry> list) {
    InflationModel.inflationList = list;
  }

  @Override
  public Money computeComparisonValue(Money originalValue,
                                      Date originalDate, 
                                      Date comparisonDate) {

    Money returnValue = MoneyFactory.copy(originalValue);

    //  return originalValue * (getCPIFigureForDate(comparisonDate) / 
    //      getCPIFigureForDate(originalDate));

    BigDecimal originalCPIForDate = new BigDecimal(getCPIFigureForDate(originalDate));
    BigDecimal comparsionCPIForDate = new BigDecimal(getCPIFigureForDate(comparisonDate));

    returnValue.setTotal(
      originalValue.getTotal().multiply(
          comparsionCPIForDate.divide(originalCPIForDate)
      )    
    );
    
    return returnValue;
  }

  @Override
  public double getCPIFigureForDate(Date date) {
    if (date.before(getEarliestDate())) {
      return getCPIFigureForDate(getEarliestDate());
    }
    if (date.after(getLatestDate())) {
      return getCPIFigureForDate(getLatestDate());
    }
    return getCPIFigureFromList(date);
  }
  
  public double getCPIFigureFromList(Date date) {
    double cpiFigure = inflationList.last().getCPIValue();
    
    for(InflationEntry entry : inflationList) {
      Date applicableFromDate = entry.getDate();

      if (applicableFromDate.before(date)) {
        break;
      }

      cpiFigure = entry.getCPIValue();
    }

    return cpiFigure;
  }

  @Override
  public double getInflationForDateRange(Date earlierDate,
                                         Date laterDate) {

    if (laterDate.before(earlierDate)) {
      return getInflationForDateRange(laterDate, earlierDate);
    } 
    return getCPIFigureForDate(laterDate) / getCPIFigureForDate(earlierDate) - 1;                                           
  }

  @Override
  public double getInflationPerAnnum(Date firstDate,
                                     Date secondDate) {
    if (secondDate.before(firstDate)) {
      return getInflationPerAnnum(secondDate, firstDate);
    } 
    return getInflationForDateRange(firstDate, secondDate) / 
           getTimeDiffInYears(firstDate, secondDate);
  }
  
  private double getTimeDiffInYears(Date earlierDate, 
                                    Date laterDate) {
    final long   MILLISECONDS_PER_DAY = 86400000;
    final double DAYS_PER_YEAR =  MILLISECONDS_PER_DAY * 365.25;

    
    return (laterDate.getTime() - earlierDate.getTime()) / DAYS_PER_YEAR;
  }

  @Override
  public Date getEarliestDate() {
    return inflationList.first().getDate();
  }
  
  @Override
  public Date getLatestDate() {
    return inflationList.last().getDate();
  }
  
  public InflationEntry addEntry() {
    assert ReflectionUtilities.callerImplements(IBudgetController.class) : CONTROLLER_ASSERT_MSG;
    InflationEntry newItem = InflationEntryFactory.createEntry();
    this.addEntry(newItem);
    return newItem;
  }

  
  public void addEntry(InflationEntry entry) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    inflationList.add(entry);
    this.changeAndNotifyObservers();
  }
  
  
  public void addEntry(int index, InflationEntry item) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    inflationList.add(index, item);
    this.changeAndNotifyObservers();
  }
  
  public InflationEntry removeEntry(int index) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    InflationEntry removedEntry = inflationList.remove(index);
    this.changeAndNotifyObservers();
    return removedEntry;
  }

  public void removeEntry(InflationEntry entry) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    inflationList.remove(entry);
    this.changeAndNotifyObservers();
  }
  
  public void setInflationEntryValue(int index, double value) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < inflationList.size());
    inflationList.get(index).setCPIValue(value);
    this.changeAndNotifyObservers();
  }

  public void setInflationEntryNotes(int index, String notes) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < inflationList.size());
    inflationList.get(index).setNotes(notes);
    this.changeAndNotifyObservers();
  }

  public void setInflationEntryDate(int index, Date date) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < inflationList.size());
    inflationList.get(index).setDate(date);
    this.changeAndNotifyObservers();
  }

  public void changeAndNotifyObservers() {
    this.setChanged();
    this.notifyObservers();
  }
}
