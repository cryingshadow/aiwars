package de.fhdw.aiwars.generators;

import java.util.*;
import java.util.function.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.logic.*;
import de.fhdw.aiwars.model.*;

public class MapGenerator {

    private static class BorderFunctions {

        private final Predicate<Coordinates> check;

        private final Supplier<Coordinates> generate;

        public BorderFunctions(final Predicate<Coordinates> check, final Supplier<Coordinates> generate) {
            this.check = check;
            this.generate = generate;
        }

    }

    private static final int INITIAL_ARMIES_FOR_PLAYERS = 5;

    private static final int MAX_CONNECTION_CHUNK_SIZE = 10;

    private static int gaussToInitialArmies(final double value) {
        if (value > 0.0) {
            if (value <= 2.0) {
                return 2;
            } else {
                return 3;
            }
        } else if (value >= -2.0) {
            return 1;
        } else {
            return 0;
        }
    }

    private final int height;

    private final int numOfPlayers;

    private final Random random;

    private final int width;

    public MapGenerator(final int width, final int height, final int numOfPlayers) {
        if (width * height < numOfPlayers * 8) {
            throw new IllegalArgumentException("Size is too small for the number of players!");
        }
        this.width = width;
        this.height = height;
        this.numOfPlayers = numOfPlayers;
        this.random = new Random();
    }

    public GameMap generateMap() {
        final GameMap map = new GameMap(this.width, this.height);
        final Coordinates[] startPositions = this.generateStartPositions();
        this.generateChunksAroundStartPositions(map, startPositions);
        this.connectChunks(map, startPositions);
        this.reachBorders(map);
        return map;
    }

    private void connectChunks(final GameMap map, final Coordinates[] startPositions) {
        for (int i = 0; i < this.numOfPlayers; i++) {
            for (int j = i + 1; j < this.numOfPlayers; j++) {
                this.connectCoordinates(map, startPositions[i], startPositions[j]);
            }
        }
    }

    private void connectCoordinates(final GameMap map, final Coordinates start, final Coordinates end) {
        final ShortestPath shortestPath = new ShortestPath(map, start, end);
        if (!shortestPath.isEmpty()) {
            return;
        }
        final DirectPath directPath = new DirectPath(start, end);
        for (final Coordinates coordinates : directPath) {
            if (!map.containsKey(coordinates)) {
                this.generateConnectionChunk(map, coordinates);
            }
        }
    }

    private List<Coordinates> findBestPositions(final int[][] distancesToAll) {
        final List<Coordinates> res = new ArrayList<Coordinates>();
        int maxMinDistance = 0;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (distancesToAll[i][j] == maxMinDistance) {
                    res.add(new Coordinates(i, j));
                } else if (distancesToAll[i][j] > maxMinDistance) {
                    res.clear();
                    maxMinDistance = distancesToAll[i][j];
                    res.add(new Coordinates(i, j));
                }
            }
        }
        return res;
    }

    private List<Coordinates> findFrontier(
        final Coordinates coordinates,
        final Set<Coordinates> in,
        final List<Coordinates> currentFrontier
    ) {
        final List<Coordinates> res = new ArrayList<Coordinates>();
        final Set<Coordinates> visited = new LinkedHashSet<Coordinates>();
        final Queue<Coordinates> queue = new ArrayDeque<Coordinates>();
        queue.offer(coordinates);
        while (!queue.isEmpty()) {
            final Coordinates next = queue.poll();
            if (visited.contains(next) || !this.inMap(next)) {
                continue;
            }
            visited.add(next);
            if (in.contains(next)) {
                queue.addAll(new SurroundingCoordinates(next));
            } else if (!currentFrontier.contains(next)) {
                res.add(next);
            }
        }
        return res;
    }

    private List<Integer> findWorstPositions(final int[][] distances, final int minimumDistance) {
        final List<Integer> res = new ArrayList<Integer>();
        int maxNumOfMinHits = 0;
        for (int i = 0; i < this.numOfPlayers; i++) {
            int currentNumOfMinHits = 0;
            for (int j = 0; j < this.numOfPlayers + 4; j++) {
                if (distances[i][j] == minimumDistance) {
                    currentNumOfMinHits++;
                }
            }
            if (currentNumOfMinHits == maxNumOfMinHits) {
                res.add(i);
            } else if (currentNumOfMinHits > maxNumOfMinHits) {
                res.clear();
                res.add(i);
                maxNumOfMinHits = currentNumOfMinHits;
            }
        }
        return res;
    }

    private void generateChunk(
        final GameMap map,
        final Coordinates start,
        final Set<Coordinates> alreadyIn,
        final int chunkSize
    ) {
        final List<Coordinates> frontier = this.findFrontier(start, alreadyIn, Collections.emptyList());
        for (int j = 1; j < chunkSize; j++) {
            if (frontier.isEmpty()) {
                return;
            }
            final Coordinates next = frontier.remove(this.random.nextInt(frontier.size()));
            alreadyIn.add(next);
            map.put(next, this.generateNonPlayerField());
            frontier.addAll(this.findFrontier(next, alreadyIn, frontier));
        }
    }

    private void generateChunksAroundStartPositions(final GameMap map, final Coordinates[] startPositions) {
        final Set<Coordinates> in = new LinkedHashSet<Coordinates>();
        in.addAll(Arrays.asList(startPositions));
        final int minChunkSize = (this.width * this.height) / (this.numOfPlayers * 16);
        System.out.println("MinChunk size: " + minChunkSize);
        final int chunkSize = minChunkSize + this.random.nextInt(12 * minChunkSize + 1);
        System.out.println("Chunk size: " + chunkSize);
        for (int i = 0; i < this.numOfPlayers; i++) {
            final Coordinates start = startPositions[i];
            map.put(start, new GameField(i, MapGenerator.INITIAL_ARMIES_FOR_PLAYERS));
            this.generateChunk(map, start, in, chunkSize);
        }
    }

    private void generateConnectionChunk(final GameMap map, final Coordinates coordinates) {
        final int numOfFields = this.random.nextInt(MapGenerator.MAX_CONNECTION_CHUNK_SIZE) + 1;
        map.put(coordinates, this.generateNonPlayerField());
        final Set<Coordinates> alreadyIn = new LinkedHashSet<Coordinates>(map.keySet());
        this.generateChunk(map, coordinates, alreadyIn, numOfFields);
    }

    private GameField generateNonPlayerField() {
        return new GameField(Game.NO_PLAYER, MapGenerator.gaussToInitialArmies(this.random.nextGaussian()));
    }

    private Coordinates[] generateStartPositions() {
        final Coordinates[] res = new Coordinates[this.numOfPlayers];
        for (int i = 0; i < this.numOfPlayers; i++) {
            res[i] = new Coordinates(this.random.nextInt(this.width), this.random.nextInt(this.height));
        }
        final int[][] distances = new int[this.numOfPlayers + 4][this.numOfPlayers + 4];
        this.updateDistances(distances, res);
        final int[][] distancesToAll = new int[this.width][this.height];
        this.updateAllDistancesWithoutWorstPosition(res, -1, distancesToAll);
        int maximumMinimumDistanceToAll = this.maximumDistance(distancesToAll);
        int minimumDistance = this.minimumDistance(distances);
        while (minimumDistance < maximumMinimumDistanceToAll) {
            final List<Integer> worstPositionIndices = this.findWorstPositions(distances, minimumDistance);
            final int worstPositionIndex = worstPositionIndices.get(this.random.nextInt(worstPositionIndices.size()));
            this.updateAllDistancesWithoutWorstPosition(res, worstPositionIndex, distancesToAll);
            final List<Coordinates> bestPositions = this.findBestPositions(distancesToAll);
            res[worstPositionIndex] = bestPositions.get(this.random.nextInt(bestPositions.size()));
            this.updateDistances(distances, res);
            minimumDistance = this.minimumDistance(distances);
            maximumMinimumDistanceToAll = this.maximumDistance(distancesToAll);
        }
        return res;
    }

    private Coordinates getBorder(final Coordinates coordinates, final int i) {
        switch (i) {
        case 0:
            return new Coordinates(coordinates.getX(), -1);
        case 1:
            return new Coordinates(coordinates.getX(), this.height);
        case 2:
            return new Coordinates(-1, coordinates.getY());
        case 3:
            return new Coordinates(this.width, coordinates.getY());
        default:
            throw new IllegalArgumentException("Only values between 0 and 3 are allowed as second argument!");
        }
    }

    private boolean inMap(final Coordinates coordinates) {
        return
            coordinates.getX() >= 0
            && coordinates.getX() < this.width
            && coordinates.getY() >= 0
            && coordinates.getY() < this.height;
    }

    private int maximumDistance(final int[][] distances) {
        return
            Arrays
            .stream(distances)
            .mapToInt(a -> Arrays.stream(a).max().getAsInt())
            .max()
            .getAsInt();
    }

    private int minimumDistance(final int[][] distances) {
        return
            Arrays
            .stream(distances)
            .mapToInt(a -> Arrays.stream(a).filter(i -> i > 0).min().getAsInt())
            .min()
            .getAsInt();
    }

    private void reachBorders(final GameMap map) {
        final List<BorderFunctions> borders = new ArrayList<BorderFunctions>();
        borders.add(
            new BorderFunctions(
                c -> c.getX() == 0,
                () -> new Coordinates(0, this.random.nextInt(this.width))
            )
        );
        borders.add(
            new BorderFunctions(
                c -> c.getY() == 0,
                () -> new Coordinates(this.random.nextInt(this.height), 0)
            )
        );
        borders.add(
            new BorderFunctions(
                c -> c.getX() == this.width - 1,
                () -> new Coordinates(this.width - 1, this.random.nextInt(this.width))
            )
        );
        borders.add(
            new BorderFunctions(
                c -> c.getY() == this.height - 1,
                () -> new Coordinates(this.random.nextInt(this.height), this.height - 1)
            )
        );
        for (final BorderFunctions borderFunctions : borders) {
            if (!map.keySet().stream().filter(borderFunctions.check).findAny().isPresent()) {
                final Coordinates border = borderFunctions.generate.get();
                map.put(border, this.generateNonPlayerField());
                this.connectCoordinates(
                    map,
                    new ArrayList<Coordinates>(map.keySet()).get(this.random.nextInt(map.size())),
                    border
                );
            }
        }
    }

    private void updateAllDistancesWithoutWorstPosition(
        final Coordinates[] res,
        final int index,
        final int[][] distancesToAll
    ) {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                int minDistance = -1;
                final Coordinates current = new Coordinates(i, j);
                for (int k = 0; k < this.numOfPlayers; k++) {
                    if (k == index) {
                        continue;
                    }
                    final int distance = current.distance(res[k]);
                    if (minDistance < 0 || minDistance > distance) {
                        minDistance = distance;
                    }
                }
                for (int k = 0; k < 4; k++) {
                    final Coordinates border = this.getBorder(current, k);
                    final int distance = current.distance(border);
                    if (minDistance < 0 || minDistance > distance) {
                        minDistance = distance;
                    }
                }
                distancesToAll[i][j] = minDistance;
            }
        }
    }

    private void updateDistances(final int[][] distances, final Coordinates[] res) {
        for (int i = 0; i < this.numOfPlayers; i++) {
            for (int j = i + 1; j < this.numOfPlayers; j++) {
                final int distance = res[i].distance(res[j]);
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
            for (int j = this.numOfPlayers; j < this.numOfPlayers + 4; j++) {
                final int distance = res[i].distance(this.getBorder(res[i], j - this.numOfPlayers));
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }
    }

}
