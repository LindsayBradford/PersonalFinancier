package blacksmyth.general.file;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class StrategicFileAdapterTest {
 
  private static IObjectFileConverter<String> adapter0;
  private static IObjectFileConverter<String> adapter1;
  private static IObjectFileConverter<String> adapter2;
  
  private static final String[] FILE_TYPES = new String[] {"0", "1", "2"};
  
  private static final String RESULT = "Result";
  
  private static HashMap<String, IObjectFileConverter<String>> adapterMap;
  private static StrategicFileAdapter<String> testAdapter;
  
  @BeforeClass
  public static void testSetup() {
    
    adapter0 = buildMock(0);
    adapter1 = buildMock(1);
    adapter2 = buildMock(2);
    
    adapterMap = new HashMap<String, IObjectFileConverter<String>>();
    
    adapterMap.put(FILE_TYPES[0], adapter0);
    adapterMap.put(FILE_TYPES[1], adapter1);
    adapterMap.put(FILE_TYPES[2], adapter2);
    
    testAdapter = new StrategicFileAdapter<String>();
    testAdapter.setAdapterMap(adapterMap);
  }

  @SuppressWarnings({ "unchecked", "cast" })
  private static IObjectFileConverter<String> buildMock(int index) {
    IObjectFileConverter<String> mockAdapter = 
        (IObjectFileConverter<String>) mock(IObjectFileConverter.class);

    when(
        mockAdapter.toObjectFromFile(
            any(String.class)
         )
    ).thenReturn(
        RESULT + FILE_TYPES[index]
    );
    
    return mockAdapter;
  }
  
  @Test
  public void ToObjectFromFile_FirstAdapterSelected() {
    String result = testAdapter.toObjectFromFile("test.0");
    
    assertTrue(
        result.equals(RESULT + FILE_TYPES[0])
    );
  }

  @Test
  public void ToFileFromObject_FirstAdapterSelected() {
    testAdapter.toFileFromObject("test.0", "fart");
    verify(adapter0).toFileFromObject("test.0", "fart");
  }
  
  @Test
  public void ToObjectFromFile_MiddleAdapterSelected() {
    String result = testAdapter.toObjectFromFile("test.1");

    assertTrue(
        result.equals(RESULT + FILE_TYPES[1])
    );
  }

  @Test
  public void ToFileFromObject_MiddleAdapterSelected() {
    testAdapter.toFileFromObject("test.1", "tart");
    verify(adapter1).toFileFromObject("test.1", "tart");
  }
  
  @Test
  public void ToObjectFromFile_LastAdapterSelected() {
    String result = testAdapter.toObjectFromFile("test.2");
    
    assertTrue(
        result.equals(RESULT + FILE_TYPES[2])
    );
  }

  @Test
  public void ToFileFromObject_LastAdapterSelected() {
    testAdapter.toFileFromObject("test.2", "cart");
    verify(adapter2).toFileFromObject("test.2", "cart");
  }
}
