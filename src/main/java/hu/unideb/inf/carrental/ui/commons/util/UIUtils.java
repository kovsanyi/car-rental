package hu.unideb.inf.carrental.ui.commons.util;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import hu.unideb.inf.carrental.ui.CarRentalUI;

public final class UIUtils {
    private UIUtils() {
    }

    public static void showNotification(String caption, String message, Notification.Type type) {
        Notification notification = new Notification(caption, message, type);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(CarRentalUI.getCurrent().getPage());
    }

    public static void showNotification(String message, Notification.Type type) {
        Notification notification = new Notification(null, message, type);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(CarRentalUI.getCurrent().getPage());
    }
}
