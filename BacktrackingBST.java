

import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private BacktrackingBST.Node root = null;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
    	if (root == null) {
    		throw new NoSuchElementException("empty tree has no root");
    	}
        return root;
    }
    
    public Node search(int k) {
    	Node curr = root;
    	while (curr != null && curr.key != k) {
    		if (k<curr.key)
    			curr=curr.left;
    		else
    			curr=curr.right;
    	}
    	return curr; 
    }
    
    
    public void insert(Node node) {
        Node curr = root;
        Node prev=null;
    	if (curr == null)
        	root = node;
    	else {
    		while (curr != null) {
    			prev = curr;
    			if (curr.key < node.key)
    				curr = curr.right;
    			else
    				curr = curr.left;
    		}
    		node.parent=prev;
    		if (node.key < prev.key)
    			prev.left = node;
    		else
    			prev.right = node;
    	}
	    	stack.push(node);
	    	stack.push("inserted");
	    	while (!redoStack.isEmpty())
	    		redoStack.pop();
    }

    public void delete(Node node) {
    	Node p = node.parent;
    	if (node.left == null & node.right == null) { //node had no children
    		stack.push(node);
        	stack.push("deleted");
    		if (p == null)//node is root & leaf
        		root = null;		
        	else if (node.key < p.key)  
        		p.left = null;
        	else 
        		p.right = null;
    	}
    	else if (node.left == null | node.right == null) { //had one child
    		stack.push(node);
        	stack.push("deleted");
    		if (p == null & node.left != null) //was a root and had only left child
    			root = node.left;
    		else if (p == null & node.right != null) //was a root and had only right child
    			root = node.right;
    		else { //isn't root
    			if (node.key < p.key) { // if deleted node was a left child 
    				if (node.left != null) { // if deleted node had a left child
    					p.left = node.left;
    					node.left.parent = node.parent;
    				}
    				else { // if deleted node had a right child
    					p.left = node.right;
    					node.right.parent = node.parent;
    				}
    			}	
    			else { // if deleted node was a right child
    				if (node.left != null) { // if deleted node had a left child
    					node.left.parent = node.parent;
    					p.right = node.left;
    				}
    				else { /// if deleted node had a right child
    					node.right.parent = node.parent;
    					p.right = node.right;
    				}
        		}
        	}
        }
    	else { //had 2 children
    		Node toreplace = successor(node);
    		Node toreplacecopy = new Node(toreplace);
    		Node deletedcopy = new Node(node);
    		Node todelete = node;
    		stack.push(deletedcopy);
        	stack.push("deleted");
        	stack.push(toreplacecopy);
    		stack.push("replaced");
        	delete(toreplace);
        	stack.pop(); stack.pop();
    		if (p == null) // if deleted node was the root
    			root = toreplace;
    		else { // if deleted node wasn't the root
    			if (todelete.key < todelete.parent.key)  // if deleted node was a left child
    				todelete.parent.left = toreplace;
    		
        		else  // if deleted node was a right child
        			todelete.parent.right = toreplace;
    		}
    		toreplace.parent = todelete.parent;
    		if (todelete.right != null)
    			todelete.right.parent = toreplace;
        	toreplace.right = todelete.right;
        	if (todelete.left != null)
        		todelete.left.parent = toreplace;
        	toreplace.left = todelete.left;
    	}  
    	while (!redoStack.isEmpty())
    		redoStack.pop(); 
    }

    public Node minimum() {
        Node curr = root;
        if (curr == null)
        	throw new NoSuchElementException();
    	while (curr.left != null)
    		curr = curr.left;
    	return curr; 
    }

    public Node maximum() {
    	Node curr = root;
    	if (curr == null)
        	throw new NoSuchElementException();
    	while (curr.right != null)
    		curr = curr.right;
    	return curr; 
    }

    public Node successor(Node node) {
        Node curr;
    	if (node.right != null) {
        	curr = node.right;
        	while (curr.left != null) 
            	curr = curr.left;
        }
        else{ 
        	curr = node;
        	while (curr.parent != null && curr.key > curr.parent.key) 
        		curr = curr.parent;
        	curr = curr.parent;	
        	if (curr == null)
            	throw new NoSuchElementException(); 
        }
    	return curr; 
    }

    public Node predecessor(Node node) {
    	Node curr;
    	if (node.left != null) {
        	curr = node.left;
        	while (curr.right != null) 
            	curr = curr.right;
        }
        else{ 
        	curr = node;
        	while (curr.parent != null && curr.key < curr.parent.key) 
        		curr = curr.parent;
        	curr = curr.parent;	
        	if (curr == null)
            	throw new NoSuchElementException(); 
        }
    	return curr; 
    }

    @Override
    public void backtrack() {
    	if (!stack.isEmpty()) {
    		String action = (String) stack.pop();
    		if (action.equals("replaced")) { // last action was deletion of node with 2 children
    			Node replaced = (Node) stack.pop();
    			stack.pop();
    			Node deleted = (Node) stack.pop();
    			redoStack.push(deleted);
    			redoStack.push("redo delete");
    			
    			if (deleted.left != null)
    				deleted.left.parent = deleted;
    			if (deleted.right != null)
    				deleted.right.parent = deleted;
    			
    			if (deleted.parent == null) {   //deleted was root
    				root = deleted;
    			}
    			else { //deleted wasn't root
    				if (deleted.parent.key > deleted.key)
    					deleted.parent.left = deleted;
    				else
    					deleted.parent.right = deleted;
    			}
    			
    			if (replaced.left != null)
    				replaced.left.parent = replaced;
    			if (replaced.right != null)
    				replaced.right.parent = replaced;
    			
    			if (replaced.parent != null) { //replaced wasn't root
    				if (replaced.parent.key > replaced.key)
    					replaced.parent.left = replaced;
    				else
    					replaced.parent.right = replaced;
    			}
    			else { //replaced was root
    				root = replaced;
    			}
    		}

    		else if (action.equals("deleted")) { // last action was deletion of node with 0/1 children
    			Node deleted = (Node) stack.pop();
    			redoStack.push(deleted);
    			redoStack.push("redo delete");
    			if (deleted.left != null)
    				deleted.left.parent = deleted;
    			if (deleted.right != null)
    				deleted.right.parent = deleted;
    			if (deleted.parent != null) {
    				if (deleted.parent.key > deleted.key)
    					deleted.parent.left = deleted;
    				else
    					deleted.parent.right = deleted;
    			}
    			else  //deleted was root
    				root = deleted;
    		}
    		else { // last action was insert
    			Node inserted = (Node) stack.pop();
    			redoStack.push(inserted);
    			redoStack.push("redo insert");
    			if (inserted.parent != null) { //inserted isn't root
    				if (inserted.parent.key > inserted.key)
    					inserted.parent.left = null;
    				else
    					inserted.parent.right = null;
    			}
    			else //inserted is root
    				root = null;
    			if (stack.pop().equals("replaced"))
    				stack.pop(); stack.pop();
    				stack.pop();
    		}	
    	}
    }

    @Override
    public void retrack() {
    	if (!redoStack.isEmpty()) {
    		String action = (String) redoStack.pop();
    		if (action.equals("redo insert"))
    			insert((Node) redoStack.pop());
    		if (action.equals("redo delete"))
    			delete((Node) redoStack.pop());
    	}
    }

    public void printPreOrder(){
        if (root != null)
        	root.printPreOrder();
    }

    @Override
    public void print() {
    	printPreOrder();
    }
    
    public String toString() {
		if (root!=null) {
			System.out.println("***************************");
			return root.toString2();
		}
		else
			return "Empty Tree";
	}

    public static class Node {
    	// These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;
        
        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }
        
        public Node(Node node) {
            this.key = node.key;
            this.value = node.value;
            this.left = node.left;
            this.right = node.right;
            this.parent = node.parent;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
        
        public void printPreOrder(){
            System.out.print(key + " ");
            if (left != null)
            	left.printPreOrder();
            if (right != null)
            	right.printPreOrder();
        }
        
        public String toString() {
			return ""+this.getKey()+"";
		}
		public String toString2() {
			String d="";
			return toString2(d);
		}

		private String toString2(String d) {
			String s="";
			if(right!=null)
				s=s+right.toString2(d+"  ");
			s=s+d+getKey()+"\n";
			if(left!=null)
				s=s+left.toString2(d+"  ");
			return s;
		}
        
    }
}
