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
public class EditLectureTime_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    LectureData lecture;
    String newTime;
    String oldTime;
    
    public EditLectureTime_Transaction(CourseSiteGeneratorApp initApp,LectureData initLecture, String newTime, String oldTime) {
        app = initApp;
        lecture = initLecture;
        this.newTime = newTime;
        this.oldTime = oldTime;
    }

    @Override
    public void doTransaction() {
        lecture.setTime(newTime);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        lecture.setTime(oldTime);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }
}