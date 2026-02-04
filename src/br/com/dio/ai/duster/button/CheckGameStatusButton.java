package br.com.dio.ai.duster.button;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class CheckGameStatusButton extends JButton {

    public CheckGameStatusButton(final ActionListener actionListener) {
        this.setText("verificar Jogo");
        this.addActionListener(actionListener);
    }
}
