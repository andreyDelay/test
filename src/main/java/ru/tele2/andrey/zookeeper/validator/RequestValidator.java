package ru.tele2.andrey.zookeeper.validator;

import ru.tele2.andrey.zookeeper.enumeration.ProductResponseStatus;
import ru.tele2.andrey.zookeeper.exception.ProductException;
import ru.tele2.andrey.zookeeper.model.ProductRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequestValidator implements ConstraintValidator<ValidateRequest, Object> {
    @Override
    public boolean isValid(Object request, ConstraintValidatorContext constraintValidatorContext) {
        if (request instanceof ProductRequest) {
            validate((ProductRequest) request);
        }
        return true;
    }

    private void validate(ProductRequest request) {
        if (Long.signum(request.getId()) < 1) {
            throw new ProductException(request, ProductResponseStatus.BP_ERROR_PARAMS);
        }
    }
}
