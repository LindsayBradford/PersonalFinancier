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

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class StrategicFileConverterTest {
 
  private static IObjectFileConverter<String> svgConverter;
  private static IObjectFileConverter<String> pdfConverter;
  private static IObjectFileConverter<String> pngConverter;
  
  private static final String[] FILE_TYPES = new String[] {"svg", "pdf", "png"};
  
  private static final int SVG_INDEX = 0;
  private static final int PDF_INDEX = 1;
  private static final int PNG_INDEX = 2;
  
  private static final String RESULT = "Result";
  
  private static HashMap<String, IObjectFileConverter<String>> converterMap;
  private static StrategicFileConverter<String> testConverter;
  
  @BeforeClass
  public static void testSetup() {
    
    svgConverter = buildMockConverter(SVG_INDEX);
    pdfConverter = buildMockConverter(PDF_INDEX);
    pngConverter = buildMockConverter(PNG_INDEX);
    
    converterMap = new HashMap<String, IObjectFileConverter<String>>();
    
    converterMap.put(FILE_TYPES[SVG_INDEX], svgConverter);
    converterMap.put(FILE_TYPES[PDF_INDEX], pdfConverter);
    converterMap.put(FILE_TYPES[PNG_INDEX], pngConverter);
    
    testConverter = new StrategicFileConverter<String>();
    testConverter.setAdapterMap(converterMap);
  }

  @SuppressWarnings({ "unchecked", "cast" })
  private static IObjectFileConverter<String> buildMockConverter(int index) {
    IObjectFileConverter<String> mockConverter = 
        (IObjectFileConverter<String>) mock(IObjectFileConverter.class);

    when(
        mockConverter.toObjectFromFile(
            any(String.class)
         )
    ).thenReturn(
        RESULT + FILE_TYPES[index]
    );
    
    return mockConverter;
  }
  
  @Test
  public void ToObjectFromFile_SvgFilenameSupplied_SvgConverterUsed() {
    String result = testConverter.toObjectFromFile("test.svg");
    
    assertTrue(
        result.equals(RESULT + FILE_TYPES[SVG_INDEX])
    );
  }

  @Test
  public void ToFileFromObject_SvgFilenameSupplied_SvgConverterUsed() {
    testConverter.toFileFromObject("test.svg", "fart");
    verify(svgConverter).toFileFromObject("test.svg", "fart");
  }
  
  @Test
  public void ToObjectFromFile_PdfFilenameSupplied_PdfConverterUsed() {
    String result = testConverter.toObjectFromFile("test.pdf");

    assertTrue(
        result.equals(RESULT + FILE_TYPES[PDF_INDEX])
    );
  }

  @Test
  public void ToFileFromObject_PdfFilenameSupplied_PdfConverterUsed() {
    testConverter.toFileFromObject("test.pdf", "tart");
    verify(pdfConverter).toFileFromObject("test.pdf", "tart");
  }
  
  @Test
  public void ToObjectFromFile_PngFilenameSupplied_PngConverterUsed() {
    String result = testConverter.toObjectFromFile("test.png");
    
    assertTrue(
        result.equals(RESULT + FILE_TYPES[PNG_INDEX])
    );
  }

  @Test
  public void ToFileFromObject_PngFilenameSupplied_PngConverterUsed() {
    testConverter.toFileFromObject("test.png", "cart");
    verify(pngConverter).toFileFromObject("test.png", "cart");
  }
  
  @Test(expected=AssertionError.class)
  public void ToObjectFromFile_UnsupportedFilenameSupplied_AssertionError() {
    testConverter.toObjectFromFile("test.crap");
  }

  @Test(expected=AssertionError.class)
  public void ToFileFromObject_UnsupportedFilenameSupplied_AssertionError() {
    testConverter.toFileFromObject("test.crap", "cart");
  }
}
