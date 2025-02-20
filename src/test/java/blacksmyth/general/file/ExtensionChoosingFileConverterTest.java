/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general.file;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.EnumSet;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

public class ExtensionChoosingFileConverterTest {
 
  private static final String EXPECTED_RESULT_PREFIX = "prefix-";
  private static final String DUMMY_CONTENT = "dummyContent";
  
  private static HashMap<String, IObjectFileConverter<String>> converterMap;
  private static ExtensionChoosingFileConverter<String> testConverter;

  @Mock
  private static IObjectFileConverter<String> defaultMockConverter;
  
  enum FileType {
	  SVG, PDF, PNG;
	  
	  public String key() {
		  return name().toLowerCase();
	  }
  }
  
  @BeforeClass
  public static void testSetup() {
		converterMap = new HashMap<String, IObjectFileConverter<String>>();
		
		EnumSet.allOf(FileType.class).forEach( 
				ft -> converterMap.put(ft.key(), buildMockConverter(ft.key()))
		);
	    
	    testConverter = new ExtensionChoosingFileConverter<String>();
	    testConverter.setConverterMap(converterMap);	
	    testConverter.setDefaultConverter(defaultMockConverter);
  }
  
  @SuppressWarnings({ "unchecked", "cast" })
  private static IObjectFileConverter<String> buildMockConverter(String fileType) {
    IObjectFileConverter<String> mockConverter = 
        (IObjectFileConverter<String>) mock(IObjectFileConverter.class);

    when(
        mockConverter.toObjectFromFile(
            any(String.class)
         )
    ).thenReturn(
        EXPECTED_RESULT_PREFIX + fileType
    );
    
    return mockConverter;
  }
  
  @Test
  public void ToObjectFromFile_SvgFilenameSupplied_SvgConverterUsed() {
    String result = testConverter.toObjectFromFile("test.svg");
    assertThat(result, is(expectedValueFor(FileType.SVG)));
  }

  @Test
  public void ToFileFromObject_SvgFilenameSupplied_SvgConverterUsed() {
    testConverter.toFileFromObject("test.svg", DUMMY_CONTENT);
    verify(mapEntryFor(FileType.SVG)).toFileFromObject("test.svg", DUMMY_CONTENT);
  }
  
  @Test
  public void ToObjectFromFile_PdfFilenameSupplied_PdfConverterUsed() {
    String result = testConverter.toObjectFromFile("test.pdf");
    assertThat(result, is(expectedValueFor(FileType.PDF)));
  }

  @Test
  public void ToFileFromObject_PdfFilenameSupplied_PdfConverterUsed() {
    testConverter.toFileFromObject("test.pdf", DUMMY_CONTENT);
    verify(mapEntryFor(FileType.PDF)).toFileFromObject("test.pdf", DUMMY_CONTENT);
  }
  
  @Test
  public void ToObjectFromFile_PngFilenameSupplied_PngConverterUsed() {
    String result = testConverter.toObjectFromFile("test.png");
    assertThat(result, is(expectedValueFor(FileType.PNG)));
  }
  
  @Test
  public void ToObjectFromFile_PdfSvgFilenameSupplied_SvgConverterUsed() {
    String result = testConverter.toObjectFromFile("test.pdf.svg");
    assertThat(result, is(expectedValueFor(FileType.SVG)));
  }
  
  private String expectedValueFor(FileType fileType) {
	  return EXPECTED_RESULT_PREFIX + fileType.key();
  }

  @Test
  public void ToFileFromObject_PngFilenameSupplied_PngConverterUsed() {
    testConverter.toFileFromObject("test.png", DUMMY_CONTENT);
    verify(mapEntryFor(FileType.PNG)).toFileFromObject("test.png", DUMMY_CONTENT);
  }
  
  private IObjectFileConverter<String> mapEntryFor(FileType fileType) {
	  return converterMap.get(fileType.key());
  }
  
  public void ToObjectFromFile_UnsupportedFilenameSupplied_DefaultUsed() {
    testConverter.toObjectFromFile("test.crap");
    verify(defaultMockConverter).toObjectFromFile("test.crap");
  }

  public void ToFileFromObject_UnsupportedFilenameSupplied_DefaultUsed() {
    testConverter.toFileFromObject("test.crap", "cart");
    verify(defaultMockConverter).toFileFromObject("test.crap", "cart");
  }
}
