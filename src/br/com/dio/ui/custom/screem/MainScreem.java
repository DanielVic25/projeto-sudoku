package br.com.dio.ui.custom.screem;

import br.com.dio.ai.duster.button.FinishGameButton;
import br.com.dio.ai.duster.button.ResetButton;
import br.com.dio.service.BoardService;
import br.com.dio.ui.custom.frame.Mainframe;
import br.com.dio.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreem {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;


    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;



    public MainScreem(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }

    public void buildMainScreem(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new Mainframe(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3) {
            var andRow = r + 2;
        }

        for (int c = 0; c < 9; c+=3) {
            var andRow = c + 2;
        }

        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGameButton(JPanel mainPanel) {
          finishGameButton = new FinishGameButton(e ->{
            if (boardService.gameIsFinished()) {
                JOptionPane.showConfirmDialog(null,
                        "Parabéns você concluio o Jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var menssagem = "Seu jogo tem alguma inconsistencia, ajuste e tente novamente";
                JOptionPane.showConfirmDialog(null, menssagem);
            }
         });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new FinishGameButton(e ->{
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOPLETE -> "O jogo está Incompleto ";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? "e contém erros" : "e não contém erros";
            JOptionPane.showConfirmDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o Jogo? ",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                boardService.reset();
            }
        });
        mainPanel.add(resetButton);
    }
}




