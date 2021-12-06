package main.java.Preference.models;

import java.util.HashMap;
import java.util.Map;

public class Fight {
    private Player[] lastPlayersQueue;
    private Map<Player, Card> cards;
    private Player[] nextPlayersQueue;

    public Fight() {
        cards = new HashMap<>();
    }

    public Map<Player, Card> getCards() {
        return cards;
    }

    public void setCards(Map<Player, Card> cards) {
        this.cards = cards;
    }

    public Player[] getLastPlayersQueue() {
        return lastPlayersQueue;
    }

    public void setLastPlayersQueue(Player[] lastPlayersQueue) {
        this.lastPlayersQueue = lastPlayersQueue;
    }

    public Player[] getNextPlayersQueue() {
        return nextPlayersQueue;
    }

    public void setNextPlayersQueue(Player[] nextPlayersQueue) {
        this.nextPlayersQueue = nextPlayersQueue;
    }
}
