package com.skillhub.models;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * Représente un client sur la plateforme SkillHub.
 * Hérite de Utilisateur.
 * Responsable : Fatma (Gestion des Missions)
 */
public class Client extends Utilisateur {
 
    // Champs privés
    private String typeClient;    // "particulier", "universitaire", "entreprise"
    private String organisation;
    private List<Mission> missions;
    private double solde;         // en Dinar Tunisien (DT)
 
    // Constructeur vide
    public Client() {
        super();
        this.missions = new ArrayList<>();
        this.solde = 0.0;
        setTypeRole("client");
    }
 
    // Constructeur complet
    public Client(int id, String nom, String prenom, String email,
                  String motDePasse, String typeClient, String organisation, double solde) {
        super(id, nom, prenom, email, motDePasse, "client");
        this.typeClient = typeClient;
        this.organisation = organisation;
        this.missions = new ArrayList<>();
        this.solde = solde;
    }
 
    // Méthodes métier
 
    /**
     * Publier une nouvelle mission sur la plateforme.
     */
    public Mission publierMission(int id, String titre, String description,
                                  double budget, String delai, String domaine) {
        if (titre == null || titre.isEmpty()) {
            System.out.println("  ? Erreur : Le titre de la mission est obligatoire.");
            return null;
        }
        if (budget <= 0) {
            System.out.println("  ? Erreur : Le budget doit être positif.");
            return null;
        }
        Mission mission = new Mission(id, titre, description, budget, delai, domaine, this);
        missions.add(mission);
        System.out.println("  ? Mission publiée : \"" + titre + "\" ? " + budget + " DT ? Domaine : " + domaine);
        System.out.println("  ?? En attente de validation par l'administrateur.");
        return mission;
    }
 
    /**
     * Modifier une mission existante (seulement si aucune candidature n'est acceptée).
     */
    public boolean modifierMission(Mission mission, String nouveauTitre, double nouveauBudget) {
        if (mission == null) {
            System.out.println("  ? Mission introuvable.");
            return false;
        }
        if (mission.isUneCandiatureAcceptee()) {
            System.out.println("  ? Impossible de modifier : une candidature a déjà été acceptée.");
            return false;
        }
        mission.setTitre(nouveauTitre);
        mission.setBudget(nouveauBudget);
        System.out.println("  ? Mission modifiée : \"" + nouveauTitre + "\" ? " + nouveauBudget + " DT");
        return true;
    }
 
    /**
     * Fermer une mission et notifier les candidats.
     */
    public void fermerMission(Mission mission) {
        if (mission == null) {
            System.out.println("  ? Mission introuvable.");
            return;
        }
        mission.setStatut("fermée");
        System.out.println("  ?? Mission \"" + mission.getTitre() + "\" fermée.");
        System.out.println("  ?? " + mission.getCandidatures().size() + " candidat(s) notifié(s) de la fermeture.");
    }
 
    /**
     * Sélectionner un candidat pour une mission.
     */
    public void selectionnerCandidat(Mission mission, Candidature candidature) {
        if (mission == null || candidature == null) {
            System.out.println("  ? Données invalides.");
            return;
        }
        candidature.setStatut("acceptée");
        mission.setUneCandiatureAcceptee(true);
        mission.setStatut("en_cours");
        System.out.println("  ? Candidature de " + candidature.getEtudiant().getPrenom()
                + " " + candidature.getEtudiant().getNom() + " acceptée pour la mission \""
                + mission.getTitre() + "\".");
    }
 
    // Getters
    public String getTypeClient() { return typeClient; }
    public String getOrganisation() { return organisation; }
    public List<Mission> getMissions() { return missions; }
    public double getSolde() { return solde; }
 
    // Setters
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
    public void setOrganisation(String organisation) { this.organisation = organisation; }
    public void setSolde(double solde) { this.solde = solde; }
 
    @Override
    public String toString() {
        return "Client{" + getPrenom() + " " + getNom() + " | " + organisation + " | " + missions.size() + " mission(s)}";
    }
}
