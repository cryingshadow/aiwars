package de.fhdw.aiwars.control.samples;

import java.lang.reflect.*;
import java.net.*;
import java.net.http.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.model.*;

public class ApiPlayer implements Player {
    public class TurnDto {
        int playerId;

        List<TurnElement> turnElementList;

        public TurnDto(final List<TurnElement> turnElementList, final int playerId) {
            this.turnElementList = turnElementList;
            this.playerId = playerId;
        }

        public int getPlayerId() {
            return this.playerId;
        }

        public List<TurnElement> getTurnElementList() {
            return this.turnElementList;
        }

        public void setPlayerId(final int playerId) {
            this.playerId = playerId;
        }

        public void setTurnElementList(final List<TurnElement> turnElementList) {
            this.turnElementList = turnElementList;
        }

    }

    public class TurnElement {
        int count;
        int player;
        int x;
        int y;
        public TurnElement(final int x, final int y, final int player, final int count){
            this.x = x;
            this.y = y;
            this.player = player;
            this.count = count;
        }

        public int getCount() {
            return this.count;
        }

        public int getPlayer() {
            return this.player;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public void setCount(final int count) {
            this.count = count;
        }

        public void setPlayer(final int player) {
            this.player = player;
        }

        public void setX(final int x) {
            this.x = x;
        }

        public void setY(final int y) {
            this.y = y;
        }
    }

    public List<TurnElement> getTurnElements(final GameMap gameMap) {
        final List<TurnElement> turnElementList = new ArrayList<>();

        for (final Map.Entry<Coordinates, GameField> entry : gameMap.entrySet()) {
            final Coordinates coordinates = entry.getKey();
            final GameField gameField = entry.getValue();

            final int x = coordinates.getX();
            final int y = coordinates.getY();
            final int player = gameField.getPlayer();
            final int count = gameField.getAmount();

            final TurnElement turnElement = new TurnElement(x, y, player, count);
            turnElementList.add(turnElement);
        }

        return turnElementList;
    }


    @Override
    public List<Move> turn(final GameMap map, final int playerId) {
        final String apiUrl = "http://localhost:8080/";

        final TurnDto requestbody = new TurnDto(this.getTurnElements(map), playerId);
        System.out.println(requestbody.toString());

        final HttpClient httpClient = HttpClient.newHttpClient();
        final Gson gson = new Gson();
        List<Move> moves = new ArrayList<Move>();

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestbody)))
                .build();

        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            final String responseBody = response.body();
            System.out.println("Response: " + responseBody);
            final Type listOfMyClassObject = new TypeToken<ArrayList<Move>>() {}.getType();
            moves = gson.fromJson(responseBody, listOfMyClassObject);

        } catch (final Exception e) {
            e.printStackTrace();
        }

        return moves;
    }
}
