package com.myproject.springboot.web.controller;

import java.lang.reflect.Method;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.myproject.springboot.web.property.MyProperties;
import com.myproject.springboot.web.service.MyService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Sample of Mock MVC
 */
@Slf4j
public class MyPropertiesMockControllerTest {

    @Mock
    private MyService myService;

    private MyPropertiesController myPropertiesController;
    private MockMvc mockMvc;

    private static final String baseUrl = "";
    protected ObjectMapper objectMapper = objectMapper();

    protected MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter =
            mappingJacksonHttpMessageConverter();

    // set up the controllerAdvice
    protected ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {

            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
                    Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(ApiExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new ApiExceptionHandler(), method);
            }

        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule("AppModule", Version.unknownVersion());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    protected String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        myPropertiesController = new MyPropertiesController();


        mockMvc = MockMvcBuilders.standaloneSetup(myPropertiesController)
                                 .setMessageConverters(mappingJacksonHttpMessageConverter)
                                 .setHandlerExceptionResolvers(createExceptionResolver()).build();

    }

    @AfterMethod
    public void teardown() {
        Mockito.reset();
    }

    @Test
    @SneakyThrows
    public void myPropsProvided_postMyProps_successful() {

        // given

        // replace the properties with a custom object
        ReflectionTestUtils.setField(myPropertiesController, "myService", myService);

        MyProperties myProperties = new MyProperties();
        myProperties.setProp1("prop123");
        myProperties.setBooleanProp(true);
        when(myService.getProperties()).thenReturn(myProperties);

        // payload to send
        MyProperties request = createMyProperties();

        String json = toJson(request);

        // when
        log.info("Send > " + json);
        mockMvc.perform(post(baseUrl + "/myprops").contentType(MediaType.APPLICATION_JSON).content(json)
                                                  .accept(MediaType.APPLICATION_JSON))

               .andDo(print()).andExpect(status().isCreated())
               .andExpect(jsonPath("$.prop1", is("prop123")))
               .andExpect(jsonPath("$.booleanProp", is(true)));;

        verify(myService, times(1)).getProperties();

    }

    @Test
    @SneakyThrows
    public void myPropsProvided_getMyProps_successful() {

        // given
        // replace the properties with a custom object
        ReflectionTestUtils.setField(myPropertiesController, "myService", myService);
        MyProperties myProperties = new MyProperties();
        myProperties.setProp1("prop123");
        myProperties.setBooleanProp(true);
        when(myService.getProperties()).thenReturn(myProperties);
        // when
        mockMvc.perform(get(baseUrl + "/myprops")
                                                  .accept(MediaType.APPLICATION_JSON))

               .andDo(print()).andExpect(status().isOk())
               .andExpect(jsonPath("$.prop1", is("prop123")))
               .andExpect(jsonPath("$.booleanProp", is(true)));;

        verify(myService, times(1)).getProperties();
    }
    private MyProperties createMyProperties() {

        MyProperties myProperties = new MyProperties();
        myProperties.setProp1("prop123");
        myProperties.setBooleanProp(true);
        return myProperties;

    }

}
