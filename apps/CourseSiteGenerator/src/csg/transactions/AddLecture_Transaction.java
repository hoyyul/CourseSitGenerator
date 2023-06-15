package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LectureData;


/**
 *
 * @author luhaoyu
 */
public class AddLecture_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    LectureData lecture;
    
    public AddLecture_Transaction(CourseSiteGeneratorData initData, LectureData initLecture) {
        data = initData;
        lecture = initLecture;
    }

    @Override
    public void doTransaction() {
        data.addLecture(lecture);
    }

    @Override
    public void undoTransaction() {
        data.removeLecture(lecture);
    }
}