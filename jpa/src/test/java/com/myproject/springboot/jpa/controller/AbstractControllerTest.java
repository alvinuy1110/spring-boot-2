package com.myproject.springboot.jpa.controller;

import java.lang.reflect.Method;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.myproject.springboot.jpa.util.WebUtil;

/**
 * Main test class for testing controllers.  Unit testing only since most of the stuff is Mocked
 */
@SpringBootTest(classes = {MockServletContext.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestExecutionListeners(inheritListeners = false,
        listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class AbstractControllerTest extends AbstractTestNGSpringContextTests {

    protected MockMvc newMockMvc(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller).setViewResolvers(newTestViewResolver())
                              .setMessageConverters(WebUtil.mappingJacksonHttpMessageConverter())
                              .setCustomArgumentResolvers(createPageableHandlerMethodArgumentResolver())
                              .setControllerAdvice(createExceptionResolver()).build();
    }

    private InternalResourceViewResolver newTestViewResolver() {

        // Allows mock MVC to resolve HTML pages in tests
        InternalResourceViewResolver testViewResolver = new InternalResourceViewResolver();
        testViewResolver.setPrefix("/templates/");
        testViewResolver.setSuffix(".html");
        return testViewResolver;
    }

    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {

            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
                    Exception exception) {
                Method method =
                        new ExceptionHandlerMethodResolver(GlobalExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method);
            }
        };
        exceptionResolver.getMessageConverters().add(WebUtil.mappingJacksonHttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }


    protected PageableHandlerMethodArgumentResolver createPageableHandlerMethodArgumentResolver() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true); // will make count from page =1
        resolver.setFallbackPageable(PageRequest.of(1, 20));
        return resolver;
    }
}
