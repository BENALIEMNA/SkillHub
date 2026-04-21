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
 
  
