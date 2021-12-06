package main.java.Preference.models;

import java.util.*;

public class Round {
    private List<Card> deck;                    //колода
    private Card[] buiIn;                       //прикуп
    private Fight[] fights;                     //бои
    private Map<Player, Integer> countTricks;   //количество взяток игроков
    private Bet bet;                            //ставка
    private Player placedPlayer;                //игрок, поставивший ставку
    private Map<Player, Boolean> isPlaced;      //могут ли игроки сделать ставку
    private Map<Player, Bet> maxBets;           //максимальная ставка игроков
    private Map<Player, List<Card>> cards;      //карты игроков
    private Map<Player, Integer> score;         //счёт за раунд

    public Round(Player[] players) {
        this.deck = new ArrayList<>();
        countTricks = new HashMap<>();
        for (int i = 0; i < players.length; i++) {
            countTricks.put(players[i], 0);
        }

        this.buiIn = new Card[2];
        this.fights = new Fight[10];
        for (int i = 0; i < 10; i++) {
            fights[i] = new Fight();
        }
        this.bet = new Bet(null, null);

        isPlaced = new HashMap<>();
        maxBets = new HashMap<>();
        cards = new HashMap<>();

        for (int i = 0; i < players.length; i++) {
            isPlaced.put(players[i], true);
            cards.put(players[i], new ArrayList<>());
        }

        score = new HashMap<>();
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public void setBuiIn(Card[] buiIn) {
        this.buiIn = buiIn;
    }

    public Card[] getBuiIn() {
        return buiIn;
    }

    public Fight[] getFights() {
        return fights;
    }

    public void addNTricksToPlayer(int n, Player player) {
        countTricks.put(player, countTricks.get(player) + n);
    }

    public Bet getBet() {
        return bet;
    }

    public Bet getMaxBet(Player p) {
        return maxBets.get(p);
    }

    public Map<Player, List<Card>> getCards() {
        return cards;
    }

    public Player getPlacedPlayer() {
        return placedPlayer;
    }

    public Map<Player, Integer> getScore() {
        return score;
    }

    public void setScore(Map<Player, Integer> score) {
        this.score = score;
    }

    public Map<Player, Integer> getCountTricks() {
        return countTricks;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }

    public void setPlacedPlayer(Player placedPlayer) {
        this.placedPlayer = placedPlayer;
    }

    public Map<Player, Boolean> getIsPlaced() {
        return isPlaced;
    }

    public void setIsPlaced(Map<Player, Boolean> isPlaced) {
        this.isPlaced = isPlaced;
    }

    public Map<Player, Bet> getMaxBets() {
        return maxBets;
    }

    public void setMaxBets(Map<Player, Bet> maxBets) {
        this.maxBets = maxBets;
    }

    public void setCards(Map<Player, List<Card>> cards) {
        this.cards = cards;
    }
}