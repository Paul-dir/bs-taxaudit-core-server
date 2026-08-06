package com.mor.itas.domain.valueobject;

public class SamplingMethod {
    private String value;

    public SamplingMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SamplingMethod RANDOM() {
        return new SamplingMethod("RANDOM");
    }

    public static SamplingMethod RISK_BASED() {
        return new SamplingMethod("RISK_BASED");
    }

    public static SamplingMethod STRATIFIED() {
        return new SamplingMethod("STRATIFIED");
    }

    public static SamplingMethod PERCENTAGE_BASED() {
        return new SamplingMethod("PERCENTAGE_BASED");
    }

    @Override
    public String toString() {
        return value;
    }
}