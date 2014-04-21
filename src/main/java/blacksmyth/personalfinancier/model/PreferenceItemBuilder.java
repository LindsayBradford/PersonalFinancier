/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

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
