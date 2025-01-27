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
import java.time.LocalDate;

import blacksmyth.personalfinancier.model.Money;

public interface InflationProvider {

  public abstract Money computeComparisonValue(
      Money originalValue, 
      LocalDate origianlDate, 
      LocalDate comparisonDate
   );

  public abstract double getCPIFigureForDate(LocalDate date);                                             

  public abstract double getInflationForDateRange(
      LocalDate earlierDate, 
      LocalDate laterDate
  );                                             

  public abstract double getInflationPerAnnum(
      LocalDate firstDate, 
      LocalDate secondDate
  );

  public abstract LocalDate getEarliestDate();
  public abstract LocalDate getLatestDate();
  
  public abstract void addListener(PropertyChangeListener listener);
}
