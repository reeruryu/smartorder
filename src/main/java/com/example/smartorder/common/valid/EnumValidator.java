package com.example.smartorder.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumTypeValid, String> {

	private EnumTypeValid annotation;

	@Override
	public void initialize(EnumTypeValid constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result = false;
		Object[] enumValues = this.annotation.enumClass().getEnumConstants();
		if (enumValues != null) {
			for (Object enumValue : enumValues) {
				if (value.equals(enumValue.toString())
					|| (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
}
