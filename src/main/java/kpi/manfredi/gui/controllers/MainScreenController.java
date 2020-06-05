package kpi.manfredi.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import kpi.manfredi.gui.Context;
import kpi.manfredi.gui.Screen;
import kpi.manfredi.model.Comb;
import kpi.manfredi.model.Data;
import kpi.manfredi.model.Storage;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.MathUtil;
import kpi.manfredi.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainScreenController.class);

    private Data data;

    private final int cellSize = 50;
    private final double bottomPadding = 2.5 * cellSize;
    private final double rightPadding = 1.5 * cellSize;
    private final double radius = 20;
    private int canvasWidth;
    private int canvasHeight;
    private int numberOfRows;
    private Point2D origin;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuSave;

    @FXML
    private MenuItem menuCloseProject;

    @FXML
    private Menu menuEdit;

    @FXML
    private MenuItem menuEditDigitalRecord;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = Context.getInstance().getData();
        refreshLocalization();
        setMenuEditDigitalRecordListener();
        setMenuSaveListener();
        setMenuCloseProjectListener();
        initCanvas();
    }

    private void refreshLocalization() {
        menuFile.setText(MessageUtil.getMessage("menu.file"));
        menuCloseProject.setText(MessageUtil.getMessage("menu.close.project"));
        menuSave.setText(MessageUtil.getMessage("menu.save"));
        menuEdit.setText(MessageUtil.getMessage("menu.edit"));
        menuEditDigitalRecord.setText(MessageUtil.getMessage("menu.edit.digital.record"));
    }

    private void setMenuEditDigitalRecordListener() {
        menuEditDigitalRecord.setOnAction(actionEvent ->
                ScreenController.activateScreen(
                        Screen.COMB_SETTINGS.getPath(), Context.getInstance().getPrimaryStage()));
    }

    private void setMenuSaveListener() {
        menuSave.setOnAction(actionEvent -> {
            try {
                Storage.saveData(data);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
        });
    }

    private void setMenuCloseProjectListener() {
        menuCloseProject.setOnAction(actionEvent -> {
            Optional<ButtonType> response = DialogsUtil.showConfirmationDialog(
                    MessageUtil.getMessage("menu.close.project"),
                    MessageUtil.getMessage("close.project.confirm")
            );
            if (response.isPresent() && response.get() == ButtonType.OK) {
                ScreenController.activateScreen(Screen.HOME.getPath(), Context.getInstance().getPrimaryStage());
            }
        });
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
        canvasWidth = cellSize * (calculateColumns() + 3); // 2 to padding (left and right) and 1 to numbering
        numberOfRows = calculateNumberOfRows();
        canvasHeight = cellSize * (numberOfRows + 3); // 2 to padding (top and bottom) and 1 to numbering

        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);

        origin = new Point2D(canvasWidth - rightPadding, canvasHeight - bottomPadding);
    }

    /**
     * This method is used to calculate number of rows
     *
     * @return number of rows
     */
    private Integer calculateNumberOfRows() {
        List<Integer> numbers = new ArrayList<>();
        for (Comb comb : data.getComb()) {
            if (comb.isVisible()) {
                numbers.add(comb.getRow().size());
            }
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
            if (comb.isVisible()) {
                for (Comb.Row row : comb.getRow()) {
                    result = (int) Math.max(result, row.getA());
                    result = (int) Math.max(result, row.getB());
                }
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
            if (comb.isVisible()) {
                drawComb(comb);
            }
        }
    }

    private void drawComb(Comb comb) {
        List<Comb.Row> rows = comb.getRow();
        for (int i = 0; i < numberOfRows - 1; i++) {
            Comb.Row row = rows.get(i % rows.size());
            Comb.Row nextRow = rows.get((i + 1) % rows.size());
            int shiftComb = (int) (row.getA() - row.getB());
            int shiftNextComb = (int) (nextRow.getA() - nextRow.getB());
            int shiftRow = (int) (Math.max(row.getA(), row.getB()) - Math.max(nextRow.getA(), nextRow.getB()));
            Case theCase = determineTheCase(shiftComb, shiftNextComb, shiftRow);

            drawSegment(theCase, row, i, nextRow);
        }
    }

    private Case determineTheCase(int shiftComb, int shiftNextComb, int shiftRow) {
        if (shiftComb > 0 && shiftNextComb > 0 && shiftRow > 0) return Case.CASE1;
        else if (shiftComb < 0 && shiftNextComb < 0 && shiftRow < 0) return Case.CASE2;
        else if (shiftComb > 0 && shiftNextComb > 0 && shiftRow < 0) return Case.CASE3;
        else if (shiftComb < 0 && shiftNextComb < 0 && shiftRow > 0) return Case.CASE4;
        else if (shiftComb > 0 && shiftNextComb < 0 && shiftRow > 0) return Case.CASE5;
        else if (shiftComb < 0 && shiftNextComb > 0 && shiftRow < 0) return Case.CASE6;
        else if (shiftComb > 0 && shiftNextComb < 0 && shiftRow < 0) return Case.CASE7;
        else if (shiftComb < 0 && shiftNextComb > 0 && shiftRow > 0) return Case.CASE8;
        else if (shiftComb == 0 && shiftNextComb > 0 && shiftRow > 0) return Case.CASE9;
        else if (shiftComb == 0 && shiftNextComb < 0 && shiftRow < 0) return Case.CASE10;
        else if (shiftComb == 0 && shiftNextComb > 0 && shiftRow < 0) return Case.CASE11;
        else if (shiftComb == 0 && shiftNextComb < 0 && shiftRow > 0) return Case.CASE12;
        else if (shiftComb > 0 && shiftNextComb == 0 && shiftRow > 0) return Case.CASE13;
        else if (shiftComb < 0 && shiftNextComb == 0 && shiftRow < 0) return Case.CASE14;
        else if (shiftComb > 0 && shiftNextComb == 0 && shiftRow < 0) return Case.CASE15;
        else if (shiftComb < 0 && shiftNextComb == 0 && shiftRow > 0) return Case.CASE16;
        else if (shiftComb == 0 && shiftNextComb == 0 && shiftRow < 0) return Case.CASE17;
        else if (shiftComb == 0 && shiftNextComb == 0 && shiftRow > 0) return Case.CASE18;
        else return Case.EMPTY;
    }

    private void drawSegment(Case theCase, Comb.Row row, int rowNum, Comb.Row nextRow) {

        switch (theCase) {
            case CASE1:
                drawSegment1(row, rowNum, nextRow);
                break;
            case CASE2:
                drawSegment2(row, rowNum, nextRow);
                break;
            case CASE3:
                drawSegment3(row, rowNum, nextRow);
                break;
            case CASE4:
                drawSegment4(row, rowNum, nextRow);
                break;
            case CASE5:
                drawSegment5(row, rowNum, nextRow);
                break;
            case CASE6:
                drawSegment6(row, rowNum, nextRow);
                break;
            case CASE7:
                drawSegment7(row, rowNum, nextRow);
                break;
            case CASE8:
                drawSegment8(row, rowNum, nextRow);
                break;
            case CASE9:
                drawSegment9(row, rowNum, nextRow);
                break;
            case CASE10:
                drawSegment10(row, rowNum, nextRow);
                break;
            case CASE11:
                drawSegment11(row, rowNum, nextRow);
                break;
            case CASE12:
                drawSegment12(row, rowNum, nextRow);
                break;
            case CASE13:
                drawSegment13(row, rowNum, nextRow);
                break;
            case CASE14:
                drawSegment14(row, rowNum, nextRow);
                break;
            case CASE15:
                drawSegment15(row, rowNum, nextRow);
                break;
            case CASE16:
                drawSegment16(row, rowNum, nextRow);
                break;
            case CASE17:
            case CASE18:
                drawSegment17and18(row, rowNum, nextRow);
                break;
            default:
                // do nothing
        }
    }

    private void drawSegment1(Comb.Row row, int rowNum, Comb.Row nextRow) {
        double x = origin.getX() - row.getB() * cellSize + radius;
        double y = origin.getY() - rowNum * cellSize;

        double bottomSide = (row.getB() - nextRow.getB()) * cellSize - radius;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + cellSize * cellSize);

        double beta = Math.atan(cellSize / bottomSide);
        double alpha = Math.asin(radius / hypotenuse);

        double t = alpha + beta;

        List<Point2D> points = MathUtil.getCirclePoints(
                new Point2D(origin.getX() - nextRow.getB() * cellSize, origin.getY() - (rowNum + 1) * cellSize),
                radius,
                0.0,
                Math.PI * 0.5 + t);

        Point2D lastPoint = new Point2D(x, y);

        points.add(lastPoint);

        drawContour(points);
    }

    private void drawSegment2(Comb.Row row, int rowNum, Comb.Row nextRow) {

        double bottomSide = (nextRow.getA() - row.getA()) * cellSize - radius;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + cellSize * cellSize);

        double alpha = Math.atan(cellSize / bottomSide);
        double beta = Math.asin(radius / hypotenuse);

        Point2D center = new Point2D(
                origin.getX() - nextRow.getA() * cellSize,
                origin.getY() - (rowNum + 1) * cellSize);

        List<Point2D> points = MathUtil.getCirclePoints(center, radius, Math.PI, Math.PI / 2 - alpha - beta);

        double x = origin.getX() - row.getA() * cellSize - radius;
        double y = origin.getY() - rowNum * cellSize;
        points.add(new Point2D(x, y));

        drawContour(points);
    }

    private void drawSegment3(Comb.Row row, int rowNum, Comb.Row nextRow) {

        double t = Math.asin(cellSize /
                Math.sqrt(cellSize * cellSize + Math.pow((nextRow.getB() - row.getB()) * cellSize, 2)));
        double angleTo = -(Math.PI / 2 + t);
        List<Point2D> points = MathUtil.getCirclePoints(
                new Point2D(origin.getX() - row.getB() * cellSize, origin.getY() - rowNum * cellSize),
                radius,
                0.0,
                angleTo);

        points.addAll(MathUtil.getCirclePoints(
                new Point2D(origin.getX() - nextRow.getB() * cellSize, origin.getY() - (rowNum + 1) * cellSize),
                radius,
                Math.PI * 1.5 - t,
                0.0
        ));

        drawContour(points);
    }

    private void drawSegment4(Comb.Row row, int rowNum, Comb.Row nextRow) {

        double t = Math.atan(1.0 / (row.getA() - nextRow.getA()));
        double angleTo = (Math.PI * 1.5 + t);
        List<Point2D> points = MathUtil.getCirclePoints(
                new Point2D(origin.getX() - row.getA() * cellSize, origin.getY() - rowNum * cellSize),
                radius,
                Math.PI,
                angleTo);

        points.addAll(MathUtil.getCirclePoints(
                new Point2D(origin.getX() - nextRow.getA() * cellSize, origin.getY() - (rowNum + 1) * cellSize),
                radius,
                t - Math.PI * 0.5,
                Math.PI
        ));

        drawContour(points);
    }

    private void drawSegment5(Comb.Row row, int rowNum, Comb.Row nextRow) {
        double x = origin.getX() - row.getB() * cellSize + radius;
        double y = origin.getY() - rowNum * cellSize;

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x, y));

        double bottomSide = (row.getB() - nextRow.getA()) * cellSize - radius;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + cellSize * cellSize);

        double beta = Math.atan(bottomSide / cellSize);
        double alpha = Math.asin(radius / hypotenuse);
        double t = Math.PI * 0.5 - alpha - beta;

        points.addAll(MathUtil.getCirclePoints(
                new Point2D(origin.getX() - nextRow.getA() * cellSize, origin.getY() - (rowNum + 1) * cellSize),
                radius,
                t - Math.PI * 0.5,
                Math.PI
        ));

        drawContour(points);
    }

    private void drawSegment6(Comb.Row row, int rowNum, Comb.Row nextRow) {
        double bottomSide = (nextRow.getB() - row.getA()) * cellSize - radius;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + cellSize * cellSize);

        double beta = Math.atan(bottomSide / cellSize);
        double alpha = Math.asin(radius / hypotenuse);
        double t = Math.PI * 0.5 - alpha - beta;

        Point2D center = new Point2D(
                origin.getX() - nextRow.getB() * cellSize,
                origin.getY() - (rowNum + 1) * cellSize);

        List<Point2D> points = MathUtil.getCirclePoints(center, radius, 0, Math.PI * 1.5 - t);

        double x = origin.getX() - row.getA() * cellSize - radius;
        double y = origin.getY() - rowNum * cellSize;
        points.add(new Point2D(x, y));

        drawContour(points);
    }

    private void drawSegment7(Comb.Row row, int rowNum, Comb.Row nextRow) {
        double bottomSide = (nextRow.getA() - row.getB()) * cellSize;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + cellSize * cellSize);

        double beta = Math.atan(cellSize / bottomSide);
        double alpha = Math.acos(2 * radius / hypotenuse);
        double t = alpha - beta;

        Point2D center1 = new Point2D(
                origin.getX() - row.getB() * cellSize, origin.getY() - rowNum * cellSize);
        List<Point2D> points = MathUtil.getCirclePoints(center1, radius, 0, t - Math.PI);

        Point2D center2 = new Point2D(
                origin.getX() - nextRow.getA() * cellSize, origin.getY() - (rowNum + 1) * cellSize);
        points.addAll(MathUtil.getCirclePoints(center2, radius, t, Math.PI));

        drawContour(points);
    }

    private void drawSegment8(Comb.Row row, int rowNum, Comb.Row nextRow) {
        double bottomSide = (row.getA() - nextRow.getB()) * cellSize;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + cellSize * cellSize);

        double beta = Math.atan(cellSize / bottomSide);
        double alpha = Math.acos(2 * radius / hypotenuse);
        double t = alpha - beta;

        Point2D center1 = new Point2D(
                origin.getX() - row.getA() * cellSize, origin.getY() - rowNum * cellSize);
        List<Point2D> points = MathUtil.getCirclePoints(center1, radius, Math.PI, Math.PI * 2 - t);

        Point2D center2 = new Point2D(
                origin.getX() - nextRow.getB() * cellSize, origin.getY() - (rowNum + 1) * cellSize);
        points.addAll(MathUtil.getCirclePoints(center2, radius, Math.PI - t, 0));

        drawContour(points);
    }

    private void drawSegment9(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x = origin.getX() - (row.getA() - 0.5) * cellSize;
        double y1 = origin.getY() - rowNum * cellSize;
        double y2 = y1 - cellSize * 0.3;
        points.add(new Point2D(x, y1));
        points.add(new Point2D(x, y2));
        Point2D center = new Point2D(
                origin.getX() - nextRow.getB() * cellSize, origin.getY() - (rowNum + 1) * cellSize);


        double bottomSide = (row.getA() - nextRow.getB() - 0.5) * cellSize;
        double side = y2 - center.getY();
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + side * side);

        double beta = Math.atan(side / bottomSide);
        double alpha = Math.asin(radius / hypotenuse);
        double t = alpha + beta;

        points.addAll(MathUtil.getCirclePoints(
                center,
                radius,
                Math.PI * 0.5 + t,
                0));

        drawContour(points);
    }

    private void drawSegment10(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x = origin.getX() - (row.getA() - 0.5) * cellSize;
        double y1 = origin.getY() - rowNum * cellSize;
        double y2 = y1 - cellSize * 0.3;
        points.add(new Point2D(x, y1));
        points.add(new Point2D(x, y2));
        Point2D center = new Point2D(
                origin.getX() - nextRow.getA() * cellSize,
                origin.getY() - (rowNum + 1) * cellSize);


        double bottomSide = x - (origin.getX() - nextRow.getA() * cellSize);
        double side = y2 - center.getY();
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + side * side);

        double beta = Math.atan(side / bottomSide);
        double alpha = Math.asin(radius / hypotenuse);
        double t = alpha + beta;

        points.addAll(MathUtil.getCirclePoints(
                center,
                radius,
                Math.PI * 0.5 - t,
                Math.PI));

        drawContour(points);
    }

    private void drawSegment11(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x = origin.getX() - (row.getA() - 0.5) * cellSize;
        double y1 = origin.getY() - rowNum * cellSize;
        double y2 = y1 - cellSize * 0.3;
        points.add(new Point2D(x, y1));
        points.add(new Point2D(x, y2));
        Point2D center = new Point2D(
                origin.getX() - nextRow.getB() * cellSize,
                origin.getY() - (rowNum + 1) * cellSize);


        double bottomSide = x - (origin.getX() - nextRow.getB() * cellSize);
        double side = y2 - center.getY();
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + side * side);

        double beta = Math.atan(bottomSide / side);
        double alpha = Math.asin(radius / hypotenuse);
        double t = Math.PI * 0.5 - alpha - beta;

        points.addAll(MathUtil.getCirclePoints(
                center,
                radius,
                Math.PI * 1.5 - t,
                0));

        drawContour(points);
    }

    private void drawSegment12(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x = origin.getX() - (row.getA() - 0.5) * cellSize;
        double y1 = origin.getY() - rowNum * cellSize;
        double y2 = y1 - cellSize * 0.3;
        points.add(new Point2D(x, y1));
        points.add(new Point2D(x, y2));
        Point2D center = new Point2D(
                origin.getX() - nextRow.getA() * cellSize,
                origin.getY() - (rowNum + 1) * cellSize);


        double bottomSide = (origin.getX() - nextRow.getA() * cellSize) - x;
        double side = y2 - center.getY();
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + side * side);

        double beta = Math.atan(bottomSide / side);
        double alpha = Math.asin(radius / hypotenuse);
        double t = Math.PI * 0.5 - alpha - beta;

        points.addAll(MathUtil.getCirclePoints(
                center,
                radius,
                t - Math.PI * 0.5,
                Math.PI));

        drawContour(points);
    }

    private void drawSegment13(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x1 = origin.getX() - row.getB() * cellSize + radius;
        double y1 = origin.getY() - rowNum * cellSize;
        double x2 = origin.getX() - (nextRow.getA() - 0.5) * cellSize;
        double y3 = origin.getY() - (rowNum + 1) * cellSize;
        double y2 = y3 + cellSize * 0.3;

        points.add(new Point2D(x1, y1));
        points.add(new Point2D(x2, y2));
        points.add(new Point2D(x2, y3));

        drawContour(points);
    }

    private void drawSegment14(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x1 = origin.getX() - row.getA() * cellSize - radius;
        double y1 = origin.getY() - rowNum * cellSize;
        double x2 = origin.getX() - (nextRow.getA() - 0.5) * cellSize;
        double y3 = origin.getY() - (rowNum + 1) * cellSize;
        double y2 = y3 + cellSize * 0.3;

        points.add(new Point2D(x1, y1));
        points.add(new Point2D(x2, y2));
        points.add(new Point2D(x2, y3));

        drawContour(points);
    }

    private void drawSegment15(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x = origin.getX() - (nextRow.getA() - 0.5) * cellSize;
        double y2 = origin.getY() - (rowNum + 1) * cellSize;
        double y1 = y2 + cellSize * 0.3;
        points.add(new Point2D(x, y2));
        points.add(new Point2D(x, y1));
        Point2D center = new Point2D(
                origin.getX() - row.getB() * cellSize,
                origin.getY() - rowNum * cellSize);


        double bottomSide = center.getX() - x;
        double side = center.getY() - y1;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + side * side);

        double beta = Math.atan(side / bottomSide);
        double alpha = Math.asin(radius / hypotenuse);
        double t = alpha + beta;

        points.addAll(MathUtil.getCirclePoints(
                center,
                radius,
                -Math.PI * 0.5 - t,
                0));

        drawContour(points);
    }

    private void drawSegment16(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x = origin.getX() - (nextRow.getA() - 0.5) * cellSize;
        double y2 = origin.getY() - (rowNum + 1) * cellSize;
        double y1 = y2 + cellSize * 0.3;
        points.add(new Point2D(x, y2));
        points.add(new Point2D(x, y1));
        Point2D center = new Point2D(
                origin.getX() - row.getA() * cellSize,
                origin.getY() - rowNum * cellSize);


        double bottomSide = x - center.getX();
        double side = center.getY() - y1;
        double hypotenuse = Math.sqrt(bottomSide * bottomSide + side * side);

        double beta = Math.atan(side / bottomSide);
        double alpha = Math.asin(radius / hypotenuse);
        double t = alpha + beta;

        points.addAll(MathUtil.getCirclePoints(
                center,
                radius,
                t - Math.PI * 0.5,
                -Math.PI));

        drawContour(points);
    }

    private void drawSegment17and18(Comb.Row row, int rowNum, Comb.Row nextRow) {
        List<Point2D> points = new ArrayList<>();
        double x1 = origin.getX() - (row.getA() - 0.5) * cellSize;
        double y11 = origin.getY() - rowNum * cellSize;
        double y12 = y11 - cellSize * 0.3;
        double x2 = origin.getX() - (nextRow.getA() - 0.5) * cellSize;
        double y22 = origin.getY() - (rowNum + 1) * cellSize;
        double y21 = y22 + cellSize * 0.3;

        points.add(new Point2D(x1, y11));
        points.add(new Point2D(x1, y12));
        points.add(new Point2D(x2, y21));
        points.add(new Point2D(x2, y22));

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
        CASE18,
        EMPTY
    }
}
