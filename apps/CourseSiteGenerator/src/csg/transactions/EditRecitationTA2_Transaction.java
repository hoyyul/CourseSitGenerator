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
public class EditRecitationTA2_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    RecitationData recitation;
    String newTa2;
    String oldTa2;
    
    public EditRecitationTA2_Transaction(CourseSiteGeneratorApp initApp,RecitationData initRecitation, String t1,String t2) {
        app = initApp;
        recitation = initRecitation;
        newTa2 = t1;
        oldTa2 = t2;
    }

    @Override
    public void doTransaction() {
        recitation.setTA2(newTa2);
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        recitation.setTA2(oldTa2);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }
}
