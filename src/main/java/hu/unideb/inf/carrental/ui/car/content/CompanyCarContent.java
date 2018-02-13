package hu.unideb.inf.carrental.ui.car.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.commons.content.car.CarContent;
import hu.unideb.inf.carrental.ui.commons.uploader.ImageUploader;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.util.List;

public class CompanyCarContent extends CarContent {

    public CompanyCarContent(CarResponse carResponse, List<CarImageResponse> carImageResponses,
                             CarService carService, CarImageService carImageService) {
        super(carResponse, carImageResponses);
        this.carService = carService;
        this.carImageService = carImageService;

        setupHeader();
    }

    private void setupHeader() {
        final HorizontalLayout container = new HorizontalLayout();
        container.setMargin(false);
        container.setSpacing(true);
        container.setSizeUndefined();

        final ImageUploader receiver = new ImageUploader(getCarResponse().getId(), carImageService);
        final Upload uploadPhoto = new Upload(null, receiver);
        uploadPhoto.setButtonCaption("Upload photo");
        uploadPhoto.addFailedListener(receiver);
        uploadPhoto.addSucceededListener(receiver);
        uploadPhoto.addFinishedListener(receiver);
        uploadPhoto.setIcon(VaadinIcons.UPLOAD);
        uploadPhoto.setImmediateMode(true);

        final Button viewSite = new Button("View site");
        viewSite.setSizeUndefined();
        viewSite.setIcon(VaadinIcons.LOCATION_ARROW);
        viewSite.addClickListener(e -> CarRentalUI.getCurrent().getPage()
                .open("/#!site/id=" + getCarResponse().getSiteId(), ""));

        final Button removeCar = new Button("Remove");
        removeCar.setSizeUndefined();
        removeCar.addStyleName(ValoTheme.BUTTON_DANGER);
        removeCar.setIcon(VaadinIcons.CAR);
        removeCar.addClickListener(e -> {
            try {
                carService.delete(getCarResponse().getId());
            } catch (NotFoundException | UnauthorizedAccessException ex) {
                UIUtils.showNotification(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            } catch (CarInRentException ex) {
                UIUtils.showNotification(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
            }

            UIUtils.showNotification("Car deleted successfully!", Notification.Type.HUMANIZED_MESSAGE);
            CarRentalUI.getCurrent().getPage().reload();
        });

        container.addComponents(
                uploadPhoto,
                viewSite,
                removeCar
        );

        getHeader().addComponent(container);
        getHeader().setComponentAlignment(container, Alignment.MIDDLE_RIGHT);
    }

    private final CarService carService;
    private final CarImageService carImageService;
}
