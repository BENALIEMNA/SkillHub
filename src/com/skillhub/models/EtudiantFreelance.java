package com.skillhub.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un étudiant freelance sur la plateforme SkillHub.
 * Hérite de Utilisateur.
 * Responsable : Zeineb (Authentification) + Yassmine (Candidature)
 */
public class EtudiantFreelance extends Utilisateur {

    // Champs privés
    private String domaine;       // ex: "Développement Web", "Design", "Traduction"
    private String universite;
    private String niveauEtude;   // ex: "Licence 2", "Master 1"
    private double noteMoyenne;
    private int nbAvisRecus;
    private List<String> competences;
    private List<Candidature> candidatures;
    private List<String> notifications;

    // Constructeur vide
    public EtudiantFreelance() {
        super();
        this.competences = new ArrayList<>();
        this.candidatures = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.noteMoyenne = 0.0;
        this.nbAvisRecus = 0;
        setTypeRole("etudiant");
    }

    // Constructeur complet
    public EtudiantFreelance(int id, String nom, String prenom, String email,
                             String motDePasse, String domaine,
                             String universite, String niveauEtude) {
        super(id, nom, prenom, email, motDePasse, "etudiant");
        this.domaine = domaine;
        this.universite = universite;
        this.niveauEtude = niveauEtude;
        this.noteMoyenne = 0.0;
        this.nbAvisRecus = 0;
        this.competences = new ArrayList<>();
        this.candidatures = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    // Méthodes métier

    /**
     * Ajouter une compétence au profil.
     */
    public void ajouterCompetence(String competence) {
        competences.add(competence);
        System.out.println("  ▸ Compétence \"" + competence + "\" ajoutée au profil.");
    }

    /**
     * Consulter les missions disponibles et postuler.
     */
    public void consulterMissions(List<Mission> missions) {
        System.out.println("  ✓ Missions disponibles pour " + getPrenom() + " " + getNom() + " :");
        if (missions.isEmpty()) {
            System.out.println("  ▸ Aucune mission disponible.");
            return;
        }
        for (Mission m : missions) {
            System.out.println("  ▸ - " + m.getTitre() + " | Budget : " + m.getBudget() + " DT | Domaine : " + m.getDomaine());
        }
    }

    /**
     * Ajouter une notification interne à l'étudiant.
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
     * Mettre à jour la note moyenne de l'étudiant.
     */
    public void mettreAJourNote(int noteNouvelle) {
        nbAvisRecus++;
        noteMoyenne = Math.round(((noteMoyenne * (nbAvisRecus - 1)) + noteNouvelle) / nbAvisRecus * 10.0) / 10.0;
        System.out.println("  ▸ Nouvelle note moyenne : " + noteMoyenne + " ⭐ (" + nbAvisRecus + " avis)");
    }

    /**
     * Afficher le profil de l'étudiant freelance.
     */
    public void afficherProfil() {
        System.out.println("  ════════════════════════════════════════════");
        System.out.println("  ► Profil : " + getPrenom() + " " + getNom());
        System.out.println("  ► Université : " + universite);
        System.out.println("  ► Niveau : " + niveauEtude);
        System.out.println("  ► Domaine : " + domaine);
        System.out.println("  ► Note moyenne : " + noteMoyenne + " ⭐ (" + nbAvisRecus + " avis)");
        System.out.println("  ► Compétences : " + competences);
        System.out.println("  ► Candidatures : " + candidatures.size());
        System.out.println("  ════════════════════════════════════════════");
    }

    // Getters
    public String getDomaine() { return domaine; }
    public String getUniversite() { return universite; }
    public String getNiveauEtude() { return niveauEtude; }
    public double getNoteMoyenne() { return noteMoyenne; }
    public int getNbAvisRecus() { return nbAvisRecus; }
    public List<String> getCompetences() { return competences; }
    public List<Candidature> getCandidatures() { return candidatures; }
    public List<String> getNotifications() { return notifications; }

    // Setters
    public void setDomaine(String domaine) { this.domaine = domaine; }
    public void setUniversite(String universite) { this.universite = universite; }
    public void setNiveauEtude(String niveauEtude) { this.niveauEtude = niveauEtude; }
    public void setNoteMoyenne(double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
    public void setNbAvisRecus(int nbAvisRecus) { this.nbAvisRecus = nbAvisRecus; }
    public void setCompetences(List<String> competences) { this.competences = competences; }
    public void setCandidatures(List<Candidature> candidatures) { this.candidatures = candidatures; }
    public void setNotifications(List<String> notifications) { this.notifications = notifications; }

    @Override
    public String toString() {
        return "EtudiantFreelance{nom=" + getNom() + ", domaine=" + domaine + ", universite=" + universite
                + ", moyenne=" + noteMoyenne + ", candidatures=" + candidatures.size() 
                + ", notifications=" + notifications.size() + "}";
    }
}
