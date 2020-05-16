package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import kpi.manfredi.gui.Context;
import kpi.manfredi.model.Comb;
import kpi.manfredi.model.Data;
import kpi.manfredi.utils.MathUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private Data data;

    private final int cellSize = 50;
    private int widthInCells;
    private int heightInCells;
    private int canvasWidth;
    private int canvasHeight;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = Context.getInstance().getData();
        initCanvas();
    }

    private void initCanvas() {
        calculateSizes();
        clearCanvas();
        drawGrid();
        drawNumbering();
    }

    /**
     * This method is used to calculate size of the grid and size of the canvas
     */
    private void calculateSizes() {
        widthInCells = calculateColumns();
        heightInCells = calculateNumberOfRows();

        canvasWidth = cellSize * (widthInCells + 3); // 2 to padding (left and right) and 1 to numbering
        canvasHeight = cellSize * (heightInCells + 3); // 2 to padding (top and bottom) and 1 to numbering

        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);
    }

    /**
     * This method is used to calculate number of rows
     *
     * @return number of rows
     */
    private Integer calculateNumberOfRows() {
        List<Integer> numbers = new ArrayList<>();
        for (Comb comb : data.getComb()) {
            numbers.add(comb.getRow().size());
        }
        return MathUtil.getLeastCommonMultiple(numbers);
    }

    /**
     * This method is used to calculate number of columns
     *
     * @return number of columns
     */
    private Integer calculateColumns() {
        int result = 0;
        for (Comb comb : data.getComb()) {
            for (Comb.Row row : comb.getRow()) {
                result = (int) Math.max(result, row.getA());
                result = (int) Math.max(result, row.getB());
            }
        }
        return result + 1;
    }

    /**
     * This method is used to clear canvas
     */
    private void clearCanvas() {
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * This method is used to draw dot grid
     */
    private void drawGrid() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.BLACK);
        double radius = 4;
        double yPoint = canvasHeight - 2.5 * cellSize;
        while (yPoint > cellSize) {
            double xPoint = canvasWidth - 1.5 * cellSize;
            while (xPoint > 2 * cellSize) {
                context.fillOval(xPoint - radius, yPoint - radius, radius, radius);
                xPoint -= cellSize;
            }
            yPoint -= cellSize;
        }
    }

    private void drawNumbering() {
        drawRowNumbering();
        drawColumnNumbering();
    }

    /**
     * This method is used to draw numbers of rows
     */
    private void drawRowNumbering() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setTextAlign(TextAlignment.RIGHT);
        int number = 1;
        double xPoint = cellSize * 1.5;
        double yPoint = canvasHeight - 2.5 * cellSize;
        while (yPoint > cellSize) {
            context.strokeText(Integer.toString(number++), xPoint, yPoint);
            yPoint -= cellSize;
        }
    }

    /**
     * This method is used to draw numbers of columns
     */
    private void drawColumnNumbering() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setTextAlign(TextAlignment.CENTER);
        int number = 0;
        double xPoint = canvasWidth - cellSize;
        double yPoint = canvasHeight - 1.5 * cellSize;
        while (xPoint > cellSize) {
            String s = Integer.toString(number++);

            context.strokeText(s, xPoint, yPoint);
            xPoint -= cellSize;
        }
    }
}
