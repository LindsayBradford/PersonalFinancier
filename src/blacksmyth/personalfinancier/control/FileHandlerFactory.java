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

import javax.swing.JFrame;

import blacksmyth.general.file.IFileHandlerModel;
import blacksmyth.general.file.IFileHandlerView;
import blacksmyth.general.file.IObjectFileConverter;
import blacksmyth.personalfinancier.UIComponents;
import blacksmyth.personalfinancier.model.IPreferenceItem;
import blacksmyth.personalfinancier.model.PreferenceItemBuilder;
import blacksmyth.personalfinancier.model.budget.BudgetFileContent;
import blacksmyth.personalfinancier.view.FileHandlerView;
import blacksmyth.personalfinancier.view.PasswordPromptView;

public final class FileHandlerFactory {
  
  public static FileHandler<BudgetFileContent> buildBudgetHandler(
      JFrame parentFrame, IFileHandlerModel<BudgetFileContent> model) {
    
    return new FileHandler<BudgetFileContent>(
            buildBudgetView(parentFrame), 
            model, 
            buildFileAdapter(), 
            buildBudgetPreferenceItem()
        );
  }
  
  private static IFileHandlerView buildBudgetView(JFrame parentFrame) {
    return new FileHandlerView(
        parentFrame,
        "Personal Financier Files",
        "pf", "epf"
    );
  }
  
  private static IPreferenceItem<String> buildBudgetPreferenceItem() {
    return PreferenceItemBuilder.buildBudgetDirectoryPreferenceItem();
  }
  
  private static IObjectFileConverter<BudgetFileContent> buildFileAdapter() {

    IObjectFileConverter<BudgetFileContent> basicAdapter = 
        new JSonFileAdapter<BudgetFileContent>();

    IObjectFileConverter<BudgetFileContent> encryptedAdapter = 
        new EncryptedJSonFileAdapter<BudgetFileContent>(
            new PasswordPromptView(UIComponents.windowFrame)
        );
    
    StrategicFileAdapter<BudgetFileContent> strategicAdapter  = 
        new StrategicFileAdapter<BudgetFileContent>();

    strategicAdapter.add("pf", basicAdapter);
    strategicAdapter.add("epf", encryptedAdapter);

    return strategicAdapter;
  }

}
