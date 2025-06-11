package dev.poli.students.game.model;

public class GameConfiguration {
    private final MapsConfiguration maps;
    private final MapConfiguration mapConfiguration;

    public MapsConfiguration getMaps() {
        return maps;
    }

    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    public GameConfiguration(MapConfiguration mapConfiguration, MapsConfiguration maps) {
        this.mapConfiguration = mapConfiguration;
        this.maps = maps;
    }
}
