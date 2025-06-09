package dev.poli.students.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GameConfiguration {
    private MapsConfiguration maps;
    private MapConfiguration mapConfiguration;
}
