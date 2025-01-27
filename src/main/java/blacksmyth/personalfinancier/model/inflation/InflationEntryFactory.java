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

import java.time.LocalDate;

import blacksmyth.general.SortedArrayList;

public class InflationEntryFactory {
  // TODO: Expand to be more than just a nickname device.

  public static InflationEntry createEntry() {
    return createEntry(
        LocalDate.now(),
        0,
        ""
      );
  }

  public static InflationEntry createEntry(SortedArrayList<InflationEntry> list) {
    if (list == null || list.size() == 0) {
      return createEntry();
    }
    
    return createEntry(
        list.last().getDate(),
        list.last().getCPIValue(),
        list.last().getNotes()
      );
  }

  
  public static InflationEntry createEntry(LocalDate date, double value, String notes) {
    return  new InflationEntry(
        date,
        value,
        notes
    );
  }
}
