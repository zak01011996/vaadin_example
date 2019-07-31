package com.example.demo.ui;

import com.example.demo.model.Lecturer;
import com.example.demo.service.DataService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

@Route
@Theme(Lumo.class)
@HtmlImport("styles/shared-styles.html")
public class Main extends VerticalLayout {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DataService ds;

    private Grid<Lecturer> lecturerGrid = new Grid<>();

    public Main(DataService ds) {
        this.ds = ds;

        setHeightFull();

        Image logo = new Image("frontend/images/logo.png", "");
        logo.getStyle().set("margin", "20px");
        add(logo);

        lecturerGrid.setHeightFull();
        lecturerGrid.setMinWidth("500px");
        lecturerGrid.removeAllColumns();
        lecturerGrid.addColumn(new ComponentRenderer<>(data -> {
            Image img = new Image(data.getImgSrc(), "");
            img.setWidth("100px");
            img.setWidth("200px");
            return img;
        })).setHeader("Image");

        lecturerGrid.addColumn(Lecturer::getName).setHeader("Name");

        lecturerGrid.addColumn(new ComponentRenderer<>(data -> {
            Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

            Optional<Cookie> voteHist = Arrays.stream(cookies).filter(v -> v.getName().equals(data.getuId())).findFirst();

            if(voteHist.isPresent()) {
                return new Html("<span class='score'>" + voteHist.get().getValue() + "</span>");
            } else {
                HorizontalLayout layout = new HorizontalLayout();

                for (int i = 1; i <= 5; i++) {
                    Long score = (long) i;
                    Button btn = new Button(Integer.toString(i));
                    btn.addClassName("success");
                    btn.addClickListener(event -> {
                        data.increaseScore(score);
                        ds.saveLecturer(data);

                        saveCookie(data.getuId(), score.toString());

                        // TODO: Detect, why cookie works so slow...
                        getUI().get().getCurrent().getPage().reload();
                    });

                    layout.add(btn);
                }

                return layout;
            }
        })).setHeader("Vote");

        updateGrid();


        add(lecturerGrid);

        setHorizontalComponentAlignment(Alignment.CENTER, logo);
    }

    private void saveCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(1024 * 1024);
        cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    private void updateGrid() {
        lecturerGrid.setItems(ds.findAllLecturers());
        lecturerGrid.getDataProvider().refreshAll();
    }
}
