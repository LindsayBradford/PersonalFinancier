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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import blacksmyth.general.ReflectionUtilities;
import blacksmyth.general.SortedArrayList;
import blacksmyth.general.file.IFileHandlerModel;
import blacksmyth.personalfinancier.control.inflation.IInflationController;
import blacksmyth.personalfinancier.control.inflation.command.InflationUndoManager;
import blacksmyth.personalfinancier.model.ModelPreferences;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;

public class InflationModel
    implements InflationProvider, IInflationController, IFileHandlerModel<InflationFileContent> {

  private static LocalDate TODAY;

  private static final String CONTROLLER_ASSERT_MSG = "Caller does not implement IInflationController.";
  // private static final String VIEWER_ASSERT_MSG = "Caller does not implement
  // IInflationObserver.";

  // Note: SortedSet of CPIEntries are to be sorted earliest to latest in date
  // order.
  private static SortedArrayList<InflationEntry> inflationList = new SortedArrayList<InflationEntry>();

  private final InflationUndoManager undoManager = new InflationUndoManager();

  private PropertyChangeSupport support;
  
  public InflationModel() {
    support = new PropertyChangeSupport(this);
    TODAY = LocalDate.now();
  }
  
  public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public SortedArrayList<InflationEntry> getInflationList() {
    return InflationModel.inflationList;
  }

  public void setInflationList(SortedArrayList<InflationEntry> list) {
    InflationModel.inflationList = list;
    this.changeAndNotifyObservers();
  }

  public InflationUndoManager getUndoManager() {
    return undoManager;
  }

  @Override
  public Money computeComparisonValue(Money originalValue, LocalDate originalDate, LocalDate comparisonDate) {

    Money returnValue = MoneyFactory.copy(originalValue);

    BigDecimal originalCPIForDate = new BigDecimal(getCPIFigureForDate(originalDate));
    BigDecimal comparsionCPIForDate = new BigDecimal(getCPIFigureForDate(comparisonDate));

    if (getCPIFigureForDate(comparisonDate) == 0) {
      returnValue.setTotal(new BigDecimal(0));
    } else {
      returnValue.setTotal(originalValue.getTotal().multiply(
          comparsionCPIForDate.divide(originalCPIForDate, ModelPreferences.getInstance().getPreferredPrecision(),
              ModelPreferences.getInstance().getPreferredRoundingMode())));
    }

    return returnValue;
  }

  @Override
  public double getCPIFigureForDate(LocalDate date) {
    if (date.isBefore(getEarliestDate())) {
      return getCPIFigureForDate(getEarliestDate());
    }
    if (date.isAfter(getLatestDate())) {
      return getCPIFigureForDate(getLatestDate());
    }
    return getCPIFigureFromList(date);
  }

  public double getCPIFigureFromList(LocalDate date) {

    if (inflationList.size() == 0) {
      return 0;
    }

    double cpiFigure = inflationList.last().getCPIValue();

    for (int i = inflationList.size() - 1; i >= 0; i--) {
      LocalDate applicableFromDate = inflationList.get(i).getDate();
      cpiFigure = inflationList.get(i).getCPIValue();

      if (applicableFromDate.isBefore(date) || applicableFromDate.equals(date)) {
        break;
      }
    }

    return cpiFigure;
  }

  @Override
  public double getInflationForDateRange(LocalDate earlierDate,  LocalDate laterDate) {

    if (inflationList.size() == 0) {
      return 0;
    }

    if (laterDate.isBefore(earlierDate)) {
      return getInflationForDateRange(laterDate, earlierDate);
    }
    return getCPIFigureForDate(laterDate) / getCPIFigureForDate(earlierDate) - 1;
  }

  @Override
  public double getInflationPerAnnum(LocalDate firstDate, LocalDate secondDate) {
    if (inflationList.size() == 0) {
      return 0;
    }

    if (secondDate.isBefore(firstDate)) {
      return getInflationPerAnnum(secondDate, firstDate);
    }

    double inflationOverDateRange = getInflationForDateRange(firstDate, secondDate);
    if (inflationOverDateRange != 0) {
      return inflationOverDateRange / getTimeDiffInYears(firstDate, secondDate);
    }
    return 0;
  }

  private double getTimeDiffInYears(LocalDate earlierDate, LocalDate laterDate) {
    final double DAYS_PER_YEAR = 365.25;

    long differenceInDays = Math.abs(ChronoUnit.DAYS.between(earlierDate, laterDate)) + 1;
    
    return differenceInDays / DAYS_PER_YEAR;
  }

  @Override
  public LocalDate getEarliestDate() {
    if (inflationList.size() == 0) {
      return TODAY;
    }

    return inflationList.first().getDate();
  }

  @Override
  public LocalDate getLatestDate() {
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

  public void setInflationEntryDate(int index, LocalDate date) {
    assert ReflectionUtilities.callerImplements(IInflationController.class) : CONTROLLER_ASSERT_MSG;
    assert (index >= 0 && index < inflationList.size());
    inflationList.get(index).setDate(date);
    this.changeAndNotifyObservers();
  }

  @Override
  public void fromSerializable(InflationFileContent content) {
    // assert ReflectionUtilities.callerImplements(IInflationController.class) :
    // CONTROLLER_ASSERT_MSG;
    inflationList = content.inflationList;
    support.firePropertyChange("Inflation Model Loaded", null, null);
  }

  public void changeAndNotifyObservers() {
    support.firePropertyChange("Inflation Model Change", null, null);
  }
  
  @Override
  public InflationFileContent toSerializable() {
    InflationFileContent content = new InflationFileContent();

    content.inflationList = inflationList;

    return content;
  }

}
