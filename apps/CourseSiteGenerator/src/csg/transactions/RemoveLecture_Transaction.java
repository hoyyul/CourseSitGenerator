package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LectureData;


/**
 *
 * @author luhaoyu
 */
public class RemoveLecture_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    LectureData lecture;
    
    public RemoveLecture_Transaction(CourseSiteGeneratorData initData, LectureData initLecture) {
        data = initData;
        lecture = initLecture;
    }

    @Override
    public void doTransaction() {
        data.removeLecture(lecture);
    }

    @Override
    public void undoTransaction() {
        data.addLecture(lecture);
    }
}