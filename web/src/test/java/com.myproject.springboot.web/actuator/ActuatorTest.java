package com.myproject.springboot.web.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.web.property.MyProperties;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a real integration test.  Spring will boot the application just like a how it will normally run.  Custom
 * properties can be injected to influence the behavior of the app.  An actual HTTP client will be used to hit the
 * Controllers/ Endpoints.
 *
 * Things to note:
 *
 * * AbstractTestNGSpringContextTests - to hook TestNG and Spring together
 * * @Test - to trigger TestNG
 * * @SpringBootTest - to load spring like a regular application.
 *     * SpringBootTest.WebEnvironment.RANDOM_PORT - to assign random port so testing wont fail
 * * @TestPropertySource - to tell Spring which properties to use. Using "classpath" delegates to spring
 * ResourceUtils for path resolution. By providing "properties", directly inject the property
 *
 * * TestRestTemplate - to allow client to hit the actual Controller
 * * @LocalServerPort - allows access to the port assigned during testing
 * * @BeforeMethod - call before every test
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.health.status.http-mapping.UNKNOWN=503","management.endpoints.web"
        + ".base-path=/actuator"})
@Test
@Slf4j
public class ActuatorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String serverContextPath;

    @Value("${management.endpoints.web.base-path}")
    private String actuatorPath;

    private String actuatorUrl;

    // Will
    @BeforeMethod
    public void setup() {
        // we assemble the url to be hit
        actuatorUrl = "http://localhost:" + this.port + serverContextPath + actuatorPath;
    }

    public void unknownStatus_getHealth_http503() {
        log.info("Testing Check Health");

        // execute the http request
        ResponseEntity<String> entity = this.testRestTemplate.getForEntity(actuatorUrl+"/health", String
                .class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);

        // get the body
        String json = entity.getBody();
        assertThat(json).isNotNull();
        assertThat(json).contains("\"status\":\"UNKNOWN\"");

    }

}
