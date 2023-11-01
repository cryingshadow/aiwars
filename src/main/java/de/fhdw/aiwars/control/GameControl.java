package de.fhdw.aiwars.control;

import java.util.*;
import java.util.logging.*;
import java.util.stream.*;

import javax.swing.event.*;

public class GameControl {

    private static final Object LOCK = new Object();

    private static final Logger LOG = Logger.getLogger(GameControl.class.getName());

    private final Game game;

    private final List<ChangeListener> listeners;

    private boolean pause;

    private final Thread thread;

    public GameControl(final Game game) {
        super();
        this.game = game;
        this.pause = true;
        this.listeners = new ArrayList<ChangeListener>();
        this.thread =
            new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        synchronized (GameControl.LOCK) {
                            while (!GameControl.this.game.getWinners().isPresent()) {
                                if (GameControl.this.pause) {
                                    try {
                                        GameControl.LOCK.wait();
                                    } catch (final InterruptedException e) {
                                        GameControl.LOG.log(Level.SEVERE, e.getMessage(), e);
                                    }
                                }
                                try {
                                    GameControl.LOCK.wait(100);
                                } catch (final InterruptedException e) {
                                    GameControl.LOG.log(Level.SEVERE, e.getMessage(), e);
                                }
                                GameControl.this.game.turn();
                            }
                        }
                        GameControl.this.logWinners();
                    }

                }
            );
    }

    public void addListener(final ChangeListener listener) {
        this.listeners.add(listener);
    }

    public void continueRunning() {
        this.pause = false;
        synchronized (GameControl.LOCK) {
            if (this.thread.isAlive()) {
                GameControl.LOCK.notify();
            } else {
                this.thread.start();
            }
        }
    }

    public void logWinners() {
        this.notifyListeners(new ChangeEvent(this.game));
        final List<Integer> winners = GameControl.this.game.getWinners().get();
        if (winners.size() == 1) {
            GameControl.LOG.log(
                Level.INFO,
                String.format(
                    "Player %d (%s) has won!",
                    winners.get(0),
                    GameControl.this.game.getPlayers()[winners.get(0)].getClass().getSimpleName()
                )
            );
        } else {
            GameControl.LOG.log(
                Level.INFO,
                String.format(
                    "Players %s (%s) have won!",
                    winners,
                    winners
                    .stream()
                    .map(i -> GameControl.this.game.getPlayers()[i].getClass().getSimpleName())
                    .collect(Collectors.toList())
                )
            );
        }

    }

    public void stopRunning() {
        this.pause = true;
    }

    public void turn() {
        this.game.turn();
        if (this.game.getWinners().isPresent()) {
            this.logWinners();
        }
    }

    private void notifyListeners(final ChangeEvent e) {
        for (final ChangeListener listener : this.listeners) {
            listener.stateChanged(e);
        }
    }

}
