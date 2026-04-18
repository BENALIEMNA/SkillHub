package com.skillhub.models;
 
import java.util.List;
 
/**
 * Représente l'administrateur de la plateforme SkillHub.
 * Hérite de Utilisateur.
 * Responsable : Emna (Tableau de Bord Admin)
 */
public class Administrateur extends Utilisateur {
 
    // Champs privés
    private int nbValidationsEffectuees;
    private int nbAvisModeres;
 
    // Constructeur vide
    public Administrateur() {
        super();
        setTypeRole("admin");
        setStatut("actif");
        this.nbValidationsEffectuees = 0;
        this.nbAvisModeres = 0;
    }
 
    // Constructeur complet
    public Administrateur(int id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse, "admin");
        setStatut("actif");
        this.nbValidationsEffectuees = 0;
        this.nbAvisModeres = 0;
    }
 
    // Méthodes métier
 
    /**
     * Valider ou rejeter un compte étudiant en attente.
     */
    public void validerCompte(Utilisateur utilisateur, boolean accepter, String motif) {
        if (!utilisateur.getStatut().equals("en_attente")) {
            System.out.println("  ??  Le compte de " + utilisateur.getPrenom() + " n'est pas en attente de validation.");
            return;
        }
        if (accepter) {
            utilisateur.setStatut("actif");
            nbValidationsEffectuees++;
            System.out.println("  ? Compte de " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " validé.");
            System.out.println("  ?? Notification envoyée : compte activé avec succès.");
        } else {
            utilisateur.setStatut("rejeté");
            System.out.println("  ? Compte de " + utilisateur.getPrenom() + " " + utilisateur.getNom()
                    + " rejeté. Motif : " + motif);
            System.out.println("  ?? Notification envoyée : compte refusé.");
        }
    }
 
    /**
     * Valider une mission soumise par un client.
     */
    public void validerMission(Mission mission) {
        if (mission == null) {
            System.out.println("  ? Mission introuvable.");
            return;
        }
        mission.activer();
        System.out.println("  ? Mission \"" + mission.getTitre() + "\" validée et publiée par l'admin.");
    }
 
    /**
     * Modérer un avis signalé (le supprimer ou l'ignorer).
     */
    public void modererAvis(Avis avis, boolean supprimer) {
        if (!avis.isSignale()) {
            System.out.println("  ??  L'avis #" + avis.getId() + " n'a pas été signalé.");
            return;
        }
        if (supprimer) {
            nbAvisModeres++;
            System.out.println("  ??  Avis #" + avis.getId() + " supprimé par l'administrateur.");
        } else {
            System.out.println("  ? Avis #" + avis.getId() + " examiné ? conservé sur la plateforme.");
        }
    }
 
    /**
     * Afficher les statistiques globales de la plateforme (tableau de bord).
     */
    public void afficherStatistiques(List<Utilisateur> utilisateurs,
                                     List<Mission> missions,
                                     double revenusJour) {
        long nbEtudiants = utilisateurs.stream()
                .filter(u -> u.getTypeRole().equals("etudiant")).count();
        long nbClients = utilisateurs.stream()
                .filter(u -> u.getTypeRole().equals("client")).count();
        long nbEnAttente = utilisateurs.stream()
                .filter(u -> u.getStatut().equals("en_attente")).count();
        long nbMissionsActives = missions.stream()
                .filter(m -> m.getStatut().equals("active") || m.getStatut().equals("en_cours")).count();
        long nbMissionsTerminees = missions.stream()
                .filter(m -> m.getStatut().equals("terminée")).count();
 
        System.out.println("  ???????????????????????????????????????????");
        System.out.println("  ?  ?? TABLEAU DE BORD ADMINISTRATEUR");
        System.out.println("  ???????????????????????????????????????????");
        System.out.println("  ?  ?? Utilisateurs inscrits : " + utilisateurs.size());
        System.out.println("  ?     ?? Étudiants freelances : " + nbEtudiants);
        System.out.println("  ?     ?? Clients             : " + nbClients);
        System.out.println("  ?  ? Comptes en attente de validation : " + nbEnAttente);
        System.out.println("  ?");
        System.out.println("  ?  ?? Missions actives  : " + nbMissionsActives);
        System.out.println("  ?  ?? Missions terminées: " + nbMissionsTerminees);
        System.out.println("  ?  ?? Revenus du jour   : " + revenusJour + " DT");
        System.out.println("  ?");
        System.out.println("  ?  ? Validations effectuées : " + nbValidationsEffectuees);
        System.out.println("  ?  ?? Avis modérés          : " + nbAvisModeres);
        System.out.println("  ???????????????????????????????????????????");
 
        // Alertes automatiques
        genererAlertes(missions);
    }
 
    /**
     * Générer des alertes pour les anomalies détectées.
     */
    public void genererAlertes(List<Mission> missions) {
        boolean alerteGeneree = false;
        for (Mission m : missions) {
            if (m.getStatut().equals("active") && m.getCandidatures().isEmpty()) {
                System.out.println("  ??  ALERTE : Mission \"" + m.getTitre() + "\" active sans aucune candidature.");
                alerteGeneree = true;
            }
        }
        if (!alerteGeneree) {
            System.out.println("  ? Aucune anomalie détectée.");
        }
    }
 
    /**
     * Afficher la liste des comptes en attente de validation.
     */
    public void afficherComptesEnAttente(List<Utilisateur> utilisateurs) {
        System.out.println("  ?? Comptes en attente de validation :");
        boolean trouve = false;
        for (Utilisateur u : utilisateurs) {
            if (u.getStatut().equals("en_attente")) {
                System.out.println("  ? " + u.getPrenom() + " " + u.getNom() + " [" + u.getTypeRole() + "] | " + u.getEmail());
                trouve = true;
            }
        }
        if (!trouve) System.out.println("  ? Aucun compte en attente.");
    }
 
    // Getters
    public int getNbValidationsEffectuees() { return nbValidationsEffectuees; }
    public int getNbAvisModeres() { return nbAvisModeres; }
 
    // Setters
    public void setNbValidationsEffectuees(int n) { this.nbValidationsEffectuees = n; }
    public void setNbAvisModeres(int n) { this.nbAvisModeres = n; }
 
    @Override
    public String toString() {
        return "Administrateur{" + getPrenom() + " " + getNom() + " | validations: " + nbValidationsEffectuees + "}";
    }
}
 
