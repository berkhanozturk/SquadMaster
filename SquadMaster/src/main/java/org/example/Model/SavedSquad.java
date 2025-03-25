package org.example.Model;

import java.util.List;

public class SavedSquad {
    private String formation;
    private List<Player> players;

    public SavedSquad() {
    }

    public SavedSquad(String formation, List<Player> players) {
        this.formation = formation;
        this.players = players;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
