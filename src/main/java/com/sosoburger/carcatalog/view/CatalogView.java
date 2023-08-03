package com.sosoburger.carcatalog.view;

import com.sosoburger.carcatalog.component.CreationDialog;
import com.sosoburger.carcatalog.component.EditingDialog;
import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route("")
public class CatalogView extends VerticalLayout {

    private List<CarDAO> carList;
    @Autowired
    CarService carService;

    Grid<CarDAO> grid;

    TextField brandField;
    TextField modelField;
    TextField categoryField;
    TextField numberField;
    TextField releaseYearField;
    Select<String> typeField;
    Select<String> trailerField;


    CatalogView(CarService carService) {
        //Создание таблицы
        grid = new Grid<>(CarDAO.class, false);
        grid.addColumn(CarDAO::getBrand).setHeader("Марка").setSortable(true);
        grid.addColumn(CarDAO::getModel).setHeader("Модель").setSortable(true);
        grid.addColumn(CarDAO::getCategory).setHeader("Категория").setSortable(true);
        grid.addColumn(CarDAO::getNumber).setHeader("Гос. номер").setSortable(true);
        grid.addColumn(CarDAO::getReleaseYear).setHeader("Дата выпуска").setSortable(true);
        grid.addColumn(CarDAO::getType).setHeader("Тип ТС").setSortable(true);
        grid.addColumn(CarDAO::getTrailer).setHeader("Наличие прицепа").setSortable(true);

        carList = carService.getAll();
        grid.setItems(carList);

        //контекстное меню для таблицы
        CarContextMenu contextMenu = new CarContextMenu(grid, carService, carList);

        //Кнопка для создания ТС
        this.carService = carService;
        Button addButton = new Button("Создать");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(event ->
                CreationDialog.getCreationDialog(carService, grid, carList).open()
        );

        //Фильтры
        HorizontalLayout filtersLayout = new HorizontalLayout();

        brandField = new TextField("Марка");

        modelField = new TextField("Модель");

        categoryField = new TextField("Категория");

        numberField = new TextField("Государственный номер");

        releaseYearField = new TextField("Год выпуска");

        typeField = new Select<>();
        typeField.setLabel("Тип ТС");
        typeField.setItems("", "Легковой", "Автобус", "Грузовой");
        typeField.setValue("");
        typeField.addValueChangeListener(e->find());
        typeField.setWidth("14%");

        trailerField = new Select<>();
        trailerField.setLabel("Наличие прицепа");
        trailerField.setItems("", "Есть", "Нет");
        trailerField.setWidth("auto");
        trailerField.setValue("");
        trailerField.addValueChangeListener(e->find());
        trailerField.setWidth("14%");

        List<TextField> fields = new ArrayList<>();
        Collections.addAll(fields,
                brandField,
                modelField,
                categoryField,
                numberField,
                releaseYearField);
        for (var item:fields
             ) {
            item.setValueChangeMode(ValueChangeMode.LAZY);
            item.addValueChangeListener(e->find());
            item.setWidth("14%");
            filtersLayout.add(item);
        }

        filtersLayout.add(typeField, trailerField);

        Scroller scroller = new Scroller();
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.setContent(filtersLayout);
        scroller.setMaxWidth("100%");
        scroller.setWidth("100%");

        add(addButton, grid, scroller);
    }

    private void find() {
        carList = carService.findBy(
                modelField.getValue(),
                brandField.getValue(),
                categoryField.getValue(),
                numberField.getValue(),
                releaseYearField.getValue(),
                typeField.getValue(),
                trailerField.getValue()
        );
        grid.setItems(carList);
    }

    private static class CarContextMenu extends GridContextMenu<CarDAO> {
        public CarContextMenu(Grid<CarDAO> target, CarService carService, List<CarDAO> carList) {
            super(target);

            addItem("Редактировать", e -> e.getItem().ifPresent(car -> EditingDialog.getCreationDialog(carService, car, target).open()));
            addItem("Удалить", e -> e.getItem().ifPresent(car -> {
                carList.remove(car);
                carService.delete(car);
                target.getDataProvider().refreshAll();
                close();
            }));
        }
    }
}
