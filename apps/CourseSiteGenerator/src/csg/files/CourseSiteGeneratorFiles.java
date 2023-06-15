package csg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_ACADEMIC_DISHONESTY_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_ACADEMIC_DISHONESTY_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_END_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_START_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_DESCRIPTION_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_DESCRIPTION_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_EMAIL_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_END_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_GRADED_COMPONENTS_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_GRADED_COMPONENTS_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_GRADING_NOTE_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_GRADING_NOTE_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_HOMEPAGE_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_HOME_CHECKBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_HWS_CHECKBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_INSTRUCTOR_OFFICEHOUR_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_LAB_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_LECTURE_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_NAME_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_NUMBER_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_OUTCOMES_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_OUTCOMES_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_PREREQUISITES_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_PREREQUISITES_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_RECITATION_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_ROOM_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_CHECKBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_ITEMS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_SEMESTER_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_SPECIAL_ASSISTANCE_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_SPECIAL_ASSISTANCE_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_START_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_STYLE_PANE;
import static csg.CourseSiteGeneratorPropertyType.CSG_SUBJECT_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_SYLLABUS_CHECKBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_TEXTBOOKS_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_TEXTBOOKS_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_TITLE_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_TOPICS_ADDBUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_TOPICS_TEXTAREA;
import static csg.CourseSiteGeneratorPropertyType.CSG_YEAR_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.FILE_ERROR_TITLE;
import static csg.CourseSiteGeneratorPropertyType.FILE_LACK_ERROR_CONTENT;
import csg.data.LabData;
import csg.data.LectureData;
import csg.data.CourseSiteGeneratorData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.SetImage_Transaction;
import csg.workspace.CourseSiteGeneratorWorkspace;
import static djf.AppPropertyType.APP_EXPORT_PATH;
import static djf.AppPropertyType.CURRENT_CSS;

import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.dialogs.AppDialogsFacade;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.json.JsonObjectBuilder;
import org.apache.commons.io.FileUtils;
import properties_manager.PropertiesManager;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author haoyu lu
 */
public class CourseSiteGeneratorFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CourseSiteGeneratorApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_PAGE = "pages";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_ROOM = "room";
    static final String JSON_LINK = "link";
    static final String JSON_HOURS = "hours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_LOGOS = "logos";
    static final String JSON_HREF = "href";
    static final String JSON_SRC = "src";
    static final String JSON_FAVICON = "favicon";
    static final String JSON_NAVBAR = "navbar";
    static final String JSON_BOTTOM_LEFT = "bottom_left";
    static final String JSON_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_TOPICS = "topics";
    static final String JSON_TOPIC = "topic";
    static final String JSON_PREREQUISITES = "prerequisites";
    static final String JSON_OUTCOMES = "outcomes";
    static final String JSON_TEXTBOOKS = "textbooks";
    static final String JSON_GRADED_COMPONENTS = "gradedComponents";
    static final String JSON_GRADING_NOTE = "gradingNote";
    static final String JSON_ACADEMIC_DISHONESTY = "academicDishonesty";
    static final String JSON_SPECIAL_ASSISTANCE = "specialAssistance";
    static final String JSON_PHOTO = "photo";
    static final String JSON_AUTHORS = "authors";
    static final String JSON_PUBLISHER = "publisher";
    static final String JSON_LECTURES = "lectures";
    static final String JSON_LABS = "labs";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_SECTION_LECTURES = "sectionLectures";
    static final String JSON_SECTION_LABS = "sectionLabs";
    static final String JSON_SECTION_RECITATIONS = "sectionRecitations";
    static final String JSON_SECTION = "section";
    static final String JSON_DAYS = "days";
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";
    static final String JSON_MONTH = "month";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_REFERENCES = "references";
    static final String JSON_HWS = "hws";
    static final String JSON_STARTING_MONDAY_MONTH = "startingMondayMonth";
    static final String JSON_STARTING_MONDAY_DAY = "startingMondayDay";
    static final String JSON_ENDING_FRIDAY_MONTH = "endingFridayMonth";
    static final String JSON_ENDING_FRIDAY_DAY = "endingFridayDay";
    static final String JSON_WEIGHT = "weight";
    
    public CourseSiteGeneratorFiles(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        //System.out.println(filePath);
        AppGUIModule gui = app.getGUIModule();
	// CLEAR THE OLD DATA OUT
	CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        dataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
        
	JsonArray jsonArray = loadJSONArray(filePath);
        JsonObject pageJS=null;
        JsonObject syllabusJS=null;
        JsonObject sectionsJS=null;
        JsonObject officeHoursJS=null;
        JsonObject scheduleJS=null;
        try{
        pageJS=jsonArray.getJsonObject(0);
        syllabusJS=jsonArray.getJsonObject(1);
        sectionsJS=jsonArray.getJsonObject(2);
        officeHoursJS=jsonArray.getJsonObject(3);
        scheduleJS=jsonArray.getJsonObject(4);
        }
        catch(Exception e){
            System.out.println(7);
        }
	// LOAD THE START AND END HOURS
        try{
	String startHour =officeHoursJS.getString(JSON_START_HOUR); ;//json.getString(JSON_START_HOUR);
        String endHour = officeHoursJS.getString(JSON_END_HOUR);
        dataManager.initHours("8", "23");
        ComboBox startTimeComboBox = (ComboBox) gui.getGUINode(CSG_START_TIME_COMBOBOX);
        ComboBox endTimeComboBox = (ComboBox) gui.getGUINode(CSG_END_TIME_COMBOBOX);   
        startTimeComboBox.setValue(dataManager.getTimeString(Integer.parseInt(startHour), ENABLED));
        endTimeComboBox.setValue(dataManager.getTimeString(Integer.parseInt(endHour), ENABLED));
        dataManager.resetTime(Integer.parseInt(startHour), Integer.parseInt(endHour));
        }
        catch(NullPointerException e){
            
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
        
        
        //Banner
        loadItemToComboBox(CSG_SUBJECT_COMBOBOX,gui,pageJS,JSON_SUBJECT,"subject");
        loadItemToComboBox(CSG_NUMBER_COMBOBOX,gui,pageJS,JSON_NUMBER,"number");
        loadItemToComboBox(CSG_SEMESTER_COMBOBOX,gui,pageJS,JSON_SEMESTER);
        loadItemToComboBox(CSG_YEAR_COMBOBOX,gui,pageJS,JSON_YEAR);
        loadItemToTextField(CSG_TITLE_TEXTFIELD,gui,pageJS,JSON_TITLE);
        //PAGE
        loadPageOptions(gui,pageJS,JSON_PAGE);
        //Instructor
        
        loadInstructor(gui,pageJS,JSON_INSTRUCTOR);
        
        //Style
        loadLogos(gui,pageJS,JSON_LOGOS);
        
        //description
        loadDescription(gui,syllabusJS,JSON_DESCRIPTION);
        loadTopics(gui,syllabusJS,JSON_TOPICS);
        loadPrerequisites(gui,syllabusJS,JSON_PREREQUISITES);
        loadOutcomes(gui,syllabusJS,JSON_OUTCOMES);
        loadTextbooks(gui,syllabusJS,JSON_TEXTBOOKS);
        loadGradedComponents(gui,syllabusJS,JSON_GRADED_COMPONENTS);
        loadGradingNote(gui,syllabusJS,JSON_GRADING_NOTE);
        loadAcademicDishonesty(gui,syllabusJS,JSON_ACADEMIC_DISHONESTY);
        loadspecialAssistance(gui,syllabusJS,JSON_SPECIAL_ASSISTANCE);
        
        
            //sections
        loadSectionLectures(gui,sectionsJS,JSON_LECTURES,dataManager);
        
        loadSectionReciation(gui,sectionsJS,JSON_RECITATIONS);
        loadSectionLab(gui,sectionsJS,JSON_LABS);
        //schedule
        
        loadScheduleTime(gui,scheduleJS);
        
    
       // ArrayList<ScheduleData> list=new ArrayList<ScheduleData>();
        loadScheduleItems(gui,scheduleJS,JSON_HOLIDAYS,dataManager);
        loadScheduleItems(gui,scheduleJS,JSON_LECTURES,dataManager); 
        loadScheduleItems(gui,scheduleJS,JSON_REFERENCES,dataManager);
        loadScheduleItems(gui,scheduleJS,JSON_RECITATIONS,dataManager);
        loadScheduleItems(gui,scheduleJS,JSON_HWS,dataManager);
        /*Collections.sort(list);
        for(int i=0;i<list.size();i++){
            ((TableView) gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).getItems().add(list.get(i));
        }*/
        
                
                
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, officeHoursJS, JSON_GRAD_TAS);
        loadTAs(dataManager, officeHoursJS, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = officeHoursJS.getJsonArray(JSON_OFFICE_HOURS);
        
        try{
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            if(!name.equals("Lecture")&&!name.equals(officeHoursJS.getJsonObject(JSON_INSTRUCTOR).getString(JSON_NAME))){
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
            dataManager.getTimeSlots().add(timeSlot);
            }
            else{
               //TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
               //timeSlot.toggleLecture(dow);
                
            }
        }
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        /*catch(Exception e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_FORMAT_ERROR_CONTENT);
            System.exit(0);
        }*/
        
    }
    
    private void loadScheduleTime(AppGUIModule gui,JsonObject json){
        try{
        String startingMondayMonth;
        String startingMondayDay;
        String startingMondayDate="";
        String endingFridayMonth;
        String endingFridayDay;
        String endingFridayDate="";
        startingMondayMonth = json.getString(JSON_STARTING_MONDAY_MONTH);
        startingMondayDay = json.getString(JSON_STARTING_MONDAY_DAY);
        endingFridayMonth = json.getString(JSON_ENDING_FRIDAY_MONTH);
        endingFridayDay = json.getString(JSON_ENDING_FRIDAY_DAY);
        if(!startingMondayMonth.equals("")&&!startingMondayDay.equals("")){
            startingMondayDate+=startingMondayMonth+"/"+startingMondayDay+"/"+Calendar.getInstance().get(Calendar.YEAR);
            ((ComboBox) gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)).setValue(startingMondayDate);
        }
        else
             ((ComboBox) gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)).setValue(null);
        if(!endingFridayMonth.equals("")&&!endingFridayDay.equals("")){
            endingFridayDate+=endingFridayMonth+"/"+endingFridayDay+"/"+Calendar.getInstance().get(Calendar.YEAR);
            ((ComboBox) gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)).setValue(endingFridayDate);}
        
        else
            ((ComboBox) gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)).setValue(null);
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
    }
    private void loadTAs(CourseSiteGeneratorData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        try{
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type;
            if(tas.contains("undergrad"))
                type = TAType.valueOf("Undergraduate");
            else
                type = TAType.valueOf("Graduate");
            
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadItemToComboBox(Object nodeID,AppGUIModule gui,JsonObject json,String jsonID){
        try{
        if(!((ComboBox) gui.getGUINode(nodeID)).getItems().contains(json.getString(jsonID))){
            if(!json.getString(jsonID).equals("")){
            ((ComboBox) gui.getGUINode(nodeID)).getItems().add(json.getString(jsonID));
            ((ComboBox) gui.getGUINode(nodeID)).getSelectionModel().select(json.getString(jsonID));
            ((ComboBox) gui.getGUINode(nodeID)).setValue(json.getString(jsonID));
            
            }
        }
        else{
            ((ComboBox) gui.getGUINode(nodeID)).setValue(json.getString(jsonID));
        }
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }  
    
    private void loadItemToComboBox(Object nodeID,AppGUIModule gui,JsonObject json,String jsonID,String s){
        try{
        if(!((ComboBox) gui.getGUINode(nodeID)).getItems().contains(json.getString(jsonID))){
            if(!json.getString(jsonID).equals("")){
            ((ComboBox) gui.getGUINode(nodeID)).getItems().add(json.getString(jsonID));
            ((ComboBox) gui.getGUINode(nodeID)).getSelectionModel().select(json.getString(jsonID));
            ((ComboBox) gui.getGUINode(nodeID)).setValue(json.getString(jsonID));
            try{
            saveOptions(((ComboBox) gui.getGUINode(nodeID)),s);}
            catch(Exception e){
                
            }
            }
        }
        else{
            ((ComboBox) gui.getGUINode(nodeID)).setValue(json.getString(jsonID));
        }
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    } 
    
    private void loadItemToTextField(Object nodeID,AppGUIModule gui,JsonObject json,String jsonID){
        try{
        ((TextField) gui.getGUINode(nodeID)).setText(json.getString(jsonID));}
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
      
    private void loadPageOptions(AppGUIModule gui,JsonObject json,String pages){
        JsonArray pagesArray = json.getJsonArray(pages);
        try{
        for (int i = 0; i < pagesArray.size(); i++) {
            JsonObject jsonOps = pagesArray.getJsonObject(i);
            String name = jsonOps.getString(JSON_NAME).toUpperCase();
            String nodeID="CSG_"+name+"_CHECKBOX";
            //SAVE BOTH
            ((CheckBox) gui.getGUINode(nodeID)).setSelected(ENABLED);
        }     
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
        
    }
    private void loadLogos(AppGUIModule gui,JsonObject json,String logos){
        JsonObject jsonLogos=json.getJsonObject(logos);
        try{
            for(Node node:((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren()){
            if(node instanceof ImageView&&GridPane.getColumnIndex(node)==1&&GridPane.getRowIndex(node)==1){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren().remove(node);
                    CourseSiteGeneratorWorkspace.urls[0]=null;
                    break;
                }
            }
            for(Node node:((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren()){
            if(node instanceof ImageView&&GridPane.getColumnIndex(node)==1&&GridPane.getRowIndex(node)==2){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren().remove(node);
                    CourseSiteGeneratorWorkspace.urls[1]=null;
                    break;
                }
            }
            for(Node node:((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren()){
            if(node instanceof ImageView&&GridPane.getColumnIndex(node)==1&&GridPane.getRowIndex(node)==3){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren().remove(node);
                    CourseSiteGeneratorWorkspace.urls[2]=null;
                    break;
                }
            }
            for(Node node:((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren()){
            if(node instanceof ImageView&&GridPane.getColumnIndex(node)==1&&GridPane.getRowIndex(node)==4){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren().remove(node);
                    CourseSiteGeneratorWorkspace.urls[3]=null;
                    break;
                }
            }
            ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:"+jsonLogos.getJsonObject(JSON_FAVICON).getString(JSON_HREF)),1,1);
            
            //System.out.print("file:"+jsonLogos.getJsonObject(JSON_FAVICON).getString(JSON_HREF));
            CourseSiteGeneratorWorkspace.urls[0]=jsonLogos.getJsonObject(JSON_FAVICON).getString(JSON_HREF);
            SetImage_Transaction.url1.add("file:"+jsonLogos.getJsonObject(JSON_FAVICON).getString(JSON_HREF));
            ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:"+jsonLogos.getJsonObject(JSON_NAVBAR).getString(JSON_SRC)),1,2);
            CourseSiteGeneratorWorkspace.urls[1]=jsonLogos.getJsonObject(JSON_NAVBAR).getString(JSON_SRC);
            SetImage_Transaction.url2.add("file:"+jsonLogos.getJsonObject(JSON_NAVBAR).getString(JSON_SRC));
            ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:"+jsonLogos.getJsonObject(JSON_BOTTOM_LEFT).getString(JSON_SRC)),1,3);
            CourseSiteGeneratorWorkspace.urls[2]=jsonLogos.getJsonObject(JSON_BOTTOM_LEFT).getString(JSON_SRC);
            SetImage_Transaction.url3.add("file:"+jsonLogos.getJsonObject(JSON_BOTTOM_LEFT).getString(JSON_SRC));
            ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:"+jsonLogos.getJsonObject(JSON_BOTTOM_RIGHT).getString(JSON_SRC)),1,4);
            CourseSiteGeneratorWorkspace.urls[3]=jsonLogos.getJsonObject(JSON_BOTTOM_RIGHT).getString(JSON_SRC);
            SetImage_Transaction.url4.add("file:"+jsonLogos.getJsonObject(JSON_BOTTOM_RIGHT).getString(JSON_SRC));
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    private void loadInstructor(AppGUIModule gui,JsonObject json,String instructor){
        JsonObject jsonInstructor=json.getJsonObject(instructor);
        try{
            ((TextField) gui.getGUINode(CSG_NAME_TEXTFIELD)).setText(jsonInstructor.getString(JSON_NAME));
            ((TextField) gui.getGUINode(CSG_ROOM_TEXTFIELD)).setText(jsonInstructor.getString(JSON_ROOM));
            ((TextField) gui.getGUINode(CSG_EMAIL_TEXTFIELD)).setText(jsonInstructor.getString(JSON_EMAIL));
            ((TextField) gui.getGUINode(CSG_HOMEPAGE_TEXTFIELD)).setText(jsonInstructor.getString(JSON_LINK));
            TextArea hours=((TextArea) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA));
            hours.setVisible(ENABLED);
            hours.setManaged(ENABLED);
            ((Button) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            String text="";
            text+="[\n";
            if(jsonInstructor.getJsonArray(JSON_HOURS).size()>0){
            for(int i=0;i<jsonInstructor.getJsonArray(JSON_HOURS).size()-1;i++){
                text+=jsonInstructor.getJsonArray(JSON_HOURS).get(i)+",\n";
            }
            text+=jsonInstructor.getJsonArray(JSON_HOURS).get(jsonInstructor.getJsonArray(JSON_HOURS).size()-1)+"\n"+"]";}
            else text="";
            hours.setText(text);
            
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadDescription(AppGUIModule gui,JsonObject json,String description){
        try{
            ((TextArea) gui.getGUINode(CSG_DESCRIPTION_TEXTAREA)).setText(json.getJsonString(description).toString());
            ((Button) gui.getGUINode(CSG_DESCRIPTION_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_DESCRIPTION_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_DESCRIPTION_TEXTAREA)).setVisible(ENABLED);
            }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadTopics(AppGUIModule gui,JsonObject json,String topics){
        JsonArray jsonTopics=json.getJsonArray(topics);
        String s="[\n";
        try{
            if(jsonTopics.size()>0){
            for(int i=0;i<jsonTopics.size()-1;i++){
                s+="\""+jsonTopics.getString(i)+"\",\n";
            }
            s+="\""+jsonTopics.getString(jsonTopics.size()-1)+"\"\n";}
            s+="]"; 
            ((Button) gui.getGUINode(CSG_TOPICS_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_TOPICS_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_TOPICS_TEXTAREA)).setVisible(ENABLED);
            ((TextArea) gui.getGUINode(CSG_TOPICS_TEXTAREA)).setText(s);
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadPrerequisites(AppGUIModule gui,JsonObject json,String prerequisites){
        try{
            ((TextArea) gui.getGUINode(CSG_PREREQUISITES_TEXTAREA)).setText(json.getJsonString(prerequisites).toString());
            ((Button) gui.getGUINode(CSG_PREREQUISITES_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_PREREQUISITES_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_PREREQUISITES_TEXTAREA)).setVisible(ENABLED);
           }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadOutcomes(AppGUIModule gui,JsonObject json,String outcomes){
        JsonArray jsonOutcomes=json.getJsonArray(outcomes);
        String s="[\n";
        try{
            if(jsonOutcomes.size()>0){
            for(int i=0;i<jsonOutcomes.size()-1;i++){
                s+="\""+jsonOutcomes.getString(i)+"\",\n";
            }
            s+="\""+jsonOutcomes.getString(jsonOutcomes.size()-1)+"\"\n";}
            s+="]"; 
            ((Button) gui.getGUINode(CSG_OUTCOMES_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_OUTCOMES_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_OUTCOMES_TEXTAREA)).setVisible(ENABLED);
            ((TextArea) gui.getGUINode(CSG_OUTCOMES_TEXTAREA)).setText(s);
            
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    
    private void loadTextbooks(AppGUIModule gui,JsonObject json,String textbooks){
        JsonArray jsonTextbooks=json.getJsonArray(textbooks);
        String s="[\n";
        try{
            if(jsonTextbooks.size()>0){
            for(int i=0;i<jsonTextbooks.size()-1;i++){
                s+=jsonTextbooks.getJsonObject(i).toString()+",\n";
            }
            s+=jsonTextbooks.getJsonObject(jsonTextbooks.size()-1).toString()+"\n";}
            s+="]";
            
            ((Button) gui.getGUINode(CSG_TEXTBOOKS_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).setVisible(ENABLED);
            ((TextArea) gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).setText(s);
            
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadGradedComponents(AppGUIModule gui,JsonObject json,String gradedComponents){
        JsonArray jsonGradedComponents=json.getJsonArray(gradedComponents);
        String s="[\n";
        try{
            if(jsonGradedComponents.size()>0){
            for(int i=0;i<jsonGradedComponents.size()-1;i++){
                s+=jsonGradedComponents.getJsonObject(i).toString()+",\n";
            }
            s+=jsonGradedComponents.getJsonObject(jsonGradedComponents.size()-1).toString()+"\n";}
            s+="]";
            
            ((Button) gui.getGUINode(CSG_GRADED_COMPONENTS_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).setVisible(ENABLED);
            ((TextArea) gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).setText(s);
            
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    
    
    private void loadGradingNote(AppGUIModule gui,JsonObject json,String gradingNote){
        try{
            ((TextArea) gui.getGUINode(CSG_GRADING_NOTE_TEXTAREA)).setText(json.getJsonString(gradingNote).toString());
            ((Button) gui.getGUINode(CSG_GRADING_NOTE_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_GRADING_NOTE_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_GRADING_NOTE_TEXTAREA)).setVisible(ENABLED);
           }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadAcademicDishonesty(AppGUIModule gui,JsonObject json,String academicDishonesty){
        try{
            ((TextArea) gui.getGUINode(CSG_ACADEMIC_DISHONESTY_TEXTAREA)).setText(json.getJsonString(academicDishonesty).toString());
            ((Button) gui.getGUINode(CSG_ACADEMIC_DISHONESTY_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_ACADEMIC_DISHONESTY_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_ACADEMIC_DISHONESTY_TEXTAREA)).setVisible(ENABLED);
           }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadspecialAssistance(AppGUIModule gui,JsonObject json,String specialAssistance){
        try{
            ((TextArea) gui.getGUINode(CSG_SPECIAL_ASSISTANCE_TEXTAREA)).setText(json.getJsonString(specialAssistance).toString());
            ((Button) gui.getGUINode(CSG_SPECIAL_ASSISTANCE_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
            ((TextArea) gui.getGUINode(CSG_SPECIAL_ASSISTANCE_TEXTAREA)).setManaged(ENABLED);
            ((TextArea) gui.getGUINode(CSG_SPECIAL_ASSISTANCE_TEXTAREA)).setVisible(ENABLED);
           }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadSectionLectures(AppGUIModule gui, JsonObject json, String lectures,CourseSiteGeneratorData dataManager)  {
        JsonArray jsonLecturesArray = json.getJsonArray(lectures);//,CourseSiteGeneratorData dataManager
        
        try{
        for (int i = 0; i < jsonLecturesArray.size(); i++) {
            int count=0;
            JsonObject jsonLecture = jsonLecturesArray.getJsonObject(i);
            String section = jsonLecture.getString(JSON_SECTION);
            String days=jsonLecture.getString(JSON_DAYS).replace("&amp;", "&");
            String time = jsonLecture.getString(JSON_TIME);
            String room = jsonLecture.getString(JSON_ROOM);
            ObservableList<LectureData> Lectures = ((TableView) gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getItems();
            LectureData lecture = new LectureData(section, days, time, room);
            Lectures.add(lecture);
            ArrayList<String> box=lecture.getTimeBox();
            /*for(int j=0;j<box.size();j++){
                TimeSlot ts=dataManager.getTimeSlot(box.get(i));
                ts.toggleLecture(DayOfWeek.TUESDAY, lecture);
                
            }*/
        }     
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadSectionReciation(AppGUIModule gui, JsonObject json, String recitations) {
        JsonArray jsonRecitationArray = json.getJsonArray(recitations);
        try{
        for (int i = 0; i < jsonRecitationArray.size(); i++) {
            JsonObject jsonRecitation = jsonRecitationArray.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION).replace("<strong>", "").replace("</strong>", "");
            String day_time = jsonRecitation.getString(JSON_DAY_TIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String TA1 = jsonRecitation.getString(JSON_TA1);
            String TA2 = jsonRecitation.getString(JSON_TA2);
            ObservableList<RecitationData> Recitations = ((TableView) gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getItems();
            RecitationData recitation = new RecitationData(section,day_time,location,TA1,TA2);
            Recitations.add(recitation);
        }     
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadSectionLab(AppGUIModule gui, JsonObject json, String labs) {
        JsonArray jsonLabArray = json.getJsonArray(labs);
        try{
        for (int i = 0; i < jsonLabArray.size(); i++) {
            JsonObject jsonLab = jsonLabArray.getJsonObject(i);
            String section = jsonLab.getString(JSON_SECTION).substring(8,11);
            String day_time = jsonLab.getString(JSON_DAY_TIME);
            String location = jsonLab.getString(JSON_LOCATION);
            String TA1 = jsonLab.getString(JSON_TA1);
            String TA2 = jsonLab.getString(JSON_TA2);
            ObservableList<LabData> Labs = ((TableView) gui.getGUINode(CSG_LAB_TABLE_VIEW)).getItems();
            LabData lab = new LabData(section,day_time,location,TA1,TA2);
            Labs.add(lab);
        }     
        }
        catch(NullPointerException e){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    private void loadScheduleItems(AppGUIModule gui, JsonObject json, String scheduleType, CourseSiteGeneratorData dataManager) {
        
        
        JsonArray jsonScheduleItemsArray = json.getJsonArray(scheduleType);
        int count=0;
        try{
        for (int i = 0; i < jsonScheduleItemsArray.size(); i++) {
            JsonObject jsonScheduleItem = jsonScheduleItemsArray.getJsonObject(i);
            String type = scheduleType.substring(0,1).toUpperCase()+scheduleType.substring(1,scheduleType.length()-1);
            if(type.equals("Hw"))
                type="HW";
            String year="";
            String day="";
            year=jsonScheduleItem.getString(JSON_MONTH);
            day=jsonScheduleItem.getString(JSON_DAY);
            String date = ScheduleData.toDateForm(year+"/"+day+"/"+Calendar.getInstance().get(Calendar.YEAR));
            
            String title = jsonScheduleItem.getString(JSON_TITLE);//.replace("?<br /><br /><br />", "").replace("?", "");
            /*if(jsonScheduleItem.getString(JSON_TITLE).contains("FINAL EXAM"))
                title="Final Exam";
            if(jsonScheduleItem.getString(JSON_TITLE).contains("HW"))
                title="HW "+(++count);*/
            String topic="";
            if(jsonScheduleItem.containsKey(JSON_TOPIC))
            topic = jsonScheduleItem.getString(JSON_TOPIC);//.replace("<br />", "");
            String link = jsonScheduleItem.getString(JSON_LINK);
            ScheduleData item = new ScheduleData(type,date,topic,title,link);
            dataManager.addSchedule(item);
        }     
        }
        catch(NullPointerException e){
            
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(), FILE_ERROR_TITLE, FILE_LACK_ERROR_CONTENT);
            System.exit(0);
        }
        
    }
    
    
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonArray loadJSONArray(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonArray json = jsonReader.readArray();
	jsonReader.close();
	is.close();
	return json;
    }
    
    private JsonObject loadJSONObject(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        //Site
        AppGUIModule gui = app.getGUIModule();
        
        
        
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<LectureData> lecturesIterator = dataManager.lecturesIterator();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
         while (lecturesIterator.hasNext()) {
             LectureData l = lecturesIterator.next();
             ArrayList<String> days=l.getDayBox();
             ArrayList<String> times=l.getTimeBox();
             for(int day=0;day<days.size();day++){
                 for(int time=0;time<times.size();time++){
                     JsonObject unit =Json.createObjectBuilder().add(JSON_START_TIME, times.get(time))
                             .add(JSON_DAY_OF_WEEK, days.get(day))
                             .add(JSON_NAME, "Lecture").build();
                     officeHoursArrayBuilder.add(unit);
                 }
             }
         }
        String instructorOhText=((TextArea)gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).getText();
        ArrayList<String> times=getTime(instructorOhText);
        ArrayList<String> days=getDay(instructorOhText);
        for(int i =0;i<days.size();i++){
            ArrayList<String> s=getTimeBox(times.get(i));
            for(int j =0;j<s.size();j++){
                JsonObject unit =Json.createObjectBuilder().add(JSON_START_TIME,s.get(j))
                        .add(JSON_DAY_OF_WEEK, days.get(i)).add(JSON_NAME, ((TextField)gui.getGUINode(CSG_NAME_TEXTFIELD)).getText()).build();
                officeHoursArrayBuilder.add(unit);
            }
        }
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        String subject;
        String number;
        String year;
        String semester;
        String title;
        try{
            subject=((ComboBox)gui.getGUINode(CSG_SUBJECT_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            subject="";
        }
        try{
            number=((ComboBox)gui.getGUINode(CSG_NUMBER_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            number="";
        }
        try{
            year=((ComboBox)gui.getGUINode(CSG_YEAR_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            year="";
        }
        try{
            semester=((ComboBox)gui.getGUINode(CSG_SEMESTER_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            semester="";
        }
        try{
            title=((TextField)gui.getGUINode(CSG_TITLE_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            title="";
        }
        //style
        String href1="";
        
        if(CourseSiteGeneratorWorkspace.urls[0]!=null&&!CourseSiteGeneratorWorkspace.urls[0].equals(""))
            href1="./"+CourseSiteGeneratorWorkspace.urls[0].substring(CourseSiteGeneratorWorkspace.urls[0].indexOf("images/"));
        JsonObject favicon=Json.createObjectBuilder().add(JSON_HREF, href1).build();

        String href2="";
        String src2="";
        if(CourseSiteGeneratorWorkspace.urls[1]!=null&&!CourseSiteGeneratorWorkspace.urls[1].equals("")){
            src2="./"+CourseSiteGeneratorWorkspace.urls[1].substring(CourseSiteGeneratorWorkspace.urls[1].indexOf("images/"));
            href2="http://www.stonybrook.edu";
        }
        JsonObject navbar=Json.createObjectBuilder().add(JSON_HREF, href2).add(JSON_SRC, src2).build();
        
        String href3="";
        String src3="";
        if(CourseSiteGeneratorWorkspace.urls[2]!=null&&!CourseSiteGeneratorWorkspace.urls[2].equals("")){
            src3="./"+CourseSiteGeneratorWorkspace.urls[2].substring(CourseSiteGeneratorWorkspace.urls[2].indexOf("images/"));
            href3="http://www.stonybrook.edu";
        }
        JsonObject bottom_left=Json.createObjectBuilder().add(JSON_HREF, href3).add(JSON_SRC, src3).build();
        
        String href4="";
        String src4="";
        if(CourseSiteGeneratorWorkspace.urls[3]!=null&&!CourseSiteGeneratorWorkspace.urls[3].equals("")){
            src4="./"+CourseSiteGeneratorWorkspace.urls[3].substring(CourseSiteGeneratorWorkspace.urls[3].indexOf("images/"));
            href4="http://www.stonybrook.edu";
        }
        JsonObject bottom_right=Json.createObjectBuilder().add(JSON_HREF, href4).add(JSON_SRC, src4).build();
        
        JsonObject logo=Json.createObjectBuilder().add(JSON_FAVICON, favicon).add(JSON_NAVBAR, navbar).add(JSON_BOTTOM_LEFT, bottom_left).add(JSON_BOTTOM_RIGHT, bottom_right).build();
        
        
        //
        String instructorName;
        String instructorLink;
        String instructorEmail;
        String instructorRoom;
        String instructorHours;
        String[] instrutorHoursBox;
        JsonArrayBuilder instructorOfficeHoursArrayBuilder = Json.createArrayBuilder();
        try{
            instructorName=((TextField)gui.getGUINode(CSG_NAME_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorName="";
        }
        try{
            instructorLink=((TextField)gui.getGUINode(CSG_HOMEPAGE_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorLink="";
        }
        try{
            instructorEmail=((TextField)gui.getGUINode(CSG_EMAIL_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorEmail="";
        }
        try{
            instructorRoom=((TextField)gui.getGUINode(CSG_ROOM_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorRoom="";
        }
        try{
            instructorHours=((TextArea)gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).getText().replace("[", "").replace("]", "").replace(" ", "").replace("{", "").replace("}", "").replace("\"day\":", "").replace("\"time\":", "").replace("\"", "").replace("\n", "");
            instrutorHoursBox=instructorHours.split(",");
            
            if(instrutorHoursBox.length>1)
            for(int i=0;i<instrutorHoursBox.length;i+=2){
                JsonObject ts=Json.createObjectBuilder().add(JSON_DAY,instrutorHoursBox[i])
                                                          .add(JSON_TIME, instrutorHoursBox[i+1]).build();
                
                instructorOfficeHoursArrayBuilder.add(ts);
            } 
            
        }
        catch(NullPointerException e){
            
            instructorHours="";
        }
        JsonArray instructorOfficeHoursArray=instructorOfficeHoursArrayBuilder.build();
        
        
        
        JsonObject instructor = Json.createObjectBuilder().add(JSON_NAME,instructorName)
                                                          .add(JSON_LINK, instructorLink)
                                                          .add(JSON_EMAIL, instructorEmail)
                                                          .add(JSON_ROOM, instructorRoom)
                                                          .add(JSON_HOURS, instructorOfficeHoursArray)
                                                          .build();
        
        JsonArrayBuilder webArrayBuilder = Json.createArrayBuilder();
        if(((CheckBox)gui.getGUINode(CSG_HOME_CHECKBOX)).selectedProperty().get()){
            JsonObject web1=Json.createObjectBuilder().add(JSON_NAME, "Home").add(JSON_LINK, "index.html").build();
            webArrayBuilder.add(web1);
        }
        
        
        if(((CheckBox)gui.getGUINode(CSG_SYLLABUS_CHECKBOX)).selectedProperty().get()){
            JsonObject web2=Json.createObjectBuilder().add(JSON_NAME, "Syllabus").add(JSON_LINK, "syllabus.html").build();
            webArrayBuilder.add(web2);
        }
       
        if(((CheckBox)gui.getGUINode(CSG_SCHEDULE_CHECKBOX)).selectedProperty().get()){
            JsonObject web3=Json.createObjectBuilder().add(JSON_NAME, "Schedule").add(JSON_LINK, "schedule.html").build();
            webArrayBuilder.add(web3);
        }
        
        if(((CheckBox)gui.getGUINode(CSG_HWS_CHECKBOX)).selectedProperty().get()){
            JsonObject web4=Json.createObjectBuilder().add(JSON_NAME, "HWs").add(JSON_LINK, "hws.html").build();
            webArrayBuilder.add(web4);
        }
        
        JsonArray webArray = webArrayBuilder.build();
        
        //syllabus
        String description=((TextArea)gui.getGUINode(CSG_DESCRIPTION_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        
        JsonArrayBuilder topicsArrayBuilder = Json.createArrayBuilder();
        String topicsText=((TextArea)gui.getGUINode(CSG_TOPICS_TEXTAREA)).getText().replace("[", "").replace("]", "");
        String[] topicsBox=topicsText.split(",\n");
        
        for(int i=0;i<topicsBox.length;i++){
            
            String s=topicsBox[i].substring(topicsBox[i].indexOf("\"")+1).replace("\"", "").replace("\n", "");//kongge
            topicsArrayBuilder.add(s);//
        }
        JsonArray topicsArray = topicsArrayBuilder.build();
        
        String prerequisites=((TextArea)gui.getGUINode(CSG_PREREQUISITES_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        
        JsonArrayBuilder outcomesArrayBuilder = Json.createArrayBuilder();
        String outcomesText=((TextArea)gui.getGUINode(CSG_OUTCOMES_TEXTAREA)).getText().replace("[", "").replace("]", "");
        String[] outcomesBox=outcomesText.split(",\n");
        for(int i=0;i<outcomesBox.length;i++){
            String s=outcomesBox[i].substring(outcomesBox[i].indexOf("\"")+1).replace("\"", "").replace("\n", "");//kongge
            outcomesArrayBuilder.add(s);//
        }
        JsonArray outcomesArray = outcomesArrayBuilder.build();
        
        int numberOfBook=0;
        for(int i=0;i<((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).getText().length();i++){
            if(((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).getText().substring(i, i+1).equals("}"))
                numberOfBook++;
        }
        String textbooksText=((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).getText();
        JsonArrayBuilder textbooksArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<numberOfBook;i++){
           JsonArrayBuilder authorArrayBuilder= Json.createArrayBuilder();
            String bookTitle=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"title\":")+8)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"title\":")+8)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"title\":")+8)+1));
            //System.out.println(bookTitle);
            
            String bookLink=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"link\":")+7)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"link\":")+7)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"link\":")+7)+1));
            
            
            String bookPhoto=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"photo\":")+8)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"photo\":")+8)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"photo\":")+8)+1));
         
            String authors=textbooksText.substring(textbooksText.indexOf("[")+1,textbooksText.indexOf("],"));
            String[] authorsBox=authors.split(",");
            textbooksText=textbooksText.substring(textbooksText.indexOf("],"));
            for(int j=0;j<authorsBox.length;j++){
                String author=authorsBox[j].substring(authorsBox[j].indexOf("\"")+1,authorsBox[j].indexOf("\"",authorsBox[j].indexOf("\"")+1));
                authorArrayBuilder.add(author);
            }
            
            String bookPublisher=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"publisher\":")+12)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"publisher\":")+12)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"publisher\":")+12)+1));
            
            
            String bookYear=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"year\":")+7)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"year\":")+7)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"year\":")+7)+1));
            
            
            JsonArray author=authorArrayBuilder.build();
            JsonObject textbook=Json.createObjectBuilder().add(JSON_TITLE,bookTitle).add(JSON_LINK,bookLink).add(JSON_PHOTO, bookPhoto).add(JSON_AUTHORS, author).add(JSON_PUBLISHER, bookPublisher).add(JSON_YEAR, bookYear).build();
            textbooksArrayBuilder.add(textbook);
            
        }
        JsonArray textbooksArray=textbooksArrayBuilder.build();
        
        
        int numberOfGradedComponents=0;
        for(int i=0;i<((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).getText().length();i++){
            if(((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).getText().substring(i, i+1).equals("}"))
                numberOfGradedComponents++;
        }
        String gradedComponentText=((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).getText();
        JsonArrayBuilder gradedComponentBuilder=Json.createArrayBuilder();
        for(int i=0;i<numberOfGradedComponents;i++){
            
            String name=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"name\":")+7)+1,gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"name\":")+7)+1));
            gradedComponentText=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"name\":")+7)+1));
            
            
            String gradeDescription=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"description\":")+14)+1,gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"description\":")+14)+1));
            gradedComponentText=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"description\":")+14)+1));
            
            String weight=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"weight\":")+9)+1,gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"weight\":")+9)+1));
            gradedComponentText=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"weight\":")+9)+1));
            
            
            
            
            JsonObject unit=Json.createObjectBuilder().add(JSON_NAME, name).add(JSON_DESCRIPTION, gradeDescription).add(JSON_WEIGHT, weight).build();
            gradedComponentBuilder.add(unit);
        }
        JsonArray gradedComponentArray=gradedComponentBuilder.build();
        String gradingNote=((TextArea)gui.getGUINode(CSG_GRADING_NOTE_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        String academicDishonesty=((TextArea)gui.getGUINode(CSG_ACADEMIC_DISHONESTY_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        String specialAssistance=((TextArea)gui.getGUINode(CSG_SPECIAL_ASSISTANCE_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        
        
        //SECTION
        ObservableList<LectureData> lectureItems=((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getItems();
        JsonArrayBuilder lectureArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<lectureItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, lectureItems.get(i).getSection())
                    .add(JSON_DAYS, lectureItems.get(i).getDays()).add(JSON_TIME, lectureItems.get(i).getTime())
                    .add(JSON_ROOM, lectureItems.get(i).getRoom()).build();
            lectureArrayBuilder.add(unit);
        }
        JsonArray lectureArray=lectureArrayBuilder.build();
        
        ObservableList<RecitationData> recitationItems=((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getItems();
        JsonArrayBuilder recitationArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<recitationItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, recitationItems.get(i).getSection())
                    .add(JSON_DAY_TIME, recitationItems.get(i).getDayTime()).add(JSON_LOCATION, recitationItems.get(i).getLocation())
                    .add(JSON_TA1, recitationItems.get(i).getTA1()).add(JSON_TA2, recitationItems.get(i).getTA2()).build();
            recitationArrayBuilder.add(unit);
        }
        JsonArray recitationArray=recitationArrayBuilder.build();
        
        ObservableList<LabData> labItems=((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getItems();
        JsonArrayBuilder labArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<labItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, labItems.get(i).getSection())
                    .add(JSON_DAY_TIME, labItems.get(i).getDayTime()).add(JSON_LOCATION, labItems.get(i).getLocation())
                    .add(JSON_TA1, labItems.get(i).getTA1()).add(JSON_TA2, labItems.get(i).getTA2()).build();
            labArrayBuilder.add(unit);
        }
        JsonArray labArray=labArrayBuilder.build();
        
        ObservableList<ScheduleData> scheduleItems=((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).getItems();
        JsonArrayBuilder HolidaysArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder LecturesArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder ReferencesArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder RecitationArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder HwsArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<scheduleItems.size();i++){
            String Type=scheduleItems.get(i).getType();
            String Date=scheduleItems.get(i).getDate();
            //System.out.print(Date);
            String Month="";
            String Day="";
            String Title=scheduleItems.get(i).getTitle();
            String Topic=scheduleItems.get(i).getTopic();
            JsonObjectBuilder unit=Json.createObjectBuilder();
            Month=Date.substring(0,Date.indexOf("/"));
            Day=Date.substring(Date.indexOf("/")+1,Date.indexOf("/",Date.indexOf("/")+1));
            //System.out.print(scheduleItems);
            
            switch(Type){
                case "Holiday":
                    HolidaysArrayBuilder
                            .add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "Lecture":
                    LecturesArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "Reference":
                    ReferencesArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "Recitation":
                    RecitationArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "HW":
                    HwsArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
            }
        }
        JsonArray HolidaysArray=HolidaysArrayBuilder.build();
        JsonArray LecturesArray=LecturesArrayBuilder.build();
        JsonArray ReferencesArray=ReferencesArrayBuilder.build();
        JsonArray RecitationArray= RecitationArrayBuilder.build();
        JsonArray HwsArray=HwsArrayBuilder.build();
        
        String startMonayDate;
        String startMonayMonth;
        String startMonayDay;
        String endFridayDate;
        String endFridayMonth;
        String endFridayDay;
        if(((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)).getValue()!=null){
            startMonayDate=((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)).getValue().toString();
            startMonayMonth=startMonayDate.substring(0, startMonayDate.indexOf("/"));
            startMonayDay=startMonayDate.substring(startMonayDate.indexOf("/")+1,startMonayDate.indexOf("/",startMonayDate.indexOf("/")+1));
        }
        else{
            startMonayMonth="";
            startMonayDay="";
        }
        
        if(((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)).getValue()!=null){
            endFridayDate=((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)).getValue().toString();
            endFridayMonth=endFridayDate.substring(0, endFridayDate.indexOf("/"));
            endFridayDay=endFridayDate.substring(endFridayDate.indexOf("/")+1,endFridayDate.indexOf("/",endFridayDate.indexOf("/")+1));
        }
        else{
            endFridayMonth="";
            endFridayDay="";
        }
        
        JsonObject officeHoursData=Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray).build();
        
        JsonObject PageDataData=Json.createObjectBuilder()
                .add(JSON_SUBJECT,subject)
                .add(JSON_NUMBER,""+number)
                .add(JSON_YEAR,""+year)
                .add(JSON_SEMESTER,semester)
                .add(JSON_TITLE,title)
                .add(JSON_LOGOS, logo)
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_PAGE, webArray).build();
        
        JsonObject SyllabusData=Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, description)
                .add(JSON_TOPICS, topicsArray)
                .add(JSON_PREREQUISITES, prerequisites)
                .add(JSON_OUTCOMES, outcomesArray)
                .add(JSON_TEXTBOOKS, textbooksArray)
                .add(JSON_GRADED_COMPONENTS, gradedComponentArray)
                .add(JSON_GRADING_NOTE, gradingNote)
                .add(JSON_ACADEMIC_DISHONESTY, academicDishonesty)
                .add(JSON_SPECIAL_ASSISTANCE, specialAssistance).build();
        
        JsonObject SectionsData=Json.createObjectBuilder()
                .add(JSON_LECTURES, lectureArray)
                .add(JSON_RECITATIONS, recitationArray)
                .add(JSON_LABS, labArray).build();
        
        JsonObject ScheduleData=Json.createObjectBuilder()
                .add(JSON_STARTING_MONDAY_MONTH, startMonayMonth)
                .add(JSON_STARTING_MONDAY_DAY, startMonayDay)
                .add(JSON_ENDING_FRIDAY_MONTH, endFridayMonth)
                .add(JSON_ENDING_FRIDAY_DAY, endFridayDay)
                .add(JSON_HOLIDAYS, HolidaysArray)
                .add(JSON_LECTURES, LecturesArray)
                .add(JSON_REFERENCES, ReferencesArray)
                .add(JSON_RECITATIONS, RecitationArray)
                .add(JSON_HWS, HwsArray)
		.build();
        
        
        JsonArray dataManagerJSO=Json.createArrayBuilder()
                .add(PageDataData)
                .add(SyllabusData)
                .add(SectionsData)
                .add(officeHoursData)
                .add(ScheduleData).build();
       
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeArray(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeArray(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    
    private void saveOptions(ComboBox c,String s) throws IOException{
        
        JsonArrayBuilder subjectArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder numberArrayBuilder = Json.createArrayBuilder();
        for(int i=0;i<c.getItems().size();i++){
            subjectArrayBuilder.add(c.getItems().get(i).toString());
        }
        
        
        JsonArray subjectArray = subjectArrayBuilder.build();
        JsonArray numberArray = numberArrayBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder().add(s,subjectArray).build();
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
	// INIT THE WRITER
	OutputStream os = new FileOutputStream("./work/"+s+"Options.json");//
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter("./work/"+s+"Options.json");//
	pw.write(prettyPrinted);
	pw.close();
        
    
    
    
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        filePath="./work/"+filePath;
        String newPath=props.getProperty(APP_EXPORT_PATH);
        
	JsonArray jsonArray = loadJSONArray(filePath);
        
        JsonObject pageJS=jsonArray.getJsonObject(0);
        JsonObject syllabusJS=jsonArray.getJsonObject(1);
        JsonObject sectionsJS=jsonArray.getJsonObject(2);
        JsonObject officeHoursJS=jsonArray.getJsonObject(3);
        JsonObject scheduleJS=jsonArray.getJsonObject(4);
        
        
        AppGUIModule gui = app.getGUIModule();
        File file = new File("./export/default");
        File newFile = new File(newPath);
        FileUtils.copyDirectory(file, newFile);
        File oldCSS= new File(newPath+props.getProperty(CURRENT_CSS));
        File newCSS= new File(newPath+"/css/sea_wolf.css");
        oldCSS.renameTo(newCSS);
        
        
        
	
	exportWriteData("PageData",newPath,pageJS);
        exportWriteData("SyllabusData",newPath,syllabusJS);
        exportWriteData("SectionsData",newPath,sectionsJS);
        exportWriteData("OfficeHoursData",newPath,officeHoursJS);
        exportWriteData("ScheduleData",newPath,scheduleJS);
        
        /*exportPageData(gui, filePath+"PageData.json");
        exportSyllabusData(gui, filePath+"SyllabusData.json");
        exportOfficeHoursDataData(dataManager,filePath+"OfficeHoursData.json");
        exportSectionsData(gui, filePath+"SectionsData.json");
        exportScheduleData(gui, filePath+"ScheduleData.json");*/
        
    }
    
    private void exportWriteData(String s,String path,JsonObject js) throws FileNotFoundException{
         Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(js);
	jsonWriter.close();
        
	// INIT THE WRITER
	OutputStream os = new FileOutputStream(path+"/js/"+s+".json");
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(js);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(path+"/js/"+s+".json");
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void exportPageData(AppGUIModule gui, String filePath)throws IOException{
        
        
        String subject;
        String number;
        String year;
        String semester;
        String title;
        try{
            subject=((ComboBox)gui.getGUINode(CSG_SUBJECT_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            subject="";
        }
        try{
            number=((ComboBox)gui.getGUINode(CSG_NUMBER_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            number="";
        }
        try{
            year=((ComboBox)gui.getGUINode(CSG_YEAR_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            year="";
        }
        try{
            semester=((ComboBox)gui.getGUINode(CSG_SEMESTER_COMBOBOX)).getValue().toString();}
        catch(NullPointerException e){
            semester="";
        }
        try{
            title=((TextField)gui.getGUINode(CSG_TITLE_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            title="";
        }
        //style
        String href1="";
        
        if(CourseSiteGeneratorWorkspace.urls[0]!=null&&!CourseSiteGeneratorWorkspace.urls[0].equals(""))
            href1="./"+CourseSiteGeneratorWorkspace.urls[0].substring(CourseSiteGeneratorWorkspace.urls[0].indexOf("images/"));
        JsonObject favicon=Json.createObjectBuilder().add(JSON_HREF, href1).build();

        String href2="";
        String src2="";
        if(CourseSiteGeneratorWorkspace.urls[1]!=null&&!CourseSiteGeneratorWorkspace.urls[1].equals("")){
            src2="./"+CourseSiteGeneratorWorkspace.urls[1].substring(CourseSiteGeneratorWorkspace.urls[1].indexOf("images/"));
            href2="http://www.stonybrook.edu";
        }
        JsonObject navbar=Json.createObjectBuilder().add(JSON_HREF, href2).add(JSON_SRC, src2).build();
        
        String href3="";
        String src3="";
        if(CourseSiteGeneratorWorkspace.urls[2]!=null&&!CourseSiteGeneratorWorkspace.urls[2].equals("")){
            src3="./"+CourseSiteGeneratorWorkspace.urls[2].substring(CourseSiteGeneratorWorkspace.urls[2].indexOf("images/"));
            href3="http://www.stonybrook.edu";
        }
        JsonObject bottom_left=Json.createObjectBuilder().add(JSON_HREF, href3).add(JSON_SRC, src3).build();
        
        String href4="";
        String src4="";
        if(CourseSiteGeneratorWorkspace.urls[3]!=null&&!CourseSiteGeneratorWorkspace.urls[3].equals("")){
            src4="./"+CourseSiteGeneratorWorkspace.urls[3].substring(CourseSiteGeneratorWorkspace.urls[3].indexOf("images/"));
            href4="http://www.stonybrook.edu";
        }
        JsonObject bottom_right=Json.createObjectBuilder().add(JSON_HREF, href4).add(JSON_SRC, src4).build();
        
        JsonObject logo=Json.createObjectBuilder().add(JSON_FAVICON, favicon).add(JSON_NAVBAR, navbar).add(JSON_BOTTOM_LEFT, bottom_left).add(JSON_BOTTOM_RIGHT, bottom_right).build();
        
        
        //
        String instructorName;
        String instructorLink;
        String instructorEmail;
        String instructorRoom;
        String instructorHours;
        String[] instrutorHoursBox;
        JsonArrayBuilder instructorOfficeHoursArrayBuilder = Json.createArrayBuilder();
        try{
            instructorName=((TextField)gui.getGUINode(CSG_NAME_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorName="";
        }
        try{
            instructorLink=((TextField)gui.getGUINode(CSG_HOMEPAGE_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorLink="";
        }
        try{
            instructorEmail=((TextField)gui.getGUINode(CSG_EMAIL_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorEmail="";
        }
        try{
            instructorRoom=((TextField)gui.getGUINode(CSG_ROOM_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorRoom="";
        }
        try{
            instructorHours=((TextArea)gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).getText().replace("[", "").replace("]", "").replace(" ", "").replace("{", "").replace("}", "").replace("\"day\":", "").replace("\"time\":", "").replace("\"", "").replace("\n", "");
            instrutorHoursBox=instructorHours.split(",");
            /*for(int i=0;i<instrutorHoursBox.length;i++){
                System.out.println(instrutorHoursBox[i]);
            }*/
            if(instrutorHoursBox.length>1)
            for(int i=0;i<instrutorHoursBox.length;i+=2){
                JsonObject ts=Json.createObjectBuilder().add(JSON_DAY,instrutorHoursBox[i])
                                                          .add(JSON_TIME, instrutorHoursBox[i+1]).build();
                
                instructorOfficeHoursArrayBuilder.add(ts);
            } 
            
        }
        catch(NullPointerException e){
            
            instructorHours="";
        }
        JsonArray instructorOfficeHoursArray=instructorOfficeHoursArrayBuilder.build();
        
        
        //JsonArray JsonInstructorHours=Json.
        JsonObject instructor = Json.createObjectBuilder().add(JSON_NAME,instructorName)
                                                          .add(JSON_LINK, instructorLink)
                                                          .add(JSON_EMAIL, instructorEmail)
                                                          .add(JSON_ROOM, instructorRoom)
                                                          .add(JSON_HOURS, instructorOfficeHoursArray)
                                                          .build();
        
        JsonArrayBuilder webArrayBuilder = Json.createArrayBuilder();
        if(((CheckBox)gui.getGUINode(CSG_HOME_CHECKBOX)).selectedProperty().get()){
            JsonObject web1=Json.createObjectBuilder().add(JSON_NAME, "Home").add(JSON_LINK, "index.html").build();
            webArrayBuilder.add(web1);
        }
        
        
        if(((CheckBox)gui.getGUINode(CSG_SYLLABUS_CHECKBOX)).selectedProperty().get()){
            JsonObject web2=Json.createObjectBuilder().add(JSON_NAME, "Syllabus").add(JSON_LINK, "syllabus.html").build();
            webArrayBuilder.add(web2);
        }
       
        if(((CheckBox)gui.getGUINode(CSG_SCHEDULE_CHECKBOX)).selectedProperty().get()){
            JsonObject web3=Json.createObjectBuilder().add(JSON_NAME, "Schedule").add(JSON_LINK, "schedule.html").build();
            webArrayBuilder.add(web3);
        }
        
        if(((CheckBox)gui.getGUINode(CSG_HWS_CHECKBOX)).selectedProperty().get()){
            JsonObject web4=Json.createObjectBuilder().add(JSON_NAME, "HWs").add(JSON_LINK, "hws.html").build();
            webArrayBuilder.add(web4);
        }
        
        JsonArray webArray = webArrayBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()//testSave
                .add(JSON_SUBJECT,subject)
                .add(JSON_NUMBER,""+number)
                .add(JSON_YEAR,""+year)
                .add(JSON_SEMESTER,semester)
                .add(JSON_TITLE,title)
                .add(JSON_LOGOS, logo)
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_PAGE, webArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void exportSyllabusData(AppGUIModule gui, String filePath)throws IOException{
        String description=((TextArea)gui.getGUINode(CSG_DESCRIPTION_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        
        JsonArrayBuilder topicsArrayBuilder = Json.createArrayBuilder();
        String topicsText=((TextArea)gui.getGUINode(CSG_TOPICS_TEXTAREA)).getText().replace("[", "").replace("]", "");
        String[] topicsBox=topicsText.split(",\n");
        
        for(int i=0;i<topicsBox.length;i++){
            
            String s=topicsBox[i].substring(topicsBox[i].indexOf("\"")+1).replace("\"", "").replace("\n", "");//kongge
            topicsArrayBuilder.add(s);//
        }
        JsonArray topicsArray = topicsArrayBuilder.build();
        
        String prerequisites=((TextArea)gui.getGUINode(CSG_PREREQUISITES_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        
        JsonArrayBuilder outcomesArrayBuilder = Json.createArrayBuilder();
        String outcomesText=((TextArea)gui.getGUINode(CSG_OUTCOMES_TEXTAREA)).getText().replace("[", "").replace("]", "");
        String[] outcomesBox=outcomesText.split(",\n");
        for(int i=0;i<outcomesBox.length;i++){
            String s=outcomesBox[i].substring(outcomesBox[i].indexOf("\"")+1).replace("\"", "").replace("\n", "");//kongge
            outcomesArrayBuilder.add(s);//
        }
        JsonArray outcomesArray = outcomesArrayBuilder.build();
        
        int numberOfBook=0;
        for(int i=0;i<((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).getText().length();i++){
            if(((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).getText().substring(i, i+1).equals("}"))
                numberOfBook++;
        }
        String textbooksText=((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)).getText();
        JsonArrayBuilder textbooksArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<numberOfBook;i++){
           JsonArrayBuilder authorArrayBuilder= Json.createArrayBuilder();
            String bookTitle=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"title\":")+8)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"title\":")+8)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"title\":")+8)+1));
            //System.out.println(bookTitle);
            
            String bookLink=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"link\":")+7)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"link\":")+7)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"link\":")+7)+1));
            
            
            String bookPhoto=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"photo\":")+8)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"photo\":")+8)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"photo\":")+8)+1));
         
            String authors=textbooksText.substring(textbooksText.indexOf("[")+1,textbooksText.indexOf("],"));
            String[] authorsBox=authors.split(",");
            textbooksText=textbooksText.substring(textbooksText.indexOf("],"));
            for(int j=0;j<authorsBox.length;j++){
                String author=authorsBox[j].substring(authorsBox[j].indexOf("\"")+1,authorsBox[j].indexOf("\"",authorsBox[j].indexOf("\"")+1));
                authorArrayBuilder.add(author);
            }
            
            String bookPublisher=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"publisher\":")+12)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"publisher\":")+12)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"publisher\":")+12)+1));
            
            
            String bookYear=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"year\":")+7)+1,textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"year\":")+7)+1));
            textbooksText=textbooksText.substring(textbooksText.indexOf("\"",textbooksText.indexOf("\"",textbooksText.indexOf("\"year\":")+7)+1));
            
            
            JsonArray author=authorArrayBuilder.build();
            JsonObject textbook=Json.createObjectBuilder().add(JSON_TITLE,bookTitle).add(JSON_LINK,bookLink).add(JSON_PHOTO, bookPhoto).add(JSON_AUTHORS, author).add(JSON_PUBLISHER, bookPublisher).add(JSON_YEAR, bookYear).build();
            textbooksArrayBuilder.add(textbook);
            
        }
        JsonArray textbooksArray=textbooksArrayBuilder.build();
        
        
        int numberOfGradedComponents=0;
        for(int i=0;i<((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).getText().length();i++){
            if(((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).getText().substring(i, i+1).equals("}"))
                numberOfGradedComponents++;
        }
        String gradedComponentText=((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)).getText();
        JsonArrayBuilder gradedComponentBuilder=Json.createArrayBuilder();
        for(int i=0;i<numberOfGradedComponents;i++){
            
            String name=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"name\":")+7)+1,gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"name\":")+7)+1));
            gradedComponentText=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"name\":")+7)+1));
            
            
            String gradeDescription=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"description\":")+14)+1,gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"description\":")+14)+1));
            gradedComponentText=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"description\":")+14)+1));
            
            String weight=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"weight\":")+9)+1,gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"weight\":")+9)+1));
            gradedComponentText=gradedComponentText.substring(gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"",gradedComponentText.indexOf("\"weight\":")+9)+1));
            
            
            
            
            JsonObject unit=Json.createObjectBuilder().add(JSON_NAME, name).add(JSON_DESCRIPTION, gradeDescription).add(JSON_WEIGHT, weight).build();
            gradedComponentBuilder.add(unit);
        }
        JsonArray gradedComponentArray=gradedComponentBuilder.build();
        String gradingNote=((TextArea)gui.getGUINode(CSG_GRADING_NOTE_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        String academicDishonesty=((TextArea)gui.getGUINode(CSG_ACADEMIC_DISHONESTY_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        String specialAssistance=((TextArea)gui.getGUINode(CSG_SPECIAL_ASSISTANCE_TEXTAREA)).getText().replace("\"", "").replace("\n", "");
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, description)
                .add(JSON_TOPICS, topicsArray)
                .add(JSON_PREREQUISITES, prerequisites)
                .add(JSON_OUTCOMES, outcomesArray)
                .add(JSON_TEXTBOOKS, textbooksArray)
                .add(JSON_GRADED_COMPONENTS, gradedComponentArray)
                .add(JSON_GRADING_NOTE, gradingNote)
                .add(JSON_ACADEMIC_DISHONESTY, academicDishonesty)
                .add(JSON_SPECIAL_ASSISTANCE, specialAssistance).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void exportOfficeHoursDataData(CourseSiteGeneratorData dataManager, String filePath)throws IOException{
        
        JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<LectureData> lecturesIterator = dataManager.lecturesIterator();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
         while (lecturesIterator.hasNext()) {
             LectureData l = lecturesIterator.next();
             ArrayList<String> days=l.getDayBox();
             ArrayList<String> times=l.getTimeBox();
             for(int day=0;day<days.size();day++){
                 for(int time=0;time<times.size();time++){
                     JsonObject unit =Json.createObjectBuilder().add(JSON_START_TIME, times.get(time))
                             .add(JSON_DAY_OF_WEEK, days.get(day))
                             .add(JSON_NAME, "Lecture").build();
                     officeHoursArrayBuilder.add(unit);
                 }
             }
         }
        
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        AppGUIModule gui = app.getGUIModule(); 
        String instructorName;
        String instructorLink;
        String instructorEmail;
        String instructorRoom;
        String instructorHours;
        String[] instrutorHoursBox;
        JsonArrayBuilder instructorOfficeHoursArrayBuilder = Json.createArrayBuilder();
        try{
            instructorName=((TextField)gui.getGUINode(CSG_NAME_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorName="";
        }
        try{
            instructorLink=((TextField)gui.getGUINode(CSG_HOMEPAGE_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorLink="";
        }
        try{
            instructorEmail=((TextField)gui.getGUINode(CSG_EMAIL_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorEmail="";
        }
        try{
            instructorRoom=((TextField)gui.getGUINode(CSG_ROOM_TEXTFIELD)).getText();}
        catch(NullPointerException e){
            instructorRoom="";
        }
        try{
            instructorHours=((TextArea)gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).getText().replace("[", "").replace("]", "").replace(" ", "").replace("{", "").replace("}", "").replace("\"day\":", "").replace("\"time\":", "").replace("\"", "").replace("\n", "");
            instrutorHoursBox=instructorHours.split(",");
            /*for(int i=0;i<instrutorHoursBox.length;i++){
                System.out.println(instrutorHoursBox[i]);
            }*/
            if(instrutorHoursBox.length>1)
            for(int i=0;i<instrutorHoursBox.length;i+=2){
                JsonObject ts=Json.createObjectBuilder().add(JSON_DAY,instrutorHoursBox[i])
                                                          .add(JSON_TIME, instrutorHoursBox[i+1]).build();
                
                instructorOfficeHoursArrayBuilder.add(ts);
            } 
            
        }
        catch(NullPointerException e){
            
            instructorHours="";
        }
        JsonArray instructorOfficeHoursArray=instructorOfficeHoursArrayBuilder.build();
        
        
        //JsonArray JsonInstructorHours=Json.
        JsonObject instructor = Json.createObjectBuilder().add(JSON_NAME,instructorName)
                                                          .add(JSON_LINK, instructorLink)
                                                          .add(JSON_EMAIL, instructorEmail)
                                                          .add(JSON_ROOM, instructorRoom)
                                                          .add(JSON_HOURS, instructorOfficeHoursArray)
                                                          .build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void exportSectionsData(AppGUIModule gui, String filePath)throws IOException{
        ObservableList<LectureData> lectureItems=((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getItems();
        JsonArrayBuilder lectureArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<lectureItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, lectureItems.get(i).getSection())
                    .add(JSON_DAYS, lectureItems.get(i).getDays()).add(JSON_TIME, lectureItems.get(i).getTime())
                    .add(JSON_ROOM, lectureItems.get(i).getRoom()).build();
            lectureArrayBuilder.add(unit);
        }
        JsonArray lectureArray=lectureArrayBuilder.build();
        
        ObservableList<RecitationData> recitationItems=((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getItems();
        JsonArrayBuilder recitationArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<recitationItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, recitationItems.get(i).getSection())
                    .add(JSON_DAY_TIME, recitationItems.get(i).getDayTime()).add(JSON_LOCATION, recitationItems.get(i).getLocation())
                    .add(JSON_TA1, recitationItems.get(i).getTA1()).add(JSON_TA2, recitationItems.get(i).getTA2()).build();
            recitationArrayBuilder.add(unit);
        }
        JsonArray recitationArray=recitationArrayBuilder.build();
        
        ObservableList<LabData> labItems=((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getItems();
        JsonArrayBuilder labArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<labItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, labItems.get(i).getSection())
                    .add(JSON_DAY_TIME, labItems.get(i).getDayTime()).add(JSON_LOCATION, labItems.get(i).getLocation())
                    .add(JSON_TA1, labItems.get(i).getTA1()).add(JSON_TA2, labItems.get(i).getTA2()).build();
            labArrayBuilder.add(unit);
        }
        JsonArray labArray=labArrayBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_LECTURES, lectureArray)
                .add(JSON_RECITATIONS, recitationArray)
                .add(JSON_LABS, labArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void exportScheduleData(AppGUIModule gui, String filePath)throws IOException{
        ObservableList<LectureData> lectureItems=((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getItems();
        JsonArrayBuilder lectureArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<lectureItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, lectureItems.get(i).getSection())
                    .add(JSON_DAYS, lectureItems.get(i).getDays()).add(JSON_TIME, lectureItems.get(i).getTime())
                    .add(JSON_ROOM, lectureItems.get(i).getRoom()).build();
            lectureArrayBuilder.add(unit);
        }
        JsonArray lectureArray=lectureArrayBuilder.build();
        
        ObservableList<RecitationData> recitationItems=((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getItems();
        JsonArrayBuilder recitationArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<recitationItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, recitationItems.get(i).getSection())
                    .add(JSON_DAY_TIME, recitationItems.get(i).getDayTime()).add(JSON_LOCATION, recitationItems.get(i).getLocation())
                    .add(JSON_TA1, recitationItems.get(i).getTA1()).add(JSON_TA2, recitationItems.get(i).getTA2()).build();
            recitationArrayBuilder.add(unit);
        }
        JsonArray recitationArray=recitationArrayBuilder.build();
        
        ObservableList<LabData> labItems=((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getItems();
        JsonArrayBuilder labArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<labItems.size();i++){
            JsonObject unit=Json.createObjectBuilder().add(JSON_SECTION, labItems.get(i).getSection())
                    .add(JSON_DAY_TIME, labItems.get(i).getDayTime()).add(JSON_LOCATION, labItems.get(i).getLocation())
                    .add(JSON_TA1, labItems.get(i).getTA1()).add(JSON_TA2, labItems.get(i).getTA2()).build();
            labArrayBuilder.add(unit);
        }
        JsonArray labArray=labArrayBuilder.build();
        
        ObservableList<ScheduleData> scheduleItems=((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).getItems();
        JsonArrayBuilder HolidaysArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder LecturesArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder ReferencesArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder RecitationArrayBuilder=Json.createArrayBuilder();
        JsonArrayBuilder HwsArrayBuilder=Json.createArrayBuilder();
        for(int i=0;i<scheduleItems.size();i++){
            String Type=scheduleItems.get(i).getType();
            String Date=scheduleItems.get(i).getDate();
            //System.out.print(Date);
            String Month="";
            String Day="";
            String Title=scheduleItems.get(i).getTitle();
            String Topic=scheduleItems.get(i).getTopic();
            JsonObjectBuilder unit=Json.createObjectBuilder();
            Month=Date.substring(0,Date.indexOf("/"));
            Day=Date.substring(Date.indexOf("/")+1,Date.indexOf("/",Date.indexOf("/")+1));
            //System.out.print(scheduleItems);
            
            switch(Type){
                case "Holiday":
                    HolidaysArrayBuilder
                            .add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "Lecture":
                    LecturesArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "Reference":
                    ReferencesArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "Recitation":
                    RecitationArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
                case "HW":
                    HwsArrayBuilder.add(unit.add(JSON_MONTH, Month).add(JSON_DAY, Day).add(JSON_TITLE,Title ).add(JSON_TOPIC, Topic).add(JSON_LINK, "").build());
                    break;
            }
        }
        JsonArray HolidaysArray=HolidaysArrayBuilder.build();
        JsonArray LecturesArray=LecturesArrayBuilder.build();
        JsonArray ReferencesArray=ReferencesArrayBuilder.build();
        JsonArray RecitationArray= RecitationArrayBuilder.build();
        JsonArray HwsArray=HwsArrayBuilder.build();
        
        String startMonayDate;
        String startMonayMonth;
        String startMonayDay;
        String endFridayDate;
        String endFridayMonth;
        String endFridayDay;
        if(((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)).getValue()!=null){
            startMonayDate=((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)).getValue().toString();
            startMonayMonth=startMonayDate.substring(0, startMonayDate.indexOf("/"));
            startMonayDay=startMonayDate.substring(startMonayDate.indexOf("/")+1,startMonayDate.indexOf("/",startMonayDate.indexOf("/")+1));
        }
        else{
            startMonayMonth="";
            startMonayDay="";
        }
        
        if(((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)).getValue()!=null){
            endFridayDate=((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)).getValue().toString();
            endFridayMonth=endFridayDate.substring(0, endFridayDate.indexOf("/"));
            endFridayDay=endFridayDate.substring(endFridayDate.indexOf("/")+1,endFridayDate.indexOf("/",endFridayDate.indexOf("/")+1));
        }
        else{
            endFridayMonth="";
            endFridayDay="";
        }
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_STARTING_MONDAY_MONTH, startMonayMonth)
                .add(JSON_STARTING_MONDAY_DAY, startMonayDay)
                .add(JSON_ENDING_FRIDAY_MONTH, endFridayMonth)
                .add(JSON_ENDING_FRIDAY_DAY, endFridayDay)
                .add(JSON_HOLIDAYS, HolidaysArray)
                .add(JSON_LECTURES, LecturesArray)
                .add(JSON_REFERENCES, ReferencesArray)
                .add(JSON_RECITATIONS, RecitationArray)
                .add(JSON_HWS, HwsArray).build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private ArrayList<String> getTimeBox(String time){
         ArrayList<String> box = new ArrayList<String>();
        String startTime = time.substring(0,time.indexOf("-"));
        String endTime = time.substring(time.indexOf("-")+1);
        String startHour;
        try{
            startHour = startTime.substring(0,startTime.indexOf(":"));
        }
        catch(Exception e){
            startHour = startTime.substring(0,1);
        }
        if(startHour.substring(0, 1).equals("0"))
            startHour=startHour.substring(1, 2);
        String endHour;
        try{
            endHour = endTime.substring(0,endTime.indexOf(":"));}
        catch(Exception e){
            endHour = endTime.substring(endTime.indexOf("-")+1,endTime.indexOf("-")+2);
        }
        if(endHour.substring(0, 1).equals("0"))
            endHour=endHour.substring(1, 2);
        String startMin;
       
        startMin = startTime.substring(startTime.indexOf(":")+1,startTime.length()-2);
        if(startMin.length()==1)
            startMin="00";
        
        String endMin;
        endMin = endTime.substring(endTime.indexOf(":")+1,endTime.length()-2);
        if(endMin.length()==1)
            endMin="00";
        
        String startSlot= startTime.substring(startTime.length()-2);
        String endSlot =endTime.substring(endTime.length()-2);
        if(startTime.length()==3){
            startTime=startHour+":00"+startSlot;
        }
        if(endTime.length()==3){
            endTime=endHour+":00"+endSlot;
        }
        if(startTime.substring(0, 1).equals("0"))
            startTime=startTime.substring(1);
        if(endTime.substring(0, 1).equals("0"))
            endTime=endTime.substring(1);
        if(startHour.equals(endHour)||(!startHour.equals(endHour)&&endMin.equals("00"))){
            box.add(startTime.replace(":", "_"));
            box.add(startHour+"_30"+startSlot);
        }
        else if(startMin.equals("00")){
            box.add(startTime.replace(":", "_"));
            box.add(startHour+"_30"+startSlot);
            box.add(endHour+"_00"+endSlot);
        }
        else if(startMin.equals("30")&&Integer.parseInt(endMin)>30){
            box.add(startTime.replace(":", "_"));
            box.add(endHour+"_00"+endSlot);
            box.add(endHour+"_30"+endSlot);
        }
        else{
            box.add(startTime.replace(":", "_"));
            box.add(endHour+"_00"+endSlot);
        }
        
       
        return box;
    
    }
    
    private ArrayList<String> getTime(String s){
        ArrayList<String> times = new ArrayList<String>();
        while(s.indexOf("\"time\"")!=-1){
            
            times.add(s.substring(s.indexOf("\"",s.indexOf("\"time\"")+6)+1,s.indexOf("m\"",s.indexOf("\"",s.indexOf("\"time\"")+6)+1)+1));
            s=s.substring(s.indexOf("m\"",s.indexOf("\"time\""))+1);
        }
        return times;
    }
    
    private ArrayList<String> getDay(String s){
        ArrayList<String> times = new ArrayList<String>();
        while(s.indexOf("\"day\"")!=-1){
            
            times.add(s.substring(s.indexOf("\"",s.indexOf("\"day\"")+5)+1,s.indexOf("day\"",s.indexOf("\"",s.indexOf("\"day\"")+5)+1)+3).toUpperCase());
            s=s.substring(s.indexOf("day\"",s.indexOf("\"day\""))+1);
        }
        return times;
    }
    
    
    
}