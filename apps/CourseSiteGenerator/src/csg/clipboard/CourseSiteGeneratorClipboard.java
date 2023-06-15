package csg.clipboard;

import djf.components.AppClipboardComponent;
import java.util.ArrayList;
import csg.CourseSiteGeneratorApp;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author luhaoyu
 */
public class CourseSiteGeneratorClipboard implements AppClipboardComponent {
    CourseSiteGeneratorApp app;
    ArrayList<TeachingAssistantPrototype> clipboardCutItems;
    ArrayList<TeachingAssistantPrototype> clipboardCopiedItems;
    
    public CourseSiteGeneratorClipboard(CourseSiteGeneratorApp initApp) {
        app = initApp;
        clipboardCutItems = null;
        clipboardCopiedItems = null;
    }
    
    @Override
    public void cut() {
    }

    @Override
    public void copy() {
    }
    
    @Override
    public void paste() {

    }    

    @Override
    public boolean hasSomethingToCut() {
        return false;
    }

    @Override
    public boolean hasSomethingToCopy() {
        return false;
    }

    @Override
    public boolean hasSomethingToPaste() {
        if ((clipboardCutItems != null) && (!clipboardCutItems.isEmpty()))
            return true;
        else if ((clipboardCopiedItems != null) && (!clipboardCopiedItems.isEmpty()))
            return true;
        else
            return false;
    }
}