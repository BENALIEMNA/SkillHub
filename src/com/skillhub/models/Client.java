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
    private List<String> notifications;
    private double solde;         // en Dinar Tunisien (DT)

    // Constructeur vide
    public Client() {
        super();
        this.missions = new ArrayList<>();
        this.notifications = new ArrayList<>();
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
        this.notifications = new ArrayList<>();
        this.solde = solde;
    }

    // Méthodes métier

    /**
     * Publier une nouvelle mission sur la plateforme.
     */
     public Mission publierMission(int id, String titre, String description,
                                   double budget, String delai, String domaine) {
         if (titre == null || titre.isEmpty()) {
             System.out.println("  ▸ Erreur : Le titre de la mission est obligatoire.");
             return null;
         }
         if (budget <= 0) {
             System.out.println("  ▸ Erreur : Le budget doit être positif.");
             return null;
         }
         Mission m = new Mission(id, titre, description, budget, delai, domaine, this);
         missions.add(m);
         System.out.println("  ▸ Mission \"" + titre + "\" créée avec succès. En attente de validation admin.");
         System.out.println("  ✓ Budget : " + budget + " DT | Délai : " + delai);
         return m;
     }

     /**
      * Consulter l'historique des missions du client.
      */
     public void consulterMissions() {
         System.out.println("  ✓ Missions du client " + getPrenom() + " " + getNom() + " :");
         if (missions.isEmpty()) {
             System.out.println("  ▸ Aucune mission publiée.");
             return;
         }
         for (Mission m : missions) {
             System.out.println("  ▸ - " + m.getTitre() + " | Budget : " + m.getBudget() + " DT | Statut : " + m.getStatut());
         }
     }

    /**
     * Ajouter une notification interne au client.
     */
    public void ajouterNotification(String message) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        notifications.add(message.trim());
    }

     /**
      * Consulter les notifications reçues.
      */
     public void consulterNotifications() {
         System.out.println("  ✓ Notifications de " + getPrenom() + " " + getNom() + " :");
         if (notifications.isEmpty()) {
             System.out.println("  ▸ Aucune notification.");
             return;
         }
         for (int i = 0; i < notifications.size(); i++) {
             System.out.println("  ▸ " + (i + 1) + ". " + notifications.get(i));
         }
     }

     /**
      * Modifier une mission existante (seulement si aucune candidature n'est acceptée).
      */
     public boolean modifierMission(Mission mission, String nouveauTitre, double nouveauBudget) {
         if (mission == null) {
             System.out.println("  ▸ Mission introuvable.");
             return false;
         }
         if (mission.isUneCandiatureAcceptee()) {
             System.out.println("  ▸ Impossible de modifier : une candidature a déjà été acceptée.");
             return false;
         }
         mission.setTitre(nouveauTitre);
         mission.setBudget(nouveauBudget);
         System.out.println("  ▸ Mission modifiée : \"" + nouveauTitre + "\" — " + nouveauBudget + " DT");
         return true;
     }

     /**
      * Fermer une mission et notifier les candidats.
      */
     public void fermerMission(Mission mission) {
         if (mission == null) {
             System.out.println("  ▸ Mission introuvable.");
             return;
         }
         mission.setStatut("fermée");
         System.out.println("  ✓ Mission \"" + mission.getTitre() + "\" fermée.");
         System.out.println("  ✓ " + mission.getCandidatures().size() + " candidat(s) notifié(s) de la fermeture.");
     }

     /**
      * Sélectionner un candidat pour une mission.
      */
     public void selectionnerCandidat(Mission mission, Candidature candidature) {
         if (mission == null || candidature == null) {
             System.out.println("  ▸ Données invalides.");
             return;
         }
         candidature.setStatut("acceptée");
         mission.setCandidatureAcceptee(true);
         mission.setStatut("en_cours");
         candidature.getEtudiant().ajouterNotification(
                 "Votre candidature pour '" + mission.getTitre() + "' a été acceptée!"
         );
         System.out.println("  ▸ Candidature de " + candidature.getEtudiant().getPrenom()
                 + " " + candidature.getEtudiant().getNom() + " acceptée pour la mission \""
                 + mission.getTitre() + "\".");
     }

     /**
      * Accepter la candidature d'un étudiant pour une mission.
      */
     public void accepterCandidature(Candidature candidature, Mission mission) {
         candidature.setStatut("acceptée");
         mission.setCandidatureAcceptee(true);
         candidature.getEtudiant().ajouterNotification(
                 "Votre candidature pour '" + mission.getTitre() + "' a été acceptée!"
         );
         System.out.println("  ▸ Candidature de " + candidature.getEtudiant().getPrenom() + " acceptée pour \"" + mission.getTitre() + "\".");
         System.out.println("  ✓ Notification envoyée à l'étudiant.");
     }

     /**
      * Refuser la candidature d'un étudiant.
      */
     public void refuserCandidature(Candidature candidature) {
         candidature.setStatut("refusée");
         System.out.println("  ▸ Candidature refusée pour " + candidature.getEtudiant().getPrenom());
     }

    // Getters
    public String getTypeClient() { return typeClient; }
    public String getOrganisation() { return organisation; }
    public List<Mission> getMissions() { return missions; }
    public List<String> getNotifications() { return notifications; }
    public double getSolde() { return solde; }

    // Setters
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
    public void setOrganisation(String organisation) { this.organisation = organisation; }
    public void setMissions(List<Mission> missions) { this.missions = missions; }
    public void setNotifications(List<String> notifications) { this.notifications = notifications; }
    public void setSolde(double solde) { this.solde = solde; }

    @Override
    public String toString() {
        return "Client{nom=" + getNom() + ", type=" + typeClient + ", organisation=" + organisation
                + ", missions=" + missions.size() + ", notifications=" + notifications.size()
                + ", solde=" + solde + " DT}";
    }
}
