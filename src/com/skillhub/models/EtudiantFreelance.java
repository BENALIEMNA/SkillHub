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

    // Constructeur vide
    public EtudiantFreelance() {
        super();
        this.competences = new ArrayList<>();
        this.candidatures = new ArrayList<>();
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
    }

    // Méthodes métier

    /**
     * Ajouter une compétence au profil.
     */
    public void ajouterCompetence(String competence) {
        competences.add(competence);
        System.out.println("  ? Compétence ajoutée : " + competence);
    }

    /**
     * Afficher le profil complet de l'étudiant.
     */
    public void afficherProfil() {
        System.out.println("  ??????????????????????????????????????????");
        System.out.println("  ? ?? Profil : " + getPrenom() + "" + getNom());
        System.out.println("  ? ?? " + niveauEtude + " ? " + universite);
        System.out.println("  ? ?? Domaine : " + domaine);
        System.out.println("  ? ? Note : " + (nbAvisRecus == 0 ? "Aucun avis" : noteMoyenne + "/5 (" + nbAvisRecus + " avis)"));
        System.out.println("  ? ??  Compétences : " + (competences.isEmpty() ? "Non renseignées" : String.join(", ", competences)));
        System.out.println("  ??????????????????????????????????????????");
    }

    /**
     * Mise à jour de la note moyenne après réception d'un nouvel avis.
     */
    public void mettreAJourNote(double nouvelleNote) {
        double totalPoints = noteMoyenne * nbAvisRecus + nouvelleNote;
        nbAvisRecus++;
        noteMoyenne = Math.round((totalPoints / nbAvisRecus) * 10.0) / 10.0;
        System.out.println("  ?? Nouvelle moyenne de " + getPrenom() + "" + getNom() + " : " + noteMoyenne + "/5 (" + nbAvisRecus + " avis)");
    }
// Getters
    public String getDomaine() { return domaine; }
    public String getUniversite() { return universite; }
    public String getNiveauEtude() { return niveauEtude; }
    public double getNoteMoyenne() { return noteMoyenne; }
    public int getNbAvisRecus() { return nbAvisRecus; }
    public List<String> getCompetences() { return competences; }
    public List<Candidature> getCandidatures() { return candidatures; }

    // Setters
    public void setDomaine(String domaine) { this.domaine = domaine; }
    public void setUniversite(String universite) { this.universite = universite; }
    public void setNiveauEtude(String niveauEtude) { this.niveauEtude = niveauEtude; }
    public void setNoteMoyenne(double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
    public void setNbAvisRecus(int nbAvisRecus) { this.nbAvisRecus = nbAvisRecus; }

    @Override
    public String toString() {
        return "EtudiantFreelance{" + getPrenom() + "" + getNom() +
" | " + domaine + " | ? " + noteMoyenne + "/5}";
    }
}
