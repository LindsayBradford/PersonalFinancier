/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.control;

public class UndoManagers {
  
  public static final FinancierUndoManager BUDGET_UNDO_MANAGER = new FinancierUndoManager();
  public static final FinancierUndoManager INFLATION_UNDO_MANAGER = new FinancierUndoManager();
}
