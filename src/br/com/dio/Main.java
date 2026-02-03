package br.com.dio;


import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        final var position = Stream.of(args)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        var option = -1;
        while (true) {
            System.out.println("Slecione uma das oções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(position);
                case 2 -> inputNumber();
                case 3 -> removerNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 ->finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inváçoda, selecione uma das opções do menu");
            }
        }

    }

    private static void startGame(Map<String, String> position) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
        return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < 9; j++) {
                var positionConfig = position.get("%s,%s".formatted(i, j));
                if (isNull(positionConfig)) {
                    spaces.get(i).add(new Space(0, false));
                    continue;
                }

                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("O Jogo está pronto para começar");
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido ");
        var col = runUnitGetValidNumber(0, 8);
        System.out.println("Informe a coluna que em que o número será inserido ");
        var row = runUnitGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição[%s,%s]\n", col, row);
        var value = runUnitGetValidNumber(1, 9);
        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s,%s] te um valor fixo\n", col, row);
        }
    }

    private static void removerNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido ");
        var col = runUnitGetValidNumber(0, 8);
        System.out.println("Informe a coluna que em que o número será inserido ");
        var row = runUnitGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição[%s,%s]\n", col, row);
        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s,%s] te um valor fixo\n", col, row);
        }
    }


    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }



        var args = new Object[90];
        var argPos = 0;
        for (int i = 0; i < 9; i++) {
            // 3. Loop das colunas (0 a 9)
            for (int j = 0; j < 10; j++) {
                var space = board.getSpaces().get(i).get(j);
                args[argPos++] = (isNull(space.getActual())) ? " " : space.getActual();
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("O jogo atualmente se ecndontra no status %s\n", board.getStatus().getLabel());
        if (board.hasErrord()) {
            System.out.println("O jogo contém erros");
        } else {
            System.out.println("O jogo não contém erros");
        }
    }


    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu prograsso");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")) {
            System.out.println("Informe (sim) ou (não)");
             confirm = scanner.next();
        }

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
        } else if (board.hasErrord()) {
            System.out.println("Seu jogo contem erros, verifique sue board e o ajuste-o");
        } else{
            System.out.println("Você ainda precisa prencher algum espaço");
            System.out.println("preencha o espaço direito");
        }

    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabens você concluio o jogo");
            showCurrentGame();
            board = null;

        }
    }

    private static int runUnitGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();
        while (current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }


}