

public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
    	int fcounter = 0;
		int bcounter = 0;
		for (int i = 0; i <arr.length & fcounter < forward; i++) {
			myStack.push(arr[i]);
			fcounter++;
			if (arr[i]==x)
				return i;
			else if (fcounter == forward) {
				while (bcounter < back) {
					i--;
					myStack.pop();
					bcounter++;
				}
				fcounter = 0; 
				bcounter = 0;
			}
		}
		return -1;
	}

    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
    	if (arr == null) throw new IllegalArgumentException();
		int output = -1;
		boolean found = false;
		int  low =  0;
		int high = arr.length-1;
		int inconsistencies = 0;
		while (low<=high & !found) {
			int mid= (low + high) / 2;
			while (inconsistencies != 0 & !myStack.isEmpty()) {
				mid = (int) myStack.pop();
				inconsistencies--;
			}
			myStack.push(mid);
			if (arr[mid] == x) {
				output = mid;
				found = true;
			}
			else if (x < arr[mid]) {
				high = mid - 1;
				low = 0;
			}
			else {
				low  = mid + 1;
				high = arr.length-1;
			}
			inconsistencies = Consistency.isConsistent(arr);
		}
		return output;
	}
    	
    
}
