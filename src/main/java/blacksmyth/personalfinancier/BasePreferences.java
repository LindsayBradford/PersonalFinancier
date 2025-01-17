/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier;

import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;

/**
 * A Singleton model that manages user preferences. It implements the {@link Observable} interface
 * allowing observers to react to user preference changes. Constructed as a singleton so that observers 
 * can subscribe to updates via an object instance reference to PreferencesModel.
 */
public abstract class BasePreferences {

  private static final String BaseNode = "blacksmyth/personalfinancier";
  protected static final Preferences BaseUserPrefs = Preferences.userRoot().node(BaseNode);

  protected PropertyChangeSupport support;
  
  protected String getPath() {
    return BaseUserPrefs.absolutePath();
  }

  protected void setChangeAndNotifyObservers() {
    support.firePropertyChange("PreferencesChanged", null, null);
  }
}
