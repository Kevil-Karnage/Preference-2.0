package main.java.Preference.services;

import main.java.Preference.models.*;
import main.java.Preference.models.enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundService {
    private PrintService ps;

    public RoundService(PrintService ps) {
        this.ps = ps;
    }

    /**
     * Добавляем колоду в раунд
     * @param r
     */
    public void addDeckInRound(Round r) {
        List<Card> deck = new ArrayList<>();
        for (CardsNominal nominal: CardsNominal.values()) {
            for (int i = 0; i < CardsSuits.values().length - 1; i++) {
                Card card = new Card(nominal, CardsSuits.get(i + 1));
                deck.add(card);
            }
        }

        ps.addDeckInRound(deck.size());
        r.setDeck(deck);
    }

    /**
     *  Перемешиваем колоду
     * @param r
     */
    public void mixDeck(Round r) {
        List<Card> deck = r.getDeck();
        List<Card> mixedDeck = new ArrayList<>();
        while(deck.size() != 0) {
            int n = (int) (deck.size() * Math.random());
            mixedDeck.add(deck.get(n));
            deck.remove(n);
        }
        r.setDeck(mixedDeck);
    }

    /**
     * Раздача карт игрокам
     */
    public void distributionCards(Round r, Player[] players) {
        List<Card>[] playersCards = new ArrayList[3];
        Card[] buiIn = new Card[2];             //прикуп
        List<Card> deck = r.getDeck();      //берём колоду

        for (int i = 0; i < players.length; i++) {
            playersCards[i] = new ArrayList<>();
        }

        //раздаём карты игрокам
        while (deck.size() > 2) {
            for (int i = 0; i < players.length; i++) {
                playersCards[i].add(deck.remove(0));
            }
        }

        //оставшиеся две карты кладём в прикуп
        int i = 0;
        while (deck.size() != 0) {
            buiIn[i] = deck.remove(0);
            i++;
        }

        //сохраняем информацию об игроках в Round
        for (int j = 0; j < players.length; j++) {
            addPlayerCards(r, players[j], playersCards[j]);
            ps.playerCards(r, players[j]);
        }
        r.setBuiIn(buiIn);
        ps.allPlayersGotCards();
    }

    /**
     * разыгрывание раунда
     * @return
     */
    public Player[] playFight(Round r, int fightNumber, Player[] lastPlayersQueue) {

        Fight fight = r.getFights()[fightNumber];

        Map<Player, Card> cards = new HashMap<>();

        for (int i = 0; i < lastPlayersQueue.length; i++) {
            Player player = lastPlayersQueue[i];
            Card movedCard = removePlayerCard(r, player, cards.get(lastPlayersQueue[0]), r.getBet().getTrump());

            cards.put(player, movedCard);

            ps.playerMove(player, movedCard);
        }
        fight.setLastPlayersQueue(lastPlayersQueue);
        fight.setCards(cards);
        fight.setNextPlayersQueue(searchNextPlayersQueue(r.getBet().getTrump(), fight));

        return fight.getNextPlayersQueue();
    }

    public Player[] searchNextPlayersQueue(CardsSuits trump, Fight f) {
        Player[] lastPlayers = f.getLastPlayersQueue();
        Map<Player, Card> cards = f.getCards();
        Player lastFirst = lastPlayers[0];

        Card main = cards.get(lastFirst);
        int numbMainPlayer = 0;
        Player[] nextPlayers;
        for (int i = 1; i < lastPlayers.length; i++) {
            Card currCard = cards.get(lastPlayers[i]);
            if (main.getSuit().equals(currCard.getSuit())) {    // если главная и текущая одной масти
                if (main.compareTo(currCard) > 0) {             // главной становится большая из них
                    main = currCard;
                    numbMainPlayer = i;
                }
            } else if (!main.getSuit().equals(trump)) {            // если главная не козырь
                if (currCard.getSuit().equals(trump)) {     // а текущая козырь
                    main = currCard;                        // текущая становится главной
                    numbMainPlayer = i;                     // i-тый игрок становится обладателем главной
                }
            }
        }

        nextPlayers = createNextPlayersQueue(lastPlayers, numbMainPlayer);
        f.setNextPlayersQueue(nextPlayers);
        return nextPlayers;
    }

    public Player[] createNextPlayersQueue(Player[] lastPlayers, int first) {
        Player[] nextPlayersQueue = new Player[lastPlayers.length];
        for (int i = first; i < lastPlayers.length; i++) {
            nextPlayersQueue[i - first] = lastPlayers[i];
        }
        int det = lastPlayers.length - first;
        for (int i = 0; i < first; i++) {
            nextPlayersQueue[i + det] = lastPlayers[i];
        }

        return nextPlayersQueue;
    }

    public void trade(Game g, Round r) {
        //торги за прикуп
        Player[] players = g.getPlayers();
        while (tradeIsActive(g, r)) {
            for (int i = 0; i < players.length; i++) {
                if (tradeIsActive(g, r)) {
                    if (isPlaced(r, players[i])) {

                        placeBet(r, players[i]);
                        if (isPlaced(r, players[i])) {
                            ps.playerTrade(r.getPlacedPlayer(), r.getBet());
                        } else {
                            ps.playerPassed(players[i]);
                        }
                    }
                }
            }
        }
        ps.bet(r);
    }

    private boolean tradeIsActive(Game g, Round r) {
        Player[] players = g.getPlayers();
        int countPlayersInTrade = 0;
        for (int i = 0; i < players.length; i++) {
            if (isPlaced(r, players[i])) {
                countPlayersInTrade++;
            }
        }
        if (countPlayersInTrade > 1) {
            return true;
        } else {
            return false;
        }
    }

    public Player[] contract(Game g, Round r) {
        Player p = r.getPlacedPlayer();
        Map<Player, List<Card>> playerCards = r.getCards();
        List<Card> cards = playerCards.get(p);
        Card[] buiIn = r.getBuiIn();
        for (int i = 0; i < buiIn.length; i++) {
            cards.add(buiIn[i]);
        }
        cards = sortForSuit(cards);
        for (int i = 0; i < buiIn.length; i++) {
            cards.remove(0);
        }
        playerCards.put(p, cards);
        r.setCards(playerCards);
        setBet(r, r.getMaxBet(p), p);
        Player[] newQueue = createNewQueue(g.getPlayers(), p);
        return newQueue;
    }

    public Player[] createNewQueue(Player[] players, Player first) {
        List<Player> newQueue = new ArrayList<>();
        int i = 0;
        while(!players[i].equals(first)) {
            i++;
        }
        for (int j = i; j < players.length; j++) {
            newQueue.add(players[j]);
        }
        for (int j = 0; j < i; j++) {
            newQueue.add(players[j]);
        }

        Player[] newPlayersQueue = new Player[players.length];
        for (int j = 0; j < newQueue.size(); j++) {
            newPlayersQueue[j] = newQueue.get(j);
        }
        return newPlayersQueue;
    }

    public void placeBet(Round r, Player p) {
        Bet rBet = r.getBet();  // текущая ставка
        CardsSuits rBetTrump = rBet.getTrump(); // масть текущей ставки

        Bet pMax = r.getMaxBet(p);  // максимальная возможная ставка игрока
        CardsSuits pMaxTrump = pMax.getTrump(); // козырь максимальной возможной ставки игрока

        Bet newBet; // ставка игрока
        if (rBetTrump == null && rBet.getCountTricks() == null) {
            newBet = new Bet(CountTricks.SIX, pMax.getTrump());
            setBet(r, newBet, p);
        } else
        if (pMax.compareTo(rBet) > 0) {
            // если максимальная ставка игрока больше текущей, то он может сделать ставку

            if (pMaxTrump.compareTo(rBetTrump) > 0) {
                // если козырь максимальной ставки игрока больше козыря текущей,
                // то ставит столько же взяток и меняет козырь на свой
                newBet = new Bet(rBet.getCountTricks(), pMaxTrump);
            } else {
                // иначе ставит на 1 взятку больше и меняет козырь на свой
                newBet = new Bet(CountTricks.get(rBet.getCountTricks().getRank() + 1), pMaxTrump);
            }
            // обновляем текущую ставку(устанавливаем ставку игрока как текущую)
            setBet(r, newBet, p);
        } else {
            // если  максимальная ставка игрока меньше или равна текущей, то он не может поставить и пасует
            setPlayerIsPlaced(r, p, false);
        }
    }

    public void scoreInRound(Round r) {
        ps.countTricksInRound(r);

        Map<Player, Integer> countTricks = r.getCountTricks();
        Map<Player, Integer> scoreInRound = new HashMap<>();
        Player placedPlayer = r.getPlacedPlayer();
        Bet bet = r.getBet();
        for (Player p: countTricks.keySet()) {
            int countPoints;
            if (p.equals(placedPlayer)) {
                if (countTricks.get(p) >= bet.getCountTricks().getCount()) {
                    countPoints = bet.getCountTricks().getCount();
                    ps.completedContract(p);
                } else {
                    countPoints = 0;
                    ps.notCompletedContract(p);
                }
            } else {
                countPoints = countTricks.get(p);
            }
            scoreInRound.put(p, countPoints);
        }
        r.setScore(scoreInRound);
    }

    public void searchMaxBet(Round r, Player p) {
        Bet playerBet = new Bet(
                CountTricks.get(1 + (int) (CountTricks.values().length * Math.random())),
                CardsSuits.get(1 + (int) (CardsSuits.values().length * Math.random()))
        );
        Map<Player, Bet> maxBets = r.getMaxBets();
        maxBets.put(p, playerBet);
        r.setMaxBets(maxBets);
    }

    public void addPlayerCards(Round r, Player p, List<Card> cards) {
        cards = sortForSuit(cards);
        Map<Player, List<Card>> playerCards = r.getCards();
        playerCards.put(p, cards);
        searchMaxBet(r, p);
    }

    private List<Card> sortForSuit (List<Card> cards) {
        boolean isSorted = false;
        Card buffer;
        while (!isSorted) {
            isSorted = true;
            for (int j = 0; j < cards.size() - 1; j++) {
                if (cards.get(j + 1).getSuit().getRank() < cards.get(j).getSuit().getRank()) {
                    buffer = cards.get(j);
                    cards.set(j, cards.get(j + 1));
                    cards.set(j + 1, buffer);
                    isSorted = false;
                } else if (cards.get(j).getSuit().getRank() == cards.get(j + 1).getSuit().getRank()) {
                    if (cards.get(j + 1).getNominal().getRank() < cards.get(j).getNominal().getRank()) {
                        buffer = cards.get(j);
                        cards.set(j, cards.get(j + 1));
                        cards.set(j + 1, buffer);
                        isSorted = false;
                    }
                }
            }
        }
        return cards;
    }

    public Card removePlayerCard(Round r,Player p, Card firstCard, CardsSuits trump) {
        Map<Player, List<Card>> cards = r.getCards();
        List<Card> playerCards = cards.get(p);
        if (firstCard == null) {
            Card returnedCard = playerCards.remove(playerCards.size() - 1);
            cards.put(p, playerCards);
            r.setCards(cards);
            return returnedCard;
        }
        if (firstCard != null) {
            for (int i = playerCards.size() - 1; i >= 0; i--) {
                if (playerCards.get(i).getSuit().equals(firstCard.getSuit())) {
                    Card returnedCard = playerCards.remove(i);
                    cards.put(p, playerCards);
                    r.setCards(cards);
                    return returnedCard;
                }
            }
        }

        if (!trump.equals(CardsSuits.NO_TRUMP)) {
            for (int i = playerCards.size() - 1; i >= 0; i--) {
                if (playerCards.get(i).getSuit().equals(trump)) {
                    Card returnedCard = playerCards.remove(i);
                    cards.put(p, playerCards);
                    r.setCards(cards);
                    return returnedCard;
                }
            }
        }

        Card card = playerCards.remove(0);
        cards.put(p, playerCards);
        r.setCards(cards);
        return card;
    }

    public void setPlayerIsPlaced(Round r, Player p, boolean isPlaced) {
        Map<Player, Boolean> placed = r.getIsPlaced();
        placed.put(p, isPlaced);
        r.setIsPlaced(placed);
    }

    public boolean isPlaced(Round r, Player p) {
        Map<Player, Boolean> placed = r.getIsPlaced();
        return placed.get(p);
    }

    public void setBet(Round r, Bet b, Player p) {
        r.setBet(b);
        r.setPlacedPlayer(p);
    }
}
