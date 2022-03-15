package org.launchcode.lifeorganizer.models;

public enum TaskPriority {
    NA("N/A"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String displayName;

    TaskPriority(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
