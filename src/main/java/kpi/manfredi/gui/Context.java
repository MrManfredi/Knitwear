package kpi.manfredi.gui;

import javafx.scene.Node;
import javafx.stage.Stage;
import kpi.manfredi.model.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * This class is used to contain configurations to manage application
 */
public class Context {

    private final List<Locale> availableLocales;

    private Stage primaryStage;
    private Locale currentLocale;
    private int numberOfCombs;
    private Data data;
    private List<Node> combPanelsList;
    private double lineThickness;
    private double cellSize;
    private double loopRadius;

    private static Context instance;

    /**
     * This method is used to return instance of {@code Context} class
     *
     * @return instance of {@code Context} class
     */
    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    private Context() {
        lineThickness = 2.0;
        cellSize = 50.0;
        loopRadius = 20.0;
        availableLocales = new LinkedList<>();
        availableLocales.add(new Locale("eng"));
        availableLocales.add(new Locale("ukr"));
        currentLocale = new Locale("ukr");  // default locale
    }

    /**
     * This method is used to set primary stage of application
     *
     * @param primaryStage primary stage
     */
    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * This method is used to return primary stage of application
     *
     * @return primary stage of application
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * This method is used to return available locales
     *
     * @return available locales
     */
    public List<Locale> getAvailableLocales() {
        return availableLocales;
    }

    /**
     * This method is used to return current locale setting
     *
     * @return current locale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * This method is used to set current locale setting
     *
     * @param currentLocale locale to set
     */
    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    public int getNumberOfCombs() {
        return numberOfCombs;
    }

    public void setNumberOfCombs(int numberOfCombs) {
        this.numberOfCombs = numberOfCombs;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Node> getCombPanelsList() {
        return combPanelsList;
    }

    public void setCombPanelsList(List<Node> combPanelsList) {
        this.combPanelsList = combPanelsList;
    }

    public double getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(double lineThickness) {
        this.lineThickness = lineThickness;
    }

    public double getCellSize() {
        return cellSize;
    }

    public void setCellSize(double cellSize) {
        this.cellSize = cellSize;
    }

    public double getLoopRadius() {
        return loopRadius;
    }

    public void setLoopRadius(double loopRadius) {
        this.loopRadius = loopRadius;
    }
}
