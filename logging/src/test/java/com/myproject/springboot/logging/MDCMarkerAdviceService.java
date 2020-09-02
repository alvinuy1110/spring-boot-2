package com.myproject.springboot.logging;

import java.util.Date;

import org.slf4j.MDC;

import com.myproject.springboot.logging.aspect.annotation.MDCMarker;
import com.myproject.springboot.logging.aspect.annotation.MDCValue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by user on 13/08/20.
 */
public class MDCMarkerAdviceService {

    
    
    @MDCMarker({ @MDCValue(value = TestConstants.MDC_KEY1, content = TestConstants.MDC_VALUE1),
            @MDCValue(value = TestConstants.MDC_KEY2, content = TestConstants.MDC_VALUE2),
            @MDCValue(TestConstants.MDC_KEY_EMPTY) })
    public void serviceWithMethod() {
        assertThat(MDC.get(TestConstants.MDC_KEY1)).isEqualTo(TestConstants.MDC_VALUE1);
        assertThat(MDC.get(TestConstants.MDC_KEY2)).isEqualTo(TestConstants.MDC_VALUE2);
        assertThat(MDC.get(TestConstants.MDC_KEY_EMPTY)).isEqualTo("");
    }

    @MDCMarker
    public void serviceWithMethodArgs(@MDCValue("ObjectValue") Object objectParam,
            @MDCValue("StringValue") String stringParam, @MDCValue("IntegerValue") Integer integerParam,
            @MDCValue("LongValue") Long longParam, @MDCValue("FloatValue") Float floatParam,
            @MDCValue("DoubleValue") Double doubleParam, @MDCValue("BooleanValue") Boolean booleanParam,
            String ignoredParam) {
        assertThat(MDC.get("ObjectValue")).isEqualTo(String.valueOf(objectParam));
        assertThat(MDC.get("StringValue")).isEqualTo(stringParam);
        assertThat(MDC.get("IntegerValue")).isEqualTo(String.valueOf(integerParam));
        assertThat(MDC.get("LongValue")).isEqualTo(String.valueOf(longParam));
        assertThat(MDC.get("FloatValue")).isEqualTo(String.valueOf(floatParam));
        assertThat(MDC.get("DoubleValue")).isEqualTo(String.valueOf(doubleParam));
        assertThat(MDC.get("BooleanValue")).isEqualTo(String.valueOf(booleanParam));
        assertThat(MDC.getCopyOfContextMap().values()).doesNotContain(ignoredParam);
    }

    @MDCMarker
    public void serviceWithMethodArgsFormat(@MDCValue(value = "DateValue", format = "%tF") Date dateParam) {
        assertThat(MDC.get("DateValue")).isEqualTo("2001-04-16");
    }
        
}
