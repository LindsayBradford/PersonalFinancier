package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.budget.IBudgetController;

public interface UndoableBudgetCommand extends IBudgetController, UndoableEdit {}
