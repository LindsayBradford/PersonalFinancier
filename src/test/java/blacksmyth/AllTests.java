package blacksmyth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import blacksmyth.general.AllBlacksmythGeneralTests;
import blacksmyth.personalfinancier.AllPersonalFinancierTests;

@RunWith(Suite.class)
@SuiteClasses({ 
  AllBlacksmythGeneralTests.class, 
  AllPersonalFinancierTests.class
})

public class AllTests {

}
