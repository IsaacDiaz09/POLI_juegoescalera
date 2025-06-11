package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Contenedor de todos los tableros (mapas) disponibles en la aplicación.
 * <p>
 * El objeto se serializa/deserializa con Jackson desde/​hacia un archivo
 * JSON de configuración.
 * Incluye — además del catálogo completo — el identificador del tablero
 * que debe considerarse “por defecto”.
 */
public class MapsConfiguration {

    /**
     * Tipo lógico de un “comodín” presente en el tablero
     * (escalera que sube / serpiente que baja).
     */
    public enum ComodinType {
        /** Escalera: avanza posiciones (de menor a mayor). */
        LADDER,
        /** Serpiente: retrocede posiciones (de mayor a menor). */
        SNAKE
    }

    /* ------------------------------------------------------------------ */
    /*                              Campos                                */
    /* ------------------------------------------------------------------ */

    /** Instancia con el mapa por defecto (cache estática, perezosa). */
    private static MapConfiguration DEFAULT_MAP = null;

    /** Identificador textual del mapa por defecto (viene del JSON). */
    @JsonProperty("default_map")
    private String defaultMap;

    /** Catálogo completo de tableros disponibles. */
    @JsonProperty("maps")
    private List<MapConfiguration> maps;

    /* ------------------------------------------------------------------ */
    /*                              Métodos                               */
    /* ------------------------------------------------------------------ */

    /**
     * Devuelve la instancia {@link MapConfiguration} correspondiente al
     * identificador definido en {@code default_map}.
     * <p>
     * Se resuelve solo la primera vez que se invoca, y se almacena en una
     * variable estática para evitar búsquedas repetidas.
     *
     * @return tablero por defecto.
     * @throws IllegalStateException si no se encuentra ninguno que coincida.
     */
    @JsonIgnore
    public MapConfiguration defaultMap() {
        if (DEFAULT_MAP == null) {
            DEFAULT_MAP = this.maps.stream()
                    .filter(map -> map.getMapId().equals(this.defaultMap))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException("Default map not found")
                    );
        }
        return DEFAULT_MAP;
    }

    /**
     * Acceso estático al mapa por defecto, una vez resuelto.
     *
     * @return instancia almacenada en caché o {@code null} si aún no se invocó
     *         {@link #defaultMap()}.
     */
    public static MapConfiguration getDefaultMap() {
        return DEFAULT_MAP;
    }

    /** @return lista completa de tableros disponibles. */
    public List<MapConfiguration> getMaps() {
        return maps;
    }

    /* ------------------------------------------------------------------ */
    /*                         Constructores                               */
    /* ------------------------------------------------------------------ */

    /** Constructor vacío requerido por Jackson. */
    public MapsConfiguration() { }

    /**
     * Constructor completo.
     *
     * @param defaultMap identificador del tablero por defecto.
     * @param maps       catálogo de tableros disponibles.
     */
    public MapsConfiguration(String defaultMap, List<MapConfiguration> maps) {
        this.defaultMap = defaultMap;
        this.maps       = maps;
    }
}
