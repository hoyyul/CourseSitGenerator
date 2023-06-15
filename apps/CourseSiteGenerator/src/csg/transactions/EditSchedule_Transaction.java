package csg.transactions;

import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_DATE_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_ITEMS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_LINK_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_TITLE_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_TOPIC_TEXTFIELD;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_TYPE_COMBOBOX;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


/**
 *
 * @author luhaoyu
 */
public class EditSchedule_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    CourseSiteGeneratorData data;
    ScheduleData newSchedule;
    ScheduleData oldSchedule;
    
    public EditSchedule_Transaction(CourseSiteGeneratorApp initApp,CourseSiteGeneratorData initData, ScheduleData newSchedule,ScheduleData  oldSchedule) {
        app=initApp;
        data = initData;
        this.newSchedule = new ScheduleData(newSchedule.getType(),ScheduleData.toDateForm(newSchedule.getDate()),newSchedule.getTopic(),newSchedule.getTitle(),newSchedule.getLink());
        this.oldSchedule=new ScheduleData(oldSchedule.getType(),ScheduleData.toDateForm(oldSchedule.getDate()),oldSchedule.getTopic(),oldSchedule.getTitle(),oldSchedule.getLink());
    }

    @Override
    public void doTransaction() {
        
        ScheduleData s=data.getSchedule(oldSchedule);
        s.setType(newSchedule.getType());
        s.setDate(ScheduleData.toDateForm(newSchedule.getDate()));
        s.setTopic(newSchedule.getTopic());
        s.setTitle(newSchedule.getTitle());
        s.setLink(newSchedule.getLink());
        onEditSchedule();
        //System.out.println(s==oldSchedule);
        ((TableView)app.getGUIModule().getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).refresh();
    }

    @Override
    public void undoTransaction() {
        
        ScheduleData s=data.getSchedule(newSchedule);
        s.setType(oldSchedule.getType());
        s.setDate(ScheduleData.toDateForm(oldSchedule.getDate()));
        s.setTopic(oldSchedule.getTopic());
        s.setTitle(oldSchedule.getTitle());
        s.setLink(oldSchedule.getLink());
        onEditSchedule();
        ((TableView)app.getGUIModule().getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).refresh();
        
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
}
