package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LectureData;
import csg.data.RecitationData;


/**
 *
 * @author luhaoyu
 */
public class RemoveRecitation_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    RecitationData recitation;
    
    public RemoveRecitation_Transaction(CourseSiteGeneratorData initData, RecitationData initRecitation) {
        data = initData;
        recitation = initRecitation;
    }

    @Override
    public void doTransaction() {
        data.removeRecitation(recitation);
    }

    @Override
    public void undoTransaction() {
        data.addRecitation(recitation);
    }
}
