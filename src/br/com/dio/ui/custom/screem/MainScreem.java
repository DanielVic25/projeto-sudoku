package br.com.dio.ui.custom.screem;

import br.com.dio.ai.duster.button.CheckGameStatusButton;
import br.com.dio.ai.duster.button.FinishGameButton;
import br.com.dio.ai.duster.button.ResetButton;
import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.frame.Mainframe;
import br.com.dio.ui.custom.input.Numbertext;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

public class MainScreem {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;


    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;



    public MainScreem(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreem() {
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new Mainframe(dimension, mainPanel);

        for (int r = 0; r < 9; r += 3) {
            int endRow = r + 3;


            for (int c = 0; c < 9; c += 3) {
                int endCol = c + 3;

                var spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                JPanel sector = genereteSection(spaces);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(final List<List<Space>> spaces,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r < endRow; r++) {
            for (int c = initCol; c < endCol; c++) {
                spaceSector.add(spaces.get(r).get(c));
            }

        }
        return spaceSector;
    }


    private SudokuSector genereteSection(final List<Space> spaces) {
        List<Numbertext> fields = new ArrayList<Numbertext>(spaces.stream().map(Numbertext::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }


    private void addFinishGameButton(JPanel mainPanel) {
          finishGameButton = new FinishGameButton(e ->{
            if (boardService.gameIsFinished()) {
                showConfirmDialog(null,
                        "Parabéns você concluio o Jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var menssagem = "Seu jogo tem alguma inconsistencia, ajuste e tente novamente";
                showConfirmDialog(null, menssagem);
            }
         });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e ->{
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOPLETE -> "O jogo está Incompleto ";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            showConfirmDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            var dialogResult = showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o Jogo? ",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }
}




