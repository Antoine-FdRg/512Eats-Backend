package team.k;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;
import io.cucumber.java.ParameterType;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;


@Suite
@IncludeEngines("cucumber")

@SelectClasspathResource("features/team/k")

@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "team.k")
public class RunCucumberTest {
    // will run all features found on the classpath
    // in the same package as this class
    @ParameterType("STUDENT|CAMPUS_EMPLOYEE")
    public Role role(String role) {
        return Role.valueOf(role);
    }

    @ParameterType("CREATED|PAID|PLACED|DELIVERING|COMPLETED|DISCOUNT_USED|CANCELED")
    public OrderStatus status(String status) {
        return OrderStatus.valueOf(status);
    }
}