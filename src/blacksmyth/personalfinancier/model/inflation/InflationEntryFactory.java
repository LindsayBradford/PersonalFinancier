package blacksmyth.personalfinancier.model.inflation;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class InflationEntryFactory {
  // TODO: Expand to be more than just a nickname device.

  public static InflationEntry createEntry() {
    return createEntry(
        new GregorianCalendar(),
        0,
        ""
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
