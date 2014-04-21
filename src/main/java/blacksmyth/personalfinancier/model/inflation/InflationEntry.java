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
