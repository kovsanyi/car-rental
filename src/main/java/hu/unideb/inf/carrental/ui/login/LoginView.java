package hu.unideb.inf.carrental.ui.login;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.signup.SignUpView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UIScope
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {

    @Autowired
    public LoginView(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setSizeFull();
        setMargin(false);
        setSpacing(false);

        final VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSizeUndefined();

        final Component loginForm = buildLoginForm();
        wrapper.addComponent(loginForm);
        wrapper.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

        final Component signUpLabel = buildSignUpLabel();
        wrapper.addComponent(signUpLabel);
        wrapper.setComponentAlignment(signUpLabel, Alignment.BOTTOM_LEFT);

        addComponent(wrapper);
        setComponentAlignment(wrapper, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);

        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildSignUpLabel() {
        final HorizontalLayout signUpPanel = new HorizontalLayout();
        signUpPanel.setSizeUndefined();
        signUpPanel.setMargin(false);

        signUpPanel.addComponent(new Label("Are you not a member?"));
        signUpPanel.addComponent(new Link("Sign up now!", new ExternalResource("#!" + SignUpView.VIEW_NAME)));

        return signUpPanel;
    }

    private Component buildFields() {
        final HorizontalLayout fields = new HorizontalLayout();

        final TextField username = new TextField("Username");
        final PasswordField password = new PasswordField("Password");
        final Button login = new Button("Login");
        login.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.focus();
        login.addClickListener(e -> {
            try {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username.getValue(), password.getValue());
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                CarRentalUI.getCurrent().getPage().reload();
            } catch (BadCredentialsException ex) {
                password.clear();
                Notification.show("Wrong username or password!", Notification.Type.WARNING_MESSAGE);
            }
        });

        fields.addComponent(username);
        fields.addComponent(password);
        fields.addComponent(login);
        fields.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);

        return fields;
    }

    private final AuthenticationManager authenticationManager;

    public static final String VIEW_NAME = "login";
}
