package blacksmyth.general.file;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.ArgumentCaptor;


import blacksmyth.general.MaskedLogger;

public class MaskedLoggerTest {
  
  private Logger spyLogger = mock(Logger.class);
  final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

  
  @Test
  public void testNonsensitiveInfoString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "There are no potentially sensative numbers in this string";
    
    loggerUnderTest.info(stringUnderTest);
    
    verify(spyLogger, times(1)).info(stringUnderTest);
  }

  @Test
  public void testSimpleSensitiveInfoString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "BSB: 123-456";
    
    loggerUnderTest.info(stringUnderTest);
    
    verify(spyLogger, times(1)).info("BSB: 1**-**6");
  }
  
  @Test
  public void testeMultipleSensitiveInfoString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "BSB: 123-456, Account: 444-444-444";
    String stringExpected  = "BSB: 1**-**6, Account: 4**-***-**4";
    
    loggerUnderTest.info(stringUnderTest);
    
    verify(spyLogger, times(1)).info(stringExpected);
  }

  @Test
  public void testeMultipleRepeatingSensitiveInfoString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "BSB: 123-456, Account: 123-456-789";
    String stringExpected  = "BSB: 1**-**6, Account: 1**-***-**9";
    
    loggerUnderTest.info(stringUnderTest);
    
    verify(spyLogger, times(1)).info(stringExpected);
  }
  
  @Test
  public void testeMultipleRepeatingSensitiveDebugString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "BSB: 123-456, Account: 123-456-789";
    String stringExpected  = "BSB: 1**-**6, Account: 1**-***-**9";
    
    loggerUnderTest.debug(stringUnderTest);
    
    verify(spyLogger, times(1)).debug(stringExpected);
  }
  
  @Test
  public void testeMultipleRepeatingSensitiveWarnString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "BSB: 123-456, Account: 123-456-789";
    String stringExpected  = "BSB: 1**-**6, Account: 1**-***-**9";
    
    loggerUnderTest.warn(stringUnderTest);
    
    verify(spyLogger, times(1)).warn(stringExpected);
  }

  @Test
  public void testeMultipleRepeatingSensitiveErrorString() {
    MaskedLogger loggerUnderTest = MaskedLogger.wrap(spyLogger);
    
    String stringUnderTest = "BSB: 123-456, Account: 123-456-789";
    String stringExpected  = "BSB: 1**-**6, Account: 1**-***-**9";
    
    loggerUnderTest.error(stringUnderTest);
    
    verify(spyLogger, times(1)).error(stringExpected);
  }
}
