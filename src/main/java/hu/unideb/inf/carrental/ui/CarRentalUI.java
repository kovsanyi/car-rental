package hu.unideb.inf.carrental.ui;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.ui.company.CompanyView;
import hu.unideb.inf.carrental.ui.customer.CustomerView;
import hu.unideb.inf.carrental.ui.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;


@Title("Car Rental")
@SpringUI
public class CarRentalUI extends UI {
    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLocale(Locale.US);

        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        if (SecurityUtils.isLoggedIn()) {
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_CUSTOMER)) {
                navigator.navigateTo(CustomerView.VIEW_NAME);
            }
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_COMPANY)) {
                navigator.navigateTo(CompanyView.VIEW_NAME);
            }
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_MANAGER)) {
                navigator.navigateTo(CustomerView.VIEW_NAME);
            }
        } else {
            navigator.navigateTo(LoginView.VIEW_NAME);
        }
    }

    public static void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
