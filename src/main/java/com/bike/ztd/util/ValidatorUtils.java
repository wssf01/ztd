package com.bike.ztd.util;

import com.bike.ztd.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ValidatorUtils {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        log.info("========>>>>> Init ValidatorFactory over");
    }

    /**
     * 验证实体内容是否正确
     *
     * @param obj
     * @return
     */
    public static Map<String, String> validator(Object obj) {

        Map<String, String> map = new HashMap<>();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<Object> con : constraintViolations) {
                map.put(con.getPropertyPath().toString(), con.getMessage());
            }
        }
        return map;
    }

    /**
     * 校验输入的对象参数是否正确.
     *
     * @param obj 校验的对象.
     */
    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        if (!constraintViolations.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<Object> con : constraintViolations) {
                sb.append(con.getPropertyPath().toString())
                        .append(":")
                        .append(con.getMessage())
                        .append(";");
            }
            throw new BusinessException(ErrorEnum.PARAMETER_ERROR.getErrorCode(), sb.toString());
        }
    }

}
