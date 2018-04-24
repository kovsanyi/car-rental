package hu.unideb.inf.carrental.ui.commons.content.car;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import hu.unideb.inf.carrental.ui.commons.content.root.CarRentalContent;

public abstract class CarsContent extends CarRentalContent {

    public CarsContent(String titleValue) {
        super(titleValue);
    }

    @Override
    protected GridLayout getBody() {
        return (GridLayout) super.getBody();
    }

    @Override
    protected GridLayout buildBody() {
        final GridLayout body = new GridLayout();
        body.setMargin(false);
        body.setSpacing(true);
        body.setSizeFull();
        body.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        body.setColumns(2);
        return body;
    }
}
