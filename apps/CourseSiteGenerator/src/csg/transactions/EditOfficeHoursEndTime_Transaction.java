package csg.transactions;

import static csg.CourseSiteGeneratorPropertyType.CSG_ALL_RADIO_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_GRAD_RADIO_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_TAS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_UNDERGRAD_RADIO_BUTTON;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;
import csg.workspace.CourseSiteGeneratorWorkspace;
import csg.workspace.controllers.CourseSiteGeneratorController;
import djf.modules.AppGUIModule;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;


/**
 *
 * @author luhaoyu
 */
public class EditOfficeHoursEndTime_Transaction implements jTPS_Transaction {
    RadioButton allRadio;
    RadioButton gradRadio;
    RadioButton undergradRadio;
    CourseSiteGeneratorData data;
    ComboBox c;
    int newEndTime;
    int oldEndTime;
    int startTime;
    CourseSiteGeneratorController controller;
    AppGUIModule gui;
    
    
    public EditOfficeHoursEndTime_Transaction(ComboBox c,Object newEndTime,Object oldEndTime,Object startTime,CourseSiteGeneratorData data,AppGUIModule gui,CourseSiteGeneratorController controller) {
        this.c=c;
        this.newEndTime=getTimeInt(newEndTime.toString());
        this.oldEndTime=getTimeInt(oldEndTime.toString());
        this.startTime=Integer.parseInt(startTime.toString());
        this.data=data;
        allRadio = (RadioButton) gui.getGUINode(CSG_ALL_RADIO_BUTTON);
        gradRadio = (RadioButton) gui.getGUINode(CSG_GRAD_RADIO_BUTTON);
        undergradRadio = (RadioButton) gui.getGUINode(CSG_UNDERGRAD_RADIO_BUTTON);
        this.controller=controller;
        this.gui=gui;
    }
    


    @Override
    public void doTransaction() {
        this.c.setValue(getTimeString(newEndTime,true));
        data.setEndHour(newEndTime);
        data.resetTime(startTime, newEndTime);
        if(undergradRadio.isSelected())controller.processSelectUndergradTAs();
        if(gradRadio.isSelected())controller.processSelectGradTAs();
        if(allRadio.isSelected())controller.processSelectAllTAs();
        ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        ((TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW)).refresh();
    }

    @Override
    public void undoTransaction() {
        //System.out.println(oldEndTime);
        this.c.setValue(getTimeString(oldEndTime,true));
        data.setEndHour(oldEndTime);
        data.resetTime(startTime, oldEndTime);
        if(undergradRadio.isSelected())controller.processSelectUndergradTAs();
        if(gradRadio.isSelected())controller.processSelectGradTAs();
        if(allRadio.isSelected())controller.processSelectAllTAs();
        ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        ((TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW)).refresh();
        
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
    
    private String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
        
    
    
    
    
}