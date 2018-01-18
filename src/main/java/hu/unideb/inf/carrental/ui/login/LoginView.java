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
        wrapper.setMargin(false);
        wrapper.setWidth(1366.f, Unit.PIXELS);
        wrapper.setHeightUndefined();
        wrapper.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        wrapper.addComponent(buildContent());

        addComponent(wrapper);
        setComponentAlignment(wrapper, Alignment.MIDDLE_CENTER);
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.setMargin(false);

        content.addComponent(buildLoginForm());
        content.addComponent(buildSignUp());

        return content;
    }

    private AbstractLayout buildLoginForm() {
        final HorizontalLayout loginLayout = new HorizontalLayout();
        loginLayout.setMargin(false);
        loginLayout.setSizeUndefined();

        username = new TextField("Username");
        password = new PasswordField("Password");

        loginLayout.addComponent(username);
        loginLayout.addComponent(password);

        Component login = buildLoginButton();
        loginLayout.addComponent(login);
        loginLayout.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);

        return loginLayout;
    }

    private Component buildLoginButton() {
        final Button login = new Button("Login");
        login.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
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
        login.focus();

        return login;
    }

    private Component buildSignUp() {
        final HorizontalLayout signUpLayout = new HorizontalLayout();
        signUpLayout.setMargin(false);
        signUpLayout.setSizeUndefined();

        signUpLayout.addComponent(new Label("Are you not a member?"));
        signUpLayout.addComponent(new Link("Sign up now!", new ExternalResource("#!" + SignUpView.VIEW_NAME)));

        return signUpLayout;
    }

    private TextField username;
    private PasswordField password;

    private final AuthenticationManager authenticationManager;

    public static final String VIEW_NAME = "login";
}
