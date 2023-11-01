package de.fhdw.aiwars.control;

import java.util.*;
import java.util.logging.*;
import java.util.stream.*;

import javax.swing.event.*;

import de.fhdw.aiwars.logic.*;
import de.fhdw.aiwars.model.*;

public class Game {

    public static final int NO_PLAYER = -1;

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private int currentPlayer;

    private final List<ChangeListener> listeners;

    private final GameMap map;

    private final int maxNumOfTurns;

    private final Player[] players;

    private int turn;

    public Game(final GameMap initialMap, final Player[] players, final int maxNumOfTurns) {
        this.map = initialMap;
        this.players = players;
        this.currentPlayer = 0;
        this.turn = 0;
        this.listeners = new ArrayList<ChangeListener>();
        this.maxNumOfTurns = maxNumOfTurns;
        final List<String> playerNames =
            Arrays
            .stream(this.players)
            .map(Player::getClass)
            .map(Class::getSimpleName)
            .collect(Collectors.toList());
        Game.LOG.log(
            Level.INFO,
            String.format("Game initialized with players %s and map %s!", playerNames, this.map)
        );
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public GameMap getMap() {
        return this.map;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Optional<List<Integer>> getWinners() {
        final Set<Integer> activePlayers = new ActivePlayers(this.map);
        if (activePlayers.size() == 1) {
            return Optional.of(Collections.singletonList(activePlayers.iterator().next()));
        }
        if (this.maxNumOfTurns > 0 && this.turn >= this.maxNumOfTurns) {
            final PointsByPlayer points = new PointsByPlayer(this.map);
            int maxPoints = 0;
            final List<Integer> winners = new ArrayList<Integer>();
            for (final Map.Entry<Integer, Integer> pointsEntry : points.entrySet()) {
                final int currentPoints = pointsEntry.getValue();
                if (currentPoints > maxPoints) {
                    winners.clear();
                    maxPoints = currentPoints;
                    winners.add(pointsEntry.getKey());
                } else if (currentPoints == maxPoints) {
                    winners.add(pointsEntry.getKey());
                }
            }
            return Optional.of(winners);
        }
        return Optional.empty();
    }

    public void listen(final ChangeListener listener) {
        this.listeners.add(listener);
    }

    public void turn() {
        Game.LOG.log(
            Level.INFO,
            String.format(
                "Turn %d... Player %d (%s) is moving...",
                this.turn,
                this.currentPlayer,
                this.players[this.currentPlayer].getClass().getSimpleName()
            )
        );
        final List<Move> moves = this.getMoves();
        Game.LOG.log(Level.INFO, String.format("Chosen moves of player %d: %s", this.currentPlayer, moves));
        new Turn(this.currentPlayer, moves).applyMoves(this.map);
        final Set<Integer> activePlayers = new ActivePlayers(this.map);
        this.advanceCurrentPlayer();
        while (!activePlayers.contains(this.currentPlayer)) {
            this.advanceCurrentPlayer();
        }
        this.turn++;
        Game.LOG.log(Level.FINE, String.format("Resulting map is %s.", this.map));
        this.notifyListeners();
    }

    private void advanceCurrentPlayer() {
        this.currentPlayer++;
        if (this.currentPlayer >= this.players.length) {
            this.currentPlayer = 0;
        }
    }

    private List<Move> getMoves() {
        return this.players[this.currentPlayer].turn(this.map.copy(), this.currentPlayer);
    }

    private void notifyListeners() {
        for (final ChangeListener listener : this.listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
