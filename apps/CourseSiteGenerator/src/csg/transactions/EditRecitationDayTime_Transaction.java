package csg.transactions;

import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_LECTURE_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.CSG_RECITATION_TABLE_VIEW;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LectureData;
import csg.data.RecitationData;
import javafx.scene.control.TableView;


/**
 *
 * @author luhaoyu
 */
public class EditRecitationDayTime_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    RecitationData recitation;
    String newDayTime;
    String oldDayTime;
    
    public EditRecitationDayTime_Transaction(CourseSiteGeneratorApp initApp,RecitationData initRecitation, String d1,String d2) {
        app = initApp;
        recitation = initRecitation;
        newDayTime = d1;
        oldDayTime = d2;
    }

    @Override
    public void doTransaction() {
        recitation.setDayTime(newDayTime);
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        recitation.setDayTime(oldDayTime);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }
}

