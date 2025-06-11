package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MapsConfiguration {

    public enum ComodinType {
        LADDER,
        SNAKE,
    }

    private static MapConfiguration DEFAULT_MAP = null;

    @JsonProperty("default_map")
    private String defaultMap;

    @JsonProperty("maps")
    private List<MapConfiguration> maps;

    @JsonIgnore
    public MapConfiguration defaultMap() {
        if (DEFAULT_MAP == null) {
            DEFAULT_MAP = this.maps.stream()
                    .filter(map -> map.getMapId().equals(this.defaultMap))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Default map not found"));
        }
        return DEFAULT_MAP;
    }

    public static MapConfiguration getDefaultMap() {
        return DEFAULT_MAP;
    }

    public List<MapConfiguration> getMaps() {
        return maps;
    }

    public MapsConfiguration() {
    }

    public MapsConfiguration(String defaultMap, List<MapConfiguration> maps) {
        this.defaultMap = defaultMap;
        this.maps = maps;
    }
}
