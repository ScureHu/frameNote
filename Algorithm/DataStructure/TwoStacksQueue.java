public  class TwoStacksQueue{
		private Stack<Integer> stackPush; 
		private Stack<Integer> stackPop;
		//Constructor two stack
		public TwoStacksQueue() {
			stackPush = new Stack<Integer>();
			stackPop = new Stack<Integer>();
		}
		
		public void push(int pushInt) {
			stackPush.push(pushInt);
		}
		
		public int poll() {
			if(stackPush.empty()&&stackPop.empty()) {
				throw new RuntimeException("queue is empty!!");
			}else if(stackPop.empty()) {
				while(!stackPush.empty()) {
					stackPop.push(stackPush.pop());
				}
			}
			return stackPop.pop();
		}
		
		public int peek() {
			if(stackPush.empty()&&stackPop.empty()) {
				throw new RuntimeException("queue is empty");
			}else if(stackPop.empty()) {
				while(!stackPush.empty()) {
					stackPop.push(stackPush.pop());
				}
			}
			return stackPop.peek();
		}
  }
