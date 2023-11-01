package de.fhdw.aiwars.view;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import de.fhdw.aiwars.control.*;

public class ControlPanel extends JPanel {

    private static final long serialVersionUID = -4121948403090267105L;

    public ControlPanel(final GameControl control) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final JButton turnButton =
            ControlPanel.addButton(
                "Next Turn",
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent event) {
                        control.turn();
                    }

                }
            );
        final JButton runButton =
            ControlPanel.addButton(
                "Run",
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent event) {
                        control.continueRunning();
                    }

                }
            );
        final JButton stopButton =
            ControlPanel.addButton(
                "Stop",
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent event) {
                        control.stopRunning();
                    }

                }
            );
        runButton.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    turnButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    runButton.setEnabled(false);
                }

            }
        );
        stopButton.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    turnButton.setEnabled(true);
                    runButton.setEnabled(true);
                    stopButton.setEnabled(false);
                }

            }
        );
        stopButton.setEnabled(false);
        control.addListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    final Game game = (Game)e.getSource();
                    if (game.getWinners().isPresent()) {
                        runButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        turnButton.setEnabled(false);
                    }
                }

            }
        );
        this.add(runButton);
        this.add(stopButton);
        this.add(turnButton);
    }

    private static JButton addButton(final String title, final ActionListener listener) {
        final JButton button = new JButton(title);
        button.addActionListener(listener);
        return button;
    }

}
