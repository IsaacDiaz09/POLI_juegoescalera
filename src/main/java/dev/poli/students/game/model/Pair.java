package dev.poli.students.game.model;

public class Pair<LEFT, RIGHT> {
    private final LEFT left;
    private final RIGHT right;

    private Pair(LEFT left, RIGHT right) {
        this.left = left;
        this.right = right;
    }

    public static <LEFT, RIGHT> Pair<LEFT, RIGHT> of(LEFT left, RIGHT right) {
        if (left == null || right == null) {
            throw new IllegalStateException("Both arguments are required");
        }
        return new Pair<>(left, right);
    }

    public LEFT getLeft() {
        return left;
    }

    public RIGHT getRight() {
        return right;
    }
}
