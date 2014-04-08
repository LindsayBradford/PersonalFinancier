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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.general.SortedArrayList;
import blacksmyth.personalfinancier.control.inflation.IInflationController;
import blacksmyth.personalfinancier.model.IFileHandlerModel;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;
import blacksmyth.personalfinancier.model.PreferencesModel;

public class InflationModel extends Observable implements InflationProvider, 
                                                          IFileHandlerModel<InflationFileContent> {
  
  private static GregorianCalendar TODAY;
  
  private static final String CONTROLLER_ASSERT_MSG = "Caller does not implement IInflationController.";
  private static final String VIEWER_ASSERT_MSG = "Caller does not implement IInflationObserver.";
  
  //Note: SortedSet of CPIEntries are to be sorted earliest to latest in date order.
  private static SortedArrayList<InflationEntry> inflationList = new SortedArrayList<InflationEntry>();

  public InflationModel() {
    TODAY = new GregorianCalendar(); TODAY.setTime(new Date());
  }
  
  public SortedArrayList<InflationEntry> getInflationList() {
    return InflationModel.inflationList;
  }
  
  public void setInflationList(SortedArrayList<InflationEntry> list) {
    InflationModel.inflationList = list;
    this.changeAndNotifyObservers();
  }

  @Override
  public Money computeComparisonValue(Money originalValue,
                                      Calendar originalDate, 
                                      Calendar comparisonDate) {

    Money returnValue = MoneyFactory.copy(originalValue);

    BigDecimal originalCPIForDate = new BigDecimal(getCPIFigureForDate(originalDate));
    BigDecimal comparsionCPIForDate = new BigDecimal(getCPIFigureForDate(comparisonDate));
    
   if (getCPIFigureForDate(comparisonDate) == 0) {
     returnValue.setTotal(
         new BigDecimal(0)
     );
   } else {
     returnValue.setTotal(
         originalValue.getTotal().multiply(
             comparsionCPIForDate.divide(
                 originalCPIForDate,
                 PreferencesModel.getInstance().getPreferredPrecision(), 
                 PreferencesModel.getInstance().getPreferredRoundingMode()
             )
         )    
       );
   }
    
    return returnValue;
  }

  @Override
  public double getCPIFigureForDate(Calendar date) {
    if (date.before(getEarliestDate())) {
      return getCPIFigureForDate(getEarliestDate());
    }
    if (date.after(getLatestDate())) {
      return getCPIFigureForDate(getLatestDate());
    }
    return getCPIFigureFromList(date);
  }
  
  public double getCPIFigureFromList(Calendar date) {
    
    if (inflationList.size() == 0) {
      return 0;
    }
    
    double cpiFigure = inflationList.last().getCPIValue();
    
    for(int i = inflationList.size() - 1; i >= 0; i--) {
      Calendar applicableFromDate = inflationList.get(i).getDate();
      cpiFigure = inflationList.get(i).getCPIValue();

      if (applicableFromDate.before(date) || applicableFromDate.equals(date)) {
        break;
      }
    }

    return cpiFigure;
  }

  @Override
  public double getInflationForDateRange(Calendar earlierDate,
                                         Calendar laterDate) {

    if (inflationList.size() == 0) {
      return 0;
    }
    
    if (laterDate.before(earlierDate)) {
      return getInflationForDateRange(laterDate, earlierDate);
    } 
    return getCPIFigureForDate(laterDate) / getCPIFigureForDate(earlierDate) - 1;                                           
  }

  @Override
  public double getInflationPerAnnum(Calendar firstDate,
                                     Calendar secondDate) {
    if (inflationList.size() == 0) {
      return 0;
    }
    
    if (secondDate.before(firstDate)) {
      return getInflationPerAnnum(secondDate, firstDate);
    } 
    
    double inflationOverDateRange = getInflationForDateRange(firstDate, secondDate);
    if (inflationOverDateRange != 0) {
      return  inflationOverDateRange / getTimeDiffInYears(firstDate, secondDate);
    }
    return 0;
  }
  
  private double getTimeDiffInYears(Calendar earlierDate, 
                                    Calendar laterDate) {
    final long   MILLISECONDS_PER_DAY = 86400000;
    final double DAYS_PER_YEAR =  MILLISECONDS_PER_DAY * 365.25;
    
    return (laterDate.getTimeInMillis() - earlierDate.getTimeInMillis()) / DAYS_PER_YEAR;
  }

  @Override
  public Calendar getEarliestDate() {
    if (inflationList.size() == 0) {
      return TODAY;
    }

    return inflationList.first().getDate();
  }
  
  @Override
  public Calendar getLatestDate() {
    if (inflationList.size() == 0) {
      return TODAY;
    }

    return inflationList.last().getDate();
  }
  
  public InflationEntry addEntry() {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    InflationEntry newItem = InflationEntryFactory.createEntry(inflationList);
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

  public void setInflationEntryDate(int index, Calendar date) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < inflationList.size());
    inflationList.get(index).setDate(date);
    this.changeAndNotifyObservers();
  }

  public void changeAndNotifyObservers() {
    this.setChanged();
    this.notifyObservers();
  }

  @Override
  public void fromSerializable(InflationFileContent content) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    inflationList = content.inflationList;
    
    this.changeAndNotifyObservers();
  }

  @Override
  public InflationFileContent toSerializable() {
    InflationFileContent content = new InflationFileContent();
    
    content.inflationList = inflationList;
    
    return content;
  }
  
 
}
