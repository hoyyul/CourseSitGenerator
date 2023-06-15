/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import djf.components.AppDataComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author luhaoyu
 */
public class RecitationData <E extends Comparable<E>> implements Comparable<E>{
    private final StringProperty section;
    private final StringProperty dayTime;
    private final StringProperty location;
    private final StringProperty TA1;
    private final StringProperty TA2;
    
    public RecitationData(String initSection, String initDay_time, String initLocation, String initTA1, String initTA2){
        section = new SimpleStringProperty(initSection);
        dayTime = new SimpleStringProperty(initDay_time);
        location = new SimpleStringProperty(initLocation);
        TA1 = new SimpleStringProperty(initTA1);
        TA2 = new SimpleStringProperty(initTA2);
    }
    
    public String getSection(){
        return section.get();
    }
    
    public String getDayTime(){
        return dayTime.get();
    }
    
    public String getLocation(){
        return location.get();
    }
    
    public String getTA1(){
        return TA1.get();
    }
    
    public String getTA2(){
        return TA2.get();
    }
    
    public void setSection(String section){
        this.section.set(section);
    }
    
    public void setDayTime(String day_time){
        this.dayTime.set(day_time);
    }
    
    public void setLocation(String location){
        this.location.set(location);
    }
    
    public void setTA1(String TA1){
        this.TA1.set(TA1);
    }
    
    public void setTA2(String TA2){
        this.TA2.set(TA2);
    }
    
    @Override
    public String toString() {
        return section.getValue();
    }

    @Override
    public int compareTo(E otherRecation) {
        return getSection().compareTo(((RecitationData)otherRecation).getSection());
    }

    
    
}