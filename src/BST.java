
import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BST<T extends Comparable<T>> implements Serializable {
    private class Node implements Serializable {
        private T data;
        private Node left;
        private Node right;
        
        public Node(T data) {
            this.data = data;
            left = null;
            right = null;
        }
    }
    
    private Node root;
    
    /***CONSTRUCTORS***/
    
    /**
     * Default constructor for BST
     * sets root to null
     */
    public BST() {
        root = null;
    }
    
    /**
     * Copy constructor for BST
     * @param bst the BST to make
     * a copy of 
     */
    public BST(final BST<T> bst) {
    	if(bst.isEmpty()) {
    		root = null;
    	}else {
    		copyHelper(bst.root);
    	}
    }
    
    /**
     * Helper method for copy constructor
     * @param node the node containing
     * data to copy
     */
    private void copyHelper(Node node) {
    	if(node == null) {
    		return;
    	}else {
    		insert(node.data);
            copyHelper(node.left);
            copyHelper(node.right);
        }
    }
    
    /***ACCESSORS***/
    
    /**
     * Returns the data stored in the root
     * @precondition !isEmpty()
     * @return the data stored in the root
     * @throws NoSuchElementException when
     * preconditon is violated
     */
    public T getRoot() throws NoSuchElementException{
    	if(isEmpty()) {
    		throw new NoSuchElementException("getRoot()" + "The root is empty,cannot access");
    	}
        return root.data;
    }
    
    /**
     * Determines whether the tree is empty
     * @return whether the tree is empty
     */
    public boolean isEmpty() {
        return getSize()==0;
    }
    
    /**
     * Returns the current size of the 
     * tree (number of nodes)
     * @return the size of the tree
     */
    public int getSize() {
        return getSize(root);
    }
    
    /**
     * Helper method for the getSize method
     * @param node the current node to count
     * @return the size of the tree
     */
    private int getSize(Node node) {
    	if( node == null ) {
    		return 0;
    	}
        return 1+getSize(node.left)+getSize(node.right);
    }
    
    /**
     * Returns the height of tree by
     * counting edges.
     * @return the height of the tree
     */
    public int getHeight() {
        return getHeight(root);
    }
    
    /**
     * Helper method for getHeight method
     * @param node the current
     * node whose height to count
     * @return the height of the tree
     */
    private int getHeight(Node node) {
    	if (node == null) return -1;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        
    }
    /**
     * Returns the smallest value in the tree
     * @precondition !isEmpty()
     * @return the smallest value in the tree
     * @throws NoSuchElementException when the
     * precondition is violated
     */
    public T findMin() throws NoSuchElementException{
    	if(root == null ) {
    		throw new NoSuchElementException("findMin(): "+ "there a empty root");
    	}
        return findMin(root);
    }
    
    /**
     * Helper method to findMin method
     * @param node the current node to check
     * if it is the smallest
     * @return the smallest value in the tree
     */
    private T findMin(Node node) {
    	if(node.left == null) {
    		return node.data;
    	}
        return findMin(node.left);
    }
    
    /**
     * Returns the largest value in the tree
     * @precondition !isEmpty()
     * @return the largest value in the tree
     * @throws NoSuchElementException when the
     * precondition is violated
     */
    public T findMax() throws NoSuchElementException{
    	if(root == null ) {
    		throw new NoSuchElementException("findMAX(): "+ "there a empty root");
    	}
        return findMax(root);
    }
    
    /**
     * Helper method to findMax method
     * @param node the current node to check
     * if it is the largest
     * @return the largest value in the tree
     */
    private T findMax(Node node) {
    	if(node.right == null) {
    		return node.data;
    	}
        return findMax(node.right);
    }
    
    /**
     * Searches for a specified value
     * in the tree
     * @param data the value to search for
     * @return whether the value is stored
     * in the tree
     */
    public boolean search(T data) {
        if(root == null) {
        	return false;
        }else {
        	return search(data, root);
        }
    }
    
    /**
     * Helper method for the search method
     * @param data the data to search for
     * @param node the current node to check
     * @return whether the data is stored
     * in the tree
     */
    private boolean search(T data, Node node) {
    	if(data.compareTo(node.data) == 0){
    		return true;
    	}else if(data.compareTo(node.data) < 0) {
    		if(node.left == null) {
    			return false;
    		}else  {
    			 return search(data,node.left);
    		}
    	}else {
    		if(node.right == null) {
        			return false;
        	}else{
        		return search(data,node.right);
    		}			
       }
    }

    public ArrayList<T> getArray(){
        ArrayList<T> bstarray = new ArrayList<>();
        getArrayHelper(root,bstarray);
        return bstarray;
    }

    private void getArrayHelper(Node node,ArrayList<T> bstarray){
        if(node == null) {
            return ;
        }else {
            getArrayHelper(node.left,bstarray);
            bstarray.add(node.data);
            getArrayHelper(node.right,bstarray);
        }
    }


    /***MUTATORS***/
    
    /**
     * Inserts a new node in the tree
     * @param data the data to insert
     */
    public void insert(T data) {
       if(root == null) {
    	   root = new Node(data);
       }else {
    	   insert(data,root);
       }
    }
    
    /**
     * Helper method to insert
     * Inserts a new value in the tree
     * @param data the data to insert
     * @param node the current node in the
     * search for the correct location
     * in which to insert
     */
    public void insert(T data, Node node) {
        if(data.compareTo(node.data)<=0) {
        	if(node.left == null) {
        		node.left = new Node(data);
        	}else {
        		insert(data, node.left);
        	}
        }else {
        	if(node.right == null) {
        		node.right = new Node(data);
        	}else {
        		insert(data, node.right);
        	}
        }
    }
    
    /**
     * Removes a value from the BST
     * @param data the value to remove
     * @precondition !isEmpty()
     * @precondition the data is located in the tree
     * @throws NoSuchElementException when the
     * precondition is violated
     */
    public void remove(T data) throws NoSuchElementException{
        if(isEmpty()) {
        	throw new NoSuchElementException("remove(): " + "The BST is empty.cannot access");
        }else {
        	root=remove(data,root);
        }
    }
    
    /**
     * Helper method to the remove method
     * @param data the data to remove
     * @param node the current node
     * @return an updated reference variable
     */
    private Node remove(T data, Node node) { 
    	if(node == null) {
    		return node;
    	}else if(data.compareTo(node.data)<0) {
    		node.left = remove(data, node.left);
    	}else if(data.compareTo(node.data)>0) {
    		node.right = remove(data, node.right);
    	}else {
    		if(node.left == null&&node.right == null ) {
    			node = null;
    		}else if(node.right == null&&node.left != null) {
    			return node.left;
    		}else if(node.left == null && node.right != null) {
    			return node.right;
    		}else {
    			T min = findMin(node.right);
                node.data = min;
                node.right=remove(min,node.right);
    		}
    	}
        return node;
    }
    
        
    /***ADDITONAL OPERATIONS***/
    
    /**
     * Prints the data in pre order
     * to the console
     */
    public String preOrderPrint() {
        return preOrderPrint(root);
    }
    
    /**
     * Helper method to preOrderPrint method
     * Prints the data in pre order
     * to the console
     */
    public String preOrderPrint(Node node) {
       String friendsData = "";
       int i =0;
       if(node == null) {
    	   return "Nothing on the Friends list";
       }else {
    	   ++i;
    	   friendsData +=" Friend" + i + ": " + node.data + "\n";
    	   preOrderPrint(node.left);
    	   preOrderPrint(node.right);
       }
       return friendsData;
    }
    
    /**
     * Prints the data in sorted order 
     * to the console
     */
    public void inOrderPrint() {
        inOrderPrint(root);
    }
    
    /**
     * Helper method to inOrderPrint method
     * Prints the data in sorted order
     * to the console
     */
    public void inOrderPrint(Node node) {
        if(node == null) {
        	return;
        }else {
        	inOrderPrint(node.left);
        	System.out.println(node.data);
        	inOrderPrint(node.right);
        }
    }
    
    /**
     * Prints the data in post order
     * to the console
     */
    public void postOrderPrint() {
        postOrderPrint(root);
    }
    
    /**
     * Helper method to postOrderPrint method
     * Prints the data in post order
     * to the console
     */
    public void postOrderPrint(Node node) {
        if( node == null ) {
        	return;
        }else {
        	postOrderPrint(node.left);
        	postOrderPrint(node.right);
        	System.out.println(node.data);
        }
    }
}