package com.act.taxaudit.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing a finding recorded during desk audit.
 */
public class DeskAuditFinding {
    private final String area;
    private final Severity severity;
    private final String description;
    private final boolean requiresRiskUpdate;

    public DeskAuditFinding(String area, Severity severity, String description, boolean requiresRiskUpdate) {
        if (area == null || area.isBlank()) {
            throw new IllegalArgumentException("Finding area cannot be null or blank");
        }
        if (severity == null) {
            throw new IllegalArgumentException("Finding severity cannot be null");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Finding description cannot be null or blank");
        }

        this.area = area;
        this.severity = severity;
        this.description = description;
        this.requiresRiskUpdate = requiresRiskUpdate;
    }

    public String getArea() {
        return area;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequiresRiskUpdate() {
        return requiresRiskUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeskAuditFinding that = (DeskAuditFinding) o;
        return requiresRiskUpdate == that.requiresRiskUpdate &&
               area.equals(that.area) &&
               severity == that.severity &&
               description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(area, severity, description, requiresRiskUpdate);
    }

    @Override
    public String toString() {
        return "DeskAuditFinding{" +
               "area='" + area + '\'' +
               ", severity=" + severity +
               ", description='" + description + '\'' +
               ", requiresRiskUpdate=" + requiresRiskUpdate +
               '}';
    }
}