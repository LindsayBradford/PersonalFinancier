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

import java.time.LocalDate;

import blacksmyth.personalfinancier.model.Money;

/**
 * An interface that all MVC controller classes of the
 * <tt>InflationModel</tt> must implement if they wish to invoke its
 * control methods.
 * @author linds
 */
public interface IInflationController {

  Money computeComparisonValue(Money originalValue, LocalDate originalDate, LocalDate comparisonDate);

  double getCPIFigureForDate(LocalDate date);

  double getInflationPerAnnum(LocalDate firstDate, LocalDate secondDate);

  double getInflationForDateRange(LocalDate earlierDate, LocalDate laterDate);
  
}


