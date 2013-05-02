/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2013 
 */

package blacksmyth.personalfinancier.model.inflation;

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
}
