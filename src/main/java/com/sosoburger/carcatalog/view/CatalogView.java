package com.sosoburger.carcatalog.view;

import com.sosoburger.carcatalog.component.CreationDialog;
import com.sosoburger.carcatalog.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class CatalogView extends VerticalLayout {

    @Autowired
    CarService carService;
    CatalogView(CarService carService){
        this.carService = carService;
        Dialog creationDialog = CreationDialog.getCreationDialog(carService);
        Button button = new Button("Создать");
        button.addClickListener(event-> creationDialog.open());

        add(button, creationDialog);
    }
}
