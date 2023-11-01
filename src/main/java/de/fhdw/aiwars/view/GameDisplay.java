package de.fhdw.aiwars.view;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.logic.*;
import de.fhdw.aiwars.model.*;

public class GameDisplay extends JPanel {

    private static final long serialVersionUID = 6431282501971750644L;

    private static Polygon createFieldPolygon(final int x, final int y, final int size) {
        final int[] xpoints =
            new int[] {
                x + (size / 2),
                x + size,
                x + size,
                x + (size / 2),
                x,
                x
            };
        final int[] ypoints =
            new int[] {
                y,
                y + (size / 3),
                y + 2 * (size / 3),
                y + size,
                y + 2 * (size / 3),
                y + (size / 3)
            };
        return new Polygon(xpoints, ypoints, 6);
    }

    private final Game game;

    private final int size;

    public GameDisplay(final Game game, final int size) {
        this.game = game;
        this.game.listen(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent event) {
                    GameDisplay.this.invalidate();
                    GameDisplay.this.repaint();
                }

            }
        );
        this.size = size;
    }

    @Override
    public int getHeight() {
        final int thirdSize = this.size / 3;
        return this.game.getMap().getHeight() * 2 * thirdSize + thirdSize;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    @Override
    public int getWidth() {
        return this.game.getMap().getWidth() * this.size + (this.size / 2);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Color originalColor = g.getColor();
        final Font originalFont = g.getFont();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (final Map.Entry<Coordinates, GameField> entry : this.game.getMap().entrySet()) {
            final Coordinates coordinates = entry.getKey();
            this.paintField(
                g,
                coordinates.getX() * this.size + (IsEven.isEven(coordinates.getY()) ? 0 : this.size / 2),
                this.getHeight() - coordinates.getY() * 2 * (this.size / 3) - this.size,
                entry.getValue()
            );
        }
        g.setFont(originalFont);
        g.setColor(originalColor);
    }

    private void paintField(final Graphics g, final int x, final int y, final GameField field) {
        final int player = field.getPlayer();
        if (player == Game.NO_PLAYER) {
            g.setColor(MainFrame.NO_PLAYER_COLOR);
        } else {
            g.setColor(MainFrame.PLAYERS_COLORS[player]);
        }
        final Polygon hexagon = GameDisplay.createFieldPolygon(x, y, this.size);
        g.fillPolygon(hexagon);
        g.setColor(Color.GRAY);
        g.drawPolygon(hexagon);
        g.setColor(Color.WHITE);
        final Font font = new Font(Font.DIALOG, Font.BOLD, this.size / 3);
        final String text = Integer.toString(field.getAmount());
        final FontMetrics metrics = g.getFontMetrics(font);
        final int textX = x + (this.size - metrics.stringWidth(text)) / 2;
        final int textY = y + ((this.size - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, textX, textY);
    }

}
