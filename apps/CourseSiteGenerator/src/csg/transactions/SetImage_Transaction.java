package csg.transactions;

import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.CSG_STYLE_PANE;
import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.LabData;
import csg.data.RecitationData;
import csg.workspace.CourseSiteGeneratorWorkspace;
import djf.modules.AppGUIModule;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


/**
 *
 * @author luhaoyu
 */
public class SetImage_Transaction implements jTPS_Transaction {
    AppGUIModule gui;
    public static ArrayList<String> url1 = new ArrayList<String>();
    public static ArrayList<String> url2 = new ArrayList<String>();
    public static ArrayList<String> url3 = new ArrayList<String>();
    public static ArrayList<String> url4 = new ArrayList<String>();
    String newFile;
    String oldFile;
    int index;
    int row;
    public SetImage_Transaction(AppGUIModule gui,File newFile,String s) {
        this.gui=gui;
        switch(s){
            case "Favicon":
                index=0;
                row=1;
                url1.add(newFile.toURI().toString());
                
                this.newFile=url1.get(url1.size()-1);
                if(url1.size()>1)
                    oldFile=url1.get(url1.size()-2);
                else{
                    url1.add(url1.get(0));
                    url1.set(0, "file:./images/SBUShieldFavicon.ico");
                    oldFile=url1.get(url1.size()-2);
                }
                break;
            case "Navbar":
                index=1;
                row=2;
                url2.add(newFile.toURI().toString());
                
                this.newFile=url2.get(url2.size()-1);
                if(url2.size()>1)
                    oldFile=url2.get(url2.size()-2);
                else{
                    url2.add(url2.get(0));
                    url2.set(0, "file:./images/SBUDarkRedShieldLogo.png");
                    oldFile=url2.get(url2.size()-2);
                }
                break;
            case "Left":
                index=2;
                row=3;
                url3.add(newFile.toURI().toString());
                
                this.newFile=url3.get(url3.size()-1);
                if(url3.size()>1)
                    oldFile=url3.get(url3.size()-2);
                else{
                    url3.add(url3.get(0));
                    url3.set(0, "file:./images/SBUWhiteShieldLogo.jpg");
                    oldFile=url3.get(url3.size()-2);
                }
                break;
            case "Right":
                index=3;
                row=4;
                url4.add(newFile.toURI().toString());
                
                this.newFile=url4.get(url4.size()-1);
                if(url4.size()>1)
                    oldFile=url4.get(url4.size()-2);
                else{
                    url4.add(url4.get(0));
                    url4.set(0, "file:./images/SBUCSLogo.png");
                    oldFile=url4.get(url4.size()-2);
                }
                break;
        }
    }

    @Override
    public void doTransaction() {
        //System.out.println(newFile);
        
        for(Node node:((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren()){
            if(node instanceof ImageView&&GridPane.getColumnIndex(node)==1&&GridPane.getRowIndex(node)==row){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren().remove(node);
                    CourseSiteGeneratorWorkspace.urls[index]=null;
                    break;
                }
            }
            ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).add(new ImageView(newFile),1,row);
            CourseSiteGeneratorWorkspace.urls[index]=newFile;
           
    }

    @Override
    public void undoTransaction() {
        
            //System.out.println(oldFile);
            for(Node node:((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren()){
            if(node instanceof ImageView&&GridPane.getColumnIndex(node)==1&&GridPane.getRowIndex(node)==row){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).getChildren().remove(node);
                    CourseSiteGeneratorWorkspace.urls[index]=null;
                    break;
                }
            }
            if(!oldFile.equals("")){
                ((GridPane)gui.getGUINode(CSG_STYLE_PANE)).add(new ImageView(oldFile),1,row);
                CourseSiteGeneratorWorkspace.urls[index]=oldFile;
            }
        
            
    }
    
    public static void main(String[]args){
        
    }
}
