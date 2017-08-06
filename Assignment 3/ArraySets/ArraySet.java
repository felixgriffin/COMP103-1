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
    /*# YOUR CODE HERE */
    Object array [];

    // --- Constructors --------------------------------------

    @SuppressWarnings("unchecked")  // this will stop Java complaining
    public ArraySet() {
        /*# YOUR CODE HERE */
        array = new Object[10];
    }

    // --- Methods --------------------------------------
    /** 
     * @return the number of items in the set  
     */
    public int size () { //Increments a dummy variable every time it finds a non-null element. Returns amount of elements.
        int count=0;
        for(int i=0; i<array.length; i++){
            if(!(array[i]==null)){
                count++;
            }
        }
        return count;
    }

    /** 
     *  Adds the specified item to this set 
     *  (if it is not already in the set).
     *  Will not add a null value (throws an IllegalArgumentException in this case).
     *  
     *  @param item the item to be added to the set
     *  @return true if the collection changes, and false if it did not change.
     */
    public boolean add(E item) {
        for(int i=0; i<array.length; i++){ //If it's already there, return.
            if(array[i].equals(item)){
                return false;
            }
        }
        if(this.size()<array.length){ //If we have space, add it to the array.
            for(int j=0; j<array.length; j++){
                if(array[j]==null){
                    array[j]=item;
                    return true;
                }
            }
        }
        else{ //If we don't have space, double the array size and add it to the array.
            this.ensureCapacity();
            for(int k=0; k<array.length; k++){
                if(array[k]==null){
                    array[k]=item;
                    return true;
                }
            }
        }
        return false;
    }

    /** 
     * @return true if this set contains the specified item. 
     * 
     */
    public boolean contains(Object item) { //Return true if the item is found in the array.
            for(int i=0; i<array.length; i++){
                if(array[i].equals(item)){
                    return true;
                }
            }
         return false;
    }

    /** 
     *  Removes an item matching a given item.
     *  @return true if the item was present and then removed.
     *  Makes no changes to the set and returns false if the item is not present.
     */
    public boolean remove (Object item) { //If an item is found in the array, replace it with last element and make last
                                            //element null.
            for(int i=0; i<array.length; i++){
                if(array[i].equals(item)){
                    array[i]=array[array.length-1];
                    array[array.length-1]=null;
                   return true;
                }
            }
        return false;
    }

    /** 
    * 
    * Ensures data array has sufficient capacity (length)
    * to accomodate a new item 
    */
    @SuppressWarnings("unchecked")  // this will stop Java complaining
    private void ensureCapacity () {
        if(this.size()==array.length){ //If the number of non-null elements is the same as the size of the array
                                        //double the size of the array.
            Object temp []= new Object [array.length]; //Copy over all the elements.
            for(int i=0; i<array.length; i++){
                temp[i]=array[i];
            }
            array = new Object [temp.length*2]; //Make a new array in the same place with double the length.
            for(int j=0; j<temp.length; j++){
                array[j]=temp[j]; //Copy over all of the elements from the temporary array.
            }
        }

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
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(item)) {
                    return i;
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

