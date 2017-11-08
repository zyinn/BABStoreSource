package com.sumscope.bab.store.commons.util;

import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by fan.bai on 2016/12/26.
 * Hibernate数据校验工具类
 */
public final class ValidationUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtil() {
    }

    /**
     * 使用Hibernate提供的数据校验工具对模型进行数据校验，如果有校验错误则转换为标准的ValidationExceptionDetails类以
     * 支持向前端发送信息。本方法并不直接抛出验证异常
     *
     * @param model 需要校验的模型，模型内的字段需要添加校验标注，比如@NotNull等
     * @param <T>   model的类型，调用时无需填写
     * @return 一个非null的列表对象，如果非空则表明有校验错误。
     */
    private static <T> List<ValidationExceptionDetails> validateModelAndCollectConstraintsViolations(T model) {
        Set<ConstraintViolation<T>> validate = validator.validate(model, Default.class);
        List<ValidationExceptionDetails> detailsList = new ArrayList<>();
        if (validate.size() > 0) {
            for (ConstraintViolation cv : validate) {
                Path propertyPath = cv.getPropertyPath();
                String message = cv.getMessage();
                ValidationExceptionDetails details = new ValidationExceptionDetails(GeneralValidationErrorType.DATA_INVALID,
                        propertyPath.toString(), message);
                detailsList.add(details);
            }
        }
        return detailsList;

    }

    /**
     * 验证数据是否合法，如果不合法则直接抛出验证异常错误。使用时请注意，如果需要做进一步的数据验证，请使用本工具的
     * {@code validateModelAndCollectConstraintsViolations}方法
     *
     * @param model 需要校验的模型，模型内的字段需要添加校验标注，比如@NotNull等
     * @param <T>   model的类型，调用时无需填写
     */
    public static <T> void validateModel(T model) {
        List<ValidationExceptionDetails> validationExceptionDetailses = ValidationUtil.validateModelAndCollectConstraintsViolations(model);
        setValidationException(validationExceptionDetailses);
    }

    private static void setValidationException(List<ValidationExceptionDetails> validationExceptionDetailses) {
        if (validationExceptionDetailses.size() > 0) {
            throw new ValidationException(validationExceptionDetailses);
        }
    }

    public static <T> List<ValidationExceptionDetails> validateModelExceptionList(T model,int index) {
        return ValidationUtil.validateModelAndCollectConstraintsViolationsIndex(model,index);
    }

    private static <T> List<ValidationExceptionDetails> validateModelAndCollectConstraintsViolationsIndex(T model, int index) {
        Set<ConstraintViolation<T>> validate = validator.validate(model, Default.class);
        List<ValidationExceptionDetails> detailsList = new ArrayList<>();
        if (validate.size() > 0) {
            for (ConstraintViolation cv : validate) {
                Path propertyPath = cv.getPropertyPath();
                String message = cv.getMessage();
                ValidationExceptionDetails details = new ValidationExceptionDetails(GeneralValidationErrorType.DATA_INVALID,
                       propertyPath.toString(), "第"+index+"行,"+ message);
                detailsList.add(details);
            }
        }
        return detailsList;

    }
}
