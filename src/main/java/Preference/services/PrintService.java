package main.java.Preference.services;

import main.java.Preference.models.*;

import java.util.List;
import java.util.Map;

public class PrintService {

    public void playPreference() {
        System.out.println("\nЗапущена игра Преферанс\n");
    }

    public void addPlayerInGame(String name) {
        System.out.printf("Игрок %s присоединяется к игре\n",
                name);
    }

    public void addDeckInRound(int n) {
        System.out.printf("\nДобавлена колода из %d карт\n",
                n);
    }

    public void allPlayersGotCards() {
        System.out.println("\nИгроки получили свои карты\n");
    }

    public void allPlayersReady() {
        System.out.println("\nВсе игроки готовы к игре\n");
    }

    public void readCountRounds() {
        System.out.print("Введите количество раундов в игре: ");
    }

    public void createNRounds(int n) {
        System.out.printf("Создано %d раундов\n",
                n);
    }

    public void startNRound(int n) {
        System.out.printf("\n/*/*/*/ Начинается %d раунд /*/*/*/\n",
                n);
    }

    public void startNFight(int n) {
        System.out.printf("\n*---------| Начинается %d бой |---------*\n",
                n);
    }

    public void scoreInGame(Game g) {

        System.out.println("\n| - - |Счёт за игру:| - - |");
        Player[] players = g.getPlayers();
        Map<Player, Integer> score = g.getScore();
        for (int i = 0; i < 3; i++) {
            Player p = players[i];
            System.out.printf("%s - %s\n", p.getName(), score.get(p));
        }
    }

    public void scoreInRound(Round round, Player[] players) {
        Map<Player, Integer> score = round.getScore();
        System.out.println("\n****Счёт за раунд****");
        for (int i = 0; i < 3; i++) {
            System.out.printf("%s: %s\n",
                    players[i].getName(), score.get(players[i]));
        }
    }
    public void playerMove(Player p, Card c) {
        System.out.printf( "%s выложил карту %s%s\n",
                p.getName(), c.getSuit().getName(), c.getNominal().getName());
    }

    public void playerTrade(Player p, Bet b) {
        System.out.printf( "%s делает ставку что он возьмёт %s, если козырем будет %s\n",
                p.getName(), b.getCountTricks().getName(), b.getTrump().getName());
    }

    public void playerPassed(Player p) {
        System.out.printf("%s пасует\n",
                p.getName());

    }

    public void bet(Round r) {
        System.out.printf("\n%s заключил контракт: %s взяток и козырь: %s",
                r.getPlacedPlayer().getName(),
                r.getBet().getCountTricks().getName(), r.getBet().getTrump().getName());
    }

    public void playerCards(Round r, Player p) {
        System.out.printf("%s: ", p.getName());
        List<Card> playerCards = r.getCards().get(p);
        for (int i = 0; i < playerCards.size(); i++) {
            System.out.printf( "%s%s ",
                    playerCards.get(i).getSuit().getName(), playerCards.get(i).getNominal().getName());
        }
        System.out.println();
    }

    public void fightWinner(Player p) {
        System.out.printf(" - - " + p.getName() + " забирает");
    }

    public void completedContract(Player p) {
        System.out.printf("\n%s выполнил контракт", p.getName());
    }

    public void notCompletedContract(Player p) {
        System.out.printf("\n%s не выполнил контракт", p.getName());
    }

    public void countTricksInRound(Round r) {
        System.out.print("\n\nИтого: ");
        Map<Player, Integer> countTricks = r.getCountTricks();
        System.out.println();
        for (Player p: countTricks.keySet()) {
            System.out.printf("%s взял %d взяток\n", p.getName(), countTricks.get(p));
        }
    }

    public void gameWinner(Game g) {
        Map<Player, Integer> score = g.getScore();
        int max = 0;
        Player winner = null;
        for (Player p: score.keySet()) {
            if (score.get(p) > max) {
                max = score.get(p);
                winner = p;
            }
        }
        System.out.printf("\n\n\nПобедитель - %s\nКонец игры\n\n\n", winner.getName());
    }
}
