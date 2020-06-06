package kpi.manfredi.gui;

/**
 * This enumeration is used to contain path to each screen in application
 *
 * @author manfredi
 */
public enum Screen {
    HOME("/screens/HomeScreen.fxml"),
    MAIN("/screens/MainScreen.fxml"),
    CREATING1("/screens/CreatingScreen1.fxml"),
    CREATING2("/screens/CreatingScreen2.fxml"),
    COMB_SETTINGS("/screens/CombSettingsScreen.fxml"),
    DISPLAY_SETTINGS("/screens/DisplaySettingsScreen.fxml");

    private final String path;

    Screen(String path) {
        this.path = path;
    }

    /**
     * This method is used to return the path to the screen
     *
     * @return path to the screen
     */
    public String getPath() {
        return path;
    }
}
