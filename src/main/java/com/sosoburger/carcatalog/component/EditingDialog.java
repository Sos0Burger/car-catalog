package com.sosoburger.carcatalog.component;

import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.exception.AlreadyExists;
import com.sosoburger.carcatalog.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class EditingDialog {
    public static Dialog getCreationDialog(CarService carService, CarDAO carDAO, Grid<CarDAO> grid) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Редактировать ТС");
        dialog.setModal(true);

        Binder<CarDAO> binder = new Binder<>();

        VerticalLayout dialogLayout = new VerticalLayout();

        //Поля для заполнения и их валидация
        TextField brandField = new TextField("Марка");
        brandField.setValue(carDAO.getBrand());
        binder.forField(brandField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getBrand, CarDAO::setBrand);

        TextField modelField = new TextField("Модель");
        modelField.setValue(carDAO.getModel());
        binder.forField(modelField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getModel, CarDAO::setModel);

        TextField categoryField = new TextField("Категория");
        categoryField.setValue(carDAO.getCategory());
        binder.forField(categoryField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getCategory, CarDAO::setCategory);

        TextField numberField = new TextField("Государственный номер");
        numberField.setValue(carDAO.getNumber());
        binder.forField(numberField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getNumber, CarDAO::setNumber);

        TextField releaseYearField = new TextField("Год выпуска");
        releaseYearField.setValue(carDAO.getReleaseYear());
        binder.forField(releaseYearField)
                .withValidator(
                        releaseYear -> releaseYear.length() == 4,
                        "Поле должно содержать 4 символа"
                )
                .withValidator(releaseYear -> releaseYear.matches("\\d{4}"),
                        "Число должно быть целым, например 2001")
                .bind(CarDAO::getReleaseYear, CarDAO::setReleaseYear);

        Select<String> typeField = new Select<>();
        typeField.setLabel("Тип ТС");
        binder.forField(typeField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getType, CarDAO::setType);
        typeField.setItems("Легковой", "Автобус", "Грузовой");
        typeField.setValue(carDAO.getType());

        Select<String> trailerField = new Select<>();
        trailerField.setLabel("Наличие прицепа");
        binder.forField(trailerField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getTrailer, CarDAO::setTrailer);
        trailerField.setItems("Есть", "Нет");
        trailerField.setValue(carDAO.getTrailer());

        dialogLayout.add(brandField, modelField, categoryField, numberField, releaseYearField, typeField, trailerField);

        //Кнопка отмены
        Button cancelButton = new Button("Закрыть", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        //Кнопка сохранения
        Button saveButton = new Button("Сохранить", e -> {
            try {
                carDAO.setBrand(brandField.getValue());
                carDAO.setModel(modelField.getValue());
                carDAO.setCategory(categoryField.getValue());
                carDAO.setNumber(numberField.getValue());
                carDAO.setReleaseYear(releaseYearField.getValue());
                carDAO.setType(typeField.getValue());
                carDAO.setTrailer(trailerField.getValue());
                carService.save(carDAO);

                grid.getDataProvider().refreshAll();

                dialog.close();
                Notification notification = Notification
                        .show("ТС успешно сохранено", 2000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (AlreadyExists ex) {
                Notification notification = Notification
                        .show("Такой номер уже существует", 1500, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveButton.setEnabled(false);
        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(binder.isValid()));

        dialog.getFooter().add(saveButton, cancelButton);


        dialog.add(dialogLayout);
        return dialog;
    }
}
