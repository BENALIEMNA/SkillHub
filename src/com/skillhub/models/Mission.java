package com.skillhub.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une mission publiée sur la plateforme SkillHub.
 * Responsable : Fatma (Gestion des Missions)
 */
public class Mission {

    // Champs privés
    private int id;
    private String titre;
    private String description;
    private double budget;        // en DT
    private String delai;         // ex: "2 semaines"
    private String domaine;       // ex: "Développement Web", "Design"
    private String statut;        // "en_attente", "active", "en_cours", "terminée", "fermée"
    private Client client;
    private List<Candidature> candidatures;
    private boolean uneCandiatureAcceptee;

    // Constructeur vide
    public Mission() {
        this.candidatures = new ArrayList<>();
        this.statut = "en_attente";
        this.uneCandiatureAcceptee = false;
    }

    // Constructeur complet
    public Mission(int id, String titre, String description, double budget,
                   String delai, String domaine, Client client) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.budget = budget;
        this.delai = delai;
        this.domaine = domaine;
        this.client = client;
        this.statut = "en_attente";
        this.candidatures = new ArrayList<>();
        this.uneCandiatureAcceptee = false;
    }

    // Méthodes métier

    /**
     * Activer la mission après validation admin.
     */
    public void activer() {
        this.statut = "active";
        System.out.println("  ? Mission \"" + titre + "\" activée et visible sur la plateforme.");
    }

    /**
     * Marquer la mission comme terminée.
     */
    public void terminer() {
        this.statut = "terminée";
        System.out.println("  ?? Mission \"" + titre + "\" marquée comme terminée.");
    }

    /**
     * Ajouter une candidature à cette mission.
     */
    public void ajouterCandidature(Candidature candidature) {
        candidatures.add(candidature);
    }

    /**
     * Afficher les détails de la mission.
     */
    public void afficherDetails() {
        System.out.println("  ??????????????????????????????????????????");
        System.out.println("  ? ?? Mission #" + id + " : " + titre);
        System.out.println("  ? ?? Domaine : " + domaine);
        System.out.println("  ? ?? Budget : " + budget + " DT");
        System.out.println("  ? ?  Délai  : " + delai);
        System.out.println("  ? ?? Description : " + description);
        System.out.println("  ? ??  Statut : " + statut);
        System.out.println("  ? ?? Candidatures : " + candidatures.size());
        System.out.println("  ??????????????????????????????????????????");
    }

    // Getters
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public double getBudget() { return budget; }
    public String getDelai() { return delai; }
    public String getDomaine() { return domaine; }
    public String getStatut() { return statut; }
    public Client getClient() { return client; }
    public List<Candidature> getCandidatures() { return candidatures; }
    public boolean isUneCandiatureAcceptee() { return uneCandiatureAcceptee; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setBudget(double budget) { this.budget = budget; }
    public void setDelai(String delai) { this.delai = delai; }
    public void setDomaine(String domaine) { this.domaine = domaine; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setClient(Client client) { this.client = client; }
    public void setCandidatures(List<Candidature> candidatures) { this.candidatures = candidatures; }
    public void setCandidatureAcceptee(boolean candidatureAcceptee) { this.uneCandiatureAcceptee = candidatureAcceptee; }

    @Override
    public String toString() {
        return "Mission{#" + id + " | " + titre + " | " + domaine + " | " + budget + " DT | " + statut + "}";
    }
}

