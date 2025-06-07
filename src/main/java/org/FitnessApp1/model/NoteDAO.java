package org.FitnessApp1.model;

import org.FitnessApp1.model.Note;
import org.FitnessApp1.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    public void saveNote(Note note) {
        String sql = "INSERT INTO note (content, konto_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note.getContent());
            stmt.setInt(2, note.getKontoId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Note> getNotesByKontoId(int kontoId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM note WHERE konto_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, kontoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setContent(rs.getString("content"));
                note.setKontoId(rs.getInt("konto_id"));
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public void updateNote(Note note) {
        String sql = "UPDATE note SET content = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note.getContent());
            stmt.setInt(2, note.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNoteById(int id) {
        String sql = "DELETE FROM note WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
