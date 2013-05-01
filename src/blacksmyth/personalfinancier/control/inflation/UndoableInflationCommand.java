package blacksmyth.personalfinancier.control.inflation;

import javax.swing.undo.UndoableEdit;

public interface UndoableInflationCommand extends IInflationController, UndoableEdit {}
