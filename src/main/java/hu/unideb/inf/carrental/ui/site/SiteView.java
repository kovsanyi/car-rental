package hu.unideb.inf.carrental.ui.site;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = SiteView.VIEW_NAME)
public class SiteView extends VerticalLayout implements View {

    public SiteView() {
    }

    public static final String VIEW_NAME = "site";
}
