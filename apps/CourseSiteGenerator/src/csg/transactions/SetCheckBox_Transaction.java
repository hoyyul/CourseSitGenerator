package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;
import static djf.modules.AppGUIModule.DISABLED;
import static djf.modules.AppGUIModule.ENABLED;
import javafx.scene.control.CheckBox;


/**
 *
 * @author luhaoyu
 */
public class SetCheckBox_Transaction implements jTPS_Transaction {
    CheckBox checkBox;
    boolean isSelected;
    
    public SetCheckBox_Transaction(CheckBox checkBox,boolean selectedStatus) {
        this.checkBox=checkBox;
        isSelected=selectedStatus;
    }

    @Override
    public void doTransaction() {
        if(isSelected==true)
            this.checkBox.setSelected(ENABLED);
        else
            this.checkBox.setSelected(DISABLED);
    }

    @Override
    public void undoTransaction() {
        if(isSelected==true)
            this.checkBox.setSelected(DISABLED);
        else
            this.checkBox.setSelected(ENABLED);
    }
}