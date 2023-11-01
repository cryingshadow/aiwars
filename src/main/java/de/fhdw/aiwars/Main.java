package de.fhdw.aiwars;

import java.io.*;
import java.util.logging.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.control.samples.*;
import de.fhdw.aiwars.generators.*;
import de.fhdw.aiwars.model.*;
import de.fhdw.aiwars.view.*;

/**
 * @author Thomas Stroeder
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(final String[] args) {
        final Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel( Level.INFO );
        rootLogger.getHandlers()[0].setLevel( Level.INFO );
        try {
            final FileHandler fileHandler = new FileHandler("aiwars.log");
            fileHandler.setLevel(Level.INFO);
            rootLogger.addHandler(fileHandler);
        } catch (SecurityException | IOException e) {
            Main.LOG.log(Level.SEVERE, "Could not add file handler!", e);
        }
        final Player[] players = Main.initializePlayers();
        final int width = 20;
        final int height = 20;
        final GameMap initialMap = Main.initializeMap(width, height, players);
        final Game game = new Game(initialMap, players, 1000);
        final MainFrame frame = new MainFrame(game);
        frame.pack();
        frame.setVisible(true);
    }

    private static GameMap initializeMap(
        final int width,
        final int height,
        final Player[] players
    ) {
        return new MapGenerator(width, height, players.length).generateMap();
//        final GameMap res = new GameMap(width, height);
//        res.put(new Coordinates(0, 0), new GameField(0, 5));
//        res.put(new Coordinates(1, 0), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(2, 0), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(3, 0), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(0, 1), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(1, 1), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(2, 1), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(3, 1), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(0, 2), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(1, 2), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(2, 2), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(3, 2), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(0, 3), new GameField(2, 5));
//        res.put(new Coordinates(1, 3), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(2, 3), new GameField(Game.NO_PLAYER, 1));
//        res.put(new Coordinates(3, 3), new GameField(1, 5));
//        return res;
    }

    private static Player[] initializePlayers() {
        // TODO Auto-generated method stub
        return new Player[]{new SimpleStrategy(), new SingleAttack(), new JustGrow(), new SimpleStrategy()};
    }

}
