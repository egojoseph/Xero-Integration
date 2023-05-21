package com.wayapay.xerointegration.validation;

import com.google.gson.Gson;
import com.wayapay.xerointegration.dto.waya.response.ValidationPayload;
import com.wayapay.xerointegration.dto.waya.response.WayaTransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Slf4j
@Component
public class ModelValidator {

    @Autowired
    private MessageSource messageSource;

    private static final Gson JSON = new Gson();

    public ValidationPayload doModelValidation(Object payload) {

        ValidationPayload validationPayload = ValidationPayload.getInstance();

        List<Field> fields = List.of(payload.getClass().getDeclaredFields());
        List<String> errorMessageList = new ArrayList<>();

        fields.forEach(field -> {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(payload);
            } catch (IllegalAccessException e) {
                log.error("Error occurred while trying to get field value for model validation. Reason: {}", e.getMessage());
                throw new RuntimeException(e);
            }

            List<Annotation> annotations = List.of(field.getDeclaredAnnotations());
            for (Annotation annotation : annotations) {
                Class<?> annotationClass = annotation.annotationType();
                if (annotationClass.isAssignableFrom(AssertFalse.class)) {
                    if (fieldValue instanceof Boolean && Objects.equals((Boolean) fieldValue, Boolean.TRUE)) {
                        errorMessageList.add(field.getAnnotation(AssertFalse.class).message());
                    }
                } else if (annotationClass.isAssignableFrom(AssertTrue.class)) {
                    if (fieldValue instanceof Boolean && Objects.equals((Boolean) fieldValue, Boolean.FALSE)) {
                        errorMessageList.add(field.getAnnotation(AssertTrue.class).message());
                    }
                } else if (annotationClass.isAssignableFrom(Pattern.class)) {
                    Pattern pattern = field.getAnnotation(Pattern.class);
                    String regex = pattern.regexp();
                    if (fieldValue == null || !java.util.regex.Pattern.matches(regex, String.valueOf(fieldValue))) {
                        errorMessageList.add(field.getAnnotation(Pattern.class).message());
                    }
                } else if (annotationClass.isAssignableFrom(NotNull.class)) {
                    if (fieldValue == null) {
                        errorMessageList.add(field.getAnnotation(NotNull.class).message());
                    }
                } else if (annotationClass.isAssignableFrom(Null.class)) {
                    if (fieldValue != null) {
                        errorMessageList.add(field.getAnnotation(Null.class).message());
                    }
                } else if (annotationClass.isAssignableFrom(NotEmpty.class)) {
                    if (fieldValue == null || String.valueOf(fieldValue).isEmpty()) {
                        errorMessageList.add(field.getAnnotation(NotEmpty.class).message());
                    }
                } else if (annotationClass.isAssignableFrom(NotBlank.class)) {
                    if (fieldValue == null || String.valueOf(fieldValue).isBlank()) {
                        errorMessageList.add(field.getAnnotation(NotBlank.class).message());
                    }
                }

            }
        });

        if (!errorMessageList.isEmpty()) {
            String completeErrorMessage = String.join(", ", errorMessageList);
            WayaTransactionResponse errorResponse = new WayaTransactionResponse();
            errorResponse.setCode(404);
            errorResponse.setStatus(false);
            errorResponse.setMessage(messageSource.getMessage("messages.request.bad-request", null, Locale.ENGLISH));
            errorResponse.setData(completeErrorMessage);
            validationPayload.setErrorJson(JSON.toJson(errorResponse));
            validationPayload.setHasError(true);
            validationPayload.setExtraItem(null);
            return validationPayload;
        }

        validationPayload.setErrorJson(null);
        validationPayload.setHasError(false);
        validationPayload.setExtraItem(null);
        return validationPayload;
    }

}
