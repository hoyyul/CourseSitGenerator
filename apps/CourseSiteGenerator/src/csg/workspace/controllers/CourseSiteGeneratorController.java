package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import javafx.scene.control.TextField;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_NAME_TEXT_FIELD;
import csg.data.CourseSiteGeneratorData;
import csg.data.TeachingAssistantPrototype;
import csg.transactions.AddTA_Transaction;


import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_START_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_START_TIME_OPTIONS;
import static csg.CourseSiteGeneratorPropertyType.CSG_EMAIL_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_FOOLPROOF_SETTINGS;
import static csg.CourseSiteGeneratorPropertyType.CSG_LAB_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_LECTURE_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_NAME_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_NO_TA_SELECTED_CONTENT;
import static csg.CourseSiteGeneratorPropertyType.CSG_NO_TA_SELECTED_TITLE;
import static csg.CourseSiteGeneratorPropertyType.CSG_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_RECITATION_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_DATE_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_ITEMS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_LINK_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_TITLE_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_TOPIC_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_TYPE_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_TAS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_TA_EDIT_DIALOG;
import static csg.CourseSiteGeneratorPropertyType.IMAGE_FILE_EXT;
import static csg.CourseSiteGeneratorPropertyType.IMAGE_FILE_EXT_DESC;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.LectureData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.AddLab_Transaction;
import csg.transactions.AddLecture_Transaction;
import csg.transactions.AddRecitation_Transaction;
import csg.transactions.AddSchedule_Transaction;
import csg.transactions.AddTA_Transaction;
import csg.transactions.EditLabDayTime_Transaction;
import csg.transactions.EditLabRoom_Transaction;
import csg.transactions.EditLabSection_Transaction;
import csg.transactions.EditLabTA1_Transaction;
import csg.transactions.EditLabTA2_Transaction;
import csg.transactions.EditLectureDays_Transaction;
import csg.transactions.EditLectureRoom_Transaction;
import csg.transactions.EditLectureSection_Transaction;
import csg.transactions.EditLectureTime_Transaction;
import csg.transactions.EditRecitationDayTime_Transaction;
import csg.transactions.EditRecitationRoom_Transaction;
import csg.transactions.EditRecitationSection_Transaction;
import csg.transactions.EditRecitationTA1_Transaction;
import csg.transactions.EditRecitationTA2_Transaction;
import csg.transactions.EditSchedule_Transaction;
import csg.transactions.EditTA_Transaction;
import csg.transactions.EditTextField_Transaction;
import csg.transactions.RemoveLab_Transaction;
import csg.transactions.RemoveLecture_Transaction;
import csg.transactions.RemoveRecitation_Transaction;
import csg.transactions.RemoveSchedule_Transaction;
import csg.transactions.RemoveTA_Transaction;
import csg.transactions.EditComboBox_Transaction;
import csg.transactions.EditOfficeHoursEndTime_Transaction;
import csg.transactions.EditOfficeHoursStartTime_Transaction;
import csg.transactions.EditScheduleComboBox_Transaction;
import csg.transactions.EditTextArea_Transaction;
import csg.transactions.SetCheckBox_Transaction;
import csg.transactions.SetImage_Transaction;
import csg.transactions.ToggleOfficeHours_Transaction;
import csg.workspace.dialogs.TADialog;
import static djf.AppPropertyType.LOAD_WORK_TITLE;
import static djf.AppTemplate.PATH_WORK;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import properties_manager.PropertiesManager;
/**
 *
 * @author luhaoyu
 */

public class CourseSiteGeneratorController {
    public static final String PATH_IMAGES = "./images";

    CourseSiteGeneratorApp app;

    public CourseSiteGeneratorController(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }
    
    public void processEditTextField(TextField text, String oldText, String newText){
        EditTextField_Transaction t=new EditTextField_Transaction(text,oldText,newText);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditTextField(TextArea text, String oldText, String newText){
        EditTextArea_Transaction t=new EditTextArea_Transaction(text,oldText,newText);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditScheduleComboBox(ComboBox c1,ComboBox c2,Object newTime, Object oldTime){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        EditScheduleComboBox_Transaction t = new EditScheduleComboBox_Transaction(data,gui,c1,c2,newTime,oldTime);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    
    public void processEditComboBox(ComboBox c,Object newTime, Object oldTime){
        
        EditComboBox_Transaction t = new EditComboBox_Transaction(c,newTime,oldTime);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditComboBox(ComboBox c,Object newTime, Object oldTime,String s){
        
        EditComboBox_Transaction t = new EditComboBox_Transaction(c,newTime,oldTime,s);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processSetCheckBox(CheckBox checkBox, boolean selectStatus){
        SetCheckBox_Transaction t = new SetCheckBox_Transaction(checkBox,selectStatus);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    
    public void processAddSchedule() {
        AppGUIModule gui = app.getGUIModule();
        try{
        String type = ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).getValue().toString();
        if(type.equals("Options")) throw new NullPointerException("Option empty");
        String date = ScheduleData.toDateForm(((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).getValue().toString());
        String title = ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).getText();
        String topic = ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).getText();
        String link = ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        ScheduleData schedule = new ScheduleData(type,date,topic.trim(),title.trim(),link);//!!!!!!
        
        AddSchedule_Transaction addScheduleTransaction = new AddSchedule_Transaction(data, schedule);
        app.processTransaction(addScheduleTransaction);

            // NOW CLEAR THE TEXT FIELDS
        ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).setText("");
        ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).setText("");
        ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).setText("");
        ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).setValue("Options");
        ((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).valueProperty().set(null);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
        
        }
        catch(NullPointerException e){
            
        }
    }
    
    public void processRemoveSchedule() {
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleData> scheduleTable =(TableView) gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        if(scheduleTable.getSelectionModel().getSelectedItem()!=null){
        ScheduleData schedule = scheduleTable.getSelectionModel().getSelectedItem();//!!!!!!
        
        RemoveSchedule_Transaction removeScheduleTransaction = new RemoveSchedule_Transaction(data, schedule);
        app.processTransaction(removeScheduleTransaction);

            // NOW CLEAR THE TEXT FIELDS
        ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).setText("");
        ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).setText("");
        ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).setText("");
        ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).setValue("Options");
        ((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).valueProperty().set(null);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
        }
        
    }
    
    public void processEditScheule(){
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleData> scheduleTable =(TableView) gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        try{
        if(scheduleTable.getSelectionModel().getSelectedItem()!=null){
            String type = ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).getValue().toString();
            if(type.equals("Options")) throw new NullPointerException("Option empty");
            String date = ScheduleData.toDateForm(((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).getValue().toString());
            String title = ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).getText();
            String topic = ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).getText();
            String link = ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).getText();
            ScheduleData oldSchedule = scheduleTable.getSelectionModel().getSelectedItem();//!!!!!!
            ScheduleData newSchedule = new ScheduleData(type,date,topic,title,link);

            EditSchedule_Transaction editScheduleTransaction = new EditSchedule_Transaction(app,data,newSchedule,oldSchedule);
            app.processTransaction(editScheduleTransaction);
        }
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
        }catch(NullPointerException e){
            
        }
        
    }
    
        
    
    
    
    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(CSG_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(CSG_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processRemoveTA() {
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        TableView<TeachingAssistantPrototype> taTable = (TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW);
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);        
        TeachingAssistantPrototype ta=taTable.getSelectionModel().getSelectedItem();
        RemoveTA_Transaction removeTATransaction = new RemoveTA_Transaction(app,data,ta);
        app.processTransaction(removeTATransaction);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
        taTable.refresh();
        officeHoursTableView.refresh();
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processAddLecture(){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LectureData> lectureTable = (TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW); 
       LectureData lecture = new LectureData("?","?","?","?");
       AddLecture_Transaction addLectureTransaction = new AddLecture_Transaction(data,lecture);
       app.processTransaction(addLectureTransaction);
       lectureTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processRemoveLecture(){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LectureData> lectureTable = (TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW); 
       LectureData lecture = lectureTable.getSelectionModel().getSelectedItem();
       RemoveLecture_Transaction removeLectureTransaction = new RemoveLecture_Transaction(data,lecture);
       app.processTransaction(removeLectureTransaction);
       lectureTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processAddRecitation(){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = new RecitationData("?","?","?","?","?");
       AddRecitation_Transaction addRecitationTransaction = new AddRecitation_Transaction(data,recitation);
       app.processTransaction(addRecitationTransaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processRemoveRecitation(){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = recitationTable.getSelectionModel().getSelectedItem();
       RemoveRecitation_Transaction removeRecitationTransaction = new RemoveRecitation_Transaction(data,recitation);
       app.processTransaction(removeRecitationTransaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processAddLab(){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> labTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData lab = new LabData("?","?","?","?","?");
       AddLab_Transaction addLabTransaction = new AddLab_Transaction(data,lab);
       app.processTransaction(addLabTransaction);
       labTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processRemoveLab(){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> labTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData lab = labTable.getSelectionModel().getSelectedItem();
       RemoveLab_Transaction removeLabTransaction = new RemoveLab_Transaction(data,lab);
       app.processTransaction(removeLabTransaction);
       labTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLectureSection(String s1,String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LectureData> lectureTable = (TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW); 
       LectureData lecture = (LectureData)(lectureTable.getSelectionModel().getSelectedItem());
       EditLectureSection_Transaction editLectureSectionTransaction = new EditLectureSection_Transaction(app,lecture,s1,s2);
       app.processTransaction(editLectureSectionTransaction);
       lectureTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLectureDays(String s1,String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LectureData> lectureTable = (TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW); 
       LectureData lecture = (LectureData)(lectureTable.getSelectionModel().getSelectedItem());
       EditLectureDays_Transaction editLectureDaysTransaction = new EditLectureDays_Transaction(app,lecture,s1,s2);
       app.processTransaction(editLectureDaysTransaction);
       lectureTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLectureTime(String s1,String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LectureData> lectureTable = (TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW); 
       LectureData lecture = (LectureData)(lectureTable.getSelectionModel().getSelectedItem());
       EditLectureTime_Transaction editLectureTimeTransaction = new EditLectureTime_Transaction(app,lecture,s1,s2);
       app.processTransaction(editLectureTimeTransaction);
       lectureTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLectureRoom(String s1,String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LectureData> lectureTable = (TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW); 
       LectureData lecture = (LectureData)(lectureTable.getSelectionModel().getSelectedItem());
       EditLectureRoom_Transaction editLectureRoomTransaction = new EditLectureRoom_Transaction(app,lecture,s1,s2);
       app.processTransaction(editLectureRoomTransaction);
       lectureTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditRecitationSection(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = (RecitationData)(recitationTable.getSelectionModel().getSelectedItem());
       EditRecitationSection_Transaction editRecitationSectionTransaction = new EditRecitationSection_Transaction(app,recitation,s1,s2);
       app.processTransaction(editRecitationSectionTransaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditRecitationDayTime(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = (RecitationData)(recitationTable.getSelectionModel().getSelectedItem());
       EditRecitationDayTime_Transaction editRecitationDayTimeTransaction= new EditRecitationDayTime_Transaction(app,recitation,s1,s2);
       app.processTransaction(editRecitationDayTimeTransaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditRecitationRoom(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = (RecitationData)(recitationTable.getSelectionModel().getSelectedItem());
       EditRecitationRoom_Transaction editRecitationRoomTransaction= new EditRecitationRoom_Transaction(app,recitation,s1,s2);
       app.processTransaction(editRecitationRoomTransaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditRecitationTA1(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = (RecitationData)(recitationTable.getSelectionModel().getSelectedItem());
       EditRecitationTA1_Transaction editRecitationTA1Transaction= new EditRecitationTA1_Transaction(app,recitation,s1,s2);
       app.processTransaction(editRecitationTA1Transaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditRecitationTA2(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<RecitationData> recitationTable = (TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW); 
       RecitationData recitation = (RecitationData)(recitationTable.getSelectionModel().getSelectedItem());
       EditRecitationTA2_Transaction editRecitationTA2Transaction= new EditRecitationTA2_Transaction(app,recitation,s1,s2);
       app.processTransaction(editRecitationTA2Transaction);
       recitationTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLabSection(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> LabTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData Lab = (LabData)(LabTable.getSelectionModel().getSelectedItem());
       EditLabSection_Transaction editLabSectionTransaction = new EditLabSection_Transaction(app,Lab,s1,s2);
       app.processTransaction(editLabSectionTransaction);
       LabTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLabDayTime(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> LabTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData Lab = (LabData)(LabTable.getSelectionModel().getSelectedItem());
       EditLabDayTime_Transaction editLabDayTimeTransaction= new EditLabDayTime_Transaction(app,Lab,s1,s2);
       app.processTransaction(editLabDayTimeTransaction);
       LabTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLabRoom(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> LabTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData Lab = (LabData)(LabTable.getSelectionModel().getSelectedItem());
       EditLabRoom_Transaction editLabRoomTransaction= new EditLabRoom_Transaction(app,Lab,s1,s2);
       app.processTransaction(editLabRoomTransaction);
       LabTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLabTA1(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> LabTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData Lab = (LabData)(LabTable.getSelectionModel().getSelectedItem());
       EditLabTA1_Transaction editLabTA1Transaction= new EditLabTA1_Transaction(app,Lab,s1,s2);
       app.processTransaction(editLabTA1Transaction);
       LabTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processEditLabTA2(String s1, String s2){
       AppGUIModule gui = app.getGUIModule();
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       TableView<LabData> LabTable = (TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW); 
       LabData Lab= (LabData)(LabTable.getSelectionModel().getSelectedItem());
       EditLabTA2_Transaction editLabTA2Transaction= new EditLabTA2_Transaction(app,Lab,s1,s2);
       app.processTransaction(editLabTA2Transaction);
       LabTable.refresh();
       app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(CSG_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, CSG_NO_TA_SELECTED_TITLE, CSG_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(CSG_TA_EDIT_DIALOG);
            System.out.print(taDialog);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }

    public void processSelectAllTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
    
    public static File showImageSelectDialog(Stage window) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        File selectedFile = fc.showOpenDialog(window);
        return selectedFile;
    }
    
    public void processSetImage(AppGUIModule gui,File newFile,String s){
        SetImage_Transaction t = new SetImage_Transaction(gui,newFile,s);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processOfficeHourStartTimeTransaction(ComboBox c,Object newStartTime,Object oldStartTime,Object endTime){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorController controller = new CourseSiteGeneratorController((CourseSiteGeneratorApp) app);
        CourseSiteGeneratorData data=((CourseSiteGeneratorData)app.getDataComponent());
        EditOfficeHoursStartTime_Transaction t = new EditOfficeHoursStartTime_Transaction(c,newStartTime,oldStartTime,endTime,data,gui,controller);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
    public void processOfficeHourEndTimeTransaction(ComboBox c,Object newEndTime,Object oldEndTime,Object startTime){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorController controller = new CourseSiteGeneratorController((CourseSiteGeneratorApp) app);
        CourseSiteGeneratorData data=((CourseSiteGeneratorData)app.getDataComponent());
        EditOfficeHoursEndTime_Transaction t = new EditOfficeHoursEndTime_Transaction(c,newEndTime,oldEndTime,startTime,data,gui,controller);
        app.processTransaction(t);
        app.getFoolproofModule().updateControls(CSG_FOOLPROOF_SETTINGS);
    }
    
}