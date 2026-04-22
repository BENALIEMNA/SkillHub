package com.skillhub.models;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for SkillHub platform
 * Provides interactive user interface for managing users, missions, and applications
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Utilisateur> users = new ArrayList<>();
    private static List<Mission> missions = new ArrayList<>();
    private static List<Avis> reviews = new ArrayList<>();
    private static Utilisateur currentUser = null;

    public static void main(String[] args) {
        displayWelcome();
        mainMenu();
    }

    private static void displayWelcome() {
        System.out.println("\n================================================");
        System.out.println("     Welcome to SkillHub - Freelancing Platform");
        System.out.println("================================================\n");
    }

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice = getIntInput();
                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        loginUser();
                        break;
                    case 3:
                        running = false;
                        System.out.println("Thank you for using SkillHub!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                userDashboard();
            }
        }
        scanner.close();
    }

    private static void registerUser() {
        System.out.println("\n--- Register New Account ---");
        System.out.print("First Name: ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Last Name: ");
        String nom = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password (min 6 characters): ");
        String password = scanner.nextLine().trim();

        System.out.println("\nSelect User Type:");
        System.out.println("1. Student Freelancer");
        System.out.println("2. Client");
        System.out.print("Choose: ");

        int userType = getIntInput();

        if (userType == 1) {
            registerStudentFreelancer(prenom, nom, email, password);
        } else if (userType == 2) {
            registerClient(prenom, nom, email, password);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void registerStudentFreelancer(String prenom, String nom, String email, String password) {
        System.out.println("\n--- Additional Information ---");
        System.out.print("University: ");
        String university = scanner.nextLine().trim();

        System.out.print("Field of Study (e.g., Web Development, Design): ");
        String domain = scanner.nextLine().trim();

        System.out.print("Year of Study (e.g., Licence 2, Master 1): ");
        String year = scanner.nextLine().trim();

        int id = users.size() + 1;
        EtudiantFreelance student = new EtudiantFreelance(id, nom, prenom, email, password, domain, university, year);

        if (student.sInscrire(nom, prenom, email, password)) {
            users.add(student);
            System.out.println("\nRegistration successful! Your account is pending admin approval.");
            System.out.println("You will receive a confirmation email at: " + email);
        }
    }

    private static void registerClient(String prenom, String nom, String email, String password) {
        System.out.println("\n--- Additional Information ---");
        System.out.print("Company/Organization Name: ");
        String organization = scanner.nextLine().trim();

        System.out.println("Select Client Type:");
        System.out.println("1. Individual");
        System.out.println("2. Academic");
        System.out.println("3. Enterprise");
        System.out.print("Choose: ");

        String clientType = "individual";
        int type = getIntInput();
        if (type == 2) clientType = "academic";
        else if (type == 3) clientType = "enterprise";

        int id = users.size() + 1;
        Client client = new Client(id, nom, prenom, email, password, clientType, organization, 0.0);

        if (client.sInscrire(nom, prenom, email, password)) {
            users.add(client);
            System.out.println("\nRegistration successful! Your account is pending admin approval.");
        }
    }

    private static void loginUser() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Find user
        for (Utilisateur user : users) {
            if (user.getEmail().equals(email)) {
                if (user.seConnecter(email, password)) {
                    currentUser = user;
                    return;
                } else {
                    System.out.println("Login failed. Invalid password or account not activated.");
                    return;
                }
            }
        }
        System.out.println("User not found.");
    }

    private static void userDashboard() {
        System.out.println("\n--- Dashboard for " + currentUser.getPrenom() + " " + currentUser.getNom() + " ---");

        if (currentUser instanceof EtudiantFreelance) {
            studentMenu((EtudiantFreelance) currentUser);
        } else if (currentUser instanceof Client) {
            clientMenu((Client) currentUser);
        }
    }

    private static void studentMenu(EtudiantFreelance student) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Student Freelancer Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Add Skill");
            System.out.println("3. Search Missions");
            System.out.println("4. View My Applications");
            System.out.println("5. Logout");
            System.out.print("Choose: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    student.afficherProfil();
                    break;
                case 2:
                    addSkill(student);
                    break;
                case 3:
                    searchMissions(student);
                    break;
                case 4:
                    viewApplications(student);
                    break;
                case 5:
                    currentUser.seDeconnecter();
                    currentUser = null;
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void clientMenu(Client client) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Client Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Publish Mission");
            System.out.println("3. View My Missions");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    System.out.println(client.toString());
                    break;
                case 2:
                    publishMission(client);
                    break;
                case 3:
                    client.consulterMissions();
                    break;
                case 4:
                    currentUser.seDeconnecter();
                    currentUser = null;
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addSkill(EtudiantFreelance student) {
        System.out.print("\nEnter skill name: ");
        String skill = scanner.nextLine().trim();
        student.ajouterCompetence(skill);
    }

    private static void searchMissions(EtudiantFreelance student) {
        System.out.println("\n--- Search Missions ---");
        System.out.print("Enter domain (leave empty for all): ");
        String domain = scanner.nextLine().trim();

        System.out.print("Maximum budget (leave empty for unlimited): ");
        String budgetStr = scanner.nextLine().trim();
        double budget = budgetStr.isEmpty() ? 0 : Double.parseDouble(budgetStr);

        List<Mission> results = Candidature.rechercherMissions(missions, domain.isEmpty() ? null : domain, budget);

        if (results.isEmpty()) {
            System.out.println("\nNo missions found matching your criteria.");
        } else {
            System.out.println("\n--- Available Missions (" + results.size() + ") ---");
            for (int i = 0; i < results.size(); i++) {
                Mission m = results.get(i);
                System.out.println((i + 1) + ". " + m.getTitre() + " - " + m.getBudget() + " DT (" + m.getDelai() + ")");
            }

            System.out.print("\nSelect mission (0 to cancel): ");
            int choice = getIntInput();
            if (choice > 0 && choice <= results.size()) {
                applyMission(student, results.get(choice - 1));
            }
        }
    }

    private static void applyMission(EtudiantFreelance student, Mission mission) {
        System.out.println("\n--- Apply to Mission: " + mission.getTitre() + " ---");
        System.out.print("Proposed Budget: ");
        double proposedBudget = getDoubleInput();

        System.out.print("Motivation Letter: ");
        String motivation = scanner.nextLine().trim();

        int id = missions.size() + 1;
        Candidature application = new Candidature(id, student, mission, motivation, proposedBudget);

        if (application.postuler()) {
            System.out.println("Application submitted successfully!");
        }
    }

    private static void viewApplications(EtudiantFreelance student) {
        List<Candidature> apps = student.getCandidatures();
        if (apps.isEmpty()) {
            System.out.println("\nYou have no applications.");
        } else {
            System.out.println("\n--- Your Applications (" + apps.size() + ") ---");
            for (Candidature app : apps) {
                System.out.println("• " + app.getMission().getTitre() + " - Status: " + app.getStatut());
            }
        }
    }

    private static void publishMission(Client client) {
        System.out.println("\n--- Publish New Mission ---");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Budget (DT): ");
        double budget = getDoubleInput();

        System.out.print("Deadline (e.g., 2 weeks): ");
        String deadline = scanner.nextLine().trim();

        System.out.print("Domain (e.g., Web Development): ");
        String domain = scanner.nextLine().trim();

        int id = missions.size() + 1;
        Mission mission = client.publierMission(id, title, description, budget, deadline, domain);

        if (mission != null) {
            missions.add(mission);
            System.out.println("\nMission published successfully! Awaiting admin approval.");
        }
    }

    private static int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            double value = Double.parseDouble(scanner.nextLine().trim());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}

