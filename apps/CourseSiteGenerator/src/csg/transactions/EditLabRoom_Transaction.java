package csg.transactions;

import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_LAB_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_LECTURE_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_RECITATION_TABLE_VIEW;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.LectureData;
import csg.data.RecitationData;
import javafx.scene.control.TableView;


/**
 *
 * @author luhaoyu
 */
public class EditLabRoom_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    LabData lab;
    String newRoom;
    String oldRoom;
    
    public EditLabRoom_Transaction(CourseSiteGeneratorApp initApp,LabData initLab,String s1, String s2) {
        app = initApp;
        lab = initLab;
        newRoom = s1;
        oldRoom = s2;
    }

    @Override
    public void doTransaction() {
        lab.setLocation(newRoom);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LAB_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        lab.setLocation(oldRoom);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_LAB_TABLE_VIEW))).refresh();
    }
}
