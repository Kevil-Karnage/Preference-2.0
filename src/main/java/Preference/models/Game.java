package main.java.Preference.models;

import java.util.Map;

public class Game {
    private Player[] players;       //игроки
    private Round[] rounds;         //раунды
    private Map<Player, Integer> score; //счёт

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Round[] getRounds() {
        return rounds;
    }

    public Round getRound(int numberRound) {
        return rounds[numberRound];
    }

    public void setRounds(Round[] rounds) {
        this.rounds = rounds;
    }

    public Map<Player, Integer> getScore() {
        return score;
    }

    public void setScore(Map<Player, Integer> score) {
        this.score = score;
    }
}
