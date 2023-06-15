package csg.transactions;

import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_END_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_BOUNDARY_START_TIME_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.CSG_SCHEDULE_ITEMS_TABLE_VIEW;
import csg.data.CourseSiteGeneratorData;
import djf.modules.AppGUIModule;
import jtps.jTPS_Transaction;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.ComboBox;
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
public class EditScheduleComboBox_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    AppGUIModule gui;
    ComboBox c1;
    ComboBox c2;
    String newTime;
    String oldTime;
    String s;
    
    
    public EditScheduleComboBox_Transaction(CourseSiteGeneratorData data,AppGUIModule gui,ComboBox c1,ComboBox c2,Object newTime,Object oldTime) {
        this.c1=c1;
        this.c2=c2;
        if(newTime==null)
            this.newTime=null;
        else
            this.newTime=newTime.toString();
        if(oldTime==null)
            this.oldTime=null;
        else
            this.oldTime=oldTime.toString();
        this.data=data;
        this.gui=gui;
    }
    

    @Override
    public void doTransaction() {
        if(!newTime.equals(""))
            this.c1.setValue(newTime);
        else
            this.c1.setValue(null);
        if(c1.getValue()!=null&&c2.getValue()!=null&&c1==((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX))){
            data.resetDate(c1.getValue().toString(), c2.getValue().toString());
            ((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).refresh();
        }
        if(c1.getValue()!=null&&c2.getValue()!=null&&c1==((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX))){
            data.resetDate(c2.getValue().toString(), c1.getValue().toString());
            ((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).refresh();
        }
    }

    @Override
    public void undoTransaction() {
        if(!oldTime.equals(""))
            this.c1.setValue(oldTime);
        else
            this.c1.setValue(null);  
        if(c1.getValue()!=null&&c2.getValue()!=null&&c1==((ComboBox)gui.getGUINode(CSG_BOUNDARY_START_TIME_COMBOBOX))){
            data.resetDate(c1.getValue().toString(), c2.getValue().toString());
            ((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).refresh();
        }
        if(c1.getValue()!=null&&c2.getValue()!=null&&c1==((ComboBox)gui.getGUINode(CSG_BOUNDARY_END_TIME_COMBOBOX))){
            data.resetDate(c2.getValue().toString(), c1.getValue().toString());
            ((TableView)gui.getGUINode(CSG_SCHEDULE_ITEMS_TABLE_VIEW)).refresh();
        }
        
    }
    
}
