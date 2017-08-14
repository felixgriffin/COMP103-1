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
 * A slideshow viewer uses the image collection and displays one image at a time on the graphics display. 
 * The user can manually move through the list, or they can request that the entire list be shown in order, 
 * with a two second delay between consecutive images.
 * 
 * A slideshow viewer object should not modify the linked list structure, only read from it.
 * 
 * @author Thomas Kuehne
 */
public class SlideshowViewer {

    // This flag identifies whether there is a slideshow running. 
    // During such time, the user can neither use the buttons nor use the cursor keys to navigate.
    private boolean slideShowIsActive = false;

    // The image collection to be viewed. The creator and the viewer share this collection. 
    private Images images;

    /**
     * Creates the slideshow viewer object.
     * 
     * @param images the image collection shared between creator and viewer
     */  
    public SlideshowViewer(Images images) {
        this.slideShowIsActive = false;
        this.images = images;
    }

    /**
     * Returns true, if there is an active slideshow
     */
    public boolean slideShowIsRunning() {
        return slideShowIsActive;
    }

    /**
     * Interprets key presses.
     */  
    public void doKey(String key) {
        // ignore key presses if a slide show is running
        if (slideShowIsRunning())  
            return;

        if (key.equals("Left"))        previousImage();
        else if (key.equals("Right"))  nextImage();
    }

    /**
     * Changes the graphics display in the UI to show the viewer. 
     */
    public void statusScreen() {
        // printer user instructions
        UI.clearText();
        UI.println("Viewer mode\n");
        UI.println("You may use the left and right cursor keys to navigate,");
        UI.println("if the image pane has the focus.)");

        this.redraw();

    }

    /**
     * Advances to the next image.
     */
    public void nextImage() {
        images.moveCursorRight();  
        this.redraw();
    }

    /**
     * Moves to the previous image.
     */
    public void previousImage() {
        images.moveCursorLeft(); 
        this.redraw();
    }

    /**
     * Runs a slideshow by shwoing each image in the collection for two seconds. 
     * 
     * Makes all input handlers ignore all input. 
     */
    public void slideshow() {

        // set up slide show screen
        UI.clearText();
        UI.println("Slideshow mode\n");
        UI.println("(butttons and keys disabled)");

        // disable input handlers
        slideShowIsActive = true;

        // perform slideshow

        if (!SlideshowApp.completion) 
        // use standard cursor movement
        {
            // save currently selected image position
            ImageNode currentImage = images.getCursor();

            // select the first image
            images.moveCursorToStart();

            // display all images one by one
            for (int i=0; i<images.count(); i++) {
                this.redraw();
                UI.sleep(2000);
                images.moveCursorRight();
            }

            // restore currently selected image position
            images.setCursor(currentImage);
        }
        else 
        // use Images iterator to display images one by one
            for (String imageFileName : images) {
                UI.clearGraphics();
                UI.drawImage(imageFileName, 10, 10, 450, 450);
                UI.sleep(2000);
            }

        // reenable input handlers
        slideShowIsActive = false;

        // reinstantiate the normal viewer status screen
        this.statusScreen();

    }

    /**
     * Uses the current cursor position to display the respective image.
     */
    private void redraw() {
        UI.clearGraphics();

        if (images.count() > 0) {
            UI.drawImage(images.getImageFileNameAtCursor(), 10, 10, 450, 450);
        }
    } 
}
