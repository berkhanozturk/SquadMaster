package org.example.Utils;

import org.example.Model.Player;
import java.util.List;
import java.util.stream.Collectors;

public class FilterUtils {

    // Mevkiye göre filtreleme
    public static List<Player> filterByPosition(List<Player> players, String position) {
        return players.stream()
                .filter(player -> player.getPosition().equalsIgnoreCase(position))
                .collect(Collectors.toList());
    }

    // Yaşa göre filtreleme (Belirli bir yaş aralığında)
    public static List<Player> filterByAge(List<Player> players, int minAge, int maxAge) {
        return players.stream()
                .filter(player -> player.getAge() >= minAge && player.getAge() <= maxAge)
                .collect(Collectors.toList());
    }
}
