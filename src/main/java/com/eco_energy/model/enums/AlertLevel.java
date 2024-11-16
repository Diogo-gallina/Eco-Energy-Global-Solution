package com.eco_energy.model.enums;

public enum AlertLevel {
    LOW("Baixo"), MEDIUM("Médio"), HIGH("Alto");

    private String label;

    AlertLevel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}

