package csg.transactions;

import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_LECTURE_TABLE_VIEW;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LectureData;
import javafx.scene.control.TableView;


/**
 *
 * @author luhaoyu
 */
public class EditLectureDays_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    LectureData lecture;
    String newDays;
    String oldDays;
    
    public EditLectureDays_Transaction(CourseSiteGeneratorApp initApp,LectureData initLecture, String newDays,String oldDays) {
        app = initApp;
        lecture = initLecture;
        this.newDays = newDays;
        this.oldDays = oldDays;
    }

    @Override
    public void doTransaction() {
        lecture.setDays(newDays);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        lecture.setDays(oldDays);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }
}