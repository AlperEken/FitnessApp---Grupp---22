package org.FitnessApp1.model;

public class Note {
    private int id;
    private String content;
    private int kontoId;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getKontoId() { return kontoId; }
    public void setKontoId(int kontoId) { this.kontoId = kontoId; }
}
