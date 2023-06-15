package csg.transactions;

import jtps.jTPS_Transaction;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;


/**
 *
 * @author luhaoyu
 */
public class EditComboBox_Transaction implements jTPS_Transaction {
    ComboBox c;
    String newTime;
    String oldTime;
    String s;
    
    public EditComboBox_Transaction(ComboBox c,Object newTime,Object oldTime) {
        this.c=c;
        if(newTime==null)
            this.newTime=null;
        else
            this.newTime=newTime.toString();
        if(oldTime==null)
            this.oldTime=null;
        else
            this.oldTime=oldTime.toString();
    }
    
    public EditComboBox_Transaction(ComboBox c,Object newTime,Object oldTime,String s) {
        this.c=c;
        if(newTime==null)
            this.newTime=null;
        else
            this.newTime=newTime.toString();
        if(oldTime==null)
            this.oldTime=null;
        else
            this.oldTime=oldTime.toString();
        this.s=s;
    }


    @Override
    public void doTransaction() {
        if(!newTime.equals(""))
            this.c.setValue(newTime);
        else
            this.c.setValue(null);
        if(!c.getItems().contains(newTime)) c.getItems().add(newTime);
        try {
            saveOptions(c,s);
        } catch (Exception ex) {
            
        }
        
    }

    @Override
    public void undoTransaction() {
        if(!oldTime.equals(""))
            this.c.setValue(oldTime);
        else
            this.c.setValue(null);  
        
    }
    
    private void saveOptions(ComboBox c,String s) throws IOException{
        
        JsonArrayBuilder subjectArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder numberArrayBuilder = Json.createArrayBuilder();
        for(int i=0;i<c.getItems().size();i++){
            subjectArrayBuilder.add(c.getItems().get(i).toString());
        }
        
        
        JsonArray subjectArray = subjectArrayBuilder.build();
        JsonArray numberArray = numberArrayBuilder.build();
        JsonObject dataManagerJSO = Json.createObjectBuilder().add(s,subjectArray).build();
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
	// INIT THE WRITER
	OutputStream os = new FileOutputStream("./work/"+s+"Options.json");//
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter("./work/"+s+"Options.json");//
	pw.write(prettyPrinted);
	pw.close();
        
    
    
    
    }
}
