package com.myproject.springboot.validation.util;

import java.util.Set;
import javax.validation.ConstraintViolation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtil {

    public static <T> void debug(Set<ConstraintViolation<T>> constraintViolations) {

        if (constraintViolations==null) {
            return;
        }
        
        constraintViolations.forEach(r -> {
            log.debug("Message:{}, InvalidValue:{}, PropertyPath:{}, Class:{}", r.getMessage(), r.getInvalidValue(), r
                    .getPropertyPath(), r.getRootBeanClass());
        });

}
}
