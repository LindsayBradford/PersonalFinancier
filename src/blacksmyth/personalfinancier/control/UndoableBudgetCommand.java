package blacksmyth.personalfinancier.control;

import javax.swing.undo.UndoableEdit;

public interface UndoableBudgetCommand extends IBudgetController, UndoableEdit {}
