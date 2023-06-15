package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;


/**
 *
 * @author luhaoyu
 */
public class AddLab_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    LabData lab;
    
    public AddLab_Transaction(CourseSiteGeneratorData initData, LabData initLab) {
        data = initData;
        lab = initLab;
    }

    @Override
    public void doTransaction() {
        data.addLab(lab);
    }

    @Override
    public void undoTransaction() {
        data.removeLab(lab);
    }
}