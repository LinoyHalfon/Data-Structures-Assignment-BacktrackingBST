import java.util.NoSuchElementException;

public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int top;

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
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
    		for (int i=0; i<top; i++) {
    			if (arr[i] == k)
    				return i;
    		}
    	}
    	return -1; 
    }

    @Override
    public void insert(Integer x) {
        if (arr.length == top) 
        	throw new IllegalArgumentException();
        arr[top] = x;
        int[] inserted = {0,top,x};
        stack.push(inserted);
        top++;
    }

    @Override
    public void delete(Integer index) {
        if (index<0 | index>=top) 
        	throw new NoSuchElementException();
        int[] deleted = {1,index,arr[index]};
        stack.push(deleted);
        arr[index] = arr[top-1];
        top--;
    }

    @Override
    public Integer minimum() {
    	if (top == 0)
    		throw new NoSuchElementException();
    	int minindex = 0;
        for (int i=1; i<top; i++) {
        	if (arr[minindex] > arr[i]) {
        		minindex = i;
        	}
        }
    	return minindex; 
    }

    @Override
    public Integer maximum() {
    	if (top == 0)
    		throw new NoSuchElementException();
    	Integer maxindex = 0;
        for (int i=1; i<top; i++) {
        	if (arr[maxindex] < arr[i])
        		maxindex = i;
        }
    	return maxindex; 
    }

    @Override
    public Integer successor(Integer index) {
        if (index>=top & index<0)
        	throw new NoSuchElementException();
        int sucindex = maximum();
        for (int i = 1; i<top; i++) {
        	if (arr[i]>arr[index] & arr[i]<arr[sucindex]) 
        		sucindex = i;
        }
        if (arr[sucindex] <= arr[index])
        	throw new NoSuchElementException();
    	return sucindex; 
    }

    @Override
    public Integer predecessor(Integer index) {
    	if (index>=top & index<0)
        	throw new NoSuchElementException();
        int preindex  = minimum();
        for (int i = 1; i<top; i++) {
        	if (arr[i]<arr[index] & arr[i]>arr[preindex]) {
        		arr[preindex] = arr[i];
        		preindex = i;
        	}
        }
        if (arr[preindex] >= arr[index])
        	throw new NoSuchElementException();
    	return preindex; 
    }

    @Override
    public void backtrack() {
    	if (!stack.isEmpty()) {
    		int[] curr = (int[]) stack.pop();
    		if (curr[0] == 0)  //undo insert
    			top--;
    		else { //undo delete
    			Integer temp = arr[curr[1]];
    			arr[curr[1]] = curr[2];
    			arr[top] = temp;
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
