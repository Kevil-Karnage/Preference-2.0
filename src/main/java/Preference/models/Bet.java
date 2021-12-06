package main.java.Preference.models;

import main.java.Preference.models.enums.CountTricks;
import main.java.Preference.models.enums.CardsSuits;

public class Bet implements Comparable<Bet> {
    private CountTricks countTricks;
    private CardsSuits trump;

    public Bet(CountTricks countTricks, CardsSuits trump) {
        this.countTricks = countTricks;
        this.trump = trump;
    }

    public CountTricks getCountTricks() {
        return countTricks;
    }

    public CardsSuits getTrump() {
        return trump;
    }

    @Override
    public int compareTo(Bet second) {
        int count = 10;
        int bet1 = trump.getRank() + count * countTricks.getRank();
        int bet2 = second.trump.getRank() + count * second.countTricks.getRank();
        return bet1 - bet2;
    }
}
