package csg.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_ADD_TA_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_END_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_START_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_EMAIL_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_END_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_NAME_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_DATE_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_START_TIME_COMBOBOX;
import csg.data.CourseSiteGeneratorData;
import csg.data.ScheduleData;
import static csg.workspace.style.CSGStyle.CLASS_CSG_TEXT_FIELD;
import static csg.workspace.style.CSGStyle.CLASS_CSG_TEXT_FIELD_ERROR;
import static djf.modules.AppGUIModule.DISABLED;
import static djf.modules.AppGUIModule.ENABLED;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableView;
/**
 *
 * @author luhaoyu
 */
public class CourseSiteGeneratorFoolproofDesign implements FoolproofDesign {

    CourseSiteGeneratorApp app;
    AppGUIModule gui;
    ComboBox startTimeComboBox;
    ComboBox endTimeComboBox;
    ComboBox startMondayComboBox;
    ComboBox endFridayComboBox;
    ComboBox scheduleDateComboBox;
    
    public CourseSiteGeneratorFoolproofDesign(CourseSiteGeneratorApp initApp) {
        app = initApp;
        gui=app.getGUIModule();
        startTimeComboBox = (ComboBox) gui.getGUINode(CSG_START_TIME_COMBOBOX);
        endTimeComboBox = (ComboBox) gui.getGUINode(CSG_END_TIME_COMBOBOX);
        startMondayComboBox =(ComboBox) gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX);
        endFridayComboBox =(ComboBox) gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX);
        scheduleDateComboBox =(ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX);
    }

    @Override
    public void updateControls() {
        updateAddTAFoolproofDesign();
        updateEditTAFoolproofDesign();
        updataTimeRangeFoolproofDesign();
    }
    private void updataTimeRangeFoolproofDesign(){
        startTimeComboBox.setCellFactory(lv->new StartHoursCell());
        endTimeComboBox.setCellFactory(lv->new EndHoursCell());
        startMondayComboBox.setCellFactory(lv->new StartMondayCell());
        endFridayComboBox.setCellFactory(lv->new EndFridayCell());
        scheduleDateComboBox.setCellFactory(lv->new ScheduleDateCell());
        
        
        ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        
    }
    
    
    
    private void updateAddTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        // FOOLPROOF DESIGN STUFF FOR ADD TA BUTTON
        TextField nameTextField = ((TextField) gui.getGUINode(CSG_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(CSG_EMAIL_TEXT_FIELD));
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        Button addTAButton = (Button) gui.getGUINode(CSG_ADD_TA_BUTTON);

        // FIRST, IF NO TYPE IS SELECTED WE'LL JUST DISABLE
        // THE CONTROLS AND BE DONE WITH IT
        boolean isTypeSelected = data.isTATypeSelected();
        if (!isTypeSelected) {
            nameTextField.setDisable(true);
            emailTextField.setDisable(true);
            addTAButton.setDisable(true);
            return;
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            nameTextField.setDisable(false);
            emailTextField.setDisable(false);
            addTAButton.setDisable(false);
        }

        // NOW, IS THE USER-ENTERED DATA GOOD?
        boolean isLegalNewTA = data.isLegalNewTA(name, email);

        // ENABLE/DISABLE THE CONTROLS APPROPRIATELY
        addTAButton.setDisable(!isLegalNewTA);
        if (isLegalNewTA) {
            nameTextField.setOnAction(addTAButton.getOnAction());
            emailTextField.setOnAction(addTAButton.getOnAction());
        } else {
            nameTextField.setOnAction(null);
            emailTextField.setOnAction(null);
        }

        // UPDATE THE CONTROL TEXT DISPLAY APPROPRIATELY
        boolean isLegalNewName = data.isLegalNewName(name);
        boolean isLegalNewEmail = data.isLegalNewEmail(email);
        foolproofTextField(nameTextField, isLegalNewName);
        foolproofTextField(emailTextField, isLegalNewEmail);
    }
    
    private void updateEditTAFoolproofDesign() {
        
    }
    
    public void foolproofTextField(TextField textField, boolean hasLegalData) {
        if (hasLegalData) {
            textField.getStyleClass().remove(CLASS_CSG_TEXT_FIELD_ERROR);
            if (!textField.getStyleClass().contains(CLASS_CSG_TEXT_FIELD)) {
                textField.getStyleClass().add(CLASS_CSG_TEXT_FIELD);
            }
        } else {
            textField.getStyleClass().remove(CLASS_CSG_TEXT_FIELD);
            if (!textField.getStyleClass().contains(CLASS_CSG_TEXT_FIELD_ERROR)) {
                textField.getStyleClass().add(CLASS_CSG_TEXT_FIELD_ERROR);
            }
        }
    }
    
    private void initStartMondayComboBox(ComboBox c,LocalDate date){
        Year year = Year.now();
        
        while (date.getYear() == year.getValue()) {
            date = date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            if(date.getYear() == year.getValue())
            c.getItems().add(date);
        }
        
    }
    
    private void initEndFridayComboBox(ComboBox c,LocalDate date){
        Year year = Year.now();
        while (date.getYear() <= year.getValue()+1) {
            date = date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            if(date.getYear() <= year.getValue()+1)
            c.getItems().add(date);
        }
    }
    
    private int getTimeInt(String time){
        String slot=time.substring(time.length()-2, time.length());
        int hour=Integer.parseInt(time.substring(0, time.length()-5));
        if(slot.equals("am"))
            return hour;
        else if(hour==12)
            return 12;
        else
            return hour+12;
    }
    
    private class StartHoursCell extends ListCell<String>{
        StartHoursCell() {
            endTimeComboBox.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
        }
        
        @Override
        protected void updateItem(String hours, boolean empty) {
            super.updateItem(hours, empty);
            if (empty) {
                setText(null);
            } else {
                setText(hours);
                updateDisableState();
            }
        }
        
        private void updateDisableState() {
            boolean disable = getItem() != null && endTimeComboBox.getValue() != null && 
                    getTimeInt(getItem()) > getTimeInt(endTimeComboBox.getValue().toString());
            setDisable(disable) ;
            setOpacity(disable ? 0.5 : 1);
        }
        
        
    }
    
    private class EndHoursCell extends ListCell<String> {

        EndHoursCell() {
            startTimeComboBox.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
        }

        @Override
        protected void updateItem(String hours, boolean empty) {
            super.updateItem(hours, empty);
            if (empty) {
                setText(null);
            } else {
                setText(hours.toString());
                updateDisableState();
            }
        }

        private void updateDisableState() {
            boolean disable = getItem() != null && startTimeComboBox.getValue() != null && 
                    getTimeInt(getItem()) < getTimeInt(startTimeComboBox.getValue().toString());
            setDisable(disable) ;
            setOpacity(disable ? 0.5 : 1);

        }
    }
    
    private class StartMondayCell extends ListCell<String>{
        StartMondayCell() {
            endFridayComboBox.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
        }
        
        @Override
        protected void updateItem(String dates, boolean empty) {
            super.updateItem(dates, empty);
            if (empty) {
                setText(null);
            } else {
                setText(dates);
                updateDisableState();
            }
        }
        
        private void updateDisableState() {
            try{
            boolean disable = getItem() != null && endFridayComboBox.getValue() != null && 
                    LocalDate.parse(ScheduleData.toDateForm(getItem())).compareTo(LocalDate.parse(ScheduleData.toDateForm(endFridayComboBox.getValue().toString())))>0;
            setDisable(disable) ;
            setOpacity(disable ? 0.5 : 1);
            }
            catch(Exception e){
                
            }
        }
        
        
    }
    
    private class EndFridayCell extends ListCell<String> {

        EndFridayCell() {
            startMondayComboBox.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
        }

        @Override
        protected void updateItem(String date, boolean empty) {
            super.updateItem(date, empty);
            if (empty) {
                setText(null);
            } else {
                setText(date);
                updateDisableState();
            }
        }

        private void updateDisableState() {
            try{
            boolean disable = getItem() != null && startMondayComboBox.getValue() != null && 
                   LocalDate.parse(ScheduleData.toDateForm(startMondayComboBox.getValue().toString())).compareTo(LocalDate.parse(ScheduleData.toDateForm(getItem())))>0;
            setDisable(disable) ;
            setOpacity(disable ? 0.5 : 1);
            }
            catch(Exception e){
                
            }
        }
    }
    
    private class ScheduleDateCell extends ListCell<String> {

        ScheduleDateCell() {
            scheduleDateComboBox.valueProperty().addListener((obs, oldEndHours, newEndHours) -> updateDisableState());
        }

        @Override
        protected void updateItem(String date, boolean empty) {
            super.updateItem(date, empty);
            if (empty) {
                setText(null);
            } else {
                setText(date);
                updateDisableState();
            }
        }

        private void updateDisableState() {
            boolean disable = getItem() != null && startMondayComboBox.getValue() != null && endFridayComboBox.getValue() != null&&(
                   LocalDate.parse(ScheduleData.toDateForm(getItem())).compareTo(LocalDate.parse(ScheduleData.toDateForm(startMondayComboBox.getValue().toString())))<0||LocalDate.parse(ScheduleData.toDateForm(getItem())).compareTo(LocalDate.parse(ScheduleData.toDateForm(endFridayComboBox.getValue().toString())))>0);
            setDisable(disable) ;
            setOpacity(disable ? 0.5 : 1);

        }
    }
}