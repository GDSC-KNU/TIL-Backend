package gdsc.knu.til.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ListSizeValidator implements ConstraintValidator<ListSize, List<?>> {

	private int validMin;
	private int validMax;
	
	@Override
	public void initialize(ListSize constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);

		validMin = constraintAnnotation.min();
		validMax = constraintAnnotation.max();
	}
	
	@Override
	public boolean isValid(List value, ConstraintValidatorContext context) {
		int size = value.size();
		
		return validMin <= size && size <= validMax;
	}
}
