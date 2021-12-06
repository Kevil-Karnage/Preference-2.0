package main.java.Preference.models.enums;

public enum CardsSuits {
    SPADE("♠", 1), CLUB("♣", 2),
    DIAMOND("♦", 3), HEART("♥", 4),
    NO_TRUMP("no trump", 5);

    private String name;
    private int rank;

    CardsSuits(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public int getRank() {
            return rank;
    }

    public String getName() {
        return name;
    }

    public static CardsSuits get(int n) {
        CardsSuits trick = CardsSuits.SPADE;
        for (CardsSuits suit: CardsSuits.values()) {
            if (suit.getRank() == n) {
                trick = suit;
                break;
            }
        }
        return trick;
    }
}
