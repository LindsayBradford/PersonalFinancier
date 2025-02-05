/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control.inflation;

import java.beans.PropertyChangeListener;

/**
 * An interface that all MVC view classes of the
 * <tt>InflationModel</tt> must implement if they wish be
 * notified of BudgetModel updates.
 * @author linds
 */
public interface InfllationObserver extends PropertyChangeListener {}
