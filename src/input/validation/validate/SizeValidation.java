package input.validation.validate;

import java.util.Collection;
import java.util.Map;
import input.validation.exception.ErrorDetail;

public class SizeValidation<T> implements ValidationHandler<T> {

    private int length;

    public SizeValidation(int length) {
        this.length = length;
    }

    @Override
    public boolean isValid(T data) {
    	
    	if(data == null) return false;
        
        if (data instanceof String) {
            return ((String) data).length() == length;
        }

        if (data instanceof Collection<?>) {
            return ((Collection<?>) data).size() == length;
        }

        if (data instanceof Map<?, ?>) {
            return ((Map<?, ?>) data).size() == length;
        }

        return false;
    }

    @Override
    public ErrorDetail getErrorCause() {
        ErrorDetail err = new ErrorDetail(StringTypeValidation.class.getName(), "Not match the length " + length);
        return err;
    }
}