package ch.hearc.cafheg.business.allocations;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"classpath:features/parentDroitAllocation.feature"},
    glue = {"ch.hearc.cafheg.business.allocations"})
public class RunCucumberTest {

}
