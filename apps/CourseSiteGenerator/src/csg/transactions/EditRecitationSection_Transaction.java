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
public class EditRecitationSection_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    RecitationData recitation;
    String newSection;
    String oldSection;
    
    public EditRecitationSection_Transaction(CourseSiteGeneratorApp initApp,RecitationData initRecitation, String s1,String s2) {
        app = initApp;
        recitation = initRecitation;
        newSection = s1;
        oldSection = s2;
    }

    @Override
    public void doTransaction() {
        recitation.setSection(newSection);
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }

    @Override
    public void undoTransaction() {
        recitation.setSection(oldSection);
        //System.out.print(lecture.getSection());
        ((TableView)(app.getGUIModule().getGUINode(CSG_RECITATION_TABLE_VIEW))).refresh();
    }
}

