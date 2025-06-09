package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MapConfiguration {
    @JsonProperty("map_id")
    private String mapId;

    @JsonProperty("img_path")
    private String imagePath;

    @JsonProperty("comodins")
    private List<Object> comodins;

    @Getter
    @AllArgsConstructor
    public static class Comodin {
        @JsonProperty("from")
        private int fromPosition;

        @JsonProperty("to")
        private int toPosition;

        @JsonIgnore
        public MapsConfiguration.ComodinType comodinType() {
            return this.fromPosition > this.toPosition ? MapsConfiguration.ComodinType.SNAKE : MapsConfiguration.ComodinType.LADDER;
        }
    }
}
