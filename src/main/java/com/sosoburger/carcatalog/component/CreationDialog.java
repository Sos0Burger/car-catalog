package com.sosoburger.carcatalog.component;

import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.exception.AlreadyExists;
import com.sosoburger.carcatalog.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CreationDialog {

    public static Dialog getCreationDialog(CarService carService) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Новый автомобиль");
        dialog.setModal(true);

        Binder<CarDAO> binder = new Binder<>();

        VerticalLayout dialogLayout = new VerticalLayout();

        TextField brandField = new TextField("Марка");
        binder.forField(brandField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getBrand, CarDAO::setBrand);

        TextField modelField = new TextField("Модель");
        binder.forField(modelField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getModel, CarDAO::setModel);

        TextField categoryField = new TextField("Категория");
        binder.forField(categoryField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getCategory, CarDAO::setCategory);

        TextField numberField = new TextField("Государственный номер");
        binder.forField(numberField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getNumber, CarDAO::setNumber);

        TextField releaseYearField = new TextField("Год выпуска");
        binder.forField(releaseYearField)
                .withValidator(
                        releaseYear -> releaseYear.length() == 4,
                        "Поле должно содержать 4 символа"
                )
                .withValidator(releaseYear-> releaseYear.matches("\\d{4}"),
                        "Число должно быть целым, например 2001")
                .bind(CarDAO::getReleaseYear, CarDAO::setReleaseYear);

        Select<String> typeField = new Select<>();
        typeField.setLabel("Тип ТС");
        binder.forField(typeField)
                .asRequired("Поле должно быть заполнено")
                .bind(CarDAO::getType, CarDAO::setType);
        typeField.setItems("Легковой", "Автобус", "Грузовой");

        dialogLayout.add(brandField, modelField, categoryField, numberField, releaseYearField, typeField);

        //Кнопка отмены
        Button cancelButton = new Button("Отменить", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        //Кнопка сохранения
        Button saveButton = new Button("Сохранить", e -> {
            try {
                carService.save(new CarDAO(
                        null,
                        brandField.getValue(),
                        modelField.getValue(),
                        categoryField.getValue(),
                        numberField.getValue(),
                        releaseYearField.getValue(),
                        typeField.getValue()));
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

        dialog.getFooter().add(saveButton, cancelButton);


        dialog.add(dialogLayout);
        return dialog;
    }
}
