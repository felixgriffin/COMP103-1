// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2017T2, Assignment 4
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;

/**
 * This class contains the main method of the program. 
 * 
 * A SlideshowApp object represents the slideshow application and sets up the buttons in the UI. 
 * It also creates the creator and viewer objects. 
 * 
 * These creator and viewer objects implement their own particular behaviour as to how to respond to user input. 
 * 
 * @author Thomas Kuehne
 */
public class SlideshowApp 
{
    // boolean field that should be toggled depending on whether 
    // you attempted the completion stage or not.
    public final static boolean completion = true;

    private Images images;             // A shared reference to a linked list of images. 

    private static SlideshowCreator creator;  // responsible for creating slideshows.
    private static SlideshowViewer viewer;    // responsible for viewing slideshows.

    private boolean viewerActive;      // flag signalling whether the creator or the viewer is active

    /**
     * Returns true, if there is an active slideshow
     */
    public static boolean slideShowIsRunning() {
        return viewer.slideShowIsRunning();
    }

    /**
     * Constructor for objects of class SlideshowApp
     * One collection of images is shared between creator and viewer. 
     */
    public SlideshowApp() {
        images = new Images();
        creator = new SlideshowCreator(images);
        viewer = new SlideshowViewer(images);
    }

    /**
     * Initialises the UI window, and sets up the buttons. 
     */
    public void initialise() {
        UI.initialise();

        UI.addButton("CREATOR mode",     () -> {if (!slideShowIsRunning()) this.showCreatorUI();});
        if (completion) 
            UI.addButton("add before",   () -> {if (!slideShowIsRunning()) creator.addImageBefore();});
        UI.addButton("add after",        () -> {if (!slideShowIsRunning()) creator.addImageAfter();});
        UI.addButton("remove image",     () -> {if (!slideShowIsRunning()) creator.removeImage();});
        UI.addButton("remove all",       () -> {if (!slideShowIsRunning()) creator.clearImages();});
        UI.addButton("go left",          () -> {if (!slideShowIsRunning()) creator.moveSelectionLeft();});
        UI.addButton("go right",         () -> {if (!slideShowIsRunning()) creator.moveSelectionRight();}); 
        UI.addButton("go to start",      () -> {if (!slideShowIsRunning()) creator.moveSelectionToStart();});
        UI.addButton("go to end",        () -> {if (!slideShowIsRunning()) creator.moveSelectionToEnd();});
        if (completion) 
            UI.addButton("reverse list", () -> {if (!slideShowIsRunning()) creator.reverseImages();});
        UI.addButton("", null);
        UI.addButton("VIEWER mode",      () -> {if (!slideShowIsRunning()) this.showViewerUI();});
        UI.addButton("start show",       () -> {if (!slideShowIsRunning()) viewer.slideshow();});
        UI.addButton("go left",          () -> {if (!slideShowIsRunning()) viewer.previousImage();});
        UI.addButton("go right",         () -> {if (!slideShowIsRunning()) viewer.nextImage();});

        showCreatorUI();

    }

    /**
     * Activates the creator behaviour
     * 
     * Note the switching of the keylistener
     */
    public void showCreatorUI() {
        viewerActive=false;

        creator.statusScreen();
        UI.setKeyListener(creator::doKey);
    }

    /**
     * Activates the viewer behaviour
     *
     * Note the switching of the keylistener
     */
    public void showViewerUI() {
        viewerActive=true;

        viewer.statusScreen();
        UI.setKeyListener(viewer::doKey);
    }

    public static void main(String[] args) {
        SlideshowApp app = new SlideshowApp();
        app.initialise();
    }

}
