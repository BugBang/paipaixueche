package com.session.dgjp.enity;

public enum ScoreLevel {

    level0("非常不满意", 0), level1("很不满意", 1), level2("不满意", 2), level3("一般", 3), level4("满意", 4), level5("很满意", 5);
    private final String name;
    private final int score;

    private ScoreLevel(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
