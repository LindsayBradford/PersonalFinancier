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
import java.util.Calendar;

import blacksmyth.personalfinancier.model.Money;

public interface InflationProvider {

  public abstract Money computeComparisonValue(
      Money originalValue, 
      Calendar origianlDate, 
      Calendar comparisonDate
   );

  public abstract double getCPIFigureForDate(Calendar date);                                             

  public abstract double getInflationForDateRange(
      Calendar earlierDate, 
      Calendar laterDate
  );                                             

  public abstract double getInflationPerAnnum(
      Calendar firstDate, 
      Calendar secondDate
  );

  public abstract Calendar getEarliestDate();
  public abstract Calendar getLatestDate();
  
  public abstract void addListener(PropertyChangeListener listener);
}
