package com.myproject.springboot.jpa.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.myproject.springboot.jpa.domain.Student;
import com.myproject.springboot.jpa.service.StudentService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is to demo the unit testing of controller
 *
 * Things to note:
 *
 * @Mock - indicates this object is to be mocked
 * @InjectMocks - the main object, where the @Mock objects will be injected into
 */
public class StudentControllerTest extends AbstractControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() {

        // assign mock annotated objects into @InjectMock
        initMocks(this);
        mockMvc = newMockMvc(studentController);
    }

    @AfterMethod
    public void teardownMethod() {
        Mockito.reset();
    }

    @Test
    public void httpGet_getStudent_httpOk() throws Exception {
        when(studentService.getStudent(any(Long.class))).thenReturn(new Student());

        // Given
        long studentId = 1;

        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        when(studentService.getStudent(any(Long.class))).thenReturn(student);

        // When
        // print allows for debugging of http calls
        ResultActions response =
                mockMvc.perform(get("/students/{id}", studentId).contentType(MediaType.APPLICATION_JSON))
                       .andDo(print());

        // Then
        response.andExpect(status().isOk());
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        response.andExpect(jsonPath("$.id", is(1)));
        response.andExpect(jsonPath("$.firstName", is("John")));
    }

}
