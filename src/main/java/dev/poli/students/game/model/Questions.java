package dev.poli.students.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Questions {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static final String EASY = "easy_questions";

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
    }

    private List<Question> questions;

    public Question getRandomQuestion() {
        int i = RANDOM.nextInt(this.questions.size());
        return this.questions.get(i);
    }
}
