package com.example.smartorder.common.valid;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EnumValidator.class)
@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumTypeValid {
	String message() default "Invalid value. This is not permitted.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	Class<? extends java.lang.Enum<?>> enumClass();
	boolean ignoreCase() default false;
}
