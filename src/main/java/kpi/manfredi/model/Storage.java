package kpi.manfredi.model;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import kpi.manfredi.gui.Context;
import kpi.manfredi.utils.DialogsUtil;
import kpi.manfredi.utils.FileManipulation;
import kpi.manfredi.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileNotFoundException;

import static kpi.manfredi.utils.DialogsUtil.showAlert;
import static kpi.manfredi.utils.MessageUtil.formatMessage;
import static kpi.manfredi.utils.MessageUtil.getMessage;

abstract public class Storage {

    private static final Logger logger = LoggerFactory.getLogger(Storage.class);
    private static final String SCHEMA_LOCATION = "/schema.xsd";

    /**
     * This method is used to save combs settings to file
     *
     * @throws FileNotFoundException schema file not found
     */
    public static void saveData(Data data) throws FileNotFoundException {
        File schemaFile = FileManipulation.getResourceFile(SCHEMA_LOCATION);
        File file = getSelectedFile(Operation.SAVE);
        if (file != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                // output pretty printed
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                        .newSchema(schemaFile);
                jaxbMarshaller.setSchema(schema);

                jaxbMarshaller.marshal(data, file);

                DialogsUtil.showAlert(Alert.AlertType.INFORMATION,
                        MessageUtil.getMessage("data.save.title"),
                        MessageUtil.formatMessage("data.saved", file.getName()));

            } catch (JAXBException | SAXException e) {
                showValidationFailedAlert(e);
            }
        }
    }

    /**
     * This method is used to show dialog window to chose file to read/write data.
     *
     * @param operation operation for which file is need
     * @return selected file. Otherwise null
     */
    private static File getSelectedFile(Operation operation) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        switch (operation) {
            case SAVE:
                return fileChooser.showSaveDialog(Context.getInstance().getPrimaryStage());
            case OPEN:
                return fileChooser.showOpenDialog(Context.getInstance().getPrimaryStage());
            default:
                return null;
        }

    }

    /**
     * This method is used to read combs settings from file
     *
     * @return {@code Data} instance if validation successful. Otherwise null
     * @throws FileNotFoundException schema file not found
     */
    public static Data getData() throws FileNotFoundException {
        Data data = null;
        File schemaFile = FileManipulation.getResourceFile(SCHEMA_LOCATION);
        File file = getSelectedFile(Operation.OPEN);
        if (file != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                        .newSchema(schemaFile);
                unmarshaller.setSchema(schema);

                data = (Data) unmarshaller.unmarshal(file);

            } catch (JAXBException | SAXException e) {
                handleParsingException(e, file);
            }
        }

        return data;
    }

    /**
     * This method is used to show alert about exception cause
     *
     * @param e    exception
     * @param file file in which validation failed
     */
    private static void handleParsingException(Exception e, File file) {
        String constraintViolation = e.getCause().getMessage().
                substring(e.getCause().getMessage().indexOf(':') + 2);

        showAlert(
                Alert.AlertType.ERROR,
                getMessage("error.title"),
                formatMessage("file.validation.failed", file),
                constraintViolation
        );
    }

    /**
     * This method is used to show alert about exception cause
     *
     * @param e exception
     */
    private static void showValidationFailedAlert(Exception e) {
        String constraintViolation = e.getCause().getMessage().
                substring(e.getCause().getMessage().indexOf(':') + 2);

        showAlert(
                Alert.AlertType.ERROR,
                getMessage("error.title"),
                constraintViolation
        );
    }

    private enum Operation {
        SAVE,
        OPEN
    }
}
