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
public class EditLectureSection_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    LectureData lecture;
    String newSection;
    String oldSection;
    
    public EditLectureSection_Transaction(CourseSiteGeneratorApp initApp,LectureData initLecture, String newSection, String oldSection) {
        app = initApp;
        lecture = initLecture;
        this.newSection = newSection;
        this.oldSection = oldSection;
    }

    @Override
    public void doTransaction() {
        lecture.setSection(newSection);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        lecture.setSection(oldSection);
        ((TableView)(app.getGUIModule().getGUINode(CSG_LECTURE_TABLE_VIEW))).refresh();
    }
}

