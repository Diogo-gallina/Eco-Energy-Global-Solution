package com.eco_energy.model.enums;

public enum UsageFrequency {
    LOW("Baixa"), MEDIUM("Média"), HIGH("Alta");

    private String label;

    UsageFrequency(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
