package com.myproject.springboot.validation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.validation.domain.ApiError;
import com.myproject.springboot.validation.domain.Student;
import com.myproject.springboot.validation.util.TestDataUtil;
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
@Test
@Slf4j
public class ValidationControllerTest extends AbstractTestNGSpringContextTests {

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
//        url = "http://localhost:" + this.port + serverContextPath + "/student";
    }

    @Test(enabled = false)
    public void propertiesLoaded_getMyProps_http200() {
        log.info("Testing Get ");
        url = "http://localhost:" + this.port + serverContextPath + "/validatestudent";
        // execute the http request
        ResponseEntity<Student> entity = this.testRestTemplate.getForEntity(url, Student.class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // get the body
        Student student = entity.getBody();
        assertThat(student).isNotNull();


    }

    public void studentValid_post_http201() {
        log.info("Testing Post ");
        url = "http://localhost:" + this.port + serverContextPath + "/student";
        // create the payload to be submitted
          Student studentInput = TestDataUtil.createValidStudent();
        
        // execute the http request
        ResponseEntity<Student> entity =
                this.testRestTemplate.postForEntity(url, studentInput, Student.class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // get the body
        Student student = entity.getBody();
        assertThat(student).isNotNull();

    }

    public void studentInvalid_post_http400() {
        log.info("Testing Post ");
        url = "http://localhost:" + this.port + serverContextPath + "/student";
        // create the payload to be submitted
        Student studentInput = Student.builder().build();
        // execute the http request
        ResponseEntity<ApiError> entity =
                this.testRestTemplate.postForEntity(url, studentInput, ApiError.class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        
        // get the body
        ApiError  apiError = entity.getBody();
        assertThat(apiError).isNotNull();
        
        log.info("ApiError: {}", apiError);
        log.info("ApiError Details: {}", apiError.getDetails());

    }


    private <T> HttpEntity<T> createEntityWithHeaders(T jsonBody, Map<String, String> headerValues) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        for (Map.Entry<String, String> entry : headerValues.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        HttpEntity<T> entity = new HttpEntity<T>(jsonBody, headers);
        return entity;
    }
    public void studentInvalidLanguageFrench_post_http400() {
        log.info("Testing Post ");
        url = "http://localhost:" + this.port + serverContextPath + "/student";
        // create the payload to be submitted
        Student studentInput = Student.builder().build();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Language","fr");
        HttpEntity<Student> requestEntity = createEntityWithHeaders(studentInput, headers);
        
        // execute the http request
        ResponseEntity<ApiError> entity =
                this.testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, ApiError.class);

        // check http status
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);


        // get the body
        ApiError  apiError = entity.getBody();
        assertThat(apiError).isNotNull();

        log.info("ApiError: {}", apiError);
        log.info("ApiError Details: {}", apiError.getDetails());

        assertThat(apiError.getDetails().get(0)).contains("Details: ne peut pas être vide");
    }
}
