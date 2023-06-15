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
public class EditRecitationRoom_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    RecitationData recitation;
    String newRoom;
    String oldRoom;
     
    public EditRecitationRoom_Transaction(CourseSiteGeneratorApp initApp,RecitationData initRecitation, String r1,String r2) {
        app = initApp;
        recitation = initRecitation;
        newRoom = r1;
        oldRoom =r2;
    }

    @Override
    public void doTransaction() {
        recitation.setLocation(newRoom);
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        recitation.setLocation(oldRoom);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }
}

