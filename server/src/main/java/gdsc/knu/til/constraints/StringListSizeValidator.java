package gdsc.knu.til.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class StringListSizeValidator implements ConstraintValidator<StringListSize, List<String>> {

	private int validMin;
	private int validMax;
	private int validElementMinSize;
	private int validElementMaxSize;
	
	@Override
	public void initialize(StringListSize constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);

		validMin = constraintAnnotation.min();
		validMax = constraintAnnotation.max();
		validElementMinSize = constraintAnnotation.elementMinSize();
		validElementMaxSize = constraintAnnotation.elementMaxSize();
	}
	
	@Override
	public boolean isValid(List<String> value, ConstraintValidatorContext context) {
		int size = value.size();

		System.out.println("test: " + size + ", " + validMin);
		if (size < validMin || validMax < size) {
			return false;
		}
		
		return value.stream().allMatch(str -> 
				validElementMinSize <= str.length() && str.length() <= validElementMaxSize);
	}
}
