package com.adria.spring_oracle.entities;

public enum Transaction_Type {
    V("V", "Virement"),
    DC("DC", "DEMANDE_CHEQUIER"),
    DE("DE", "DEMANDE_CREDIT"),
    DOC("DOC", "DEMANDE_OPPOSITION_CHEQUE"),
    PF("PF", "PAIEMENT_FACTURE");

    private final String code;
    private final String description;

    Transaction_Type(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // Méthode statique pour récupérer Transaction_Type à partir du code
    public static Transaction_Type fromCode(String code) {
        for (Transaction_Type type : Transaction_Type.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }
}
