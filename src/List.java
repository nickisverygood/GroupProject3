/**
 * CIS 22C, Lab 4
 *
 * @author Guoyi li
 * @author Bo Ying, Su
 */

import java.io.Serializable;
import java.util.NoSuchElementException;

public class List<T extends Comparable<T>> implements Serializable {

    private class Node implements Serializable {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**** CONSTRUCTOR ****/

    /**
     * Instantiates a new List with default values
     *
     * @postcondition the variable length, first and last initialize the data.
     */
    public List() {

        length = 0;
        first = null;
        last = null;
        iterator = null;
    }

    /**
     * Instantiates a new List with default values
     *
     * @postcondition create a deep copy of linked list.
     */
    public List(List<T> original) {
        if (original.length == 0) {
            length = 0;
            first = null;
            last = null;
            iterator = null;
        } else {
            Node temp = original.first;
            while (temp != null) {
                addLast(temp.data);
                temp = temp.next;
            }
            iterator = null;
        }
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     *
     * @return the value stored at node first
     * @throws NoSuchElementException when precondition is violated
     * @precondition linked list is not empty
     */
    public T getFirst() throws NoSuchElementException {

        if (first == null) {
            throw new NoSuchElementException("getFirst(): " + "The Linked list is emtpy,cannnot return the data!");
        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     *
     * @return the integer value stored in the node last
     * @throws NoSuchElementException when precondition is violated
     * @precondition linked list is not empty
     */
    public T getLast() throws NoSuchElementException {

        if (length == 0) {
            throw new NoSuchElementException("getLast(): " + "The Linked list is emtpy,cannnot return the data!");
        }
        return last.data;
    }

    /**
     * Returns the current length of the list
     *
     * @return the length of the list from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the list is currently empty
     *
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns returns the element currently pointed at by the iterator
     *
     * @return the iterator's data
     * @precondition iterator is not null
     */
    public T getIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("getIterator()" + "The iterator is off end. cannot access!");
        }
        return iterator.data;
    }

    /**
     * Returns whether the iterator is currently empty
     *
     * @return whether the iterator is empty
     */
    public boolean offEnd() {
        return iterator == null;

    }

    /**
     * compares this list to another list to see if they contain the same data in
     * the same order.
     *
     * @return whether the two list is equal or not.
     * @postcondition overrides the equals method for object to
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        } else if (!(obj instanceof List)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
			List<T> L = (List<T>) obj;
            if (this.length != L.length) {
                return false;
            } else {
                Node temp1 = this.first;
                Node temp2 = L.first;
                while (temp1 != null) {
                    if (!temp1.data.equals(temp2.data)) {
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
            }
        }
    }

    /**
     * Determines whether a List is sorted
     * by calling the recursive helper method
     * isSorted
     * Note: A List that is empty can be
     * considered to be (trivially) sorted
     *
     * @return whether this List is sorted
     */
    public boolean isSorted() {
        return isSorted(first);
    }

    /**
     * Recursively determines whether
     * a List is sorted in ascending order
     *
     * @return whether this List is sorted
     */
    public boolean isSorted(Node n) {
        if (n.next != null) {
            if (n.data.compareTo(n.next.data) <= 0) {
                return isSorted(n.next);
            } else {
                return false;
            }
        }
        return true;


    }

    /**
     * Returns the index of the iterator
     * from 1 to n. Note that there is
     * no index 0.
     *
     * @return the index of the iterator
     * @throws NullPointerException when
     *                              the precondition is violated
     * @precondition
     */
    public int getIndex() throws NullPointerException {
        if(iterator == null) throw  new NullPointerException("getIndex(): Iterator is null!");
        Node indexIterator = first;
        int currentPos = 1;
        while(indexIterator != iterator){
            indexIterator = indexIterator.next;
            currentPos++;
        }
        return currentPos;
    }

    /**
     * Searches the List for the specified
     * value using the iterative linear
     * search algorithm
     * @param value the value to search for
     * @return the location of value in the
     * List or -1 to indicate not found
     * Note that if the List is empty we will
     * consider the element to be not found
     * @postcondition: position of the iterator remains
     * unchanged!
     */
    public int linearSearch(T value) {
        if(isEmpty()) return -1;
        int initIteratorIndex;
        try{
            initIteratorIndex = getIndex();
        }catch (NullPointerException e){
            initIteratorIndex = -1;
        }

        pointIterator();
        while (!offEnd()){
            if(getIterator().compareTo(value) == 0){
                int foundIndex = getIndex();
                if(initIteratorIndex==-1){
                    iterator=null;
                }else {
                    moveToIndex(initIteratorIndex);
                }
                return foundIndex;
            }else {
                advanceIterator();
            }
        }

        if(initIteratorIndex==-1){
            iterator=null;
        }else {
            moveToIndex(initIteratorIndex);
        }

        return -1;
    }



    /**
     * Returns the index from 1 to length
     * where value is located in the List
     * by calling the private helper method
     * binarySearch
     * @param value the value to search for
     * @return the index where value is
     * stored from 1 to length, or -1 to
     * indicate not found
     * @precondition isSorted()
     * @postcondition the position of the
     * iterator must remain unchanged!
     * @throws IllegalStateException when the
     * precondition is violated.
     */
    public int binarySearch(T value) throws IllegalStateException {

        int initIteratorIndex ;
        try{
            initIteratorIndex = getIndex();
        }catch (NullPointerException ex){
            initIteratorIndex=-1;
        }
        int result = binarySearch(1,length,value);
        if(initIteratorIndex==-1){
            iterator=null;
        }else {
            moveToIndex(initIteratorIndex);
        }
        return result;
    }

    /**
     * Searches for the specified value in
     * the List by implementing the recursive
     * binarySearch algorithm
     * @param low the lowest bounds of the search
     * @param high the highest bounds of the search
     * @param value the value to search for
     * @return the index at which value is located
     * or -1 to indicate not found
     * @postcondition the location of the iterator
     * must remain unchanged
     */
    private int binarySearch(int low, int high, T value) {
        if(isEmpty()) return -1;
        if(high<low) return -1;


        pointIterator();
        int mid =low + (high-low)/2;
        moveToIndex(mid);
        if(getIterator().compareTo(value) == 0){
            return mid;
        }else if(getIterator().compareTo(value) > 0){
            return binarySearch(low, mid-1, value);
        }else {
            return binarySearch(mid+1, high, value);
        }


    }



/**Manipulation Procedures*/

    /**
     * Points the iterator at first
     * and then iteratively advances
     * it to the specified index
     *
     * @param index the index where
     *              the iterator should be placed
     * @throws IndexOutOfBoundsException when precondition is violated
     * @precondition 1 <= index <= length
     */
    public void moveToIndex(int index) throws IndexOutOfBoundsException {
        if(!(index>=1 && index <=length) )throw new IndexOutOfBoundsException("moveToIndex(): Invalid Index!");
        pointIterator();
        int currentPos = 1;
        while(currentPos < index){
            currentPos++;
            advanceIterator();
        }
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     *
     * @param data the data to insert at the front of the list
     * @postcondition A newNode added in front of the list
     */
    public void addFirst(T data) {

        if (first == null) {
            first = last = new Node(data);
        } else {
            Node newNode = new Node(data);
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        length++;
    }

    /**
     * Creates a new last element
     *
     * @param data the data to insert at the end of the list
     * @postcondition A newNode added in back of the list
     */
    public void addLast(T data) {

        if (length == 0) {
            first = last = new Node(data);
        } else {
            Node newNode = new Node(data);
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        length++;
    }

    /**
     * removes the element at the front of the list
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition the linked list is not empty
     * @postcondition remove the first node in the linked list
     */
    public void removeFirst() throws NoSuchElementException {

        if (length == 0) {
            throw new NoSuchElementException("removeFirst(): " + "The Linked list is emtpy,cannnot access!");
        } else if (length == 1) {
            first = last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        length--;

    }

    /**
     * removes the element at the end of the list
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition the linked list is not empty
     * @postcondition remove the last node in the linked list
     */
    public void removeLast() throws NoSuchElementException {

        if (length == 0) {
            throw new NoSuchElementException("removeLast(): Cannot remove from an empty List!");
        } else if (length == 1) {
            first = last = null;
        } else {

            last = last.prev;
            last.next = null;
        }
        length--;
    }

    /**
     * let iterator point the first element in the Linked list
     */
    public void pointIterator() {
        iterator = first;
    }

    /**
     * removes the element currently pointed to by the iterator.
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition iterator cannot be empty
     * @postcondition remove the node in the linked list
     */
    public void removeIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("removeIterator()" + "The iterator is off end. cannot access!");
        } else if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.prev.next = iterator.next;
            iterator.next.prev = iterator.prev;
            length--;
        }
        iterator = null;
    }

    /**
     * @throws NoSuchElementException when precondition is violated
     * @precondition iterator != null
     * @postcondition inserts an element after the node currently pointed to by the
     * iterator
     */
    public void addIterator(T data) throws NullPointerException {
        // Precondition
        if (offEnd()) {
            throw new NullPointerException(
                    "addIterator" + "\"The iterator is off end. cannot add data on the Linked list!");
        } else if (iterator == last) {
            addLast(data);
        } else {
            Node newNode = new Node(data);
            newNode.next = iterator.next;
            newNode.prev = iterator;
            iterator.next.prev = newNode;
            iterator.next = newNode;
            length++;
        }
    }

    /**
     * Advances the iterator by one node in the List
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition !offEnd()
     * @postcondition moves the iterator up by one node
     */
    public void advanceIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator(): " + "Iterator is off end. cannnot advance!");
        }
        iterator = iterator.next;
        return;
    }

    /**
     * Reverses the iterator by one node in the List
     *
     * @throws NoSuchElementException when precondition is violated
     * @precondition the linked list cannot be empty. iterator cannot be empty or
     * the first element of linked list
     * @postcondition moves the iterator down by one node
     */
    public void reverseIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator(): " + "Iterator is off end. cannnot advance!");
        }
        iterator = iterator.prev;
    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * List with each value separated by a blank space At the end of the List a new
     * line
     *
     * @return the List as a String for display
     */
    @Override
    public String toString() {
        String result = "";
        Node temp = first;
        while (temp != null) {
            result += temp.data;
            temp = temp.next;
            result += " ";
        }
        result += "\n";
        return result;
    }

    /**
     * Prints the contents of the linked list to the screen in the format #.
     * <element> followed by a newline
     */
    public void printNumberedList() {
        int count = 0;
        Node temp = first;
        while (temp != null) {
            count++;
            System.out.print(count + ". " + temp.data.toString() + "\n");
            temp = temp.next;
        }
    }

    /**
     * Prints a linked list to the console
     * in reverse by calling the private
     * recursive helper method printReverse
     */
    public void printReverse() {
        printReverse(last);
    }

    /**
     * Prints a linked list to the console
     * recursively (no loop)
     * in reverse order from last to first
     * Each element separated by a space
     * Should print a new line after all
     * elements have been displayed
     */

    private void printReverse(Node n) {
        if (n != null) {
            System.out.print(n.data + " ");
            printReverse(n.prev);
        } else {
            System.out.print("\n");
        }
    }

}
