package gdsc.knu.til.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ListSizeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListSize {
	
	String message() default "List size is not valid";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

	/**
	 * @return 리스트 원소의 최소 개수
	 */
	int min() default 0;

	/**
	 * @return 리스트 원소의 최대 개수
	 */
	int max() default Integer.MAX_VALUE;
}
