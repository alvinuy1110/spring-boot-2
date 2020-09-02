package com.myproject.springboot.logging;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.logging.aspect.MDCMarkerAspect;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {MDCMarkerAdviceTest.TestAdviceConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class MDCMarkerAdviceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MDCMarkerAdviceService mdcMarkerAdviceService;
    
    @BeforeMethod
    public void setup() {
        MDC.clear();
    }

    @Test
    public void serviceWithAspect_callMethodFixedValues_mdcCleared() {
        assertThat(MDC.get(TestConstants.MDC_KEY1)).isNull();
        assertThat(MDC.get(TestConstants.MDC_KEY2)).isNull();
        assertThat(MDC.get(TestConstants.MDC_KEY_EMPTY)).isNull();
        mdcMarkerAdviceService.serviceWithMethod();

        // then
        assertThat(MDC.get(TestConstants.MDC_KEY1)).isNull();
        assertThat(MDC.get(TestConstants.MDC_KEY2)).isNull();
        assertThat(MDC.get(TestConstants.MDC_KEY_EMPTY)).isNull();
    }

    @Test
    public void serviceWithAspect_callMethodParam_mdcCleared() {
        assertThat(MDC.get("ObjectValue")).isNull();
        assertThat(MDC.get("StringValue")).isNull();
        assertThat(MDC.get("IntegerValue")).isNull();
        assertThat(MDC.get("LongValue")).isNull();
        assertThat(MDC.get("FloatValue")).isNull();
        assertThat(MDC.get("DoubleValue")).isNull();
        assertThat(MDC.get("BooleanValue")).isNull();
        mdcMarkerAdviceService.serviceWithMethodArgs(new Object(), "Test String", 1, 2L, 3.1F, 4.2, true, "Ignored String");
        
        // then
        assertThat(MDC.get("ObjectValue")).isNull();
        assertThat(MDC.get("StringValue")).isNull();
        assertThat(MDC.get("IntegerValue")).isNull();
        assertThat(MDC.get("LongValue")).isNull();
        assertThat(MDC.get("FloatValue")).isNull();
        assertThat(MDC.get("DoubleValue")).isNull();
        assertThat(MDC.get("BooleanValue")).isNull();    }



    @Test
    public void serviceWithAspectPreviousMDCValue_callMethodFixedValues_mdcContainsPreviousValue() {
        
        String origValue= "someValue";
        MDC.put(TestConstants.MDC_KEY1,origValue);
        assertThat(MDC.get(TestConstants.MDC_KEY1)).isEqualTo(origValue);
        assertThat(MDC.get(TestConstants.MDC_KEY2)).isNull();
        assertThat(MDC.get(TestConstants.MDC_KEY_EMPTY)).isNull();
        mdcMarkerAdviceService.serviceWithMethod();

        // then
        assertThat(MDC.get(TestConstants.MDC_KEY1)).isEqualTo(origValue);
        assertThat(MDC.get(TestConstants.MDC_KEY2)).isNull();
        assertThat(MDC.get(TestConstants.MDC_KEY_EMPTY)).isNull();
    }


    @Test
    public void 
    serviceWithAspectPreviousMDCValue_callMethodParam_mdcContainsPreviousValue() {
        String origValue= "someValue";
        MDC.put("StringValue", origValue);
        assertThat(MDC.get("ObjectValue")).isNull();
        assertThat(MDC.get("StringValue")).isEqualTo(origValue);
        assertThat(MDC.get("IntegerValue")).isNull();
        assertThat(MDC.get("LongValue")).isNull();
        assertThat(MDC.get("FloatValue")).isNull();
        assertThat(MDC.get("DoubleValue")).isNull();
        assertThat(MDC.get("BooleanValue")).isNull();
        mdcMarkerAdviceService.serviceWithMethodArgs(new Object(), "Test String", 1, 2L, 3.1F, 4.2, true, "Ignored String");

        // then
        assertThat(MDC.get("ObjectValue")).isNull();
        assertThat(MDC.get("StringValue")).isEqualTo(origValue);
        assertThat(MDC.get("IntegerValue")).isNull();
        assertThat(MDC.get("LongValue")).isNull();
        assertThat(MDC.get("FloatValue")).isNull();
        assertThat(MDC.get("DoubleValue")).isNull();
        assertThat(MDC.get("BooleanValue")).isNull();    }



    @Test
    public void serviceWithAspect_callMethodParamFormat_mdcCleared() {
        assertThat(MDC.get("DateValue")).isNull();
        
        Calendar calendar = Calendar.getInstance();
        
        // the month starts at 0
        calendar.set(2001,3,16);
        
        Date date = calendar.getTime();
        mdcMarkerAdviceService
                .serviceWithMethodArgsFormat(date);

        // then
        assertThat(MDC.get("DateValue")).isNull();
    }

    @Configuration
    @EnableAspectJAutoProxy
    public static class TestAdviceConfig {

        @Bean
        public MDCMarkerAspect mdcMarkerAspect() {
            return new MDCMarkerAspect();
        }

        @Bean
        public MDCMarkerAdviceService mdcMarkerAdviceService() {
            return new MDCMarkerAdviceService();
        }
    }
}
