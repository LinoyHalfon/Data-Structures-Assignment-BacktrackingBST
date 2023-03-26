import java.util.NoSuchElementException;

public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    int top;

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        top = 0;
    }
    
    @Override
    public Integer get(int index){
    	return arr[index];
    }

    @Override
    public Integer search(int k) {
    	if (top != 0) { 
    		int  low =  0;
    		int high = top-1;
    		while (low<=high) {
    			int mid= (low + high) / 2;
    			if (arr[mid] == k) {
    				return mid;
    			}
    			else if (k < arr[mid]) 
    				high = mid - 1;
    			else 
    				low  = mid + 1;
    		}
    	}
		return -1;
	}

    @Override
    public void insert(Integer x) {
    	if (arr.length == top) 
        	throw new IllegalArgumentException();
    	boolean flag = true;
    	if (top == 0) {
    		arr[0] = x;
    		int[] inserted = {0,0,x};
            stack.push(inserted);
    		flag = false;
    	}
    	
        for (int i = top-1; i>=0 & flag; i--) {
        	if (arr[i]>x) {
        		arr[i+1]=arr[i];
        		if (i == 0) {
        			arr[0] = x;
        			int[] inserted = {0,0,x};
                    stack.push(inserted);
            		flag = false;
        		}
        	}
        	else {
        		arr[i+1]=x;
        		int[] inserted = {0,i+1,x};
                stack.push(inserted);
        		flag = false;
        	}
        }
        top++;
    }

    @Override
    public void delete(Integer index) {
    	if (index<0 | index>=top) 
        	throw new NoSuchElementException();
    	int[] deleted = {1,index,arr[index]};
        stack.push(deleted);
    	while (index<top) {
    		arr[index]=arr[index+1];
    		index++;
    	}
    	top--;
    }

    @Override
    public Integer minimum() {
    	if (top == 0)
    		throw new NoSuchElementException();
    	return 0; 
    }

    @Override
    public Integer maximum() {
    	if (top == 0)
    		throw new NoSuchElementException();
    	return top-1; 
    }

    @Override
    public Integer successor(Integer index) {
        if(top <= index+1)
        	throw new NoSuchElementException();
        return index+1;
    }

    @Override
    public Integer predecessor(Integer index) {
    	 if(index == 0)
         	throw new NoSuchElementException();
         return index-1;
     }

    @Override
    public void backtrack() {
    	if (!stack.isEmpty()) {
    		int[] curr = (int[]) stack.pop();
    		if (curr[0] == 0) { 					//undo insert
    			int i = curr[1];
    			while (i<top-1) {
    				arr[i]=arr[i+1];
    				i++;
    			}
    			top--;
    		}
    		else {								//undo delete
    			for (int i=top-1; i>=curr[1]; i--) {
    				arr[i+1] = arr[i];
    			}
    			arr[curr[1]]=curr[2];
    			top++;
    		}		
    	}   
    }

    @Override
    public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
    }

    @Override
    public void print() {
    	if (top>0) {
        	for (int i = 0; i<top; i++)
        		System.out.print(arr[i] + " ");
        	System.out.println("");
        }
    }
}
