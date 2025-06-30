package com.hospital.prioridadehospitalar.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Prioridade {
    EMERGENCIA(4, "Emergência"),
    MUITA_URGENCIA(3, "Muita Urgência"),
    POUCA_URGENCIA(2, "Pouca Urgência"),
    NAO_URGENCIA(1, "Não Urgência");

    private final int nivel;
    private final String descricao;

    Prioridade(int nivel, String descricao) {
        this.nivel = nivel;
        this.descricao = descricao;
    }

    public int getNivel() {
        return nivel;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static Prioridade fromDescricao(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Descrição de prioridade não pode ser nula.");
        }
        for (Prioridade p : Prioridade.values()) {
            if (p.descricao.equalsIgnoreCase(text.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("Prioridade inválida: '" + text + "' Não é uma das descrições aceitas: " +
                "['Emergência', 'Muita Urgência', 'Pouca Urgência', 'Não Urgência']");
    }

    public static Prioridade fromString(String text) {
        return fromDescricao(text);
    }
}