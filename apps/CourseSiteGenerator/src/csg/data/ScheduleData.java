/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import djf.components.AppDataComponent;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author luhaoyu
 */
public class ScheduleData <E extends Comparable<E>> implements Comparable<E>{
    private final StringProperty type;
    private final StringProperty date;
    private final StringProperty topic;
    private final StringProperty title;
    private final StringProperty link;
    
    public ScheduleData(String initType, String initDate, String initTopic, String initTitle, String initLink){
        type = new SimpleStringProperty(initType);
        date = new SimpleStringProperty(initDate);
        topic = new SimpleStringProperty(initTopic);
        title = new SimpleStringProperty(initTitle);
        link = new SimpleStringProperty(initLink);
    }
    
    public String getType(){
        return type.get();
    }
    
    public String getDate(){
        
        return toStringForm(date.get());
    }
    
    public String getTopic(){
        return topic.get();
    }
    
    public String getTitle(){
        return title.get();
    }
    
    public String getLink(){
        return link.get();
    }
    
    public void setType(String type){
        this.type.set(type);
    }
    
    public void setDate(String date){
        this.date.set(date);
    }
    
    public void setTopic(String topic){
        this.topic.set(topic);
    }
    
    public void setTitle(String title){
        this.title.set(title);
    }
    
    public void setLink(String link){
        this.link.set(link);
    }
    
    @Override
    public String toString() {
        return title.getValue();
    }

    @Override
    public int compareTo(E otherSchedule) {
        return ScheduleData.toDateForm(getDate()).compareTo(ScheduleData.toDateForm(((ScheduleData)otherSchedule).getDate()));
    }

    public static String toStringForm(String s){
        //2018-01-01
        String year= s.substring(0,4);
        String month;
        String day;
        String date="";
        if(s.substring(8,9).equals("0"))
            day=s.substring(9);
        else
            day=s.substring(8);
        if(s.substring(5,6).equals("0"))
            month=s.substring(6,7);
        else
            month=s.substring(5,7);
        date+=month+"/"+day+"/"+year;
        return date;
    }
    
    public static String toDateForm(String s){
        if(!s.equals("")){
        String year=s.substring(s.length()-4);
        String month;
        String day;
        String date="";
        if(s.substring(0,s.indexOf("/")).length()==1)
            month="0"+s.substring(0,s.indexOf("/"));
        else
            month=s.substring(0,s.indexOf("/"));
        if(s.substring(s.indexOf("/")+1,s.indexOf("/",s.indexOf("/")+1)).length()==1)
            day="0"+s.substring(s.indexOf("/")+1,s.indexOf("/",s.indexOf("/")+1));
        else
            day=s.substring(s.indexOf("/")+1,s.indexOf("/",s.indexOf("/")+1));
        date+=year+"-"+month+"-"+day;
        return date;}
        else
            return "";
    }
    
    public static void main(String[]args){
        System.out.println(LocalDate.parse("2018-01-02").compareTo(LocalDate.parse("2018-01-12")));
        
    }
    
}
