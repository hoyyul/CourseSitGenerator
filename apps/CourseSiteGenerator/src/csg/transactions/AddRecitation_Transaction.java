package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.RecitationData;


/**
 *
 * @author luhaoyu
 */
public class AddRecitation_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    RecitationData recitation;
    
    public AddRecitation_Transaction(CourseSiteGeneratorData initData, RecitationData initRecitation) {
        data = initData;
        recitation = initRecitation;
    }

    @Override
    public void doTransaction() {
        data.addRecitation(recitation);
    }

    @Override
    public void undoTransaction() {
        data.removeRecitation(recitation);
    }
}