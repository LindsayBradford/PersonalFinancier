/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control;

public class UndoManagers {
  
  public static final FinancierUndoManager BUDGET_UNDO_MANAGER = new FinancierUndoManager();
  public static final FinancierUndoManager INFLATION_UNDO_MANAGER = new FinancierUndoManager();
}
