package com.skillhub.models;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * Représente une candidature soumise par un étudiant freelance pour une mission.
 * Responsable : Yassmine (Recherche & Candidature)
 */
public class Candidature {
 
    // Champs privés
    private int id;
    private EtudiantFreelance etudiant;
    private Mission mission;
    private String lettreMotivation;
    private String statut;   // "envoyée", "vue", "acceptée", "refusée"
    private double propositionBudget; // en DT
 
    // Constructeur vide
    public Candidature() {
        this.statut = "envoyée";
    }
 
    // Constructeur complet
    public Candidature(int id, EtudiantFreelance etudiant, Mission mission,
                       String lettreMotivation, double propositionBudget) {
        this.id = id;
        this.etudiant = etudiant;
        this.mission = mission;
        this.lettreMotivation = lettreMotivation;
        this.propositionBudget = propositionBudget;
        this.statut = "envoyée";
    }
 
    // Méthodes métier
 
    /**
     * Postuler à une mission : valide et enregistre la candidature.
     */
    public boolean postuler() {
        if (etudiant == null || mission == null) {
            System.out.println("  ? Données manquantes pour la candidature.");
            return false;
        }
        if (!mission.getStatut().equals("active")) {
            System.out.println("  ? La mission \"" + mission.getTitre() + "\" n'est pas ouverte aux candidatures.");
            return false;
        }
        mission.ajouterCandidature(this);
        etudiant.getCandidatures().add(this);
        System.out.println("  ? Candidature envoyée pour la mission \"" + mission.getTitre() + "\".");
        System.out.println("  ?? Le client " + mission.getClient().getPrenom() + " " + mission.getClient().getNom() + " a été notifié.");
        return true;
    }
 
    /**
     * Afficher le statut de la candidature.
     */
    public void afficherStatut() {
        System.out.println("  ?? Candidature #" + id + " pour \"" + mission.getTitre() + "\" ? Statut : " + statut);
    }
 
    /**
     * Rechercher des missions filtrées par domaine et budget maximum.
     * Méthode statique utilitaire.
     */
    public static List<Mission> rechercherMissions(List<Mission> toutesLesMissions,
                                                    String domaine, double budgetMax) {
        List<Mission> resultats = new ArrayList<>();
        for (Mission m : toutesLesMissions) {
            boolean domaineOk = (domaine == null || domaine.isEmpty() || m.getDomaine().equalsIgnoreCase(domaine));
            boolean budgetOk = (budgetMax <= 0 || m.getBudget() <= budgetMax);
            boolean statutOk = m.getStatut().equals("active");
            if (domaineOk && budgetOk && statutOk) {
                resultats.add(m);
            }
        }
        return resultats;
    }
 
    // Getters
    public int getId() { return id; }
    public EtudiantFreelance getEtudiant() { return etudiant; }
    public Mission getMission() { return mission; }
    public String getLettreMotivation() { return lettreMotivation; }
    public String getStatut() { return statut; }
    public double getPropositionBudget() { return propositionBudget; }
 
    // Setters
    public void setId(int id) { this.id = id; }
    public void setEtudiant(EtudiantFreelance etudiant) { this.etudiant = etudiant; }
    public void setMission(Mission mission) { this.mission = mission; }
    public void setLettreMotivation(String lettreMotivation) { this.lettreMotivation = lettreMotivation; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setPropositionBudget(double propositionBudget) { this.propositionBudget = propositionBudget; }
 
    @Override
    public String toString() {
        return "Candidature{#" + id + " | " + etudiant.getPrenom() + " ? " + mission.getTitre() + " | " + statut + "}";
    }
}
 

