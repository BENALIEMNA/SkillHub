import com.skillhub.models.*;
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
        initData();
        displayWelcome();
        mainMenu();
    }

    // Initialise quelques données utiles (compte administrateur par défaut)
    private static void initData() {
        // Crée un administrateur par défaut (email: admin@skillhub.com / mdp: admin123)
        Administrateur admin = new Administrateur(0, "Admin", "SkillHub", "admin@skillhub.com", "admin123");
        admin.setStatut("actif");
        users.add(admin);
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
            // Pour le développement, on peut garder les comptes en attente pour tester l'admin
            // student.setStatut("actif");

            users.add(student);
            System.out.println("\nRegistration successful! Your account is now pending admin approval.");
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
            // Pour le développement, on peut garder les comptes en attente pour tester l'admin
            // client.setStatut("actif");

            users.add(client);
            System.out.println("\nRegistration successful! Your account is now pending admin approval.");
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
        } else if (currentUser instanceof Administrateur) {
            adminMenu((Administrateur) currentUser);
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
            System.out.println("5. View Notifications");
            System.out.println("6. Logout");
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
                    student.consulterNotifications();
                    break;
                case 6:
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
            System.out.println("4. Select Candidate");
            System.out.println("5. Rate Student & Complete Mission");
            System.out.println("6. View Notifications");
            System.out.println("7. Logout");
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
                    selectCandidate(client);
                    break;
                case 5:
                    rateAndCompleteMission(client);
                    break;
                case 6:
                    client.consulterNotifications();
                    break;
                case 7:
                    currentUser.seDeconnecter();
                    currentUser = null;
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Menu administrateur : approuver comptes et missions
    private static void adminMenu(Administrateur admin) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Administrateur Menu ---");
            System.out.println("1. Voir comptes en attente");
            System.out.println("2. Approuver/Rejeter un compte");
            System.out.println("3. Voir missions en attente");
            System.out.println("4. Approuver une mission");
            System.out.println("5. Consulter tableau de bord");
            System.out.println("6. Logout");
            System.out.print("Choose: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    afficherComptesEnAttente();
                    break;
                case 2:
                    traiterCompte(admin);
                    break;
                case 3:
                    afficherMissionsEnAttente();
                    break;
                case 4:
                    traiterMission(admin);
                    break;
                case 5:
                    displayAdminStatistics(admin);
                    break;
                case 6:
                    currentUser.seDeconnecter();
                    currentUser = null;
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Affiche les comptes dont le statut est 'en_attente'
    private static void afficherComptesEnAttente() {
        System.out.println("\n-- Comptes en attente --");
        int index = 1;
        for (Utilisateur u : users) {
            if ("en_attente".equals(u.getStatut())) {
                System.out.println(index + ". [id=" + u.getId() + "] " + u.getPrenom() + " " + u.getNom() + " (" + u.getTypeRole() + ") - " + u.getEmail());
                index++;
            }
        }
        if (index == 1) System.out.println("Aucun compte en attente.");
    }

    // Traiter (approuver ou rejeter) un compte
    private static void traiterCompte(Administrateur admin) {
        System.out.println("\nChoisissez l'id de l'utilisateur à traiter (ou 0 pour annuler):");
        // Liste les utilisateurs avec leur id
        for (Utilisateur u : users) {
            System.out.println("[id=" + u.getId() + "] " + u.getPrenom() + " " + u.getNom() + " - statut: " + u.getStatut());
        }
        int id = getIntInput();
        if (id <= 0) return;
        Utilisateur cible = null;
        for (Utilisateur u : users) {
            if (u.getId() == id) { cible = u; break; }
        }
        if (cible == null) {
            System.out.println("Utilisateur introuvable.");
            return;
        }
        System.out.println("1. Approuver\n2. Rejeter\nChoose: ");
        int choix = getIntInput();
        if (choix == 1) {
            admin.validerCompte(cible, true, null);
        } else if (choix == 2) {
            System.out.print("Motif du rejet: ");
            String motif = scanner.nextLine().trim();
            admin.validerCompte(cible, false, motif);
        } else {
            System.out.println("Option invalide.");
        }
    }

    // Affiche missions en attente
    private static void afficherMissionsEnAttente() {
        System.out.println("\n-- Missions en attente --");
        int index = 1;
        for (Mission m : missions) {
            if ("en_attente".equals(m.getStatut())) {
                System.out.println(index + ". [id=" + m.getId() + "] " + m.getTitre() + " - " + m.getDomaine() + " - " + m.getClient().getPrenom());
                index++;
            }
        }
        if (index == 1) System.out.println("Aucune mission en attente.");
    }

    // Traiter (approuver) une mission
    private static void traiterMission(Administrateur admin) {
        System.out.println("\nChoisissez l'id de la mission à approuver (ou 0 pour annuler):");
        for (Mission m : missions) {
            System.out.println("[id=" + m.getId() + "] " + m.getTitre() + " - statut: " + m.getStatut());
        }
        int id = getIntInput();
        if (id <= 0) return;
        Mission cible = null;
        for (Mission m : missions) {
            if (m.getId() == id) { cible = m; break; }
        }
        if (cible == null) {
            System.out.println("Mission introuvable.");
            return;
        }
        admin.validerMission(cible);
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

    private static void selectCandidate(Client client) {
        List<Mission> clientMissions = client.getMissions();
        if (clientMissions.isEmpty()) {
            System.out.println("\nYou have no missions.");
            return;
        }
        
        System.out.println("\n--- Your Missions ---");
        for (int i = 0; i < clientMissions.size(); i++) {
            Mission m = clientMissions.get(i);
            System.out.println((i + 1) + ". " + m.getTitre() + " [" + m.getCandidatures().size() + " candidatures] - Status: " + m.getStatut());
        }
        
        System.out.print("\nSelect mission (0 to cancel): ");
        int missionChoice = getIntInput();
        if (missionChoice <= 0 || missionChoice > clientMissions.size()) return;
        
        Mission mission = clientMissions.get(missionChoice - 1);
        List<Candidature> candidatures = mission.getCandidatures();
        
        if (candidatures.isEmpty()) {
            System.out.println("\nNo applications for this mission.");
            return;
        }
        
        System.out.println("\n--- Candidates for \"" + mission.getTitre() + "\" ---");
        for (int i = 0; i < candidatures.size(); i++) {
            Candidature c = candidatures.get(i);
            System.out.println((i + 1) + ". " + c.getEtudiant().getPrenom() + " " + c.getEtudiant().getNom() 
                    + " - Proposed: " + c.getPropositionBudget() + " DT - Status: " + c.getStatut());
        }
        
        System.out.print("\nSelect candidate (0 to cancel): ");
        int candChoice = getIntInput();
        if (candChoice <= 0 || candChoice > candidatures.size()) return;
        
        Candidature selected = candidatures.get(candChoice - 1);
        client.selectionnerCandidat(mission, selected);
    }

    private static void rateAndCompleteMission(Client client) {
        List<Mission> clientMissions = client.getMissions();
        List<Mission> activeOrInProgressMissions = new ArrayList<>();
        
        for (Mission m : clientMissions) {
            if ("active".equals(m.getStatut()) || "en_cours".equals(m.getStatut())) {
                activeOrInProgressMissions.add(m);
            }
        }
        
        if (activeOrInProgressMissions.isEmpty()) {
            System.out.println("\nNo active or in-progress missions to complete.");
            return;
        }
        
        System.out.println("\n--- Missions to Complete ---");
        for (int i = 0; i < activeOrInProgressMissions.size(); i++) {
            Mission m = activeOrInProgressMissions.get(i);
            System.out.println((i + 1) + ". " + m.getTitre() + " - Status: " + m.getStatut());
        }
        
        System.out.print("\nSelect mission (0 to cancel): ");
        int missionChoice = getIntInput();
        if (missionChoice <= 0 || missionChoice > activeOrInProgressMissions.size()) return;
        
        Mission mission = activeOrInProgressMissions.get(missionChoice - 1);
        
        // Mark mission as complete
        mission.setStatut("terminée");
        System.out.println("\n✓ Mission marked as completed!");
        
        // Rate the selected student
        if (mission.getCandidatures().isEmpty()) {
            System.out.println("No students assigned to this mission.");
            return;
        }
        
        List<Candidature> acceptedCandidatures = new ArrayList<>();
        for (Candidature c : mission.getCandidatures()) {
            if ("acceptée".equals(c.getStatut())) {
                acceptedCandidatures.add(c);
            }
        }
        
        if (acceptedCandidatures.isEmpty()) {
            System.out.println("No accepted candidates for this mission.");
            return;
        }
        
        System.out.println("\n--- Rate Student ---");
        Candidature accepted = acceptedCandidatures.get(0);
        System.out.print("Rating (1-5 stars): ");
        int rating = getIntInput();
        
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating.");
            return;
        }
        
        System.out.print("Review comment: ");
        String comment = scanner.nextLine().trim();
        
        Avis review = new Avis(reviews.size() + 1, rating, comment, client, accepted.getEtudiant(), mission);
        if (review.ajouterAvis()) {
            reviews.add(review);
            System.out.println("\n✓ Review published successfully!");
        }
    }

    private static void displayAdminStatistics(Administrateur admin) {
        System.out.println("\n=============================================");
        System.out.println("   TABLEAU DE BORD ADMINISTRATEUR");
        System.out.println("=============================================");
        
        // User statistics
        long nbEtudiants = users.stream().filter(u -> u.getTypeRole().equals("etudiant")).count();
        long nbClients = users.stream().filter(u -> u.getTypeRole().equals("client")).count();
        long nbEnAttente = users.stream().filter(u -> u.getStatut().equals("en_attente")).count();
        
        System.out.println("\n📊 UTILISATEURS:");
        System.out.println("   Total: " + users.size() + " | Étudiants: " + nbEtudiants + " | Clients: " + nbClients);
        System.out.println("   En attente de validation: " + nbEnAttente);
        
        // Mission statistics
        long nbMissionsActives = missions.stream().filter(m -> m.getStatut().equals("active")).count();
        long nbMissionsEnCours = missions.stream().filter(m -> m.getStatut().equals("en_cours")).count();
        long nbMissionsTerminees = missions.stream().filter(m -> m.getStatut().equals("terminée")).count();
        long nbMissionsEnAttente = missions.stream().filter(m -> m.getStatut().equals("en_attente")).count();
        double totalBudgetMissions = missions.stream().mapToDouble(Mission::getBudget).sum();
        double revenusCommission = Math.round(totalBudgetMissions * 0.05 * 100.0) / 100.0; // 5% commission
        
        System.out.println("\n📋 MISSIONS:");
        System.out.println("   Total: " + missions.size());
        System.out.println("   Actives: " + nbMissionsActives + " | En cours: " + nbMissionsEnCours + " | Terminées: " + nbMissionsTerminees);
        System.out.println("   En attente: " + nbMissionsEnAttente);
        System.out.println("   Budget total: " + totalBudgetMissions + " DT");
        System.out.println("   Commissions (5%): " + revenusCommission + " DT");
        
        // Reviews statistics
        long nbAvisPublies = reviews.size();
        System.out.println("\n⭐ AVIS & ÉVALUATIONS:");
        System.out.println("   Total: " + nbAvisPublies);
        
        System.out.println("\n👤 ADMIN:");
        System.out.println("   Validations effectuées: " + admin.getNbValidationsEffectuees());
        System.out.println("   Avis modérés: " + admin.getNbAvisModeres());
        
        // Alerts
        System.out.println("\n⚠️  ALERTES:");
        boolean alerteGeneree = false;
        for (Mission m : missions) {
            if (m.getStatut().equals("active") && m.getCandidatures().isEmpty()) {
                System.out.println("   🔔 Mission \"" + m.getTitre() + "\" active sans candidatures");
                alerteGeneree = true;
            }
        }
        if (!alerteGeneree) {
            System.out.println("   ✅ Aucune anomalie détectée");
        }
        
        System.out.println("\n=============================================\n");
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

