package hu.unideb.inf.carrental.ui.event;

import hu.unideb.inf.carrental.site.resource.model.SiteResponse;

public abstract class CarRentalEvent {

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
    }
}