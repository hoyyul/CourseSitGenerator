/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import djf.components.AppDataComponent;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author luhaoyu
 */
public class LectureData <E extends Comparable<E>> implements Comparable<E>{
    private final StringProperty section;
    private final StringProperty days;
    private final StringProperty time;
    private final StringProperty room;
    
    public LectureData(String initSection, String initDays, String initTime, String initRoom){
        section = new SimpleStringProperty(initSection);
        days = new SimpleStringProperty(initDays);
        time = new SimpleStringProperty(initTime);
        room = new SimpleStringProperty(initRoom);
    }
    
    public String getSection(){
        return section.get();
    }
    
    public String getDays(){
        return days.get();
    }
    
    public String getTime(){
        return time.get();
    }
    
    public String getRoom(){
        return room.get();
    }
    
    public void setSection(String section){
        this.section.set(section);
    }
    
    public void setDays(String days){
        this.days.set(days);
    }
    
    public void setTime(String time){
        this.time.set(time);
    }
    
    public void setRoom(String room){
        this.room.set(room);
    }
    
    
    
    @Override
    public String toString() {
        return section.getValue();
    }

    @Override
    public int compareTo(E otherLecture) {
        return getSection().compareTo(((LectureData)otherLecture).getSection());
    }
    
    public ArrayList<String> getTimeBox(){
        ArrayList<String> box = new ArrayList<String>();
        String startTime = getTime().substring(0,getTime().indexOf("-"));
        String endTime = getTime().substring(getTime().indexOf("-")+1);
        String startHour;
        try{
            startHour = startTime.substring(0,startTime.indexOf(":"));
        }
        catch(Exception e){
            startHour = startTime.substring(0,1);
        }
        if(startHour.substring(0, 1).equals("0"))
            startHour=startHour.substring(1, 2);
        String endHour;
        try{
            endHour = endTime.substring(0,endTime.indexOf(":"));}
        catch(Exception e){
            endHour = endTime.substring(endTime.indexOf("-")+1,endTime.indexOf("-")+2);
        }
        if(endHour.substring(0, 1).equals("0"))
            endHour=endHour.substring(1, 2);
        String startMin;
       
        startMin = startTime.substring(startTime.indexOf(":")+1,startTime.length()-2);
        if(startMin.length()==1)
            startMin="00";
        
        String endMin;
        endMin = endTime.substring(endTime.indexOf(":")+1,endTime.length()-2);
        if(endMin.length()==1)
            endMin="00";
        
        String startSlot= startTime.substring(startTime.length()-2);
        String endSlot =endTime.substring(endTime.length()-2);
        if(startTime.length()==3){
            startTime=startHour+":00"+startSlot;
        }
        if(endTime.length()==3){
            endTime=endHour+":00"+endSlot;
        }
        if(startTime.substring(0, 1).equals("0"))
            startTime=startTime.substring(1);
        if(endTime.substring(0, 1).equals("0"))
            endTime=endTime.substring(1);
        if(startHour.equals(endHour)||(!startHour.equals(endHour)&&endMin.equals("00"))){
            box.add(startTime.replace(":", "_"));
            box.add(startHour+"_30"+startSlot);
        }
        else if(startMin.equals("00")){
            box.add(startTime.replace(":", "_"));
            box.add(startHour+"_30"+startSlot);
            box.add(endHour+"_00"+endSlot);
        }
        else if(startMin.equals("30")&&Integer.parseInt(endMin)>30){
            box.add(startTime.replace(":", "_"));
            box.add(endHour+"_00"+endSlot);
            box.add(endHour+"_30"+endSlot);
        }
        else{
            box.add(startTime.replace(":", "_"));
            box.add(endHour+"_00"+endSlot);
        }
        
       
        return box;
    }
    public ArrayList<String> getDayBox(){
        ArrayList<String> list= new ArrayList<>();
        if(days.get().contains("Monday")){
            list.add("MONDAY");
        }
        if(days.get().contains("Tuesday")){
            list.add("TUESDAY");
        }
        if(days.get().contains("Wednesday")){
            list.add("WENDESDAY");
        }
        if(days.get().contains("Thursday")){
            list.add("THURSDAY");
        }
        if(days.get().contains("Friday")){
            list.add("FRIDAY");
        }
        if(days.get().contains("Saturday")){
            list.add("SATURDAY");
        }
        if(days.get().contains("Sunday")){
            list.add("SUNDAY");
        }
        return list;
    }
    
    public static void main(String[] args){
        LectureData l = new LectureData("01","Tuesday & Thursday","2pm-3pm","101");
        //System.out.print(l.getTimeBox());
        //System.out.print(l.getDayBox());
        
        String s1 = "Hello";
String s2 = "Hello";
if (s1 == s2) System.out.println(1);

String s3 = new String("Hello");
if (s1 == s3) System.out.println(2);

String s4 = "Hell";
s4 += "o";
if (s1 == s4) System.out.println(3);
    }
    
}
