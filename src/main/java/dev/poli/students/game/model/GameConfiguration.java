package dev.poli.students.game.model;

/**
 * Encapsula la configuración de mapas que se usará durante la partida.
 * <p>
 * Mantiene tanto la colección completa de mapas disponibles
 * ({@link MapsConfiguration}) como la selección concreta de mapa
 * ({@link MapConfiguration}) para la sesión actual.
 *
 * <p>La clase es inmutable: todos sus atributos son {@code final} y solo
 * se asignan a través del constructor.</p>
 */
public class GameConfiguration {

    /** Conjunto de mapas disponibles en la aplicación. */
    private final MapsConfiguration maps;

    /** Mapa seleccionado para la partida actual. */
    private final MapConfiguration mapConfiguration;

    /* ------------------------------------------------------------------ */
    /*                             Constructor                            */
    /* ------------------------------------------------------------------ */

    /**
     * Crea una configuración de juego.
     *
     * @param mapConfiguration mapa concreto que se usará en la partida
     *                         (por ejemplo, “Nivel 1” o “Tablero clásico”).
     * @param maps             colección (catálogo) de todos los mapas que la
     *                         aplicación conoce y podría mostrar al usuario.
     */
    public GameConfiguration(MapConfiguration mapConfiguration,
                             MapsConfiguration maps) {
        this.mapConfiguration = mapConfiguration;
        this.maps            = maps;
    }

    /* ------------------------------------------------------------------ */
    /*                                 Getters                            */
    /* ------------------------------------------------------------------ */

    /**
     * Devuelve la colección completa de mapas disponibles.
     *
     * @return instancia {@link MapsConfiguration} con la lista/catálogo global.
     */
    public MapsConfiguration getMaps() {
        return maps;
    }

    /**
     * Devuelve el mapa elegido para la partida.
     *
     * @return instancia {@link MapConfiguration} con el tablero seleccionado.
     */
    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }
}
