// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2017T2, Assignment 4
 * Name: Daniel Satur
 * Username: saturdani
 * ID: 300375193
 */

/**
 * Class ImageNode implements a node that forms a linked list data structure in conjunction with other nodes of the same type.
 * 
 * A node represents an images by storing the filename of the image.
 * A node furthermore links to their successor node.
 * 
 * @author Thomas Kuehne 
 */
public class ImageNode {

    // A string that contains the full file name of an image file.
    private String imageFileName;

    // A reference to the next ImageNode in the linked list.
    private ImageNode next;

    /**
     * Creates an image node
     * 
     * @param imageFileNameStr the file name of the image to be represented by this node
     * @param nextNode the reference to the next node in the list
     */
    public ImageNode(String imageFileNameStr, ImageNode nextNode)
    {
        this.imageFileName = imageFileNameStr;
        this.next = nextNode;
    }

    /**
     * Returns the filename of the image. 
     */
    public String getFileName() {
        return imageFileName;
    }

    /** 
     * Returns the successor of this node.
     */
    public ImageNode getNext() {
        return next;
    }

    /**
     * Changes the successor of this node.
     */
    public void setNext(ImageNode newNext) {
        this.next = newNext;
    }

    /**
     * Returns the number of elements in the list starting from this node 
     *
     * For the core part of the assignment.
     * 
     * @return the number of nodes in the list starting at this node.
     * 
     */
    public int count() {
        int count=0;
        ImageNode temp = this;
        while(temp!=null){ //If there is another node in the list
            count++; //Increase the count
            temp = temp.getNext(); //Move the "cursor" forward
        }
        return count;
    }

    /**
     * Inserts a node after this node.
     * 
     * @param newNode the node to be inserted
     * 
     */
    public void insertAfter(ImageNode newNode) {
        newNode.setNext(this.getNext()); //Make sure the new node points to what comes after the current node
        this.setNext(newNode); //Place the node after the current one
    }

    /**
     * Inserts a new node before the cursor position
     * 
     * For the core part of the assignment.
     * 
     * Assumption: The cursor points to some node that is part of the list started by the successor of this node.
     * In other words, the cursor cannot point to this node, but will point to one of the successors of this node.
     * 
     * @param newNode the new node to be inserted
     * @param cursor  the position before which the node needs to be inserted
     * 
     * HINT: Make use of method 'nodeBefore' in order to find the node before the cursor.
     * HINT: Once you have the previous node, you can make use of method 'insertAfter'.
     *
     */
    public void insertBefore(ImageNode newNode, ImageNode cursor) {
        if(this==null){
            insertAfter(newNode); //If the list is empty, just put it in
        }
        cursor=nodeBefore(cursor); //Find the node before the current node at the cursor
        cursor.setNext(newNode); //Place the new node between the cursor and the node before it
    } 

    /**
     * 
     * Returns the node before the provided node.
     * 
     * For the core part of the assignment.
     * 
     * Assumption: The provided node is one of the successors of this node.
     *
     * @param target the node whose predecessor is required 
     * @return the node before the provided node
     */
    public ImageNode nodeBefore(ImageNode target) {
        if(this==null){
            return null; //Quit if the list is empty
        }
        ImageNode temp = this;
        while(this.getNext()!=target){
            temp.setNext(temp.getNext()); //Quit when you find the node before the cursor.
        }
        return temp;
    }

    /**
     * Removes the node at the cursor, given a reference to the previous node
     * 
     * @param previous the node preceding this node
     * 
     */
    public void removeNodeUsingPrevious(ImageNode previous) {
        previous.setNext(this.getNext());
    }     
}
