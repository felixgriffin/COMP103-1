// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2017T2, Assignment 3
 * Name: Daniel Satur
 * Username: saturdani
 * ID: 300375193
 */

import java.lang.reflect.Array;
import java.util.*;

/**
 *  ArraySet - a Set collection;
 *
 *  The implementation uses an array to store the items
 *  and a count variable to store the number of items in the array.
 * 
 *  The items in the set should be stored in positions
 *    0, 1,... (count-1) of the array.
 * 
 *  The length of the array when the set is first created should be 10. 
 * 
 *  The items need not be in any particular order, and may change the
 *    order when an item is removed. There is no need to shift all the items 
 *    up or down to keep them in a specific order.
 * 
 *  Note that a set does not allow null items or duplicates.
 *  Attempting to add null should throw an IllegalArgumentException exception
 *  Adding an item which is already present should simply return false, without
 *    changing the set.
 *  It should always compare items using equals()  (not using ==)
 *  When the array is full, it will create a new array of double the current
 *    size, and copy all the items over to the new array
 */

public class ArraySet <E> extends AbstractSet <E> {

    // Data fields
    E[] data; //Generic array
    int count; //Global field for number of non-null elements in the array

    // --- Constructors --------------------------------------

    @SuppressWarnings("unchecked")  // this will stop Java complaining
    public ArraySet() {
        this.data = (E[]) new Object[10]; //Initialise the array to a size of 10.
        this.count = 0;
    }

    // --- Methods --------------------------------------
    /** 
     * @return the number of items in the set  
     */
    public int size () {return this.count;}

    /** 
     *  Adds the specified item to this set 
     *  (if it is not already in the set).
     *  Will not add a null value (throws an IllegalArgumentException in this case).
     *  
     *  @param item the item to be added to the set
     *  @return true if the collection changes, and false if it did not change.
     */
    public boolean add(E item){
        if(item==null){ //If trying to add a null.
            throw new IllegalArgumentException(); //Throw an exception.
        }
        if(!(contains(item))){ //If the array doesn't already contain the element.
            if (this.count == data.length) { //If the array is full.
                this.ensureCapacity(); //Double the arrays size.
            }
            data[this.count] = item; //Add the new item in.
            this.count++; //Increase the count of populated spaces in the array.
            return true; //Return that the array has changed.
        }
        return false; //If the collection hasn't changed, return false.
    }

    /** 
     * @return true if this set contains the specified item. 
     * 
     */
    public boolean contains(Object item) { //Return true if the item is found in the array.
        if(!(this.count==0)){
            for (int i = 0; i < this.count; i++) {
                if (data[i].equals(item)) {
                    return true;
                }
            }
        }
         return false;
    }

    /** 
     *  Removes an item matching a given item.
     *  @return true if the item was present and then removed.
     *  Makes no changes to the set and returns false if the item is not present.
     */
    public boolean remove (Object item) {
        if(!(this.count==0)){
            for (int i = 0; i < this.count; i++) {
                if (data[i].equals(item)) { //If the item is found in the array.
                    data[i] = data[this.count-1]; //Replace the item with the last element.
                    data[this.count-1]=null; //Remove the element that just replaced it.
                    this.count--; //Decrease the amount of populated elements in the array.
                    return true; //Return that the array has changed.
                }
            }
        }
        return false; //If the item isn't found, return that no changes to the array have occured.
    }

    /* 08.08.17 Get help with removing the last element that has been moved to cover the removed element
    without setting it to null. */

    /** 
    * 
    * Ensures data array has sufficient capacity (length)
    * to accomodate a new item 
    */
    @SuppressWarnings("unchecked")  // this will stop Java complaining
    private void ensureCapacity () {
            E[] temp = (E[]) new Object [data.length*2]; //Double the size of the array.
            for(int i=0; i<data.length; i++){
                temp[i]=data[i]; //Copy over the elements.
            }
            data = temp; //Set it back to original array with doubled size and original elements remaining.
    }

    // You may find it convenient to define the following method and use it in
    // the methods above, but you don't need to do it this way.

    /** 
     *  Finds the index of an item in the dataarray.
     *  Assumes that the item is not null.
     *  
     *  @return the index of the item, or -1 if not present
     */
    private int findIndexOf(Object item) {
        if(!(this.count==0)){
            for (int i = 0; i < data.length; i++) {
                if (data[i].equals(item)) { //If the element exists in the array.
                    return i; //Return the index the element is stored at in the array.
                }
            }
        }
        return -1;
    }

    /** ---------- The code below is already written for you ---------- **/

    /** 
     * @return an iterator over the items in this set. 
     * 
     */
    public Iterator <E> iterator() {
        return new ArraySetIterator<E>(this);
    }

    private class ArraySetIterator <E> implements Iterator <E> {

        // needs fields, constructor, hasNext(), next(), and remove()

        // fields
        private ArraySet<E> set;
        private int nextIndex = 0;
        private boolean canRemove = false;

        // constructor
        private ArraySetIterator(ArraySet<E> s) {
            set = s;
        }

        /**
         * @return true if iterator has at least one more item
         */
        public boolean hasNext() {
            return (nextIndex < set.count);
        }

        /** 
         * Returns the next element or throws a 
         * NoSuchElementException exception if none exists. 
         * 
         * @return next item in the set
         */
        public E next() {
            if (nextIndex >= set.count)
                throw new NoSuchElementException();

            canRemove = true;

            return set.data[nextIndex++];
        }

        /** 
         *  Removes the last item returned by the iterator from the set.
         *  Can only be called once per call to next.
         */
        public void remove() {
            if (! canRemove)
                throw new IllegalStateException();

            nextIndex--;
            set.count--;
            set.data[nextIndex] = set.data[set.count];
            set.data[set.count] = null;
            canRemove = false;
        }
    }
}

