/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control.inflation.command;

import java.time.LocalDate;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.inflation.UndoableInflationCommand;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

public abstract class AbstractInflationCommand implements UndoableInflationCommand {

  protected InflationModel model;
  protected int inflationEntryIndex;
  
  public AbstractInflationCommand(InflationModel model, int index) {
    this.model = model;
    this.inflationEntryIndex = index;
  }
  
  @Override
  public boolean addEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public boolean canRedo() {
    return true;
  }

  @Override
  public boolean canUndo() {
    return true;
  }

  @Override
  public void die() {
    // deliberately does nothing in the abstract class.
  }

  @Override
  public String getPresentationName() {
    return null;
  }

  @Override
  public String getRedoPresentationName() {
    return null;
  }

  @Override
  public String getUndoPresentationName() {
    return null;
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public boolean isSignificant() {
    return true;
  }

  @Override
  public abstract void redo() throws CannotRedoException; 

  @Override
  public abstract void undo() throws CannotUndoException;
  
  protected String toString(LocalDate date) {
    return date.format(WidgetFactory.DATE_FORMAT);
  }
  
  protected String dateAsString() {
    LocalDate date = model.getInflationList().get(inflationEntryIndex).getDate();
    return toString(date);
  }


}
