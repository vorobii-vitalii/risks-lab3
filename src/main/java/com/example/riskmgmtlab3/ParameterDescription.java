package com.example.riskmgmtlab3;

import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public record ParameterDescription(
        StringProperty parameterName,
        StringProperty notation,
        StringProperty unitOfMeasurement,
        StringProperty min,
        StringProperty nominal,
        StringProperty max
) {

    public static final MathContext MATH_CONTEXT = new MathContext(8);

    public BigDecimal maxValue() {
        return new BigDecimal(max.getValue(), MATH_CONTEXT);
    }

    public BigDecimal nomMaxProportion() {
        return nominalValue().divide(maxValue(), MATH_CONTEXT);
    }

    public BigDecimal nominalValue() {
        return new BigDecimal(nominal.getValue(), MATH_CONTEXT);
    }

}
