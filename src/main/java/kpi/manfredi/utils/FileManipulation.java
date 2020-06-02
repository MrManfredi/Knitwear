package kpi.manfredi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

import static kpi.manfredi.utils.MessageUtil.formatMessage;

public abstract class FileManipulation {
    private static final Logger logger = LoggerFactory.getLogger(FileManipulation.class);

    /**
     * This method is used to return copy of resource file (temporary file) by relative path
     * (like {@code '/path/to/file.txt'})
     *
     * @param path path to resource file
     * @return <ui>
     * <li>A temporary copy of the resource extracted from this file if a program is called from
     * {@code *.jar} file</li>
     * <li>Otherwise, the method returns a resource file.</li>
     * </ui>
     * @throws FileNotFoundException file not found
     */
    public static File getResourceFile(String path) throws FileNotFoundException {
        File file;

        URL res = FileManipulation.class.getResource(path);
        if (res == null) throw new FileNotFoundException(formatMessage("file.not.found", path));

        if (res.getProtocol().equals("jar")) {
            file = getCopyOfFile(path);
        } else {
            file = new File(res.getFile()); // it will work from IDE, but not from a JAR
        }

        if (file != null && !file.exists()) {
            throw new FileNotFoundException(formatMessage("file.not.found", file));
        }
        return file;
    }

    /**
     * This method is used to return copy of file (temporary file) by relative path
     *
     * @param path path to resource file
     * @return copy of file
     */
    private static File getCopyOfFile(String path) {
        File file = null;
        try {
            InputStream input = FileManipulation.class.getResourceAsStream(path);
            file = File.createTempFile("tempfile", ".tmp");
            OutputStream out = new FileOutputStream(file);

            int read;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.close();
            file.deleteOnExit();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return file;
    }

}
