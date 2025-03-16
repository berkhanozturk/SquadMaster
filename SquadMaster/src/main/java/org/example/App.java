package org.example;

import org.example.Model.Player;
import org.example.Service.ApiService;
import org.example.Utils.FilterUtils;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        ApiService apiService = new ApiService();
        List<Player> allPlayers = new ArrayList<>();

        try {
            // Premier Lig'deki tüm takımların ID'lerini çek
            List<Integer> teamIds = apiService.getPremierLeagueTeams();
            if (teamIds.isEmpty()) {
                System.err.println("Premier Lig takımları bulunamadı!");
                return;
            }

            // Her takımın oyuncularını çek
            for (int teamId : teamIds) {
                List<Player> teamPlayers = apiService.getPlayers(teamId);
                allPlayers.addAll(teamPlayers);
            }

            // Tüm oyuncuları ekrana yazdır
            System.out.println("Premier Lig'deki Tüm Oyuncular:");
            allPlayers.forEach(System.out::println);

            // Sadece "Forward" pozisyonundaki oyuncuları filtrele
            List<Player> forwards = FilterUtils.filterByPosition(allPlayers, "Forward");
            System.out.println("\nForvet Oyuncular:");
            forwards.forEach(System.out::println);

            // 20-30 yaş arasındaki oyuncuları filtrele
            List<Player> youngPlayers = FilterUtils.filterByAge(allPlayers, 20, 30);
            System.out.println("\n20-30 Yaş Arasındaki Oyuncular:");
            youngPlayers.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Hata oluştu: " + e.getMessage());
        }
    }
}
