package hu.unideb.inf.carrental.ui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarRentalEventBus implements SubscriberExceptionHandler {

    public static void post(final Object event) {
        LOGGER.debug("Posting event");
        CarRentalUI.getCarRentalEventBus().eventBus.post(event);
    }

    public static void register(final Object object) {
        LOGGER.debug("Registering an object");
        CarRentalUI.getCarRentalEventBus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        LOGGER.debug("Unregistering an object");
        CarRentalUI.getCarRentalEventBus().eventBus.unregister(object);
    }

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        LOGGER.error("Exception in event bus");
        exception.printStackTrace();
    }

    private final EventBus eventBus = new EventBus(this);

    private static final Logger LOGGER = LoggerFactory.getLogger(CarRentalEventBus.class);
}
