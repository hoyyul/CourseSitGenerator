package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;
import javafx.scene.control.TextField;


/**
 *
 * @author luhaoyu
 */
public class EditTextField_Transaction implements jTPS_Transaction {
    TextField text;
    String oldText;
    String newText;
    
    public EditTextField_Transaction(TextField text,String oldText,String newText) {
        this.text=text;
        this.newText=newText;
        this.oldText=oldText;
        
    }

    @Override
    public void doTransaction() {
        text.setText(newText);
    }

    @Override
    public void undoTransaction() {
        text.setText(oldText);
    }
}