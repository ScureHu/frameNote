public class Queue {
	private int start;
	private int end;
	private int size;
	private int[] arr;
	
	public Queue() {
		arr = new int[5];
		this.start = 0;
		this.end = 0;
	}
	
	public void push(int i) {
		if(size==arr.length) {
			throw new ArrayIndexOutOfBoundsException("The queue is full");
		}
		arr[end++] = i;
		size++;
	}
	
	public int pop() {
		if(size!=0) {
			 int i = arr[start++];
			 size--;
			 return i;
		}else {
			throw new ArrayIndexOutOfBoundsException("The queue is empty");
		}
	}
	
	public int peek() {
		if(size!=0) {
			 int i = arr[start];
			 return i;
		}else {
			throw new ArrayIndexOutOfBoundsException("The queue is empty");
		}
	}
}
