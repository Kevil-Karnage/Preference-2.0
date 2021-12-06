package main.java.Preference.models;

import main.java.Preference.models.enums.CardsNominal;
import main.java.Preference.models.enums.CardsSuits;

public class Card implements Comparable<Card>{
    private CardsNominal nominal;
    private CardsSuits suit;

    public Card(CardsNominal nominal, CardsSuits suit) {
        this.nominal = nominal;
        this.suit = suit;
    }

    public CardsNominal getNominal() {
        return nominal;
    }

    public CardsSuits getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card second) {
        return second.nominal.getRank() -
                this.nominal.getRank();
    }
}
