package hu.unideb.inf.carrental.ui.event;

import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.ui.commons.component.window.CarWindow;

public abstract class CarRentalEvent {

    public static class OpenManagerWindow {
        private final long siteID;

        public OpenManagerWindow(long siteID) {
            this.siteID = siteID;
        }

        public long getSiteID() {
            return siteID;
        }
    }

    public static class OpenReservationWindow {
        private final long carID;

        public OpenReservationWindow(long carID) {
            this.carID = carID;
        }

        public long getCarID() {
            return carID;
        }
    }

    public static class OpenSiteWindowForAddingEvent {
    }

    public static final class OpenSiteWindowForEditingEvent {
        private final SiteResponse siteResponse;

        public OpenSiteWindowForEditingEvent(SiteResponse siteResponse) {
            this.siteResponse = siteResponse;
        }

        public SiteResponse getSiteResponse() {
            return siteResponse;
        }
    }

    public static class OpenCarWindowForAddingEvent {
        private final CarWindow.Type type;

        public OpenCarWindowForAddingEvent(CarWindow.Type type) {
            this.type = type;
        }

        public CarWindow.Type getType() {
            return type;
        }
    }

    public static class RefreshRequestEvent {
        private String navigateTo;

        public RefreshRequestEvent() {
            navigateTo = null;
        }

        public RefreshRequestEvent(String navigateTo) {
            this.navigateTo = navigateTo;
        }

        public String getNavigateTo() {
            return navigateTo;
        }
    }

    public static class LogoutRequestEvent {
    }
}
