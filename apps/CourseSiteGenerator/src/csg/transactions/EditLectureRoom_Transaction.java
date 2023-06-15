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
public class EditLectureRoom_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    LectureData lecture;
    String newRoom;
    String oldRoom;
    
    public EditLectureRoom_Transaction(CourseSiteGeneratorApp initApp,LectureData initLecture, String newRoom,String oldRoom) {
        app = initApp;
        lecture = initLecture;
        this.newRoom = newRoom;
        this.oldRoom = oldRoom;
    }

    @Override
    public void doTransaction() {
        lecture.setRoom(newRoom);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        lecture.setRoom(oldRoom);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }
}
