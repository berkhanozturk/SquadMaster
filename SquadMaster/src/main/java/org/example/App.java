package org.example;

import org.example.Model.Player;
import org.example.Service.ApiService;

import java.util.List;

public class App {
    public static void main(String[] args) {
        ApiService apiService = new ApiService();
        List<Player> players = apiService.fetchPlayers();

        // İlk 5 oyuncuyu ekrana yazdır (çok fazla veri çekmemek için)
        for (int i = 0; i < Math.min(5, players.size()); i++) {
            System.out.println(players.get(i));
        }
    }
}
