package csg.transactions;

import static csg.CourseSiteGeneratorPropertyType.CSG_ALL_RADIO_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_GRAD_RADIO_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.CSG_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_TAS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_UNDERGRAD_RADIO_BUTTON;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.workspace.controllers.CourseSiteGeneratorController;
import djf.modules.AppGUIModule;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;


/**
 *
 * @author luhaoyu
 */
public class EditOfficeHoursStartTime_Transaction implements jTPS_Transaction {
    RadioButton allRadio;
    RadioButton gradRadio;
    RadioButton undergradRadio;
    CourseSiteGeneratorData data;
    ComboBox c;
    int newStartTime;
    int oldStartTime;
    int endTime;
    CourseSiteGeneratorController controller;
    AppGUIModule gui;
    
    
    public EditOfficeHoursStartTime_Transaction(ComboBox c,Object newStartTime,Object oldStartTime,Object endTime,CourseSiteGeneratorData data,AppGUIModule gui,CourseSiteGeneratorController controller) {
        this.c=c;
        this.newStartTime=getTimeInt(newStartTime.toString());
        this.oldStartTime=getTimeInt(oldStartTime.toString());
        this.endTime=Integer.parseInt(endTime.toString());
        this.data=data;
        allRadio = (RadioButton) gui.getGUINode(CSG_ALL_RADIO_BUTTON);
        gradRadio = (RadioButton) gui.getGUINode(CSG_GRAD_RADIO_BUTTON);
        undergradRadio = (RadioButton) gui.getGUINode(CSG_UNDERGRAD_RADIO_BUTTON);
        this.controller=controller;
        this.gui=gui;
    }
    


    @Override
    public void doTransaction() {
        this.c.setValue(getTimeString(newStartTime,true));
        data.setStartHour(newStartTime);
        data.resetTime(newStartTime, endTime);
        if(undergradRadio.isSelected())controller.processSelectUndergradTAs();
        if(gradRadio.isSelected())controller.processSelectGradTAs();
        if(allRadio.isSelected())controller.processSelectAllTAs();
        ((TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW)).refresh();
        ((TableView) gui.getGUINode(CSG_TAS_TABLE_VIEW)).refresh();
    }

    @Override
    public void undoTransaction() {
        this.c.setValue(getTimeString(oldStartTime,true));
        data.setStartHour(oldStartTime);
        data.resetTime(oldStartTime, endTime);
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