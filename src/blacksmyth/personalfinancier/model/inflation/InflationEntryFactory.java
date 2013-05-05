/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.model.inflation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import blacksmyth.general.SortedArrayList;

public class InflationEntryFactory {
  // TODO: Expand to be more than just a nickname device.

  public static InflationEntry createEntry() {
    return createEntry(
        new GregorianCalendar(),
        0,
        ""
      );
  }

  public static InflationEntry createEntry(SortedArrayList<InflationEntry> list) {
    if (list == null || list.size() == 0) {
      return createEntry();
    }
    
    return createEntry(
        (Calendar) list.last().getDate().clone(),
        list.last().getCPIValue(),
        list.last().getNotes()
      );
  }

  
  public static InflationEntry createEntry(Calendar date, double value, String notes) {
    return  new InflationEntry(
        date,
        value,
        notes
    );
  }
}
