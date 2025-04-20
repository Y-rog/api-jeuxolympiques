package com.yrog.apijeuxolympiques.enums;

public enum CartStatus {
    EN_ATTENTE("En attente"),
    PAYE("Payé");

    private final String label;

    CartStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

