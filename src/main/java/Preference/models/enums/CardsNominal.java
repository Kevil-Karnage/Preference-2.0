package main.java.Preference.models.enums;

public enum CardsNominal {
    SEVEN("7", 7), EIGHT("8", 8),
    NINE("9", 9), TEN("10", 10), JACK("J", 11),
    QUEEN("Q", 12), KING("K", 13), ACE("A", 14);

    private String name;
    private int rank;
    private int maxRank = 14;

    CardsNominal(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }
}
