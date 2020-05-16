package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
    private final double bottomPadding = 2.5 * cellSize;
    private final double rightPadding = 1.5 * cellSize;
    private final double radius = 20;
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
        drawCombs();
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
        double yPoint = canvasHeight - bottomPadding;
        while (yPoint > cellSize) {
            double xPoint = canvasWidth - rightPadding;
            while (xPoint > 2 * cellSize) {
                context.fillOval(xPoint - radius / 2, yPoint - radius / 2, radius, radius);
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
        double yPoint = canvasHeight - bottomPadding;
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
        double yPoint = canvasHeight - rightPadding;
        while (xPoint > cellSize) {
            String s = Integer.toString(number++);

            context.strokeText(s, xPoint, yPoint);
            xPoint -= cellSize;
        }
    }

    private void drawCombs() {
        for (Comb comb : data.getComb()) {
            drawComb(comb);
        }
    }

    private void drawComb(Comb comb) {
        List<Comb.Row> rows = comb.getRow();
        for (int i = 0; i < rows.size() - 1; i++) {
            Comb.Row row = rows.get(i);
            Comb.Row nextRow = rows.get(i + 1);
            int shiftComb = (int)(row.getA() - row.getB());
            int shiftNextComb = (int)(nextRow.getA() - nextRow.getB());
            int shiftRow = (int)(Math.max(row.getA(), row.getB()) - Math.max(nextRow.getA(), nextRow.getB()));
            Case theCase = determineTheCase(shiftComb, shiftNextComb, shiftRow);

            drawSegment(theCase, row, i, nextRow);
        }
    }

    private Case determineTheCase(int shiftComb, int shiftNextComb, int shiftRow) {

        if (shiftComb > 0 && shiftNextComb > 0 && shiftRow > 0) return Case.CASE1;
        if (shiftComb < 0 && shiftNextComb < 0 && shiftRow < 0) return Case.CASE2;
        if (shiftComb > 0 && shiftNextComb > 0 && shiftRow < 0) return Case.CASE3;
        if (shiftComb < 0 && shiftNextComb < 0 && shiftRow > 0) return Case.CASE4;
        if (shiftComb > 0 && shiftNextComb < 0 && shiftRow > 0) return Case.CASE5;
        if (shiftComb < 0 && shiftNextComb > 0 && shiftRow < 0) return Case.CASE6;
        if (shiftComb > 0 && shiftNextComb < 0 && shiftRow < 0) return Case.CASE7;
        if (shiftComb < 0 && shiftNextComb > 0 && shiftRow > 0) return Case.CASE8;
        if (shiftComb == 0 && shiftNextComb > 0 && shiftRow > 0) return Case.CASE9;
        if (shiftComb == 0 && shiftNextComb < 0 && shiftRow < 0) return Case.CASE10;
        if (shiftComb == 0 && shiftNextComb > 0 && shiftRow < 0) return Case.CASE11;
        if (shiftComb == 0 && shiftNextComb < 0 && shiftRow > 0) return Case.CASE12;
        if (shiftComb > 0 && shiftNextComb == 0 && shiftRow > 0) return Case.CASE13;
        if (shiftComb < 0 && shiftNextComb == 0 && shiftRow < 0) return Case.CASE14;
        if (shiftComb > 0 && shiftNextComb == 0 && shiftRow < 0) return Case.CASE15;
        if (shiftComb < 0 && shiftNextComb == 0 && shiftRow > 0) return Case.CASE16;
        if (shiftComb == 0 && shiftNextComb == 0 && shiftRow < 0) return Case.CASE17;
        if (shiftComb == 0 && shiftNextComb == 0 && shiftRow > 0) return Case.CASE18;

        return null;
    }

    private void drawSegment(Case theCase, Comb.Row row, int rowNum, Comb.Row nextRow) {

        switch (theCase) {
            case CASE1:
                drawSegment1(row, rowNum, nextRow);
                break;
            case CASE2:
                break;
            case CASE3:
                break;
            case CASE4:
                break;
            case CASE5:
                break;
            case CASE6:
                break;
            case CASE7:
                break;
            case CASE8:
                break;
            case CASE9:
                break;
            case CASE10:
                break;
            case CASE11:
                break;
            case CASE12:
                break;
            case CASE13:
                break;
            case CASE14:
                break;
            case CASE15:
                break;
            case CASE16:
                break;
            case CASE17:
                break;
            case CASE18:
                break;
        }
    }

    private void drawSegment1(Comb.Row row, int rowNum, Comb.Row nextRow) {
        double   endX = getOrigin().getX() - row.getB() * cellSize + radius;
        double   endY = getOrigin().getY() - rowNum * cellSize;

        double side = getOrigin().getX() - nextRow.getB() * cellSize - endX;

        double angleTo = 
                Math.PI / 2 +
                Math.asin(radius / Math.sqrt(cellSize * cellSize + Math.pow(side, 2))) +
                Math.atan(cellSize / (side));

        List<Point2D> points = MathUtil.getCirclePoints(
                new Point2D(getOrigin().getX() - nextRow.getB() * cellSize, getOrigin().getY() - (rowNum + 1) * cellSize),
                radius,
                0.0,
                angleTo);

        Point2D lastPoint = new Point2D(endX, endY);
        
        points.add(lastPoint);
        
        drawContour(points);
    }

    private void drawContour(List<Point2D> points) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setLineWidth(1.0);
        context.beginPath();
        for (Point2D point : points) {
            context.lineTo(point.getX(), point.getY());
        }
        context.stroke();
    }

    private void drawLine(Line line) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.beginPath();
        context.moveTo(line.getStartX(), line.getStartY());
        context.lineTo(line.getEndX(), line.getEndY());
        context.stroke();
    }
    
    private Point2D getOrigin() {
        return new Point2D(canvasWidth - rightPadding, canvasHeight - bottomPadding);
    }

    private enum Case {
        CASE1,
        CASE2,
        CASE3,
        CASE4,
        CASE5,
        CASE6,
        CASE7,
        CASE8,
        CASE9,
        CASE10,
        CASE11,
        CASE12,
        CASE13,
        CASE14,
        CASE15,
        CASE16,
        CASE17,
        CASE18;
    }
}
