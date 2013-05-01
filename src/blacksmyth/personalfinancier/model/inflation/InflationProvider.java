/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2013 
 */

package blacksmyth.personalfinancier.model.inflation;

import java.util.Date;

import blacksmyth.personalfinancier.model.Money;

public interface InflationProvider {

  public abstract Money computeComparisonValue(
      Money originalValue, 
      Date  origianlDate, 
      Date  comparisonDate
   );

  public abstract double getCPIFigureForDate(Date date);                                             

  public abstract double getInflationForDateRange(
      Date earlierDate, 
      Date laterDate
  );                                             

  public abstract double getInflationPerAnnum(
      Date firstDate, 
      Date secondDate
  );

  public abstract Date getEarliestDate();
  public abstract Date getLatestDate();
}
