package com.act.taxaudit.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Value object representing sample selection criteria and results.
 */
public class SampleSelection {
    private final SamplingMethod method;
    private final String criteria;
    private final List<String> selectedItems;
    private final int sampleSize;

    @JsonCreator
    public SampleSelection(
            @JsonProperty("method") SamplingMethod method,
            @JsonProperty("criteria") String criteria,
            @JsonProperty("selectedItems") List<String> selectedItems,
            @JsonProperty("sampleSize") int sampleSize) {
        if (method == null) {
            throw new IllegalArgumentException("Sampling method cannot be null");
        }
        if (criteria == null || criteria.isBlank()) {
            throw new IllegalArgumentException("Criteria cannot be null or blank");
        }
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new IllegalArgumentException("Selected items cannot be null or empty");
        }
        if (sampleSize <= 0) {
            throw new IllegalArgumentException("Sample size must be positive");
        }
        if (selectedItems.size() != sampleSize) {
            throw new IllegalArgumentException("Selected items size must match sample size");
        }

        this.method = method;
        this.criteria = criteria;
        this.selectedItems = List.copyOf(selectedItems);
        this.sampleSize = sampleSize;
    }

    public SamplingMethod getMethod() {
        return method;
    }

    public String getCriteria() {
        return criteria;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleSelection that = (SampleSelection) o;
        return sampleSize == that.sampleSize &&
               method == that.method &&
               criteria.equals(that.criteria) &&
               selectedItems.equals(that.selectedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, criteria, selectedItems, sampleSize);
    }
}