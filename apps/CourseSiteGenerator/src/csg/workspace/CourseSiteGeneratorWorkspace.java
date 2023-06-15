package csg.workspace;
/**
 *
 * @author luhaoyu
 */
import static djf.AppPropertyType.APP_PATH_ICONS;
import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.DISABLED;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorPropertyType;
import static csg.CourseSiteGeneratorPropertyType.*;
import csg.data.LabData;
import csg.data.LectureData;
import csg.data.CourseSiteGeneratorData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.workspace.controllers.CourseSiteGeneratorController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CourseSiteGeneratorFoolproofDesign;
import static csg.workspace.style.CSGStyle.*;
import static djf.AppPropertyType.APP_EXPORT_PAGE;
import static djf.AppPropertyType.APP_EXPORT_PATH;
import static djf.AppPropertyType.CLOSE_BUTTON;
import static djf.AppPropertyType.CURRENT_CSS;
import static djf.AppPropertyType.EXIT_BUTTON;
import static djf.AppPropertyType.SAVE_BUTTON;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author luhaoyu
 */
public class CourseSiteGeneratorWorkspace extends AppWorkspaceComponent {
    public static String[] urls=new String[4];
    public CourseSiteGeneratorWorkspace(CourseSiteGeneratorApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // SETUP FOOLPROOF DESIGN FOR THIS APP
        initFoolproofDesign();
        
        initDialogs();
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        
        AppNodesBuilder csgBuilder = app.getGUIModule().getNodesBuilder();
        
        TabPane tPane = csgBuilder.buildTabPane(CSG_TAB_PANE, null, CLASS_CSG_TAB_PANE , ENABLED);
        
        
        
        VBox sitePane=csgBuilder.buildVBox(CSG_SITE_PANE, null, CLASS_CSG_SITE_PANE, ENABLED);
        VBox syllabusPane=csgBuilder.buildVBox(CSG_SYLLABUS_PANE, null, CLASS_CSG_SITE_PANE, ENABLED);
        VBox officeHoursPane=csgBuilder.buildVBox(CSE_OFFICEHOUR_PANE, null, CLASS_CSG_PANE, ENABLED);
        VBox sectionsPane=csgBuilder.buildVBox(CSG_SECTIONS_PANE, null, CLASS_CSG_SITE_PANE, ENABLED);
        VBox schedulePane=csgBuilder.buildVBox(CSG_SCHEDULE_PANE, null, CLASS_CSG_SITE_PANE, ENABLED);
        
        Tab siteTab= csgBuilder.buildTab(CLASS_CSG_PANE_TAB,CSG_SITE_TAB);
        Tab syllabusTab= csgBuilder.buildTab(CLASS_CSG_PANE_TAB,CSG_SYLLABUS_TAB);
        Tab sectionsTab= csgBuilder.buildTab(CLASS_CSG_PANE_TAB,CSG_SECTIONS_TAB);
        Tab officeHoursTab= csgBuilder.buildTab(CLASS_CSG_PANE_TAB,CSG_OFFICE_HOURS_TAB);
        Tab scheduleTab= csgBuilder.buildTab(CLASS_CSG_PANE_TAB,CSG_SCHEDULE_TAB);
        tPane.getTabs().addAll(siteTab,syllabusTab,sectionsTab,officeHoursTab,scheduleTab);
        
        
        
        
        
        //Sitetab
        VBox bannerPane= csgBuilder.buildVBox(CSG_BANNER_PANE,sitePane,CLASS_CSG_PANE, ENABLED);
        bannerPane.setSpacing(5);
        csgBuilder.buildLabel(CSG_BANNER_LABEL, bannerPane,CLASS_SITE_TITLE_LABEL, ENABLED);
        GridPane bannerBodyPane= csgBuilder.buildGridPane(CSG_BANNER_PANE,bannerPane,EMPTY_TEXT , ENABLED);
        bannerBodyPane.setVgap(20); bannerBodyPane.setHgap(30);
        csgBuilder.buildLabel(CSG_SUBJECT_LABEL, bannerBodyPane,0,0,1,1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_SEMESTER_LABEL, bannerBodyPane,0,1,1,1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_TITLE_LABEL, bannerBodyPane,0,2,1,1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_NUMBER_LABEL, bannerBodyPane,2,0,1,1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_YEAR_LABEL, bannerBodyPane,2,1,1,1, CLASS_SITE_LABEL, ENABLED);
        ComboBox subjectComboBox=csgBuilder.buildComboBox(CSG_SUBJECT_COMBOBOX, bannerBodyPane,1, 0, 1, 1, CLASS_CSG_COMBOBOX, ENABLED, EMPTY_TEXT, EMPTY_TEXT);//data
        ComboBox numberComboBox=csgBuilder.buildComboBox(CSG_NUMBER_COMBOBOX, bannerBodyPane,4, 0, 1, 1, CLASS_CSG_COMBOBOX, ENABLED, EMPTY_TEXT, EMPTY_TEXT);
        ComboBox semesterComboBox=csgBuilder.buildComboBox(CSG_SEMESTER_COMBOBOX, bannerBodyPane,1, 1, 1, 1, CLASS_CSG_COMBOBOX, ENABLED, SEMESTER_OPTIONS, EMPTY_TEXT);
        ComboBox yearComboBox=csgBuilder.buildComboBox(CSG_YEAR_COMBOBOX, bannerBodyPane,4, 1, 1, 1, CLASS_CSG_COMBOBOX, ENABLED, EMPTY_TEXT, EMPTY_TEXT);
        try{
        loadComboBox(subjectComboBox,"subject");}
        catch(Exception e){
            
        }
        try{
        loadComboBox(numberComboBox,"number");
        }
        catch(Exception e){
            
        }
        yearComboBox.getItems().add(Calendar.getInstance().get(Calendar.YEAR)+"");
        yearComboBox.getItems().add((Calendar.getInstance().get(Calendar.YEAR)+1)+"");
        
        TextField bannerTitleText=csgBuilder.buildTextField(CSG_TITLE_TEXTFIELD, bannerBodyPane, 1, 2, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        HBox exportPane=csgBuilder.buildHBox(CSG_EXPORT_PANE, bannerPane, EMPTY_TEXT, ENABLED);
        Label l1=csgBuilder.buildLabel(CSG_EXPORT_LABEL1, exportPane,CLASS_SITE_LABEL, ENABLED);
        Label l2=csgBuilder.buildLabel(CSG_EXPORT_LABEL2, exportPane,EMPTY_TEXT, ENABLED);
        exportPane.getChildren().add(new Label(" _ _ _ "));
        Label l3=csgBuilder.buildLabel(CSG_EXPORT_LABEL3, exportPane,EMPTY_TEXT, ENABLED);
        subjectComboBox.setEditable(ENABLED);
        numberComboBox.setEditable(ENABLED);
        
        
        
        subjectComboBox.valueProperty().addListener(new ChangeListener<String>() {
        @Override 
        public void changed(ObservableValue ov, String t, String t1) {
          props.addProperty(APP_EXPORT_PAGE, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("index_", "index."));
          props.addProperty(APP_EXPORT_PATH, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("/index_html", ""));
        }    
        });
        numberComboBox.valueProperty().addListener(new ChangeListener<String>() {
        @Override 
        public void changed(ObservableValue ov, String t, String t1) {
          props.addProperty(APP_EXPORT_PAGE, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("index_", "index."));
          props.addProperty(APP_EXPORT_PATH, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("/index_html", ""));
        }    
        });
        semesterComboBox.valueProperty().addListener(new ChangeListener<String>() {
        @Override 
        public void changed(ObservableValue ov, String t, String t1) {
          props.addProperty(APP_EXPORT_PAGE, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("index_", "index."));
          props.addProperty(APP_EXPORT_PATH, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("/index_html", ""));
        }    
        });
        yearComboBox.valueProperty().addListener(new ChangeListener<String>() {
        @Override 
        public void changed(ObservableValue ov, String t, String t1) {
          props.addProperty(APP_EXPORT_PAGE, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("index_", "index."));
          props.addProperty(APP_EXPORT_PATH, l2.getText().replace("\\", "/")+freshDirectory(subjectComboBox,numberComboBox,semesterComboBox,yearComboBox,exportPane)+l3.getText().replace("\\", "/").replace("/index_html", ""));
          
        }    
        });
        
        
        GridPane pagePane= csgBuilder.buildGridPane(CSG_PAGE_PANE,sitePane,CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(CSG_PAGE_BOX_LABEL, pagePane,0,0,1,1, CLASS_SITE_TITLE_LABEL, ENABLED);
        pagePane.add(new Label("               "), 1, 0);
        csgBuilder.buildCheckBox(CSG_HOME_CHECKBOX, pagePane,2,0,1,1,CLASS_CSG_BOX, ENABLED);
        csgBuilder.buildLabel(CSG_PAGE_HOME_LABEL, pagePane,3,0,1,1, CLASS_SITE_LABEL, ENABLED);
        pagePane.add(new Label("    "), 4, 0);
        csgBuilder.buildCheckBox(CSG_SYLLABUS_CHECKBOX, pagePane,5,0,1,1, CLASS_CSG_BOX, ENABLED);
        csgBuilder.buildLabel(CSG_PAGE_SYLLABUS_LABEL, pagePane,6,0,1,1, CLASS_SITE_LABEL, ENABLED);
        pagePane.add(new Label("    "), 7, 0);
        csgBuilder.buildCheckBox(CSG_SCHEDULE_CHECKBOX, pagePane,8,0,1,1, CLASS_CSG_BOX, ENABLED);
        csgBuilder.buildLabel(CSG_PAGE_SCHEDULE_LABEL, pagePane, 9,0,1,1,CLASS_SITE_LABEL, ENABLED);
        pagePane.add(new Label("    "), 10, 0);
        csgBuilder.buildCheckBox(CSG_HWS_CHECKBOX, pagePane,11,0,1,1, CLASS_CSG_BOX, ENABLED);
        csgBuilder.buildLabel(CSG_PAGE_HWS_LABEL, pagePane,12,0,1,1, CLASS_SITE_LABEL, ENABLED);
        
        
        GridPane StylePane = csgBuilder.buildGridPane(CSG_STYLE_PANE, sitePane, CLASS_CSG_PANE, ENABLED);
        StylePane.setVgap(20);
        csgBuilder.buildLabel(CSG_STYLE_LABEL, StylePane,0,0,1,1, CLASS_SITE_TITLE_LABEL, ENABLED);
        csgBuilder.buildTextButton(CSG_FAVICON_TEXTBUTTON, StylePane, 0, 1, 1, 1, CLASS_CSG_TEXT_FIELD , ENABLED);
        csgBuilder.buildTextButton(CSG_NAVBAR_TEXTBUTTON, StylePane, 0, 2, 1, 1, CLASS_CSG_TEXT_FIELD , ENABLED);
        csgBuilder.buildTextButton(CSG_LEFT_TEXTBUTTON, StylePane, 0, 3, 1, 1, CLASS_CSG_TEXT_FIELD , ENABLED);
        csgBuilder.buildTextButton(CSG_RIGHT_TEXTBUTTON, StylePane, 0, 4, 1, 1, CLASS_CSG_TEXT_FIELD , ENABLED);
        HBox styleHBox1= new HBox();
        HBox styleHBox2= new HBox();
        styleHBox1.setSpacing(20);
        csgBuilder.buildLabel(CSG_CHANGE_STYLE_LABEL, styleHBox1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_STYLE_HINT_LABEL, styleHBox2, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildComboBox(CSG_CSS_STYLE_COMBOBOX,CSS_OPTIONS,EMPTY_TEXT,styleHBox1, CLASS_CSG_COMBOBOX, ENABLED);
        StylePane.add(styleHBox1, 0, 5);
        StylePane.add(styleHBox2, 0, 6);
        
        
        VBox instructorPane=csgBuilder.buildVBox(CSG_INSTRUCTOR_PANE, sitePane, CLASS_CSG_PANE, ENABLED);
        GridPane instructorInformationPane = csgBuilder.buildGridPane(CSG_INSTRUCTOR_INFORMATION_PANE, instructorPane,EMPTY_TEXT , ENABLED);
        instructorInformationPane.setVgap(20);
        csgBuilder.buildLabel(CSG_INSTRUCTOR_LABEL, instructorInformationPane,0,0,1,1, CLASS_SITE_TITLE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_INSTRUCTOR_NAME_LABEL, instructorInformationPane, 0,1,1,1,CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_INSTRUCTOR_ROOM_LABEL, instructorInformationPane, 3,1,1,1,CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_INSTRUCTOR_EMAIL_LABEL, instructorInformationPane, 0,2,1,1,CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_INSTRUCTOR_HOMEPAGE_LABEL, instructorInformationPane, 3,2,1,1,CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildTextField(CSG_NAME_TEXTFIELD,instructorInformationPane, 1, 1, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(CSG_ROOM_TEXTFIELD,instructorInformationPane, 4, 1, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(CSG_EMAIL_TEXTFIELD,instructorInformationPane, 1, 2, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(CSG_HOMEPAGE_TEXTFIELD,instructorInformationPane, 4, 2, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        instructorInformationPane.add(new Label("    "), 2, 1);
        instructorInformationPane.add(new Label("    "), 2, 2);
        HBox InstructorOfficeHours= new HBox();
        HBox InstructorOfficeHoursText = new HBox();
        Button InstructorOfficeHoursButton=csgBuilder.buildIconButton(CSG_INSTRUCTOR_OFFICEHOUR_ADDBUTTON, null, CLASS_CSG_ICON_BUTTON, ENABLED);
        TextArea InstrutorOfficeHoursTextArea=csgBuilder.buildTextArea(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA,null,CLASS_CSG_TEXT_AREA,ENABLED);
        InstrutorOfficeHoursTextArea.setManaged(DISABLED);
        InstrutorOfficeHoursTextArea.setVisible(DISABLED);
        InstrutorOfficeHoursTextArea.setWrapText(ENABLED);
        InstructorOfficeHours.getChildren().add(InstructorOfficeHoursButton);
        InstructorOfficeHoursText.getChildren().add(InstrutorOfficeHoursTextArea);
        csgBuilder.buildLabel(CSG_INSTRUCTOR_OFFICEHOUR_LABEL, InstructorOfficeHours,CLASS_SITE_LABEL, ENABLED);
        InstructorOfficeHours.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(InstrutorOfficeHoursTextArea, Priority.ALWAYS);
        instructorPane.getChildren().add(InstructorOfficeHours);
        instructorPane.getChildren().add(InstructorOfficeHoursText);
        
        
        //Syllabus
        VBox syllabusTextPane1=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane1,CSG_DESCRIPTION_ADDBUTTON,CSG_DESCRIPTION_TEXTAREA,CSG_DESCRIPTION_LABEL);
        VBox syllabusTextPane2=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane2,CSG_TOPICS_ADDBUTTON,CSG_TOPICS_TEXTAREA,CSG_TOPICS_LABEL);
        VBox syllabusTextPane3=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane3,CSG_PREREQUISITES_ADDBUTTON,CSG_PREREQUISITES_TEXTAREA,CSG_PREREQUISITES_LABEL);
        VBox syllabusTextPane4=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane4,CSG_OUTCOMES_ADDBUTTON,CSG_OUTCOMES_TEXTAREA,CSG_OUTCOMES_LABEL);
        VBox syllabusTextPane5=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane5,CSG_TEXTBOOKS_ADDBUTTON,CSG_TEXTBOOKS_TEXTAREA,CSG_TEXTBOOKS_LABEL);
        VBox syllabusTextPane6=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane6,CSG_GRADED_COMPONENTS_ADDBUTTON,CSG_GRADED_COMPONENTS_TEXTAREA,CSG_GRADED_COMPONENTS_LABEL);
        VBox syllabusTextPane7=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane7,CSG_GRADING_NOTE_ADDBUTTON,CSG_GRADING_NOTE_TEXTAREA,CSG_GRADING_NOTE_LABEL);
        VBox syllabusTextPane8=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane8,CSG_ACADEMIC_DISHONESTY_ADDBUTTON,CSG_ACADEMIC_DISHONESTY_TEXTAREA,CSG_ACADEMIC_DISHONESTY_LABEL);
        VBox syllabusTextPane9=csgBuilder.buildVBox(CSG_SYLLABUS_TEXT_PANE, syllabusPane, CLASS_CSG_PANE, ENABLED);
        createSyllabusPane(csgBuilder,syllabusTextPane9,CSG_SPECIAL_ASSISTANCE_ADDBUTTON,CSG_SPECIAL_ASSISTANCE_TEXTAREA,CSG_SPECIAL_ASSISTANCE_LABEL);
        
        
        //Office Hours
        officeHoursPane.setSpacing(30);
        VBox officeHoursTAPane=csgBuilder.buildVBox(CSG_TA_OFFCIEHOUR_PANE, officeHoursPane, EMPTY_TEXT, ENABLED);
        officeHoursTAPane.setSpacing(10);
        HBox officeHoursPane1=csgBuilder.buildHBox(CSG_TAS_HEADER_PANE, officeHoursTAPane, EMPTY_TEXT, ENABLED);
        csgBuilder.buildIconButton(CSG_OFFICEHOUR_REMOVEBUTTON, officeHoursPane1, CLASS_CSG_ICON_BUTTON, ENABLED);
        csgBuilder.buildLabel(CSG_TA_LABEL, officeHoursPane1, CLASS_SITE_TITLE_LABEL, ENABLED);
        officeHoursPane1.setAlignment(Pos.CENTER_LEFT);
        //toglegroup
        ToggleGroup tg = new ToggleGroup();
        csgBuilder.buildRadioButton(CSG_ALL_RADIO_BUTTON, officeHoursPane1, CLASS_CSG_RADIO_BUTTON, ENABLED, tg, true);
        csgBuilder.buildRadioButton(CSG_GRAD_RADIO_BUTTON, officeHoursPane1, CLASS_CSG_RADIO_BUTTON, ENABLED, tg, false);
        csgBuilder.buildRadioButton(CSG_UNDERGRAD_RADIO_BUTTON, officeHoursPane1, CLASS_CSG_RADIO_BUTTON, ENABLED, tg, false);
        TableView<TeachingAssistantPrototype> taTable = csgBuilder.buildTableView(CSG_TAS_TABLE_VIEW, officeHoursTAPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        taTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = csgBuilder.buildTableColumn(CSG_NAME_TABLE_COLUMN, taTable, CLASS_CSG_COLUMN);
        TableColumn emailColumn = csgBuilder.buildTableColumn(CSG_EMAIL_TABLE_COLUMN, taTable, CLASS_CSG_COLUMN);
        TableColumn slotsColumn = csgBuilder.buildTableColumn(CSG_SLOTS_TABLE_COLUMN, taTable, CLASS_CSG_CENTERED_COLUMN);
        TableColumn typeColumn = csgBuilder.buildTableColumn(CSG_TYPE_TABLE_COLUMN, taTable, CLASS_CSG_CENTERED_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        
        

        HBox taBox = csgBuilder.buildHBox(CSG_ADD_TA_PANE, officeHoursTAPane, EMPTY_TEXT, ENABLED);
        taBox.setSpacing(5);
        csgBuilder.buildTextField(CSG_NAME_TEXT_FIELD, taBox, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(CSG_EMAIL_TEXT_FIELD, taBox, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextButton(CSG_ADD_TA_BUTTON, taBox, CLASS_CSG_BUTTON, !ENABLED);
        
        
        
        
        VBox officeHoursTogglePane=csgBuilder.buildVBox(CSG_OFFCIEHOUR_TOGGLE_PANE, officeHoursPane, EMPTY_TEXT, ENABLED);
        officeHoursTogglePane.setSpacing(10);
        HBox officeHoursPane2=csgBuilder.buildHBox(CSG_OFFICE_HOURS_HEADER_PANE, officeHoursTogglePane, EMPTY_TEXT, ENABLED);
        officeHoursPane2.setSpacing(3);
        csgBuilder.buildLabel(CSG_OFFICEHOUR_LABEL, officeHoursPane2, CLASS_SITE_TITLE_LABEL, ENABLED);
        officeHoursPane2.getChildren().add(new Label("                     "));
        csgBuilder.buildLabel(CSG_START_TIME_LABEL, officeHoursPane2, CLASS_SITE_TITLE_LABEL, ENABLED);
        ComboBox startTimeCombobox=csgBuilder.buildComboBox(CSG_START_TIME_COMBOBOX, CSG_START_TIME_OPTIONS, CSG_START_TIME_DEFAULT, officeHoursPane2, CLASS_CSG_COMBOBOX, ENABLED);
        officeHoursPane2.getChildren().add(new Label("    "));
        csgBuilder.buildLabel(CSG_END_TIME_LABEL, officeHoursPane2, CLASS_SITE_TITLE_LABEL, ENABLED);
        ComboBox endTimeCombobox=csgBuilder.buildComboBox(CSG_END_TIME_COMBOBOX, CSG_END_TIME_OPTIONS, CSG_END_TIME_DEFAULT, officeHoursPane2, CLASS_CSG_COMBOBOX, ENABLED);
        officeHoursPane2.setAlignment(Pos.CENTER_LEFT);
        TableView<TimeSlot> officeHoursTable = csgBuilder.buildTableView(CSG_OFFICE_HOURS_TABLE_VIEW, officeHoursTogglePane, CLASS_CSG_TABLE_VIEW, ENABLED);
        officeHoursTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupOfficeHoursColumn(CSG_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_CSG_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(CSG_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_CSG_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(CSG_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(CSG_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(CSG_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(CSG_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(CSG_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "friday");
        
       
        //Sections
        VBox lecturePane=csgBuilder.buildVBox(CSG_LECTURE_PANE, sectionsPane, CLASS_CSG_PANE, ENABLED);
        lecturePane.setSpacing(5);
        HBox lectureHeader=csgBuilder.buildHBox(CSG_LECTURE_PANE, lecturePane, EMPTY_TEXT, ENABLED);
        lectureHeader.setAlignment(Pos.CENTER_LEFT);
        Button addLectureButton=csgBuilder.buildIconButton(CSG_ADD_LECTURE_BUTTON, lectureHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        Button removeLectureButton=csgBuilder.buildIconButton(CSG_REMOVE_LECTURE_BUTTON, lectureHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        csgBuilder.buildLabel(CSG_LECTURE_HEADER_LABLE, lectureHeader, CLASS_SITE_TITLE_LABEL, ENABLED);
        TableView<LectureData> lectureTable = csgBuilder.buildTableView(CSG_LECTURE_TABLE_VIEW, lecturePane, CLASS_CSG_TABLE_VIEW, ENABLED);
        lectureTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        lectureTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lectureTable.getSelectionModel().setCellSelectionEnabled(ENABLED);
        lectureTable.setEditable(ENABLED);
        TableColumn section1Column = csgBuilder.buildTableColumn(CSG_LECTURE_SECTION_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        TableColumn days1Column = csgBuilder.buildTableColumn(CSG_LECTURE_DAYS_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        TableColumn time1Column = csgBuilder.buildTableColumn(CSG_LECTURE_TIME_TABLE_COLUMN, lectureTable, CLASS_CSG_CENTERED_COLUMN);
        TableColumn room1Column = csgBuilder.buildTableColumn(CSG_LECTURE_ROOM_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        section1Column.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        section1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        days1Column.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        days1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        time1Column.setCellValueFactory(new PropertyValueFactory<String, String>("time"));
        time1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        room1Column.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        room1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        section1Column.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        days1Column.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        time1Column.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        room1Column.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 4.0));
        lectureTable.setFixedCellSize(25);
        lectureTable.prefHeightProperty().bind(lectureTable.fixedCellSizeProperty().multiply(Bindings.size(lectureTable.getItems()).add(5)));
        lectureTable.minHeightProperty().bind(lectureTable.prefHeightProperty());
        lectureTable.maxHeightProperty().bind(lectureTable.prefHeightProperty());
        
        VBox recitationPane=csgBuilder.buildVBox(CSG_RECITATION_PANE, sectionsPane, CLASS_CSG_PANE, ENABLED);
        recitationPane.setSpacing(5);
        HBox recitationHeader=csgBuilder.buildHBox(CSG_RECITATION_PANE, recitationPane, EMPTY_TEXT, ENABLED);
        recitationHeader.setAlignment(Pos.CENTER_LEFT);
        Button addRecitationButton=csgBuilder.buildIconButton(CSG_ADD_RECITATION_BUTTON, recitationHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        Button removeRecitationButton=csgBuilder.buildIconButton(CSG_REMOVE_RECITATION_BUTTON, recitationHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        csgBuilder.buildLabel(CSG_RECITATION_HEADER_LABLE, recitationHeader, CLASS_SITE_TITLE_LABEL, ENABLED);
        TableView<RecitationData> recitationTable = csgBuilder.buildTableView(CSG_RECITATION_TABLE_VIEW, recitationPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        recitationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recitationTable.getSelectionModel().setCellSelectionEnabled(ENABLED);
        recitationTable.setEditable(ENABLED);
        TableColumn section2Column = csgBuilder.buildTableColumn(CSG_RECITATION_SECTION_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn daysTime2Column = csgBuilder.buildTableColumn(CSG_RECITATION_DAYS_TIME_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn room2Column = csgBuilder.buildTableColumn(CSG_RECITATION_ROOM_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn TA12Column = csgBuilder.buildTableColumn(CSG_RECITATION_TA1_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn TA22Column = csgBuilder.buildTableColumn(CSG_RECITATION_TA2_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        section2Column.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        section2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        daysTime2Column.setCellValueFactory(new PropertyValueFactory<String, String>("dayTime"));
        daysTime2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        room2Column.setCellValueFactory(new PropertyValueFactory<String, String>("location"));
        room2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        TA12Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA1"));
        TA12Column.setCellFactory(TextFieldTableCell.forTableColumn());
        TA22Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA2"));
        TA22Column.setCellFactory(TextFieldTableCell.forTableColumn());
        section2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 5.0));
        daysTime2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 5.0));
        room2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 5.0));
        TA12Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 5.0));
        TA22Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 5.0));
        recitationTable.setFixedCellSize(25);
        recitationTable.prefHeightProperty().bind(recitationTable.fixedCellSizeProperty().multiply(Bindings.size(recitationTable.getItems()).add(5)));
        recitationTable.minHeightProperty().bind(recitationTable.prefHeightProperty());
        recitationTable.maxHeightProperty().bind(recitationTable.prefHeightProperty());
        
        VBox labPane=csgBuilder.buildVBox(CSG_LAB_PANE, sectionsPane, CLASS_CSG_PANE, ENABLED);
        labPane.setSpacing(5);
        HBox labHeader=csgBuilder.buildHBox(CSG_LAB_PANE, labPane, EMPTY_TEXT, ENABLED);
        labHeader.setAlignment(Pos.CENTER_LEFT);
        Button addLabButton=csgBuilder.buildIconButton(CSG_ADD_LAB_BUTTON, labHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        Button removeLabButton=csgBuilder.buildIconButton(CSG_REMOVE_LAB_BUTTON, labHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        csgBuilder.buildLabel(CSG_LAB_HEADER_LABLE, labHeader, CLASS_SITE_TITLE_LABEL, ENABLED);
        TableView<LabData> labTable = csgBuilder.buildTableView(CSG_LAB_TABLE_VIEW, labPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        labTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        labTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        labTable.getSelectionModel().setCellSelectionEnabled(ENABLED);
        labTable.setEditable(ENABLED);
        TableColumn section3Column = csgBuilder.buildTableColumn(CSG_LAB_SECTION_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn daysTime3Column = csgBuilder.buildTableColumn(CSG_LAB_DAYS_TIME_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn room3Column = csgBuilder.buildTableColumn(CSG_LAB_ROOM_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn TA13Column = csgBuilder.buildTableColumn(CSG_LAB_TA1_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn TA23Column = csgBuilder.buildTableColumn(CSG_LAB_TA2_COLUMN, labTable, CLASS_CSG_COLUMN);
        section3Column.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        section3Column.setCellFactory(TextFieldTableCell.forTableColumn());
        daysTime3Column.setCellValueFactory(new PropertyValueFactory<String, String>("dayTime"));
        daysTime3Column.setCellFactory(TextFieldTableCell.forTableColumn());
        room3Column.setCellValueFactory(new PropertyValueFactory<String, String>("location"));
        room3Column.setCellFactory(TextFieldTableCell.forTableColumn());
        TA13Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA1"));
        TA13Column.setCellFactory(TextFieldTableCell.forTableColumn());
        TA23Column.setCellValueFactory(new PropertyValueFactory<String, String>("TA2"));
        TA23Column.setCellFactory(TextFieldTableCell.forTableColumn());
        section3Column.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        daysTime3Column.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        room3Column.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        TA13Column.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        TA23Column.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        labTable.setFixedCellSize(25);
        labTable.prefHeightProperty().bind(labTable.fixedCellSizeProperty().multiply(Bindings.size(labTable.getItems()).add(5)));
        labTable.minHeightProperty().bind(labTable.prefHeightProperty());
        labTable.maxHeightProperty().bind(labTable.prefHeightProperty());
        
        
        //Schedule
        VBox boundaryPane=csgBuilder.buildVBox(CSG_BOUNDARY_PANE, schedulePane, CLASS_CSG_PANE, ENABLED);
        boundaryPane.setSpacing(5);
        csgBuilder.buildLabel(CSG_BOUNDARY_LABEL, boundaryPane, CLASS_SITE_TITLE_LABEL, ENABLED);
        HBox boundarySelectPane = new HBox();
        boundarySelectPane.setSpacing(3);
        csgBuilder.buildLabel(CSG_BOUNDARY_START_TIME_LABEL, boundarySelectPane, CLASS_SITE_LABEL, ENABLED);
        ComboBox startMonday=csgBuilder.buildComboBox(CSG_BOUNDARY_START_TIME_COMBOBOX, EMPTY_TEXT, EMPTY_TEXT, boundarySelectPane, CLASS_CSG_COMBOBOX, ENABLED);
        initStartMondayComboBox(startMonday);
        boundarySelectPane.getChildren().add(new Label("            "));
        csgBuilder.buildLabel(CSG_BOUNDARY_END_TIME_LABEL, boundarySelectPane, CLASS_SITE_LABEL, ENABLED);
        ComboBox endFriday=csgBuilder.buildComboBox(CSG_BOUNDARY_END_TIME_COMBOBOX, EMPTY_TEXT, EMPTY_TEXT, boundarySelectPane, CLASS_CSG_COMBOBOX, ENABLED);
        initEndFridayComboBox(endFriday);
        boundarySelectPane.setAlignment(Pos.CENTER_LEFT);
        boundaryPane.getChildren().add(boundarySelectPane);
        
        VBox scheduleItemsPane=csgBuilder.buildVBox(CSG_BOUNDARY_PANE, schedulePane, CLASS_CSG_PANE, ENABLED);
        scheduleItemsPane.setSpacing(5);
        HBox scheduleItemHeader = new HBox();
        csgBuilder.buildIconButton(CSG_SCHEDULE_ITEMS_HEARDER_REMOVEBUTTON, scheduleItemHeader, CLASS_CSG_ICON_BUTTON, ENABLED);
        csgBuilder.buildLabel(CSG_SCHEDULE_ITEMS_HEARDER_LABEL, scheduleItemHeader, CLASS_SITE_TITLE_LABEL, ENABLED);
        scheduleItemHeader.setAlignment(Pos.CENTER_LEFT);
        scheduleItemsPane.getChildren().add(scheduleItemHeader);
        TableView<ScheduleData> scheduleItemsTable = csgBuilder.buildTableView(CSG_SCHEDULE_ITEMS_TABLE_VIEW, scheduleItemsPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        scheduleItemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        scheduleItemsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn scheduleTypeColumn = csgBuilder.buildTableColumn(CSG_SCHEDULE_TYPE_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        TableColumn scheduleDateColumn = csgBuilder.buildTableColumn(CSG_SCHEDULE_DATE_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        TableColumn scheduleTitleColumn = csgBuilder.buildTableColumn(CSG_SCHEDULE_TITLE_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        TableColumn scheduleTopicColumn = csgBuilder.buildTableColumn(CSG_SCHEDULE_TOPIC_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        scheduleTypeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        scheduleDateColumn.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        scheduleTitleColumn.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        scheduleTopicColumn.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        
        scheduleTypeColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(1.0 / 4.0));
        scheduleDateColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(1.0 / 4.0));
        scheduleTitleColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(1.0 / 4.0));
        scheduleTopicColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(1.0 / 4.0));
        
        
        VBox scheduleItemsEditPane=csgBuilder.buildVBox(CSG_SCHEDULE_EDIT_PANE, schedulePane, CLASS_CSG_PANE, ENABLED);
        scheduleItemsEditPane.setSpacing(5);
  
        csgBuilder.buildLabel(CSG_SCHEDULE_EDIT_LABEL, scheduleItemsEditPane, CLASS_SITE_TITLE_LABEL, ENABLED);
        GridPane scheduleItemsEditBodyPane=csgBuilder.buildGridPane(CSG_SCHEDULE_EDIT_BODY_PANE, scheduleItemsEditPane, EMPTY_TEXT, ENABLED);
        scheduleItemsEditBodyPane.setVgap(15);
        scheduleItemsEditBodyPane.setHgap(30);
        csgBuilder.buildLabel(CSG_SCHEDULE_TYPE_LABEL, scheduleItemsEditBodyPane, 0, 1, 1, 1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_SCHEDULE_DATE_LABEL, scheduleItemsEditBodyPane, 0, 2, 1, 1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_SCHEDULE_TITLE_LABEL, scheduleItemsEditBodyPane, 0, 3, 1, 1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_SCHEDULE_TOPIC_LABEL, scheduleItemsEditBodyPane, 0, 4, 1, 1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildLabel(CSG_SCHEDULE_LINK_LABEL, scheduleItemsEditBodyPane, 0, 5, 1, 1, CLASS_SITE_LABEL, ENABLED);
        csgBuilder.buildComboBox(CSG_SCHEDULE_TYPE_COMBOBOX, scheduleItemsEditBodyPane, 1, 1, 1, 1, CLASS_CSG_COMBOBOX, ENABLED, CSG_SCHEDULE_TYPE_OPTIONS, CSG_SCHEDULE_TYPE_DEFAULT);
        ComboBox scheduleDate=csgBuilder.buildComboBox(CSG_SCHEDULE_DATE_COMBOBOX, scheduleItemsEditBodyPane, 1, 2, 1, 1, CLASS_CSG_COMBOBOX, ENABLED, EMPTY_TEXT, EMPTY_TEXT);
        initScheduleDate(scheduleDate);
        //initScheduleDate(scheduleDate,startMonday.getSelectionModel().getSelectedItem().toString(),endFriday.getSelectionModel().getSelectedItem().toString());
        csgBuilder.buildTextField(CSG_SCHEDULE_TITLE_TEXTFIELD, scheduleItemsEditBodyPane,1,3,1,1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(CSG_SCHEDULE_TOPIC_TEXTFIELD, scheduleItemsEditBodyPane, 1,4,1,1,CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(CSG_SCHEDULE_LINK_TEXTFIELD, scheduleItemsEditBodyPane,1,5,1,1, CLASS_CSG_TEXT_FIELD, ENABLED);
        
        HBox scheduleEidtButtonPane = csgBuilder.buildHBox(CSG_SCHEDULE_EDIT_BUTTON_PANE,  schedulePane, EMPTY_TEXT, ENABLED);
        scheduleEidtButtonPane.setSpacing(20);
        Button scheduleEidtButton=csgBuilder.buildTextButton(CSG_ADD_EDIT_SCHEDULE_BUTTON, scheduleEidtButtonPane, CLASS_CSG_BUTTON, ENABLED);
        Button scheduleCleanButton=csgBuilder.buildTextButton(CSG_CLEAN_SCHEDULE_BUTTON, scheduleEidtButtonPane, CLASS_CSG_BUTTON, ENABLED);
        scheduleItemsEditPane.getChildren().add(scheduleEidtButtonPane);
        
        
        ScrollPane siteScroll = new ScrollPane();
        siteScroll.setContent(sitePane);
        siteScroll.setFitToWidth(true);
        siteTab.setContent(siteScroll);
        ScrollPane syllabusScroll = new ScrollPane();
        syllabusScroll.setContent(syllabusPane);
        syllabusScroll.setFitToWidth(true);
        syllabusTab.setContent(syllabusScroll);
        ScrollPane officeHoursScroll = new ScrollPane();
        officeHoursScroll.setContent(officeHoursPane);
        officeHoursScroll.setFitToWidth(true);
        officeHoursTab.setContent(officeHoursScroll);
        ScrollPane sectionsScroll = new ScrollPane();
        sectionsScroll.setContent(sectionsPane);
        sectionsScroll.setFitToWidth(true);
        sectionsTab.setContent(sectionsScroll);
        ScrollPane scheduleScroll = new ScrollPane();
        scheduleScroll.setContent(schedulePane);
        scheduleScroll.setFitToWidth(true);
        scheduleTab.setContent(scheduleScroll);
        
        
        
        workspace = new VBox();
        ((VBox)workspace).getChildren().add(tPane);
        tPane.widthProperty().addListener((observable, oldValue, newValue) ->
        {
        tPane.setTabMinWidth(tPane.getWidth() / 5-20);
        tPane.setTabMaxWidth(tPane.getWidth() / 5-20);      
        });
        
        ((GridPane)app.getGUIModule().getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:./images/SBUShieldFavicon.ico"),1,1);
        ((GridPane)app.getGUIModule().getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:./images/SBUDarkRedShieldLogo.png"),1,2);
        ((GridPane)app.getGUIModule().getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:./images/SBUWhiteShieldLogo.jpg"),1,3);
        ((GridPane)app.getGUIModule().getGUINode(CSG_STYLE_PANE)).add(new ImageView("file:./images/SBUCSLogo.png"),1,4);
        
        
        
        
    }

    private void initControllers() {
        CourseSiteGeneratorController controller = new CourseSiteGeneratorController((CourseSiteGeneratorApp) app);
        AppGUIModule gui = app.getGUIModule();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        ((Button) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_ADDBUTTON)).setOnAction(e -> {
            if(((TextArea) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).visibleProperty().get()==false){
                ((Button) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
                ((TextArea) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).setManaged(ENABLED);
                ((TextArea) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).setVisible(ENABLED);
            }
            else{
                ((Button) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_ADDBUTTON)).setGraphic(new ImageView(new Image("file:./images/icons/AddItem.png")));
                ((TextArea) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).setManaged(DISABLED);
                ((TextArea) gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)).setVisible(DISABLED);
            }
        });
        
        createSyllabusController(gui,CSG_DESCRIPTION_ADDBUTTON,CSG_DESCRIPTION_TEXTAREA);
        createSyllabusController(gui,CSG_TOPICS_ADDBUTTON,CSG_TOPICS_TEXTAREA);
        createSyllabusController(gui,CSG_PREREQUISITES_ADDBUTTON,CSG_PREREQUISITES_TEXTAREA);
        createSyllabusController(gui,CSG_OUTCOMES_ADDBUTTON,CSG_OUTCOMES_TEXTAREA);
        createSyllabusController(gui,CSG_TEXTBOOKS_ADDBUTTON,CSG_TEXTBOOKS_TEXTAREA);
        createSyllabusController(gui,CSG_GRADED_COMPONENTS_ADDBUTTON,CSG_GRADED_COMPONENTS_TEXTAREA);
        createSyllabusController(gui,CSG_GRADING_NOTE_ADDBUTTON,CSG_GRADING_NOTE_TEXTAREA);
        createSyllabusController(gui,CSG_ACADEMIC_DISHONESTY_ADDBUTTON,CSG_ACADEMIC_DISHONESTY_TEXTAREA);
        createSyllabusController(gui,CSG_SPECIAL_ASSISTANCE_ADDBUTTON,CSG_SPECIAL_ASSISTANCE_TEXTAREA);
        
        
        TextField nameTextField = ((TextField) gui.getGUINode(CSG_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(CSG_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(CSG_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });

        TableView officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });
        //System.out.print((RadioButton) gui.getGUINode(CSG_ALL_RADIO_BUTTON));
        RadioButton allRadio = (RadioButton) gui.getGUINode(CSG_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
            ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(CSG_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
            ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(CSG_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
            ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        });
        
        ComboBox startTimeComboBox = (ComboBox) gui.getGUINode(CSG_START_TIME_COMBOBOX);
        ComboBox endTimeComboBox = (ComboBox) gui.getGUINode(CSG_END_TIME_COMBOBOX);
        
        
        ((Button) gui.getGUINode(CSG_OFFICEHOUR_REMOVEBUTTON)).setOnAction(e -> {
            controller.processRemoveTA();
        });
        performOfficeHourStartTime(startTimeComboBox,controller,endTimeComboBox);
        performOfficeHourEndTime(endTimeComboBox,controller,startTimeComboBox);
        
       
        
        ((Button) gui.getGUINode(CSG_FAVICON_TEXTBUTTON)).setOnMouseClicked(e -> {
            File f=CourseSiteGeneratorController.showImageSelectDialog(app.getGUIModule().getWindow());
            if(f!=null){
                controller.processSetImage(gui,f,"Favicon");
           }
            
        });
        ((Button) gui.getGUINode(CSG_NAVBAR_TEXTBUTTON)).setOnMouseClicked(e -> {
            File f=CourseSiteGeneratorController.showImageSelectDialog(app.getGUIModule().getWindow());
            if(f!=null){
                controller.processSetImage(gui,f,"Navbar");
            }
        });
        ((Button) gui.getGUINode(CSG_LEFT_TEXTBUTTON)).setOnMouseClicked(e -> {
            File f=CourseSiteGeneratorController.showImageSelectDialog(app.getGUIModule().getWindow());
            if(f!=null){
                controller.processSetImage(gui,f,"Left");
            }
        });
        ((Button) gui.getGUINode(CSG_RIGHT_TEXTBUTTON)).setOnMouseClicked(e -> {
            File f=CourseSiteGeneratorController.showImageSelectDialog(app.getGUIModule().getWindow());
            if(f!=null){
                controller.processSetImage(gui,f,"Right");
            }
        });
        
        ((Button) gui.getGUINode(CSG_ADD_LECTURE_BUTTON)).setOnAction(e->{
            controller.processAddLecture();
        });
        
        
        
        ((TableColumn)((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getColumns().get(0)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLectureSection(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getColumns().get(1)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLectureDays(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getColumns().get(2)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLectureTime(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getColumns().get(3)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLectureRoom(s1,s2);
        });
        ((Button)gui.getGUINode(CSG_REMOVE_LECTURE_BUTTON)).setOnAction(e->{
            if(((TableView)gui.getGUINode(CSG_LECTURE_TABLE_VIEW)).getSelectionModel().getSelectedItem()!=null){
                controller.processRemoveLecture();
            }
        });
        
        ((Button) gui.getGUINode(CSG_ADD_RECITATION_BUTTON)).setOnAction(e->{
            controller.processAddRecitation();
        });
         ((TableColumn)((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getColumns().get(0)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditRecitationSection(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getColumns().get(1)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditRecitationDayTime(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getColumns().get(2)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditRecitationRoom(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getColumns().get(3)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditRecitationTA1(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getColumns().get(4)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditRecitationTA2(s1,s2);
        });
        ((Button)gui.getGUINode(CSG_REMOVE_RECITATION_BUTTON)).setOnAction(e->{
            if(((TableView)gui.getGUINode(CSG_RECITATION_TABLE_VIEW)).getSelectionModel().getSelectedItem()!=null){
                controller.processRemoveRecitation();
            }
        });
        
        ((Button) gui.getGUINode(CSG_ADD_LAB_BUTTON)).setOnAction(e->{
            controller.processAddLab();
        });
         ((TableColumn)((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getColumns().get(0)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLabSection(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getColumns().get(1)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLabDayTime(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getColumns().get(2)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLabRoom(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getColumns().get(3)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLabTA1(s1,s2);
        });
        ((TableColumn)((TableView)gui.getGUINode(CSG_LAB_TABLE_VIEW)).getColumns().get(4)).setOnEditCommit(e->{
            String s1=((CellEditEvent)e).getNewValue().toString();
            String s2=((CellEditEvent)e).getOldValue().toString();
            controller.processEditLabTA2(s1,s2);
        });
        ((Button)gui.getGUINode(CSG_REMOVE_LAB_BUTTON)).setOnAction(e->{
            
                controller.processRemoveLab();
            
        });
        ((Button)gui.getGUINode(CSG_ADD_EDIT_SCHEDULE_BUTTON)).setOnAction(e->{
            if(((TableView) gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).getSelectionModel().getSelectedItem()==null){
                controller.processAddSchedule();
            }
            else{
                controller.processEditScheule();
            }
        });
        
        ((Button)gui.getGUINode(CSG_SCHEDULE_ITEMS_HEARDER_REMOVEBUTTON)).setOnAction(e->{
                controller.processRemoveSchedule();
            
        });
        
        ((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).setOnMouseClicked(e->{
            onEditSchedule();
        
        });
        
        ((Button)gui.getGUINode(CSG_CLEAN_SCHEDULE_BUTTON)).setOnAction(e->{
            ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).setText("");
            ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).setText("");
            ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).setText("");
            ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).setValue("Options");
            ((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).valueProperty().set(null);
            ((TableView) gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).getSelectionModel().clearSelection();
            
        
        });
        
        
        performFocusTextField(((TextField)gui.getGUINode(CSG_TITLE_TEXTFIELD)),controller);
        performFocusTextField(((TextField)gui.getGUINode(CSG_NAME_TEXTFIELD)),controller);
        performFocusTextField(((TextField)gui.getGUINode(CSG_ROOM_TEXTFIELD)),controller);
        performFocusTextField(((TextField)gui.getGUINode(CSG_EMAIL_TEXTFIELD)),controller);
        performFocusTextField(((TextField)gui.getGUINode(CSG_HOMEPAGE_TEXTFIELD)),controller);
        performFocusEditableComboBox(((ComboBox)gui.getGUINode(CSG_SUBJECT_COMBOBOX)),controller,"subject");
        performFocusComboBox(((ComboBox)gui.getGUINode(CSG_YEAR_COMBOBOX)),controller);
        performFocusComboBox(((ComboBox)gui.getGUINode(CSG_SEMESTER_COMBOBOX)),controller);
        performFocusEditableComboBox(((ComboBox)gui.getGUINode(CSG_NUMBER_COMBOBOX)),controller,"number");
        performFocusScheduleComboBox(((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)),((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)),controller);
        performFocusScheduleComboBox(((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX)),((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_INSTRUCTOR_OFFICEHOUR_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_DESCRIPTION_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_TOPICS_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_PREREQUISITES_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_OUTCOMES_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_TEXTBOOKS_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_GRADED_COMPONENTS_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_GRADING_NOTE_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_ACADEMIC_DISHONESTY_TEXTAREA)),controller);
        performFocusTextArea(((TextArea)gui.getGUINode(CSG_SPECIAL_ASSISTANCE_TEXTAREA)),controller);
        
        ((CheckBox)gui.getGUINode(CSG_HOME_CHECKBOX)).setOnAction(e->{
            
            controller.processSetCheckBox(((CheckBox)gui.getGUINode(CSG_HOME_CHECKBOX)),((CheckBox)gui.getGUINode(CSG_HOME_CHECKBOX)).isSelected());
        });
        
        ((CheckBox)gui.getGUINode(CSG_SYLLABUS_CHECKBOX)).setOnAction(e->{
            
            controller.processSetCheckBox(((CheckBox)gui.getGUINode(CSG_SYLLABUS_CHECKBOX)),((CheckBox)gui.getGUINode(CSG_SYLLABUS_CHECKBOX)).isSelected());
        });
        
        ((CheckBox)gui.getGUINode(CSG_SCHEDULE_CHECKBOX)).setOnAction(e->{
            
            controller.processSetCheckBox(((CheckBox)gui.getGUINode(CSG_SCHEDULE_CHECKBOX)),((CheckBox)gui.getGUINode(CSG_SCHEDULE_CHECKBOX)).isSelected());
        });
        
        ((CheckBox)gui.getGUINode(CSG_HWS_CHECKBOX)).setOnAction(e->{
            
            controller.processSetCheckBox(((CheckBox)gui.getGUINode(CSG_HWS_CHECKBOX)),((CheckBox)gui.getGUINode(CSG_HWS_CHECKBOX)).isSelected());
        });
        
        
        ((ComboBox)gui.getGUINode(CSG_CSS_STYLE_COMBOBOX)).setOnAction(e->{
            
            if(((ComboBox)gui.getGUINode(CSG_CSS_STYLE_COMBOBOX)).getValue().toString().equals("sea_wolf.css"))
                props.addProperty(CURRENT_CSS, "/css/style1.css");
            else
                props.addProperty(CURRENT_CSS, "/css/style2.css");
            
        });
        
    }

    private void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(CSG_FOOLPROOF_SETTINGS,
                new CourseSiteGeneratorFoolproofDesign((CourseSiteGeneratorApp) app));
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
    
    
    private void createSyllabusPane(AppNodesBuilder csgBuilder,Pane parent,Object button_nodeID,Object textarea_nodeID,Object label_nodeID){
        HBox Title= new HBox();
        HBox Text = new HBox();
        Button button=csgBuilder.buildIconButton(button_nodeID, null, CLASS_CSG_ICON_BUTTON, ENABLED);
        TextArea textArea=csgBuilder.buildTextArea(textarea_nodeID,null,CLASS_CSG_TEXT_AREA,ENABLED);
        textArea.setManaged(DISABLED);
        textArea.setVisible(DISABLED);
        textArea.setWrapText(ENABLED);
        Title.getChildren().add(button);
        Text.getChildren().add(textArea);
        csgBuilder.buildLabel(label_nodeID, Title,CLASS_SITE_LABEL, ENABLED);
        Title.setAlignment(Pos.CENTER_LEFT);
         HBox.setHgrow(textArea, Priority.ALWAYS);
        parent.getChildren().add(Title);
        parent.getChildren().add(Text);
    }
    
    private void createSyllabusController(AppGUIModule gui,Object button_nodeID,Object textarea_nodeID){
        ((Button) gui.getGUINode(button_nodeID)).setOnAction(e -> {
            if(((TextArea) gui.getGUINode(textarea_nodeID)).visibleProperty().get()==false){
                ((Button) gui.getGUINode(button_nodeID)).setGraphic(new ImageView(new Image("file:./images/icons/RemoveItem.png")));
                ((TextArea) gui.getGUINode(textarea_nodeID)).setManaged(ENABLED);
                ((TextArea) gui.getGUINode(textarea_nodeID)).setVisible(ENABLED);
            }
            else{
                ((Button) gui.getGUINode(button_nodeID)).setGraphic(new ImageView(new Image("file:./images/icons/AddItem.png")));
                ((TextArea) gui.getGUINode(textarea_nodeID)).setManaged(DISABLED);
                ((TextArea) gui.getGUINode(textarea_nodeID)).setVisible(DISABLED);
            }
        });
    }
    
    
    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(CSG_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
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
    
    private void initDialogs() {
        TADialog taDialog = new TADialog((CourseSiteGeneratorApp) app);
        app.getGUIModule().addDialog(CSG_TA_EDIT_DIALOG, taDialog);
    }
    
    private void tableRefresh(CourseSiteGeneratorApp app){
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
    
    private String freshDirectory(ComboBox subject,ComboBox number,ComboBox semester, ComboBox year,HBox exportPane){
        
        String[] directory=new String[4];
        String newS="";
        if(subject.getValue()!=null)
            directory[0]=subject.getValue().toString();
        else
            directory[0]="";
        if(number.getValue()!=null)
            directory[1]=number.getValue().toString();
        else
            directory[1]="";
        if(semester.getValue()!=null)
            directory[2]=semester.getValue().toString();
        else
            directory[2]="";
        if(year.getValue()!=null)
            directory[3]=year.getValue().toString();
        else
            directory[3]="";
        newS+=directory[0]+"_"+directory[1]+"_"+directory[2]+"_"+directory[3];
        ((Label)exportPane.getChildren().get(2)).setText(newS);
        return newS;
        
        
    }
    
    private void initStartMondayComboBox(ComboBox c){
        Year year = Year.now();
        Month month = Month.valueOf("January".toUpperCase());
        LocalDate date = year.atMonth(month).atDay(1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        while (date.getYear() <= year.getValue()+1) {
            
            c.getItems().add(ScheduleData.toStringForm(date.toString()));
            date = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            
        }
        
    }
    
    private void initEndFridayComboBox(ComboBox c){
        Year year = Year.now();
        Month month = Month.valueOf("January".toUpperCase());
        LocalDate date = year.atMonth(month).atDay(1).with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY));
        
        while (date.getYear() <= year.getValue()+1) {
            
            c.getItems().add(ScheduleData.toStringForm(date.toString()));
            date = date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            
        }
    }
    
    private void initScheduleDate(ComboBox c){
        Year year = Year.now();
        LocalDate date = year.atMonth(Month.valueOf("January".toUpperCase())).atDay(1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        while(date.getYear()<=year.getValue()+1){
            c.getItems().add(ScheduleData.toStringForm(date.toString()));
            date = LocalDate.parse(date.toString()).plusDays(1);
        }
    }
    
    private void onEditSchedule(){
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleData> scheduleTable =(TableView) gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        if(scheduleTable.getSelectionModel().getSelectedItem()!=null){
            ScheduleData schedule = scheduleTable.getSelectionModel().getSelectedItem();
            ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).setText(schedule.getTitle());
            ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).setText(schedule.getTopic());
            ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).setText(schedule.getLink());
            ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).setValue(schedule.getType());
            ((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).valueProperty().set(schedule.getDate());
        }
        else{
            ((TextField) gui.getGUINode(CSG_SCHEDULE_TITLE_TEXTFIELD)).setText("");
            ((TextField) gui.getGUINode(CSG_SCHEDULE_TOPIC_TEXTFIELD)).setText("");
            ((TextField) gui.getGUINode(CSG_SCHEDULE_LINK_TEXTFIELD)).setText("");
            ((ComboBox) gui.getGUINode(CSG_SCHEDULE_TYPE_COMBOBOX)).setValue("Options");
            ((ComboBox) gui.getGUINode(CSG_SCHEDULE_DATE_COMBOBOX)).valueProperty().set(null);
        }
    
    }
    
    private void performFocusTextField(TextField text,CourseSiteGeneratorController controller){
        String[] s= new String[]{"",""};
        text.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {  
            if (newPropertyValue)
            { 
                s[0]= text.getText();
                
            }
            else{
                s[1]=text.getText();
                if(!s[0].equals(s[1])){
                    controller.processEditTextField(text, s[0], s[1]);
                }
            }
        }
        });
    }
    
    private void performFocusEditableComboBox(ComboBox c,CourseSiteGeneratorController controller){
        String[] s= new String[]{"",""};
        c.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {
            if (newPropertyValue){ 
        
                if(c.getValue()!=null){
                    s[0]= c.getValue().toString();
                    
                }
                comboBoxTextField(c,s);
            }
            else{
                if(!s[0].equals(s[1])){
                    controller.processEditComboBox(c, s[1], s[0]);
                    //System.out.print(5);
                }
                
            }
            //c.getItems().remove("");
        }
        });
    }
    
    private void performFocusEditableComboBox(ComboBox c,CourseSiteGeneratorController controller,String S){
        String[] s= new String[]{"",""};
        c.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {
            if (newPropertyValue){ 
        
                if(c.getValue()!=null){
                    s[0]= c.getValue().toString();
                    s[1]=s[0];
                    
                }
                comboBoxTextField(c,s);
            }
            else{
                if(!s[0].equals(s[1])){
                    //System.out.println("old: "+s[0]+"new: "+s[1]);
                    controller.processEditComboBox(c, s[1], s[0],S);
                }
                
            }
        }
        });
    }
    
    private void comboBoxTextField(ComboBox c,String[] s){
        
        
        c.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            s[1]=newValue;
        }
        });
    }
    
    private void performFocusComboBox(ComboBox c,CourseSiteGeneratorController controller){
        String[] s= new String[]{"",""};
        c.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {
            if (newPropertyValue)
            {   if(c.getValue()!=null)
                    s[0]= c.getValue().toString();
                
            }
            else{
                if(c.getValue()!=null){
                s[1]=c.getValue().toString();
                if(!s[0].equals(s[1])){
                    controller.processEditComboBox(c, s[1], s[0]);
                    
                }
                }
            }
        }
        });
        
        
    }
    
    private void performFocusScheduleComboBox(ComboBox c1,ComboBox c2,CourseSiteGeneratorController controller){
        String[] s= new String[]{"",""};
        c1.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {
            if (newPropertyValue)
            {   if(c1.getValue()!=null)
                    s[0]= c1.getValue().toString();
                
            }
            else{
                if(c1.getValue()!=null){
                s[1]=c1.getValue().toString();
                if(!s[0].equals(s[1])){
                    controller.processEditScheduleComboBox(c1,c2, s[1], s[0]);
                    
                }
                }
            }
        }
        });
        
        
    }
    
    private void loadComboBox(ComboBox c,String s) throws IOException{
        try{
        JsonObject json = loadJSONFile("./work/"+s+"Options.json");//
        for(int i =0;i<json.getJsonArray(s).size();i++){
            c.getItems().add(json.getJsonArray(s).getString(i));
        }
        }
        catch(IOException ie){
            
        }
    }
    
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    private void performFocusTextArea(TextArea text,CourseSiteGeneratorController controller){
        String[] s= new String[]{"",""};
        text.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {  
            if (newPropertyValue)
            { 
                s[0]= text.getText();
                
            }
            else{
                s[1]=text.getText();
                if(!s[0].equals(s[1])){
                    controller.processEditTextField(text, s[0], s[1]);
                }
            }
        }
        });
    }
    
    private void performOfficeHourStartTime(ComboBox c,CourseSiteGeneratorController controller,ComboBox endTimeComboBox){
        String[] s= new String[]{"",""};
        c.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {
            if (newPropertyValue)
            {   if(c.getValue()!=null)
                    s[0]= c.getValue().toString();
                
            }
            else{
                if(c.getValue()!=null){
                s[1]=c.getValue().toString();
                if(!s[0].equals(s[1])){
                    int endTime=getTimeInt(endTimeComboBox.getSelectionModel().getSelectedItem().toString());
                    controller.processOfficeHourStartTimeTransaction(c, s[1], s[0],endTime);
                    
                }
                }
            }
        }
        });
        
        
    }
    
    private void performOfficeHourEndTime(ComboBox c,CourseSiteGeneratorController controller,ComboBox startTimeComboBox){
        String[] s= new String[]{"",""};
        c.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
        {
            if (newPropertyValue)
            {   if(c.getValue()!=null)
                    s[0]= c.getValue().toString();
                
            }
            else{
                if(c.getValue()!=null){
                s[1]=c.getValue().toString();
                if(!s[0].equals(s[1])){
                    int startTime=getTimeInt(startTimeComboBox.getSelectionModel().getSelectedItem().toString());
                    controller.processOfficeHourEndTimeTransaction(c, s[1], s[0],startTime);
                    
                }
                }
            }
        }
        });
        
        
    }
}
