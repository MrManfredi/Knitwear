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
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileNotFoundException;

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
        File file = getSelectedFile();
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
                logger.error(e.getMessage());
            }
        }
    }

    private static File getSelectedFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        return fileChooser.showSaveDialog(Context.getInstance().getPrimaryStage());
    }
}
