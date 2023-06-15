/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author luhaoyu
 */
public class SiteBannerData {
    private final StringProperty subject;
    private final StringProperty number;
    private final StringProperty semester;
    private final StringProperty year;
    
    public SiteBannerData(String initSubject, String initNumber, String initSemester,String initYear){
        subject = new SimpleStringProperty(initSubject);
        number = new SimpleStringProperty(initNumber);
        semester = new SimpleStringProperty(initSemester);
        year = new SimpleStringProperty(initYear);
    }
}
