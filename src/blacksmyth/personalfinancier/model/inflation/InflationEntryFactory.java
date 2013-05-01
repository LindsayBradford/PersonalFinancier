package blacksmyth.personalfinancier.model.inflation;

import java.util.Date;

public class InflationEntryFactory {
  // TODO: Expand to be more than just a nickname device.

  public static InflationEntry createEntry() {
    return createEntry(
        new Date(),
        0,
        ""
      );
  }
  
  public static InflationEntry createEntry(Date date, double value, String notes) {
    return  new InflationEntry(
        date,
        value,
        notes
    );
  }
}
