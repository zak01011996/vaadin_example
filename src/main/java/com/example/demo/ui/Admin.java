package com.example.demo.ui;

import com.example.demo.model.Lecturer;
import com.example.demo.service.DataService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("admin")
@Theme(Lumo.class)
@HtmlImport("styles/shared-styles.html")
public class Admin extends VerticalLayout {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DataService ds;

    private Grid<Lecturer> lecturerGrid = new Grid<>();

    public Admin(DataService ds) {
        this.ds = ds;

        setHeightFull();

        Image logo = new Image("frontend/images/logo.png", "");
        logo.getStyle().set("margin", "20px");
        add(logo);


        lecturerGrid.setHeightFull();
        lecturerGrid.removeAllColumns();
        lecturerGrid.addColumn(new ComponentRenderer<>(data -> {
            Image img = new Image(data.getImgSrc(), "");
            img.setWidth("100px");
            img.setWidth("200px");
            return img;
        })).setHeader("Image");

        lecturerGrid.addColumn(Lecturer::getId).setHeader("Id").setSortable(true);

        lecturerGrid.addColumn(Lecturer::getName).setHeader("Name");

        lecturerGrid.addColumn(new ComponentRenderer<>(data -> {
            return new Html("<span class='score'>" + data.getScore() + "</span>");
        })).setHeader("Total Score").setSortable(true);

        updateGrid();

        HorizontalLayout form = createForm();

        add(form, lecturerGrid);

        setHorizontalComponentAlignment(Alignment.CENTER, logo);
    }

    private HorizontalLayout createForm() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.addClassName("form_layout");

        Binder<Lecturer> binder = new Binder<>(Lecturer.class);

        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("21em", 2),
                new FormLayout.ResponsiveStep("22em", 3));

        TextField name = new TextField();
        name.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        name.setPlaceholder("Name");
        name.setLabel("Fill lecturer name");

        TextField imageSrc = new TextField();
        imageSrc.setLabel("Image URL");
        imageSrc.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        imageSrc.setPlaceholder("Paste image URL");

        Button saveBtn = new Button("Save");
        saveBtn.addClickListener(event -> {
            Lecturer data = new Lecturer();
            if(binder.writeBeanIfValid(data)) {
                ds.saveLecturer(data);

                name.clear();
                imageSrc.clear();

                updateGrid();
            }
        });

        form.add(name, imageSrc, saveBtn);

        binder.forField(name).withValidator(
                new StringLengthValidator("Name cannot be empty", 1, null)
        ).bind(
                Lecturer::getName,
                Lecturer::setName
        );

        binder.forField(imageSrc).withValidator(
                new RegexpValidator("Bad URL", "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
        ).bind(
                Lecturer::getImgSrc,
                Lecturer::setImgSrc
        );

        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setWidth("50%");
        formLayout.getStyle().set("padding", "15px");
        formLayout.add(form);

        layout.add(formLayout);

        return layout;
    }


    private void updateGrid() {
        lecturerGrid.setItems(ds.findAllLecturers());
        lecturerGrid.getDataProvider().refreshAll();
    }
}
