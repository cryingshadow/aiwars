package de.fhdw.aiwars.control;

import java.util.*;
import java.util.Optional;

import org.testng.*;
import org.testng.annotations.*;

import de.fhdw.aiwars.*;
import de.fhdw.aiwars.control.samples.*;
import de.fhdw.aiwars.model.*;

public class GameTest {

    @Test
    public void noWinner() {
        final Player[] players = TestData.getDefaultPlayers();
        final GameMap map = TestData.getDefaultMap();
        final Game game = new Game(map, players, 0);
        Assert.assertEquals(game.getWinners(), Optional.empty());
    }

    @Test
    public void noWinnerNoPlayer() {
        final Player[] players = TestData.getDefaultPlayers();
        final GameMap map = TestData.getDefaultMap();
        map.put(new Coordinates(0, 0), new GameField(Game.NO_PLAYER, 1));
        map.put(new Coordinates(1, 1), new GameField(Game.NO_PLAYER, 1));
        final Game game = new Game(map, players, 0);
        Assert.assertEquals(game.getWinners(), Optional.empty());
    }

    @Test
    public void player0Wins() {
        final Player[] players = TestData.getDefaultPlayers();
        final GameMap map = TestData.getDefaultMap();
        map.put(new Coordinates(1, 1), new GameField(0, 1));
        final Game game = new Game(map, players, 0);
        Assert.assertTrue(game.getWinners().isPresent());
        final List<Integer> winners = game.getWinners().get();
        Assert.assertEquals(winners.size(), 1);
        Assert.assertEquals(winners.get(0), Integer.valueOf(0));
    }

    @Test
    public void player1Wins() {
        final Player[] players = TestData.getDefaultPlayers();
        final GameMap map = TestData.getDefaultMap();
        map.put(new Coordinates(0, 0), new GameField(1, 1));
        final Game game = new Game(map, players, 0);
        Assert.assertTrue(game.getWinners().isPresent());
        final List<Integer> winners = game.getWinners().get();
        Assert.assertEquals(winners.size(), 1);
        Assert.assertEquals(winners.get(0), Integer.valueOf(1));
    }

    @Test
    public void turn() {
        final Player[] players =
            new Player[] {
                new JustGrow(),
                new Player() {

                    private int turn = 0;

                    @Override
                    public List<Move> turn(final GameMap map, final int playerId) {
                        switch (this.turn++) {
                        case 0:
                            return Collections.singletonList(new Move(new Coordinates(1, 1), Direction.SOUTHWEST, 5));
                        case 1:
                            final List<Move> res = new ArrayList<Move>();
                            res.add(new Move(new Coordinates(1, 0), Direction.WEST, 7));
                            res.add(new Move(new Coordinates(1, 1), Direction.SOUTHWEST, 1));
                            return res;
                        default:
                            return Collections.emptyList();
                        }
                    }

                }
            };
        final GameMap map = TestData.getDefaultMap();
        final Game game = new Game(map, players, 0);
        Assert.assertEquals(game.getCurrentPlayer(), 0);
        GameField field1 = map.get(new Coordinates(0, 0));
        GameField field2 = map.get(new Coordinates(1, 0));
        GameField field3 = map.get(new Coordinates(1, 1));
        Assert.assertEquals(field1.getPlayer(), 0);
        Assert.assertEquals(field1.getAmount(), 5);
        Assert.assertEquals(field2.getPlayer(), Game.NO_PLAYER);
        Assert.assertEquals(field2.getAmount(), 1);
        Assert.assertEquals(field3.getPlayer(), 1);
        Assert.assertEquals(field3.getAmount(), 5);
        game.turn();
        Assert.assertEquals(game.getCurrentPlayer(), 1);
        field1 = map.get(new Coordinates(0, 0));
        field2 = map.get(new Coordinates(1, 0));
        field3 = map.get(new Coordinates(1, 1));
        Assert.assertEquals(field1.getPlayer(), 0);
        Assert.assertEquals(field1.getAmount(), 7);
        Assert.assertEquals(field2.getPlayer(), Game.NO_PLAYER);
        Assert.assertEquals(field2.getAmount(), 1);
        Assert.assertEquals(field3.getPlayer(), 1);
        Assert.assertEquals(field3.getAmount(), 5);
        game.turn();
        Assert.assertEquals(game.getCurrentPlayer(), 0);
        field1 = map.get(new Coordinates(0, 0));
        field2 = map.get(new Coordinates(1, 0));
        field3 = map.get(new Coordinates(1, 1));
        Assert.assertEquals(field1.getPlayer(), 0);
        Assert.assertEquals(field1.getAmount(), 7);
        Assert.assertEquals(field2.getPlayer(), 1);
        Assert.assertEquals(field2.getAmount(), 7);
        Assert.assertEquals(field3.getPlayer(), 1);
        Assert.assertEquals(field3.getAmount(), 1);
        game.turn();
        Assert.assertEquals(game.getCurrentPlayer(), 1);
        field1 = map.get(new Coordinates(0, 0));
        field2 = map.get(new Coordinates(1, 0));
        field3 = map.get(new Coordinates(1, 1));
        Assert.assertEquals(field1.getPlayer(), 0);
        Assert.assertEquals(field1.getAmount(), 9);
        Assert.assertEquals(field2.getPlayer(), 1);
        Assert.assertEquals(field2.getAmount(), 7);
        Assert.assertEquals(field3.getPlayer(), 1);
        Assert.assertEquals(field3.getAmount(), 1);
        game.turn();
        Assert.assertEquals(game.getCurrentPlayer(), 0);
        field1 = map.get(new Coordinates(0, 0));
        field2 = map.get(new Coordinates(1, 0));
        field3 = map.get(new Coordinates(1, 1));
        Assert.assertEquals(field1.getPlayer(), 0);
        Assert.assertEquals(field1.getAmount(), 4);
        Assert.assertEquals(field2.getPlayer(), 1);
        Assert.assertEquals(field2.getAmount(), 2);
        Assert.assertEquals(field3.getPlayer(), 1);
        Assert.assertEquals(field3.getAmount(), 1);
    }

}
