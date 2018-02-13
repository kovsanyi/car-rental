package hu.unideb.inf.carrental.ui.commons.uploader;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;

public class ImageUploader implements Upload.Receiver, Upload.StartedListener, Upload.FailedListener,
        Upload.SucceededListener, Upload.FinishedListener {
    public ImageUploader(long carID, CarImageService carImageService) {
        this.carID = carID;
        this.carImageService = carImageService;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        OutputStream fileOutputStream;

        try {
            imageFile = new File(System.getProperty("user.dir") + filename);
            fileOutputStream = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return fileOutputStream;
    }

    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        UIUtils.showNotification("Image uploading has been started!", Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        UIUtils.showNotification("Error while uploading the image!", Notification.Type.ERROR_MESSAGE);
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        UIUtils.showNotification("Image uploaded successfully!", Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        if (imageFile.exists()) {
            try {
                carImageService.save(carID, Files.readAllBytes(imageFile.toPath()));
            } catch (NotFoundException | IOException | UnauthorizedAccessException e) {
                LOGGER.warn("Image ({}) can not be read due to {}", e.getMessage());
            } finally {
                if (imageFile.delete()) {
                    LOGGER.debug("Image ({}) deleted successfully", imageFile.getName());
                } else {
                    LOGGER.warn("Image ({}) can not be deleted: file does not exists", imageFile.getName());
                }
            }
        }
    }

    private File imageFile;

    private final long carID;
    private final CarImageService carImageService;
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageUploader.class);
}
