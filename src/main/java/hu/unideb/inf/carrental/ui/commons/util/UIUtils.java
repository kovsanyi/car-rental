package hu.unideb.inf.carrental.ui.commons.util;

import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
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

    public static class Params {
        public static String createKeyValuePair(String key, String value) {
            return String.format("/%s=%s", key, value);
        }
    }

    public static class HTML {
        public static String bold(String text) {
            return String.format("<b>%s</b>", text);
        }

        public static String italic(String text) {
            return String.format("<i>%s</i>", text);
        }

        public static Label buildKeyLabel(String text) {
            final Label label = new Label(bold(text), ContentMode.HTML);
            label.setId("key");
            return label;
        }

        public static Label buildValueLabel(String text) {
            final Label label = new Label(text);
            label.setId("value");
            return label;
        }

        public static Label buildValueLabel(String text, ContentMode contentMode) {
            final Label label = new Label(text, contentMode);
            label.setId("value");
            return label;
        }
    }
}
