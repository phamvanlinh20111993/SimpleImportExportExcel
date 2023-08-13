package input.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import input.validation.utils.ValidateType;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface Type {
	public String pattern() default "";
	
	public ValidateType type() default ValidateType.STRING;
}
