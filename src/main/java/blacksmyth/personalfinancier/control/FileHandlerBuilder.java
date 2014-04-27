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

import java.util.HashMap;

import javax.swing.JFrame;

import blacksmyth.general.file.FileHandler;
import blacksmyth.general.file.IFileHandler;
import blacksmyth.general.file.IFileHandlerModel;
import blacksmyth.general.file.IFileHandlerView;
import blacksmyth.general.file.IObjectFileConverter;
import blacksmyth.personalfinancier.UIComponents;
import blacksmyth.personalfinancier.dependencies.encryption.EncryptionBridge;
import blacksmyth.personalfinancier.model.IPreferenceItem;
import blacksmyth.personalfinancier.model.PreferenceItemBuilder;
import blacksmyth.personalfinancier.model.budget.BudgetFileContent;
import blacksmyth.personalfinancier.model.inflation.InflationFileContent;
import blacksmyth.personalfinancier.view.FileHandlerView;
import blacksmyth.personalfinancier.view.PasswordPromptView;

public final class FileHandlerBuilder {
  
  public static IFileHandler<BudgetFileContent> buildBudgetHandler(
      JFrame parentFrame,
      IFileHandlerModel<BudgetFileContent> model) {
    
    HashMap<String, IObjectFileConverter<BudgetFileContent>> availableAdapters = 
        buildAvailableAdapters();
    
    IFileHandler<BudgetFileContent> handler = new FileHandler<BudgetFileContent>();
    
    handler.setModel(model);
    
    handler.setView(
        buildBudgetView(parentFrame, availableAdapters)
     );
    
    handler.setObjectFileConverter(
        buildBudgetFileAdapter(availableAdapters)
    );
    
    handler.setFilePathPreferenceItem(
        buildBudgetPreferenceItem()
    );
    
    return handler;
  }
  
  private static HashMap<String, IObjectFileConverter<BudgetFileContent>> buildAvailableAdapters() {
    HashMap<String, IObjectFileConverter<BudgetFileContent>> availableAdapters = 
        new HashMap<String, IObjectFileConverter<BudgetFileContent>> ();
    
    availableAdapters.put("pf", new JSonFileAdapter<BudgetFileContent>());
    
    EncryptionBridge bridge = new EncryptionBridge();
    
    if (bridge.encryptionAvailable()) {
      IObjectFileConverter<BudgetFileContent> encryptedAdapter = 
          new EncryptedJSonFileAdapter<BudgetFileContent>(
              new PasswordPromptView(UIComponents.windowFrame)
          );

      availableAdapters.put("epf", encryptedAdapter);
    }
    
    return availableAdapters;
  }
  
  private static IFileHandlerView buildBudgetView(
      JFrame parentFrame, 
      HashMap<String, IObjectFileConverter<BudgetFileContent>> availableAdapters) {

    return new FileHandlerView(
        parentFrame,
        "Personal Financier Files",
        availableAdapters.keySet().toArray(new String[0])  // new String[0] needed for valid typecast.
    );
  }
  
  private static IPreferenceItem<String> buildBudgetPreferenceItem() {
    return PreferenceItemBuilder.buildBudgetDirectoryPreferenceItem();
  }
  
  private static IObjectFileConverter<BudgetFileContent> buildBudgetFileAdapter(
      HashMap<String, IObjectFileConverter<BudgetFileContent>> availableAdapters) {
   
    return new StrategicFileAdapter<BudgetFileContent>(availableAdapters);
  }
  
  public static IFileHandler<InflationFileContent> buildInflationHandler(
      JFrame parentFrame, IFileHandlerModel<InflationFileContent> model) {
    
    IFileHandler<InflationFileContent> handler = new FileHandler<InflationFileContent>();

    handler.setModel(model);
    
    handler.setView(
        buildInflationView(parentFrame)
     );
    
    handler.setObjectFileConverter(
        buildInflationFileAdapter()
    );
    
    handler.setFilePathPreferenceItem(
        buildInflationPreferenceItem()
    );
    
    return handler;
  }
  
  private static FileHandlerView buildInflationView(JFrame parentFrame) {
    return new FileHandlerView(
        parentFrame,
        "JSon Files",
        "json"
    );  
  }
  
  private static IObjectFileConverter<InflationFileContent> buildInflationFileAdapter() {
    return new JSonFileAdapter<InflationFileContent>(); 
  }
  
  private static IPreferenceItem<String> buildInflationPreferenceItem() {
    return PreferenceItemBuilder.buildInflationDirectoryPreferenceItem();
  }

}
