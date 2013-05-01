package blacksmyth.personalfinancier.control.inflation.command;

import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.inflation.IInflationController;

public interface UndoableInflationCommand extends IInflationController, UndoableEdit {}
