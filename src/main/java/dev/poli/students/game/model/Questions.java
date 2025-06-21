package dev.poli.students.game.model;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contenedor de un banco de preguntas y utilidades para consultarlo.
 * <p>
 * Proporciona dos operaciones de uso frecuente:
 * <ul>
 *   <li>{@link #getRandomQuestion()} – devuelve una pregunta aleatoria.</li>
 *   <li>{@link #findQuestion(String)} – busca una pregunta por texto exacto.</li>
 * </ul>
 *
 * <p>La selección aleatoria utiliza {@link SecureRandom} para garantizar
 * una distribución uniforme y segura.</p>
 */
public class Questions {

    /* ------------------------------------------------------------------ */
    /*                               Constantes                           */
    /* ------------------------------------------------------------------ */

    /**
     * Fuente de aleatoriedad para elegir preguntas sin sesgo.
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Clave JSON utilizada por los archivos de nivel “fácil”.
     */
    public static final String EASY = "easy_questions";

    /**
     * Clave JSON utilizada por los archivos de nivel “fácil”.
     */
    public static final String MEDIUM = "medium_questions";

    /**
     * Clave JSON utilizada por los archivos de nivel “dificil”.
     */
    public static final String HARD = "hard_questions";

    /**
     * Dificultad asociada a un grupo de preguntas.
     * Se puede emplear como clave para cargar distintos archivos JSON
     * o filtrar bancos de preguntas en memoria.
     */
    public enum Difficulty {
        EASY("Facil"),
        MEDIUM("Medio"),
        HARD("Dificil");

        Difficulty(String description) {
            this.description = description;
        }

        public Difficulty byName(String name) {
            return Stream.of(Difficulty.values())
                    .filter(d -> d.toString().equals(name))
                    .findFirst()
                    .orElseThrow();
        }

        private final String description;

        @Override
        public String toString() {
            return this.description;
        }
    }

    /* ------------------------------------------------------------------ */
    /*                               Campos                               */
    /* ------------------------------------------------------------------ */

    /**
     * Lista de preguntas contenida en el banco.
     */
    private List<Question> questions;

    /* ------------------------------------------------------------------ */
    /*                               Métodos                              */
    /* ------------------------------------------------------------------ */

    /**
     * Devuelve una pregunta elegida al azar del banco.
     *
     * @return instancia {@link Question} seleccionada aleatoriamente.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Question getRandomQuestion() {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalStateException("Question list is empty");
        }
        int i = RANDOM.nextInt(this.questions.size());
        return this.questions.get(i);
    }

    /**
     * Busca una pregunta cuyo enunciado coincida exactamente con el texto dado.
     *
     * @param question texto completo de la pregunta a buscar.
     * @return {@link Optional} con la pregunta si existe, vacío en caso contrario.
     */
    public Optional<Question> findQuestion(String question) {
        return this.questions.stream()
                .filter(q -> q.getQuestion().equals(question))
                .findFirst();
    }

    /* ------------------------------------------------------------------ */
    /*                            Constructores                           */
    /* ------------------------------------------------------------------ */

    /**
     * Constructor vacío (por ejemplo, para frameworks de deserialización).
     */
    public Questions() {
    }

    /**
     * Constructor completo.
     *
     * @param questions lista de preguntas que conforman el banco.
     */
    public Questions(List<Question> questions) {
        this.questions = questions;
    }

    /* ------------------------------------------------------------------ */
    /*                                Getter                              */
    /* ------------------------------------------------------------------ */

    /**
     * @return lista interna de preguntas.
     */
    public List<Question> getQuestions() {
        return questions;
    }
}
