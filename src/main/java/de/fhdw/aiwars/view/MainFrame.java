package de.fhdw.aiwars.view;

import java.awt.*;

import javax.swing.*;

import de.fhdw.aiwars.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFrame extends JFrame {

    public static final Color NO_PLAYER_COLOR = new Color(50, 50, 50);

    public static final Color[] PLAYERS_COLORS = new Color[] {
        new Color(255, 0, 0),
        new Color(0, 0, 255),
        new Color(0, 230, 0),
        new Color(255, 155, 0),
        new Color(255, 0, 255),
        new Color(0, 255, 255)
    };

    private static final long serialVersionUID = -8973960042014040243L;

    /*private static final int WIDTH = 750;
    private static final int HEIGHT = 500;
    private static Stage stage;
    private static Scene startScene;
    private static Scene displayScene;
    private static GameDisplay board;
    private static ControlPanel buttonMenu;

    public void Interface () {
    }*/

    public MainFrame(final Game game) {
        /*Stage stage = new Stage();
        stage.setTitle("aiwars");
        stage.setResizable(true);
        stage.setHeight(750);
        stage.setWidth(1200);

        board = new GameDisplay(game, 50);
        buttonMenu = new ControlPanel(new GameControl(game));*/

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final Container content = this.getContentPane();
        content.setLayout(new FlowLayout());
        content.add(new JScrollPane(new GameDisplay(game, 50)));
        content.add(new ControlPanel(new GameControl(game)));
    }

}
/*
-
 */