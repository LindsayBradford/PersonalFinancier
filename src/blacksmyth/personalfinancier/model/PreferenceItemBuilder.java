package blacksmyth.personalfinancier.model;

public class PreferenceItemBuilder {
  
  public static IPreferenceItem<String> buildBudgetDirectoryPreferenceItem() {
    return new IPreferenceItem<String>() {

      public void setPreference(String t) {
        PreferencesModel.getInstance().setLastUsedBudgetFilePath(t);
      }

      public String getPreference() {
        return PreferencesModel.getInstance().getLastUsedBudgetFilePath();
      }
    };
  }

  public static IPreferenceItem<String> buildInflationDirectoryPreferenceItem() {
    return new IPreferenceItem<String>() {

      public void setPreference(String t) {
        PreferencesModel.getInstance().setLastUsedInflationFilePath(t);
      }

      public String getPreference() {
        return PreferencesModel.getInstance().getLastUsedInflationFilePath();
      }
    };
  }
}
