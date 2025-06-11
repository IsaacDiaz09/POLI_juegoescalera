package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Representa la configuración visual y lógica de un tablero
 * (mapa) para el juego de escaleras y serpientes.
 * <p>
 * El objeto se serializa/deserializa con Jackson.
 * Atributos:
 * <ul>
 *   <li><strong>mapId</strong> – identificador único del mapa.</li>
 *   <li><strong>imagePath</strong> – ruta de la imagen PNG/JPG que
 *       se mostrará en la UI.</li>
 *   <li><strong>comodins</strong> – lista de “comodines” (escaleras
 *       o serpientes) que modifican la posición del jugador.</li>
 * </ul>
 */
public class MapConfiguration {

    /** Identificador único del mapa (clave en la base de datos o JSON). */
    @JsonProperty("map_id")
    private String mapId;

    /** Ruta de la imagen de fondo que representa el tablero. */
    @JsonProperty("img_path")
    private String imagePath;

    /** Lista de escaleras/serpientes asociadas al mapa. */
    @JsonProperty("comodins")
    private List<Object> comodins; // se usa Object para compatibilidad; idealmente List<Comodin>

    /* ------------------------------------------------------------------ */
    /*                         Clase interna: Comodin                     */
    /* ------------------------------------------------------------------ */

    /**
     * Describe un desplazamiento especial en el tablero.
     * Un “comodín” puede ser:
     * <ul>
     *   <li>Una <em>escalera</em> (sube): {@code fromPosition &lt; toPosition}</li>
     *   <li>Una <em>serpiente</em> (baja): {@code fromPosition &gt; toPosition}</li>
     * </ul>
     *
     * <p>El tipo se determina en tiempo de ejecución mediante
     * {@link #comodinType()}.</p>
     */
    public static class Comodin {

        @JsonProperty("from")
        private int fromPosition;

        @JsonProperty("to")
        private int toPosition;

        /**
         * Define si el comodín es escalera o serpiente.
         *
         * @return {@link MapsConfiguration.ComodinType#SNAKE} si baja,
         *         {@link MapsConfiguration.ComodinType#LADDER} si sube.
         */
        @JsonIgnore
        public MapsConfiguration.ComodinType comodinType() {
            return this.fromPosition > this.toPosition
                    ? MapsConfiguration.ComodinType.SNAKE
                    : MapsConfiguration.ComodinType.LADDER;
        }

        /* ------------------------------- Constructores ------------------------- */

        /**
         * Constructor completo.
         *
         * @param fromPosition casilla de origen.
         * @param toPosition   casilla de destino.
         */
        public Comodin(int fromPosition, int toPosition) {
            this.fromPosition = fromPosition;
            this.toPosition   = toPosition;
        }

        /** Constructor vacío requerido por Jackson. */
        public Comodin() { }

        /* -------------------------------- Getters ----------------------------- */

        /** @return casilla de origen del comodín */
        public int getFromPosition() { return fromPosition; }

        /** @return casilla de destino del comodín */
        public int getToPosition()   { return toPosition; }
    }

    /* ------------------------------------------------------------------ */
    /*                           Constructores                            */
    /* ------------------------------------------------------------------ */

    /** Constructor vacío necesario para la deserialización de Jackson. */
    public MapConfiguration() { }

    /**
     * Constructor completo.
     *
     * @param mapId      identificador único.
     * @param imagePath  ruta al recurso gráfico del tablero.
     * @param comodins   lista de escaleras/serpientes del mapa.
     */
    public MapConfiguration(String mapId,
                            String imagePath,
                            List<Object> comodins) {
        this.mapId     = mapId;
        this.imagePath = imagePath;
        this.comodins  = comodins;
    }

    /* ------------------------------------------------------------------ */
    /*                                 Getters                            */
    /* ------------------------------------------------------------------ */

    /** @return identificador único del mapa */
    public String getMapId()       { return mapId; }

    /** @return ruta de la imagen asociada */
    public String getImagePath()   { return imagePath; }

    /**
     * Devuelve la lista de comodines (escaleras o serpientes).
     * Nota: el tipo genérico se mantiene como {@code List<Object>} para
     * compatibilidad con Jackson; idealmente debería ser {@code List<Comodin>}.
     *
     * @return lista de comodines.
     */
    public List<Object> getComodins() { return comodins; }
}
