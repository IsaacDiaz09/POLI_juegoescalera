package dev.poli.students.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Modelo que representa una pregunta de selección múltiple.
 * <p>
 * Se serializa/deserializa con Jackson para cargar bancos de preguntas
 * desde archivos JSON.  Cada pregunta contiene:
 * <ul>
 *   <li>El texto enunciado ({@code question}).</li>
 *   <li>Un mapa de respuestas posibles, donde la clave suele ser la letra
 *       de la opción (por ejemplo «A», «B», «C», «D»).</li>
 *   <li>La clave de la opción correcta ({@code correctAnswer}).</li>
 * </ul>
 */
public class Question {

    /** Texto del enunciado de la pregunta. */
    @JsonProperty("question")
    private String question;

    /** Opciones de respuesta: clave (letra) → texto mostrado. */
    @JsonProperty("answers")
    private Map<String, String> answers;

    /** Letra/clave que indica la opción correcta dentro del mapa {@code answers}. */
    @JsonProperty("correct")
    private String correctAnswer;

    /* ------------------------------------------------------------------ */
    /*                            Constructores                           */
    /* ------------------------------------------------------------------ */

    /** Constructor vacío requerido por Jackson. */
    public Question() { }

    /**
     * Constructor completo.
     *
     * @param question       texto del enunciado.
     * @param answers        mapa de opciones (clave → texto).
     * @param correctAnswer  clave correspondiente a la respuesta correcta.
     */
    public Question(String question,
                    Map<String, String> answers,
                    String correctAnswer) {
        this.question       = question;
        this.answers        = answers;
        this.correctAnswer  = correctAnswer;
    }

    /* ------------------------------------------------------------------ */
    /*                                Getters                             */
    /* ------------------------------------------------------------------ */

    /** @return texto de la pregunta. */
    public String getQuestion()        { return question; }

    /** @return mapa de respuestas disponibles. */
    public Map<String, String> getAnswers() { return answers; }

    /** @return clave de la respuesta correcta. */
    public String getCorrectAnswer()   { return correctAnswer; }
}
