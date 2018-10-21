public  class TwoQueueStack {
		private Queue<Integer> queue;
		private Queue<Integer> help;
		
		public TwoQueueStack() {
			queue = new LinkedList<>();
			help = new LinkedList<>();
		}
		
		public void push(int pushInt) {
			queue.add(pushInt);
		}
		
		public int peek() {
			if(queue.isEmpty()) {
				throw new RuntimeException();
			}
			while(queue.size()!=1) {
				help.add(queue.poll());
			}
			int i = queue.poll();
			help.add(i);
			swap();
			return i;
		}
		
		public int pop() {
			if(queue.isEmpty()) {
				throw new RuntimeException();
			}
			while(queue.size()>1) {
				help.add(queue.poll());
			}
			int i = queue.poll();
			swap();
			return i;
		}
		
		private void swap(){
			Queue<Integer> itemQueue = queue;
			queue = help;
			help = itemQueue;
		}
	}
