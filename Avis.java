package com.skillhub.models;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * Représente un avis (notation + commentaire) laissé par un client sur un étudiant.
 * Responsable : Wafa (Notation & Avis)
 */
public class Avis {
 
    // Champs privés
    private int id;
    private int note;             // 1 à 5 étoiles
    private String commentaire;
    private Client auteur;
    private EtudiantFreelance destinataire;
    private Mission mission;
    private boolean signale;      // signalé à l'admin ?
    private String reponseEtudiant;
 
    // Constructeur vide
    public Avis() {
        this.signale = false;
    }
 
    // Constructeur complet
    public Avis(int id, int note, String commentaire, Client auteur,
                EtudiantFreelance destinataire, Mission mission) {
        this.id = id;
        this.note = note;
        this.commentaire = commentaire;
        this.auteur = auteur;
        this.destinataire = destinataire;
        this.mission = mission;
        this.signale = false;
    }
 
    // Méthodes métier
 
    /**
     * Ajouter un avis : valide et met à jour la note de l'étudiant.
     */
    public boolean ajouterAvis() {
        if (note < 1 || note > 5) {
            System.out.println("  ? Note invalide. Elle doit être entre 1 et 5.");
            return false;
        }
        if (!mission.getStatut().equals("terminée")) {
            System.out.println("  ? Impossible de noter : la mission \"" + mission.getTitre() + "\" n'est pas encore terminée.");
            return false;
        }
        destinataire.mettreAJourNote(note);
        System.out.println("  ? Avis ajouté : " + etoiles(note) + " pour " + destinataire.getPrenom()
                + " " + destinataire.getNom() + " par " + auteur.getPrenom() + " " + auteur.getNom());
        System.out.println("  ?? Commentaire : \"" + commentaire + "\"");
        return true;
    }
 
    /**
     * L'étudiant répond à un avis.
     */
    public void repondreAvis(String reponse) {
        this.reponseEtudiant = reponse;
        System.out.println("  ?? " + destinataire.getPrenom() + " a répondu à l'avis : \"" + reponse + "\"");
    }
 
    /**
     * Signaler un avis négatif à l'administrateur.
     */
    public void signalerAvis() {
        this.signale = true;
        System.out.println("  ?? Avis #" + id + " signalé à l'administrateur pour modération.");
    }
 
    /**
     * Calculer la moyenne des avis d'un étudiant à partir d'une liste.
     */
    public static double calculerMoyenne(List<Avis> avis) {
        if (avis == null || avis.isEmpty()) return 0.0;
        double total = 0;
        for (Avis a : avis) {
            total += a.getNote();
        }
        double moyenne = Math.round((total / avis.size()) * 10.0) / 10.0;
        return moyenne;
    }
 
    /**
     * Afficher tous les avis d'un étudiant par ordre chronologique (par id).
     */
    public static void afficherAvis(List<Avis> avis, EtudiantFreelance etudiant) {
        System.out.println("  ?? Avis reçus par " + etudiant.getPrenom() + " " + etudiant.getNom() + " :");
        if (avis.isEmpty()) {
            System.out.println("  ? Aucun avis pour le moment.");
            return;
        }
        for (Avis a : avis) {
            System.out.println("  " + etoiles(a.getNote()) + " ? " + a.getCommentaire()
                    + " (par " + a.getAuteur().getPrenom() + " " + a.getAuteur().getNom() + ")");
            if (a.getReponseEtudiant() != null) {
                System.out.println("    ? Réponse : \"" + a.getReponseEtudiant() + "\"");
            }
        }
        System.out.println("  ?? Moyenne globale : " + calculerMoyenne(avis) + "/5");
    }
 
    /**
     * Génère une représentation en étoiles.
     */
    private static String etoiles(int note) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < note; i++) sb.append("?");
        return sb.toString() + " (" + note + "/5)";
    }
 
    // Getters
    public int getId() { return id; }
    public int getNote() { return note; }
    public String getCommentaire() { return commentaire; }
    public Client getAuteur() { return auteur; }
    public EtudiantFreelance getDestinataire() { return destinataire; }
    public Mission getMission() { return mission; }
    public boolean isSignale() { return signale; }
    public String getReponseEtudiant() { return reponseEtudiant; }
 
    // Setters
    public void setId(int id) { this.id = id; }
    public void setNote(int note) { this.note = note; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public void setAuteur(Client auteur) { this.auteur = auteur; }
    public void setDestinataire(EtudiantFreelance destinataire) { this.destinataire = destinataire; }
    public void setMission(Mission mission) { this.mission = mission; }
    public void setSignale(boolean signale) { this.signale = signale; }
    public void setReponseEtudiant(String reponse) { this.reponseEtudiant = reponse; }
 
    @Override
    public String toString() {
        return "Avis{#" + id + " | " + note + "/5 | " + auteur.getPrenom() + " ? " + destinataire.getPrenom() + "}";
    }
}
 
