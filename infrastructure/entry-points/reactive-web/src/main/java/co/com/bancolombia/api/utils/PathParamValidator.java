package co.com.bancolombia.api.utils;

import co.com.bancolombia.exceptions.BadRequestException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PathParamValidator {

    public static Integer validateRequiredParam(String paramName, String value) {
        checkNotNullOrBlank(paramName, value);
        Integer parsed = parseInteger(paramName, value);
        checkPositive(paramName, parsed);
        return parsed;
    }

    private static void checkNotNullOrBlank(String paramName, String value) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("Path parameter '" + paramName + "' is required and cannot be null or blank.");
        }
    }

    private static Integer parseInteger(String paramName, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Path parameter '" + paramName + "' must be a valid integer.");
        }
    }

    private static void checkPositive(String paramName, Integer number) {
        if (number <= 0) {
            throw new BadRequestException("Path parameter '" + paramName + "' must be a positive number.");
        }
    }


}

