package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;
import csg.data.ScheduleData;


/**
 *
 * @author luhaoyu
 */
public class AddSchedule_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    ScheduleData schedule;
    
    public AddSchedule_Transaction(CourseSiteGeneratorData initData, ScheduleData initSchedule) {
        data = initData;
        schedule = initSchedule;
    }

    @Override
    public void doTransaction() {
        data.addSchedule(schedule);
    }

    @Override
    public void undoTransaction() {
        data.removeSchedule(schedule);
    }
}
