/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.model.inflation;

import java.util.Calendar;

public class InflationEntry {
  
  private Calendar Date;
  private double CPIValue;
  private String Notes;
  
  public InflationEntry(Calendar date, double value, String notes) {
    this.Date = date;
    this.CPIValue = value;
    this.Notes = notes;
  }

  public Calendar getDate() {
    return Date;
  }

  public void setDate(Calendar date) {
    this.Date = date;
  }

  public double getCPIValue() {
    return CPIValue;
  }
  
  public void setCPIValue(double figure) {
    this.CPIValue = figure;
  }

  public String getNotes() {
    return Notes;
  }

  public void setNotes(String notes) {
    Notes = notes;
  }
}
