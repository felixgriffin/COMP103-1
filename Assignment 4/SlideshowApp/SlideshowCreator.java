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
 * A creator allows to populate a list of images for later viewing with a slideshow viewer
 * Supported operations on images are adding, removing, and reversing.
 * 
 * Unless the list of images is empty, there is always a currently selected image and the selection can be moved in various ways.
 * The reference to the "currently selected image" (called a cursor) is maintained by the "images" object 
 * 
 * @author Thomas Kuehne 
 */
public class SlideshowCreator 
{
    private String[] hardcodedfilenames = {"Atmosphere.jpg", "BachalpseeFlowers.jpg", "BoraBora.jpg", "Branch.jpg", "DesertHills.jpg",
            "DropsOfDew.jpg", "Earth_Apollo17.jpg", "Frame.jpg", "Galunggung.jpg", "HopetounFalls.jpg",
            "Palma.jpg", "Sky.jpg", "SoapBubble.jpg", "Sunrise.jpg", "Winter.jpg"};
    private int fileadditionindex = 0;

    // reference to the image list
    private Images images;

    // constants that are used to calculate the width of a standard thumbnail, and how they should be laid out in the graphics display.
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_GAP = 10;
    private static final int GRAPHICS_WIDTH = 600;

    /**
     * Creates the slideshow creator object.
     * 
     * @param images the image list shared between creator and viewer
     */
    public SlideshowCreator(Images images) {
        this.images = images;
    }

    /**
     * Changes the graphics display in the UI to now show the creator. 
     */
    public void statusScreen() {
        // printer user instructions
        UI.clearText();
        UI.println("Creator mode\n");
        UI.println("You may use the left, right, home and end cursor keys to navigate,");
        UI.println("if the image pane has the focus.)");

        this.redraw();
    }

    /**
     * Interprets key presses.
     */  
    public void doKey(String key) {
        if (SlideshowApp.slideShowIsRunning())
            return;

        if (key.equals("Left"))         moveSelectionLeft();
        else if (key.equals("Right"))   moveSelectionRight();
        else if (key.equals("Home"))    moveSelectionToStart();
        else if (key.equals("End"))     moveSelectionToEnd();
    }

    /**
     * Moves the current selection to the left, assuming it is not already at the start of the list.
     */
    public void moveSelectionLeft() {
        images.moveCursorLeft();
        this.redraw();
    }

    /**
     * Moves the current selection to the right, assuming that it is not already at the end of the list.
     */
    public void moveSelectionRight() {
        images.moveCursorRight();
        this.redraw();
    }

    /**
     * Changes the current selection to the first image in the list.
     */
    public void moveSelectionToStart() {
        images.moveCursorToStart();
        this.redraw();
    }

    /**
     * Changes the current selection to the last image in the list.
     */
    public void moveSelectionToEnd() {
        images.moveCursorToEnd();
        this.redraw();

    }

    /**
     * Returns an image from the pool as long a new one is available.
     * 
     * @return an image from the pool
     */
    public String imageFromPool() {
        // are there any more images to add?
        if (fileadditionindex >= hardcodedfilenames.length) 
            return null;

        return hardcodedfilenames[fileadditionindex++];
    }

    /**
     * Adds an image from the pool after the currently selected image
     */
    public void addImageAfter() {
        String imageFileName = imageFromPool();

        // have we ran out of images?
        if (imageFileName == null)
            return;

        images.addImageAfter(imageFileName);
        this.redraw();
    }

    /**
     * Adds an image from the pool before the currently selected image
     */  
    public void addImageBefore() {
        String imageFileName = imageFromPool();

        // have we ran out of images?
        if (imageFileName == null)
            return;

        images.addImageBefore(imageFileName);  
        this.redraw();
    }

    /**
     * Removes the currently selected image from the list.
     */
    public void removeImage() {
        images.remove();
        this.redraw();
    }

    /**
     * Clears the list of images.
     */
    public void clearImages() {
        images.removeAll(); 
        this.redraw();
    }

    /**
     * Reverses the list of images. 
     */
    public void reverseImages() {
        images.reverseImages();
        this.redraw();
    }

    /**
     * Draws the list of image thumbnails on the graphics pane.
     */
    public void redraw() { 
        UI.clearGraphics();

        // starting coordinates
        int x = 10;
        int y = 20;

        int size = images.count();

        // is there anything to display?
        if (size < 1) 
            return;

        // calculate thumbnail width
        int width = Math.min(THUMBNAIL_WIDTH, (GRAPHICS_WIDTH - THUMBNAIL_WIDTH) / size);

        if (!SlideshowApp.completion) 
        // use standard cursor movement to iterate through images
        {
            // save current selection
            ImageNode selectedImageNode = images.getCursor();

            // select the first image
            images.moveCursorToStart();

            for (int i=0; i<size; i++) {       

                // draw selection frame, if applicable
                if (images.getCursor() == selectedImageNode) {
                    UI.setColor(new java.awt.Color(255, 0, 0));
                    UI.fillRect(x, y, width+16, width + 16);
                } 

                // draw image 
                String imageFileName = images.getImageFileNameAtCursor();
                UI.drawImage(imageFileName, x+8, y+8, width, width);

                // advance drawing position and list position
                x += (width + THUMBNAIL_GAP);
                images.moveCursorRight();
            }

            // restore image selection
            images.setCursor(selectedImageNode);
        }
        else
        // use Images iterator
        {
            for (String imageFileName : images) {
                // draw selection frame, if applicable
                if (imageFileName.equals(images.getImageFileNameAtCursor())) {
                    UI.setColor(new java.awt.Color(255, 0, 0));
                    UI.fillRect(x, y, width+16, width + 16);
                } 

                // draw image
                UI.drawImage(imageFileName, x+8, y+8, width, width);

                // advance drawing position and list position
                x += (width + THUMBNAIL_GAP);
            }
        }
    }        
}
