package de.fhdw.aiwars.model;

import java.util.Optional;

import org.testng.*;
import org.testng.annotations.*;

import de.fhdw.aiwars.*;

public class DirectionTest {

    @Test
    public void getCoordinates() {
        final GameMap map = TestData.getDefaultMap();
        Assert.assertEquals(
            Direction.EAST.getCoordinatesInMap(new Coordinates(0, 0), map),
            Optional.of(new Coordinates(1, 0))
        );
        Assert.assertEquals(
            Direction.NORTHWEST.getCoordinatesInMap(new Coordinates(0, 0), map),
            Optional.empty()
        );
        Assert.assertEquals(
            Direction.WEST.getCoordinatesInMap(new Coordinates(1, 0), map),
            Optional.of(new Coordinates(0, 0))
        );
        Assert.assertEquals(
            Direction.NORTHEAST.getCoordinatesInMap(new Coordinates(0, 0), map),
            Optional.empty()
        );
        Assert.assertEquals(
            Direction.NORTHEAST.getCoordinatesInMap(new Coordinates(1, 0), map),
            Optional.of(new Coordinates(1, 1))
        );
        Assert.assertEquals(
            Direction.SOUTHEAST.getCoordinatesInMap(new Coordinates(1, 1), map),
            Optional.empty()
        );
        Assert.assertEquals(
            Direction.SOUTHWEST.getCoordinatesInMap(new Coordinates(1, 1), map),
            Optional.of(new Coordinates(1, 0))
        );
        Assert.assertEquals(
            Direction.SOUTHEAST.getCoordinatesInMap(new Coordinates(0, 1), map),
            Optional.of(new Coordinates(1, 0))
        );
        Assert.assertEquals(
            Direction.SOUTHWEST.getCoordinatesInMap(new Coordinates(0, 1), map),
            Optional.of(new Coordinates(0, 0))
        );
    }

    @Test
    public void getField() {
        final GameMap map = TestData.getDefaultMap();
        final GameField field1 = map.get(new Coordinates(0, 0));
        final GameField field2 = map.get(new Coordinates(1, 0));
        final GameField field3 = map.get(new Coordinates(1, 1));
        Assert.assertEquals(
            Direction.EAST.getField(new Coordinates(0, 0), map),
            Optional.of(field2)
        );
        Assert.assertEquals(
            Direction.NORTHWEST.getField(new Coordinates(0, 0), map),
            Optional.empty()
        );
        Assert.assertEquals(
            Direction.WEST.getField(new Coordinates(1, 0), map),
            Optional.of(field1)
        );
        Assert.assertEquals(
            Direction.NORTHEAST.getField(new Coordinates(0, 0), map),
            Optional.empty()
        );
        Assert.assertEquals(
            Direction.NORTHEAST.getField(new Coordinates(1, 0), map),
            Optional.of(field3)
        );
        Assert.assertEquals(
            Direction.SOUTHEAST.getField(new Coordinates(1, 1), map),
            Optional.empty()
        );
        Assert.assertEquals(
            Direction.SOUTHWEST.getField(new Coordinates(1, 1), map),
            Optional.of(field2)
        );
        Assert.assertEquals(
            Direction.SOUTHEAST.getField(new Coordinates(0, 1), map),
            Optional.of(field2)
        );
        Assert.assertEquals(
            Direction.SOUTHWEST.getField(new Coordinates(0, 1), map),
            Optional.of(field1)
        );
    }

    @Test
    public void inverse() {
        Assert.assertEquals(Direction.inverse(Direction.EAST), Direction.WEST);
        Assert.assertEquals(Direction.inverse(Direction.NORTHEAST), Direction.SOUTHWEST);
        Assert.assertEquals(Direction.inverse(Direction.NORTHWEST), Direction.SOUTHEAST);
        Assert.assertEquals(Direction.inverse(Direction.SOUTHEAST), Direction.NORTHWEST);
        Assert.assertEquals(Direction.inverse(Direction.SOUTHWEST), Direction.NORTHEAST);
        Assert.assertEquals(Direction.inverse(Direction.WEST), Direction.EAST);
    }

    @Test
    public void nextClockwise() {
        Assert.assertEquals(Direction.nextClockwise(Direction.EAST), Direction.SOUTHEAST);
        Assert.assertEquals(Direction.nextClockwise(Direction.NORTHEAST), Direction.EAST);
        Assert.assertEquals(Direction.nextClockwise(Direction.NORTHWEST), Direction.NORTHEAST);
        Assert.assertEquals(Direction.nextClockwise(Direction.SOUTHEAST), Direction.SOUTHWEST);
        Assert.assertEquals(Direction.nextClockwise(Direction.SOUTHWEST), Direction.WEST);
        Assert.assertEquals(Direction.nextClockwise(Direction.WEST), Direction.NORTHWEST);
    }

    @Test
    public void previousClockwise() {
        Assert.assertEquals(Direction.previousClockwise(Direction.EAST), Direction.NORTHEAST);
        Assert.assertEquals(Direction.previousClockwise(Direction.NORTHEAST), Direction.NORTHWEST);
        Assert.assertEquals(Direction.previousClockwise(Direction.NORTHWEST), Direction.WEST);
        Assert.assertEquals(Direction.previousClockwise(Direction.SOUTHEAST), Direction.EAST);
        Assert.assertEquals(Direction.previousClockwise(Direction.SOUTHWEST), Direction.SOUTHEAST);
        Assert.assertEquals(Direction.previousClockwise(Direction.WEST), Direction.SOUTHWEST);
    }

}
