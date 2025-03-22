package org.example.Utils;

import org.example.Model.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterUtils {

    private static final Map<String, String> POSITION_MAP = Map.of(
            "Forvet", "Attacker",
            "Orta Saha", "Midfielder",
            "Defans", "Defender",
            "Kaleci", "Goalkeeper"
    );

    public static List<Player> filterByAge(List<Player> players, int minAge, int maxAge) {
        return players.stream()
                .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    public static List<Player> filterByExactPosition(List<Player> players, String positionTurkish) {
        String englishPosition = POSITION_MAP.getOrDefault(positionTurkish, positionTurkish);
        return players.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase(englishPosition))
                .collect(Collectors.toList());
    }

    public static List<Player> filterByName(List<Player> players, String namePart) {
        String lower = namePart.toLowerCase();
        return players.stream()
                .filter(p -> p.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
