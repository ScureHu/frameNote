# 实现一个特殊的栈,在实现栈的基本功能的基础上,再实现返回栈中最小元素的操作
## 要求
### 1.pop,push,getMin操作的时间复杂度都是O(1)
### 2.设计的栈类型可以使用现成的栈结构

#### 解题思路

使用两个栈,第一个栈用来存放真实的元素,第二个栈用来存放最小的元素,每次放新元素的时候,需要与第二个栈的栈头对比,
如果大则继续给第二栈压入上一次本栈压入的元素，如果小则压入该元素


#### 代码实现

```java
public class GetMinStack {
	private Stack<Integer> stackDatas;
	private Stack<Integer> stackMins;
	
	public GetMinStack() {
		stackDatas = new Stack<>();
		stackMins = new Stack<>();
	}
	//入栈
	public void push(int i) {
		stackDatas.push(i);
		if(stackMins.isEmpty()) {
			stackMins.push(i);
		}else if(i<stackMins.peek()) {
			stackMins.push(i);
		}else{
			stackMins.push(stackMins.peek());
		}
	}
	
	//出栈
	public Integer pop() {
		stackMins.pop();
		return stackDatas.pop();
	}
	//取得最小值
	
	public Integer getMin() {
		if (this.stackMins.isEmpty()) {
			throw new RuntimeException("Your stack is empty.");
		}
		return this.stackMins.peek();
	}
}
```
