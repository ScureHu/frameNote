public class Stack {
	private int size;
	private int[] arr;
	
	public Stack(int initSize) {
		if(initSize<0) {
			throw new IllegalArgumentException("The init size is less than 0");
		}
		arr = new int[initSize];
		size = 0;
	}
	
	public Integer peek() {
		if(size==0) {
			return null;
		}
		return arr[size];
	}
	
	public Integer pop() {
		if(size==0) {
			throw new ArrayIndexOutOfBoundsException("The queue is empty");
		}
		return arr[--size];
	}
	
	public void push(int obj) {
		if(size == arr.length) {
			throw new ArrayIndexOutOfBoundsException("The queue is empty");
		}
		arr[size++] = obj;
	}
}
