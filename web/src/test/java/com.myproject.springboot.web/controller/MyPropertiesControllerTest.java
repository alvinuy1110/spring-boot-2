package com.myproject.springboot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@TestPropertySource(properties = {"prop=example"})
@Test
@Slf4j
public class MyPropertiesControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String serverContextPath;
    private String url;

    // Will
    @BeforeMethod
    public void setup() {
        // we assemble the url to be hit
        url = "http://localhost:" + this.port + serverContextPath + "/myprops";
    }

    public void propertiesLoaded_getMyProps_http200() {
        log.info("Testing Get MyProps");

        // execute the http request
        ResponseEntity<MyProperties> entity = this.testRestTemplate.getForEntity(url, MyProperties.class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // get the body
        MyProperties myProperties = entity.getBody();
        assertThat(myProperties).isNotNull();

        assertThat(myProperties.getProp1()).isEqualTo("Property 1");
        assertThat(myProperties.getProp2()).isEqualTo("Property 2");

    }

    public void propertiesLoaded_postMyProps_http201() {
        log.info("Testing Post MyProps");

        // create the payload to be submitted
        MyProperties myPropertiesPayload = new MyProperties();
        myPropertiesPayload.setProp1("inputProp1");
        myPropertiesPayload.setProp2("inputProp2");
        myPropertiesPayload.setBooleanProp(true);

        // execute the http request
        ResponseEntity<MyProperties> entity =
                this.testRestTemplate.postForEntity(url, myPropertiesPayload, MyProperties.class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // get the body
        MyProperties myProperties = entity.getBody();
        assertThat(myProperties).isNotNull();

        assertThat(myProperties.getProp1()).isEqualTo("inputProp1");
        assertThat(myProperties.getProp2()).isEqualTo("inputProp2");
        assertThat(myProperties.isBooleanProp()).isEqualTo(true);

    }
}
