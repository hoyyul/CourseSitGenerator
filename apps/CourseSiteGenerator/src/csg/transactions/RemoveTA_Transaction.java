/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;
/**
 *
 * @author luhaoyu
 */
import djf.modules.AppGUIModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_OFFICE_HOURS_TABLE_VIEW;
import csg.data.CourseSiteGeneratorData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;


public class RemoveTA_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    CourseSiteGeneratorData data;
    TeachingAssistantPrototype ta;
    HashMap<TimeSlot, ArrayList<TimeSlot.DayOfWeek>> timeslots;
    
    
    
    public RemoveTA_Transaction(CourseSiteGeneratorApp initApp,
                                CourseSiteGeneratorData initData, 
                                TeachingAssistantPrototype initTA
                                ) {
        app=initApp;
        data = initData;
        ta = initTA;
        timeslots=data.getTATimeSlots(ta);
    }

    @Override
    public void doTransaction() { 
        //System.out.print(timeslots);
        Iterator it = timeslots.entrySet().iterator();
        while(it.hasNext()){
           Map.Entry pair = (Map.Entry)it.next();
           TimeSlot ts=(TimeSlot)pair.getKey();
           for(TimeSlot.DayOfWeek dow:(ArrayList<TimeSlot.DayOfWeek>)pair.getValue()){
                ts.toggleTA(dow, ta);
                tableRefresh(app);
           }
        }
        
        data.removeTA(ta);
    }

    @Override
    public void undoTransaction() {
        
        Iterator it = timeslots.entrySet().iterator();
        while(it.hasNext()){
           Map.Entry pair = (Map.Entry)it.next();
           TimeSlot ts=(TimeSlot)pair.getKey();
           for(TimeSlot.DayOfWeek dow:(ArrayList<TimeSlot.DayOfWeek>)pair.getValue()){
               ts.toggleTA(dow, ta);
               tableRefresh(app);
           }
        }
        data.addTA(ta);
    }
    
    private void tableRefresh(CourseSiteGeneratorApp app){
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(CSG_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
}

