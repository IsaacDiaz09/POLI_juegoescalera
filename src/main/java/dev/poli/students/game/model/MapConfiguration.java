package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MapConfiguration {
    @JsonProperty("map_id")
    private String mapId;

    @JsonProperty("img_path")
    private String imagePath;

    @JsonProperty("comodins")
    private List<Object> comodins;

    public static class Comodin {
        @JsonProperty("from")
        private int fromPosition;

        @JsonProperty("to")
        private int toPosition;

        @JsonIgnore
        public MapsConfiguration.ComodinType comodinType() {
            return this.fromPosition > this.toPosition ? MapsConfiguration.ComodinType.SNAKE : MapsConfiguration.ComodinType.LADDER;
        }

        public Comodin(int fromPosition, int toPosition) {
            this.fromPosition = fromPosition;
            this.toPosition = toPosition;
        }

        public Comodin() {
        }

        public int getFromPosition() {
            return fromPosition;
        }

        public int getToPosition() {
            return toPosition;
        }
    }

    public String getMapId() {
        return mapId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public List<Object> getComodins() {
        return comodins;
    }

    public MapConfiguration() {
    }

    public MapConfiguration(String mapId, String imagePath, List<Object> comodins) {
        this.mapId = mapId;
        this.imagePath = imagePath;
        this.comodins = comodins;
    }
}
