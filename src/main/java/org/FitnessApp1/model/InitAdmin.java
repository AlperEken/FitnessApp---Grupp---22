package org.FitnessApp1.model;

public class InitAdmin {
    public static void main(String[] args) {
        AdminDAO dao = new AdminDAO();
        boolean skapad = dao.createAdmin("123");
        System.out.println("Admin skapad? " + skapad);
    }
}
