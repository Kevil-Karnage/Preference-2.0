package main.java.Preference.services;

import main.java.Preference.models.Game;
import main.java.Preference.models.Player;
import main.java.Preference.models.Round;

import java.util.HashMap;
import java.util.Map;

public class GameService {
    private RoundService rs;
    private PrintService ps;

    public GameService(RoundService rs, PrintService ps) {
        this.rs = rs;
        this.ps = ps;
    }

    /**
     * добавляем игроков
     * @param g
     */
    public void addPlayersInGame(Game g) {
        String[] playersNames = {"Коля", "Игорь", "Саша"};

        Player[] players = new Player[3];
        Map<Player, Integer> score = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            players[i] = new Player(playersNames[i]);
            ps.addPlayerInGame(playersNames[i]);
            score.put(players[i], 0);
        }
        System.out.println();
        g.setPlayers(players);
        g.setScore(score);
        ps.allPlayersReady();
    }

    /**
     * добавляем n раундов
     * @param g
     * @param countRounds
     */
    public void addRoundsInGame(Game g, int countRounds) {
        Round[] rounds = new Round[countRounds];
        for (int i = 0; i < countRounds; i++) {
            rounds[i] = new Round(g.getPlayers());
        }
        g.setRounds(rounds);
        ps.createNRounds(countRounds);
    }

    /**
     * начинаем раунд
     * @param g
     * @param roundNumber
     */
    public Player[] playRound(Game g, Player[] basedPlayersQueue, int roundNumber) {
        Round r = g.getRound(roundNumber);

        rs.addDeckInRound(r);               //Добавляем колоду в раунд
        rs.mixDeck(r);                      //перемешиваем колоду
        rs.distributionCards(r, basedPlayersQueue);      //раздаём карты
        rs.trade(g, r);                  //создаем торги за прикуп
        Player[] playerQueue = rs.contract(g, r);                     //создаём контракт

        for (int i = 0; i < 10; i++) {
            ps.startNFight(i + 1);

            playerQueue = rs.playFight(
                    r, i, playerQueue);
            ps.fightWinner(playerQueue[0]);
            r.addNTricksToPlayer(1, playerQueue[0]);
        }

        rs.scoreInRound(r);
        addRoundScoreToGameScore(g, r);          //выводим счёт за раунд и добавляем его в общий счёт
        Player lastPlayer = basedPlayersQueue[0];
        for (int i = 0; i < basedPlayersQueue.length - 1; i++) {
            basedPlayersQueue[i] = basedPlayersQueue[i + 1];
        }
        basedPlayersQueue[basedPlayersQueue.length - 1] = lastPlayer;
        return basedPlayersQueue;
    }

    /**
     * добавляем счёт за 1 раунд в счёт за игру
     * @param g
     * @param round
     */
    private void addRoundScoreToGameScore(Game g, Round round) {
        Player[] players = g.getPlayers();
        Map<Player, Integer> score = g.getScore();
        Map<Player, Integer> scoreInRound = round.getScore();
        for (int i = 0; i < players.length; i++) {
            int playerScore = score.get(players[i]) + scoreInRound.get(players[i]);
            score.put(players[i], playerScore);
        }

        ps.scoreInRound(round, g.getPlayers());      //выводим счёт за раунд
        g.setScore(score);                           //обновляем общий счёт
    }
}
