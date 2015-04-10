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

import blacksmyth.general.ApplicationMessagePresenter;
import blacksmyth.general.file.FileHandler;
import blacksmyth.general.file.FileSystemBridge;
import blacksmyth.general.file.IFileHandler;
import blacksmyth.general.file.IFileHandlerModel;
import blacksmyth.general.file.IFileHandlerView;
import blacksmyth.general.file.IObjectFileConverter;
import blacksmyth.general.file.StrategicFileConverter;
import blacksmyth.personalfinancier.dependencies.encryption.EncryptionBridge;
import blacksmyth.personalfinancier.dependencies.json.EncryptedJSonFileConverter;
import blacksmyth.personalfinancier.dependencies.json.JSonBridge;
import blacksmyth.personalfinancier.dependencies.json.JSonObjectFileConverter;
import blacksmyth.personalfinancier.model.IPreferenceItem;
import blacksmyth.personalfinancier.model.PreferenceItemBuilder;
import blacksmyth.personalfinancier.model.budget.BudgetFileContent;
import blacksmyth.personalfinancier.model.inflation.InflationFileContent;
import blacksmyth.personalfinancier.view.FileHandlerView;
import blacksmyth.personalfinancier.view.PasswordPromptView;
import blacksmyth.personalfinancier.view.PersonalFinancierView;

public final class FileHandlerBuilder {
  
  private static final ApplicationMessagePresenter MSG_PRESENTER = new ApplicationMessagePresenter();
  
  public static IFileHandler<BudgetFileContent> buildBudgetHandler(
      PersonalFinancierView parentFrame,
      IFileHandlerModel<BudgetFileContent> model) {
    
    HashMap<String, IObjectFileConverter<BudgetFileContent>> availableConverters = 
        buildAvailableAdapters(parentFrame);
    
    IFileHandler<BudgetFileContent> handler = new FileHandler<BudgetFileContent>();
    
    handler.setModel(model);
    
    handler.setView(
        buildBudgetView(parentFrame, availableConverters)
     );
    
    handler.setObjectFileConverter(
        buildBudgetFileConverter(availableConverters)
    );
    
    handler.setAppMessagePresenter(MSG_PRESENTER);
    
    handler.setFilePathPreferenceItem(
        buildBudgetPreferenceItem()
    );
    
    return handler;
  }
  
  private static HashMap<String, IObjectFileConverter<BudgetFileContent>> 
      buildAvailableAdapters(PersonalFinancierView parentView) {
    
    HashMap<String, IObjectFileConverter<BudgetFileContent>> availableConverters = 
    new HashMap<String, IObjectFileConverter<BudgetFileContent>> ();
    
    JSonObjectFileConverter<BudgetFileContent> jsonConverter = 
        new JSonObjectFileConverter<BudgetFileContent>();
    
    jsonConverter.setJSonBridge(
        new JSonBridge<BudgetFileContent>()
    );
    
    jsonConverter.setFileSystemBridge(
        new FileSystemBridge()
    );
    
    availableConverters.put("pf", jsonConverter);
    
    EncryptionBridge bridge = new EncryptionBridge();
    
    if (bridge.encryptionAvailable()) {
      EncryptedJSonFileConverter<BudgetFileContent> encryptedConverter = 
          new EncryptedJSonFileConverter<BudgetFileContent>();
      
      encryptedConverter.setView(
          new PasswordPromptView(parentView)
      );
      
      encryptedConverter.setEncryptionBridge(bridge);

      encryptedConverter.setJSonBridge(
          new JSonBridge<BudgetFileContent>()
      );
      
      encryptedConverter.setFileSystemBridge(
          new FileSystemBridge()
      );

      availableConverters.put("epf", encryptedConverter);
    }
    
    return availableConverters;
  }
  
  private static IFileHandlerView buildBudgetView(
      PersonalFinancierView parentView, 
      HashMap<String, IObjectFileConverter<BudgetFileContent>> converters) {

    return new FileHandlerView(
        parentView.getWindowFrame(),
        "Personal Financier Files",
        converters.keySet().toArray(new String[0])  // new String[0] needed for valid typecast.
    );
  }
  
  private static IPreferenceItem<String> buildBudgetPreferenceItem() {
    return PreferenceItemBuilder.buildBudgetDirectoryPreferenceItem();
  }
  
  private static IObjectFileConverter<BudgetFileContent> buildBudgetFileConverter(
      HashMap<String, IObjectFileConverter<BudgetFileContent>> converters) {
    
    StrategicFileConverter<BudgetFileContent> strategicConverter = 
        new StrategicFileConverter<BudgetFileContent>();

    strategicConverter.setAdapterMap(converters);
   
    return strategicConverter;
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
    
    handler.setAppMessagePresenter(MSG_PRESENTER);
    
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
    
    JSonObjectFileConverter<InflationFileContent> jsonConverter = 
        new JSonObjectFileConverter<InflationFileContent>();
    
    jsonConverter.setJSonBridge(
        new JSonBridge<InflationFileContent>()
    );
    
    jsonConverter.setFileSystemBridge(
        new FileSystemBridge()
    );
    
    return jsonConverter; 
  }
  
  private static IPreferenceItem<String> buildInflationPreferenceItem() {
    return PreferenceItemBuilder.buildInflationDirectoryPreferenceItem();
  }

}
