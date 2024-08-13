package com.example.qrcodearticleapp.Dto;

public class ArticleDTO {
    private Long serialNumber;
    private String nom;
    private String longueur;
    private String largeur;
    private String hauteur;
    private String categorie;
    private String entrepotNom; // should not be null
    private String fabricantName; // should not be null
    private String fournisseurName; // should not be null

    // Getters and setters
    private Long entrepotId;
    private Long fabricantId;
    private Long fournisseurId;

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLongueur() {
        return longueur;
    }

    public void setLongueur(String longueur) {
        this.longueur = longueur;
    }

    public String getLargeur() {
        return largeur;
    }

    public void setLargeur(String largeur) {
        this.largeur = largeur;
    }

    public String getHauteur() {
        return hauteur;
    }

    public void setHauteur(String hauteur) {
        this.hauteur = hauteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getEntrepotNom() {
        return entrepotNom;
    }

    public void setEntrepotNom(String entrepotNom) {
        this.entrepotNom = entrepotNom;
    }

    public String getFabricantName() {
        return fabricantName;
    }

    public void setFabricantName(String fabricantName) {
        this.fabricantName = fabricantName;
    }

    public String getFournisseurName() {
        return fournisseurName;
    }

    public void setFournisseurName(String fournisseurName) {
        this.fournisseurName = fournisseurName;
    }

    // Optional: Constructor
    public ArticleDTO() {}

    public ArticleDTO(Long serialNumber, String nom, String longueur, String largeur, String hauteur, String categorie, String entrepotNom, String fabricantName, String fournisseurName) {
        this.serialNumber = serialNumber;
        this.nom = nom;
        this.longueur = longueur;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.categorie = categorie;
        this.entrepotNom = entrepotNom;
        this.fabricantName = fabricantName;
        this.fournisseurName = fournisseurName;
    }

    // Optional: toString method
    @Override
    public String toString() {
        return "ArticleDTO{" +
                "serialNumber=" + serialNumber +
                ", nom='" + nom + '\'' +
                ", longueur='" + longueur + '\'' +
                ", largeur='" + largeur + '\'' +
                ", hauteur='" + hauteur + '\'' +
                ", categorie='" + categorie + '\'' +
                ", entrepotNom=" + entrepotNom +
                ", fabricantName=" + fabricantName +
                ", fournisseurName=" + fournisseurName +
                '}';
    }
}
