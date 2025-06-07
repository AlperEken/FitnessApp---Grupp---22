package org.FitnessApp1.model;

public class SessionManager {

    private static int aktivtKontoID = -1;  // Lagrar konto-ID för den aktiva användaren
    private static String username = ""; // Lägger till en variabel för användarnamnet

    // Hämta aktivt konto-ID
    public static int getActiveAccountID() {
        return aktivtKontoID;
    }

    // Sätt aktivt konto-ID
    public static void setActiveAccountID(int kontoID) {
        aktivtKontoID = kontoID;
    }

    // Hämta användarnamn
    public static String getUsername() {
        return username;
    }

    // Sätt användarnamn
    public static void setUsername(String user) {
        username = user;
    }

    // Rensa aktivt konto-ID (logga ut användaren)
    public static void clearActiveAccountID() {
        aktivtKontoID = -1; // Återställ till en ogiltig ID, vilket innebär att ingen är inloggad
        username = ""; // Rensa användarnamn vid utloggning
    }
}
