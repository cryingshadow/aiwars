package de.fhdw.aiwars.control.samples;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import javax.swing.text.Style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApiPlayer implements Player {
    public class TurnElement {
        public TurnElement(int x, int y, int player, int count){
            this.x = x;
            this.y = y;
            this.player = player;
            this.count = count;
        }
        int x;
        int y;
        int player;
        int count;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getPlayer() {
            return player;
        }

        public int getCount() {
            return count;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setPlayer(int player) {
            this.player = player;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public class TurnDto {
        public TurnDto(List<TurnElement> turnElementList, int playerId) {
            this.turnElementList = turnElementList;
            this.playerId = playerId;
        }

        List<TurnElement> turnElementList;

        public List<TurnElement> getTurnElementList() {
            return turnElementList;
        }

        public void setTurnElementList(List<TurnElement> turnElementList) {
            this.turnElementList = turnElementList;
        }

        int playerId;

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public int getPlayerId() {
            return playerId;
        }

    }

    public List<TurnElement> getTurnElements(GameMap gameMap) {
        List<TurnElement> turnElementList = new ArrayList<>();

        for (Map.Entry<Coordinates, GameField> entry : gameMap.entrySet()) {
            Coordinates coordinates = entry.getKey();
            GameField gameField = entry.getValue();

            int x = coordinates.getX();
            int y = coordinates.getY();
            int player = gameField.getPlayer();
            int count = gameField.getAmount();

            TurnElement turnElement = new TurnElement(x, y, player, count);
            turnElementList.add(turnElement);
        }

        return turnElementList;
    }


    @Override
    public List<Move> turn(final GameMap map, final int playerId) {
        String apiUrl = "http://localhost:8080/";

        TurnDto requestbody = new TurnDto(getTurnElements(map), playerId);
        System.out.println(requestbody.toString());
        
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        List<Move> moves = new ArrayList<Move>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestbody)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Response: " + responseBody);
            Type listOfMyClassObject = new TypeToken<ArrayList<Move>>() {}.getType();
            moves = gson.fromJson(responseBody, listOfMyClassObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return moves;
    }
}
