package org.example.Test;

import org.example.Service.ApiService;
import org.example.Model.Player;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiServiceTest {
    
private final ApiService apiService = new ApiService();//Updated line

    @Test
    public void testGetPremierLeagueTeams() {
        List<Integer> teamIds = apiService.getPremierLeagueTeams();

        assertNotNull(teamIds, "Takım listesi null dönmemeli.");
        assertFalse(teamIds.isEmpty(), "Takım listesi boş olmamalı.");
        System.out.println("Premier Lig Takım ID'leri: " + teamIds);
    }

    @Test
    public void testGetPlayersFromFirstTeam() {
        List<Integer> teamIds = apiService.getPremierLeagueTeams();
        assertFalse(teamIds.isEmpty(), "Takım listesi boş, oyuncu testine geçilemiyor.");

        int firstTeamId = teamIds.get(0);
        List<Player> players = apiService.getPlayers(firstTeamId);

        assertNotNull(players, "Oyuncu listesi null olmamalı.");
        assertFalse(players.isEmpty(), "Oyuncu listesi boş olmamalı.");
        System.out.println("İlk takımdan gelen oyuncular:");
        players.forEach(System.out::println);
    }
    

    @Test
    void testInvalidApiResponseHandling() {
        
        List<Player> players = apiService.fetchPlayers();
        assertNotNull(players);
        assertTrue(players.size() > 0, "API response should not be empty");
    }
}
