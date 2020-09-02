package com.myproject.springboot.logging.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;

import com.myproject.springboot.logging.aspect.annotation.MDCMarker;
import com.myproject.springboot.logging.aspect.annotation.MDCValue;

@Aspect
public class MDCMarkerAspect {

    @Around("methodsAnnoatatedWithMethodWithMDCMarker()")
    public Object extendMDC(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        MDCMarker methodAnnotation = method.getAnnotation(MDCMarker.class);

        Map<String, String> origContextMap = MDC.getCopyOfContextMap();
        try {

            addMethodDefinedValues(methodAnnotation);
            addParameterDefinedValues(method.getParameters(), pjp.getArgs());
            return pjp.proceed();
        }
        finally {
            removeMethodDefinedValues(methodAnnotation, origContextMap);
            removeParameterDefinedValues(method.getParameters(), origContextMap);
            clear(origContextMap);
        }
        

    }

    private void clear(Map<String, String> origContextMap) {
        if (origContextMap!=null) {
            origContextMap.clear();
            origContextMap = null;
        }

    }
    @Pointcut(value = "@annotation(com.myproject.springboot.logging.aspect.annotation.MDCMarker)")
    private void methodsAnnoatatedWithMethodWithMDCMarker() {
        // defines pointcut for methods annotated with MDCMarker
    }

    private void addMethodDefinedValues(MDCMarker methodAnnotation) {
        for (MDCValue value : methodAnnotation.value()) {
            MDC.put(value.value(), value.content());

        }
    }

    private void removeMethodDefinedValues(MDCMarker methodAnnotation, Map<String, String> origContextMap) {
        for (MDCValue value : methodAnnotation.value()) {
            removeOrReplace(value, origContextMap);
        }
    }

    private void addParameterDefinedValues(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            MDCValue value = parameter.getAnnotation(MDCValue.class);

            addMDCValue(value, args[i]);
        }
    }

    private void addMDCValue(MDCValue value, Object object) {
        if (value != null) {
            String key = value.value();
            String format = value.format();
            if (format!=null && !format.isEmpty()){
                MDC.put(key, String.format(format, object));
            } else {
                MDC.put(key, String.valueOf(object));
            }
        }
    }
    private void removeParameterDefinedValues(Parameter[] parameters, Map<String, String> origContextMap) {
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            MDCValue value = parameter.getAnnotation(MDCValue.class);
            removeOrReplace(value, origContextMap);
        }
    }

    private void removeOrReplace(MDCValue value, Map<String, String> origContextMap) {
        if (value != null) {

            String key = value.value();

            if (origContextMap != null && origContextMap.containsKey(key)) {
                MDC.put(key, origContextMap.get(key));
            }
            else {
                MDC.remove(value.value());
            }
        }
    }
}
