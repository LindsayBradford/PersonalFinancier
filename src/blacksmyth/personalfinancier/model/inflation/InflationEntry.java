/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.model.inflation;

import java.util.Date;

public class InflationEntry {
  
  private double CPIValue;
  private Date Date;
  private String Notes;
  
  public InflationEntry(Date date, double value, String notes) {
    this.Date = date;
    this.CPIValue = value;
    this.Notes = notes;
  }

  public double getCPIValue() {
    return CPIValue;
  }
  
  public void setCPIValue(double figure) {
    this.CPIValue = figure;
  }

  public Date getDate() {
    return Date;
  }

  public void setDate(Date date) {
    this.Date = date;
  }

  public String getNotes() {
    return Notes;
  }

  public void setNotes(String notes) {
    Notes = notes;
  }
}
