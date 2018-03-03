package hu.unideb.inf.carrental.ui.commons.component.window;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.commons.exception.ManagerCollisionException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.manager.service.ManagerService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.util.Objects;

public class ManagerWindow extends Window {

    public ManagerWindow(ManagerService managerService, SiteService siteService, long siteID) {
        this.managerService = managerService;
        this.siteService = siteService;
        this.siteID = siteID;

        managerUsername = buildManagerUsername();
        setManager = buildSetManagerButton();
        deleteManager = buildDeleteManagerButton();

        setCaption("Setting manager");
        setModal(true);
        setDraggable(false);
        setResizable(false);

        setContent(buildContent());
        loadSiteManager();
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.setMargin(true);
        content.setSpacing(true);

        content.addComponents(
                managerUsername,
                setManager,
                deleteManager,
                buildCancelButton()
        );

        return content;
    }

    private TextField buildManagerUsername() {
        final TextField managerUsername = new TextField("Manager username");
        managerUsername.setWidth(150.f, Unit.PIXELS);
        return managerUsername;
    }

    private Button buildSetManagerButton() {
        final Button setManager = new Button("Set manager", e -> {
            try {
                siteService.setManagerByUsername(siteID, managerUsername.getValue());
                close();
                UIUtils.showNotification("Manager set successfully!", Notification.Type.TRAY_NOTIFICATION);
            } catch (NotFoundException | ManagerCollisionException ex) {
                UIUtils.showNotification(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
            } catch (UnauthorizedAccessException ex) {
                UIUtils.showNotification(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        setManager.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        setManager.setWidth(100.f, Unit.PERCENTAGE);

        return setManager;
    }

    private Button buildDeleteManagerButton() {
        final Button deleteManager = new Button("Delete manager", e -> {
            try {
                siteService.deleteManagerBySiteId(siteID);
                close();
                UIUtils.showNotification("Manager deleted successfully!", Notification.Type.TRAY_NOTIFICATION);
            } catch (UnauthorizedAccessException | NotFoundException ex) {
                UIUtils.showNotification("Error, message: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        deleteManager.setWidth(100.f, Unit.PERCENTAGE);
        deleteManager.addStyleName(ValoTheme.BUTTON_DANGER);

        return deleteManager;
    }

    private Button buildCancelButton() {
        final Button cancel = new Button("Cancel", e -> close());
        cancel.setWidth(100.f, Unit.PERCENTAGE);
        return cancel;
    }

    private void loadSiteManager() {
        try {
            Long managerID = siteService.getById(siteID).getManagerId();
            if (Objects.nonNull(managerID)) {
                String username = managerService.getById(managerID).getUserUsername();
                managerUsername.setValue(username);
                managerUsername.setEnabled(false);
                setManager.setVisible(false);
            } else {
                deleteManager.setVisible(false);
            }
        } catch (NotFoundException e) {
            UIUtils.showNotification("Error, message: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private TextField managerUsername;
    private Button setManager;
    private Button deleteManager;

    private final ManagerService managerService;
    private final SiteService siteService;
    private final long siteID;
}
