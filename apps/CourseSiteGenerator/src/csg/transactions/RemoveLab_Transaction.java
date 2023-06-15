package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.LectureData;


/**
 *
 * @author luhaoyu
 */
public class RemoveLab_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    LabData lab;
    
    public RemoveLab_Transaction(CourseSiteGeneratorData initData, LabData initLab) {
        data = initData;
        lab = initLab;
    }

    @Override
    public void doTransaction() {
        data.removeLab(lab);
    }

    @Override
    public void undoTransaction() {
        data.addLab(lab);
    }
}
