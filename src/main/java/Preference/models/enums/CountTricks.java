package main.java.Preference.models.enums;

public enum CountTricks {
    SIX(6, 1, "6"),
    SEVEN(7, 2, "7"),
    EIGHT(8, 3, "8"),
    NINE(9, 4, "9"),
    TEN(10, 5, "10");

    private int count;
    private int rank;
    private String name;

    CountTricks(int count, int rank, String name) {
        this.count = count;
        this.rank = rank;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }

    public static CountTricks get(int n) {
        CountTricks returnedTrick = CountTricks.SIX;
        for (CountTricks trick: CountTricks.values()) {
            if (trick.getRank() == n) {
                returnedTrick = trick;
                break;
            }
        }
        return returnedTrick;
    }

    public String getName() {
        return name;
    }
}
