/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;
/**
 *
 * @author luhaoyu
 */
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;


public class EditTA_Transaction implements jTPS_Transaction {
    TeachingAssistantPrototype taToEdit;
    String oldName, newName;
    String oldEmail, newEmail;
    String oldType, newType;
    
    public EditTA_Transaction(TeachingAssistantPrototype initTAToEdit, 
            String name, String email, String type) {
        taToEdit = initTAToEdit;
        oldName = initTAToEdit.getName();
        oldEmail = initTAToEdit.getEmail();
        oldType = initTAToEdit.getType();
        newName = name;
        newEmail = email;
        newType = type;
    }


    @Override
    public void doTransaction() {
        taToEdit.setName(newName);
        taToEdit.setEmail(newEmail);
        taToEdit.setType(newType);
    }

    @Override
    public void undoTransaction() {
        taToEdit.setName(oldName);
        taToEdit.setEmail(oldEmail);
        taToEdit.setType(oldType);
    }
}
