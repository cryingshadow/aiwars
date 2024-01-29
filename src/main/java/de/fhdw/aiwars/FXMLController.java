package de.fhdw.aiwars;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.*;

import static javafx.stage.Screen.getPrimary;

public class FXMLController implements Initializable {

    @FXML
    private VBox content = new VBox();

    private HBox buttonWrapper = new HBox(10);
    private Rectangle coolRectangle = new Rectangle();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Screen screen = getPrimary();

        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        Button button3 = new Button("Button 3");

        buttonWrapper.getChildren().addAll(button1, button2, button3);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setPadding(new Insets(10, 0, 10, 0));

        coolRectangle.setWidth(screen.getVisualBounds().getWidth());
        coolRectangle.setHeight(screen.getVisualBounds().getHeight() - 100);
        coolRectangle.setFill(Color.AQUA);

        content.getChildren().addAll(coolRectangle, buttonWrapper);
        content.setAlignment(Pos.CENTER);
    }
}
