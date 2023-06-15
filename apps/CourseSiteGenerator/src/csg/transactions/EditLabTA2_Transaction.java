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
public class EditLabTA2_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    LabData lab;
    String newTA2;
    String oldTA2;
    
    public EditLabTA2_Transaction(CourseSiteGeneratorApp initApp,LabData initLab, String s1, String s2) {
        app = initApp;
        lab = initLab;
        newTA2 = s1;
        oldTA2 = s2;
    }

    @Override
    public void doTransaction() {
        lab.setTA2(newTA2);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LAB_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        lab.setTA2(oldTA2);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_LAB_TABLE_VIEW))).refresh();
    }
}
