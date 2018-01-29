package hu.unideb.inf.carrental.ui.login;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.signup.SignUpView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.showNotification;

@UIScope
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {

    @Autowired
    public LoginView(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setMargin(false);
        setSpacing(false);
        setSizeFull();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("loginview");
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(false);
        content.setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        content.setHeight(100.f, Unit.PERCENTAGE);

        content.addComponents(
                buildLabels(),
                buildLogin()
        );
        return content;
    }

    private AbstractOrderedLayout buildLabels() {
        final VerticalLayout labelsLayout = new VerticalLayout();
        labelsLayout.setWidth(100.f, Unit.PERCENTAGE);
        labelsLayout.setHeightUndefined();
        labelsLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        final Label title = new Label("Car Rental");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        final Label bio = new Label("The easiest way to rent a car");
        bio.addStyleName(ValoTheme.LABEL_H2);
        bio.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        labelsLayout.addComponents(
                title,
                bio
        );
        return labelsLayout;
    }

    private AbstractOrderedLayout buildLogin() {
        final VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setSizeFull();
        loginLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        loginLayout.addComponent(buildLoginPanel());
        return loginLayout;
    }

    private Panel buildLoginPanel() {
        final Panel panel = new Panel();
        panel.setSizeUndefined();
        panel.setCaption("Login");

        final HorizontalLayout loginLayout = new HorizontalLayout();
        loginLayout.setSizeUndefined();
        loginLayout.setMargin(false);

        username = new TextField("Username");
        password = new PasswordField("Password");

        loginLayout.addComponent(username);
        loginLayout.addComponent(password);

        final Component login = buildLoginButton();
        loginLayout.addComponent(login);
        loginLayout.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);

        final VerticalLayout panelContent = new VerticalLayout();
        panelContent.addComponents(
                loginLayout,
                buildSignUp()
        );

        panel.setContent(panelContent);
        return panel;
    }

    private Component buildSignUp() {
        final HorizontalLayout signUpLayout = new HorizontalLayout();
        signUpLayout.setMargin(false);
        signUpLayout.setSizeUndefined();

        signUpLayout.addComponents(
                new Label("Are you not a member?"),
                new Link("Sign up now!", new ExternalResource("#!" + SignUpView.VIEW_NAME))
        );

        return signUpLayout;
    }

    private Button buildLoginButton() {
        final Button login = new Button("Login");
        login.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addClickListener(e -> {
            try {
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(username.getValue(), password.getValue());
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                CarRentalUI.getCurrent().getPage().reload();
            } catch (BadCredentialsException ex) {
                password.clear();
                showNotification("Wrong username or password!", Notification.Type.WARNING_MESSAGE);
            }
        });
        login.focus();

        return login;
    }

    private TextField username;
    private PasswordField password;

    private final AuthenticationManager authenticationManager;

    public static final String VIEW_NAME = "login";
}
