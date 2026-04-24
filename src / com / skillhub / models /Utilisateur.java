package com.skillhub.models;

/**
 * Classe de base représentant un utilisateur de la plateforme SkillHub.
 * Responsable : Zeineb (Authentification)
 */
public abstract class Utilisateur {

    // Champs privés
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String statut; // "actif", "en_attente", "rejeté"
    private String typeRole; // "etudiant", "client", "admin"

    // Constructeur vide
    public Utilisateur() {
        this.statut = "en_attente";
    }

    // Constructeur complet
    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse, String typeRole) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.typeRole = typeRole;
        this.statut = "en_attente";
    }

    // Méthodes métier

    /**
     * Inscription d'un nouvel utilisateur.
     * Valide les champs et crée le compte avec statut 'en_attente'.
     */
    public boolean sInscrire(String nom, String prenom, String email, String motDePasse) {
        if (nom == null || nom.isEmpty()) {
            System.out.println("  ? Erreur : Le nom est obligatoire.");
            return false;
        }
        if (email == null || !email.contains("@")) {
            System.out.println("  ? Erreur : Email invalide.");
            return false;
        }
        if (motDePasse == null || motDePasse.length() < 6) {
            System.out.println("  ? Erreur : Le mot de passe doit contenir au moins 6 caractères.");
            return false;
        }
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.statut = "en_attente";
        System.out.println("  ? Compte créé avec succès pour " + prenom + " " + nom + " ? statut : en attente de validation.");
        System.out.println("  ?? Email de confirmation envoyé à : " + email);
        return true;
    }

    /**
     * Connexion de l'utilisateur avec email et mot de passe.
     */
    public boolean seConnecter(String email, String motDePasse) {
        if (!this.email.equals(email)) {
            System.out.println("  ? Email introuvable.");
            return false;
        }
        if (!this.motDePasse.equals(motDePasse)) {
            System.out.println("  ? Mot de passe incorrect.");
            return false;
        }
        if (!this.statut.equals("actif")) {
            System.out.println("  ? Compte non validé par l'administrateur. Statut : " + this.statut);
            return false;
        }
        System.out.println("  ? Connexion réussie ! Bienvenue, " + this.prenom + " " + this.nom + " (" + this.typeRole + ")");
        return true;
    }

    /**
     * Déconnexion de l'utilisateur.
     */
    public void seDeconnecter() {
        System.out.println("  ?? " + this.prenom + " " + this.nom + " s'est déconnecté.");
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getStatut() { return statut; }
    public String getTypeRole() { return typeRole; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setTypeRole(String typeRole) { this.typeRole = typeRole; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nom=" + nom + ", prenom=" + prenom
                + ", email=" + email + ", statut=" + statut + ", role=" + typeRole + "}";
    }
}

    

