# 先中后打印二叉树


### 要求

使用递归
  
### 进阶要求：
使用非递归

### 解题思路：

  递归的思路很好想，主要讲讲非递归的思路
 - 先序遍历
   - 如果头节点不为空,则准备一个栈
   - 把头节点压入栈中
   - 如果栈不为空,弹出第一个元素,并且输出它的值
   - 然后判断这个头结点是否含有右节点,有的话就压栈,然后判断这个头结点是否含有左节点,有的话就压栈
   - 直到栈中元素都弹出 
   
 - 中序遍历
   - 判断头节点是否为空
   - 不为空则进入循环,终止条件:栈空或者头节点空
   - 如果头结点不为空,则压栈,并且把头结点指向自己的左孩子,直到左孩子都压空了
   - 弹出左孩子,并打印左孩子,把头结点指向当前弹出左孩子的右孩子
   - 直到栈空
   
- 后续遍历
 - 准备两个栈,一个栈用来遍历中右左,然后把数据依次压入另外一个栈中，最后循环遍历打印这个栈
 
### 代码

```java

public class PreInPosTraversal {
	
	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}
	
	/**
	 * 递归先序遍历
	 */
	public static void preOrderRecur(Node head) {
		if(head==null) {
			return;
		}
		System.out.print(head.value+" ");
		preOrderRecur(head.left);
		preOrderRecur(head.right);
	}
	
	
	/**
	 * 递归中序遍历
	 */
	public static void inOrderRecur(Node head) {
		if(head==null) {
			return;
		}
		inOrderRecur(head.left);
		System.out.print(head.value+" ");
		inOrderRecur(head.right);
	}
	
	/**
	 * 递归后序遍历
	 */
	public static void posOrderRecur(Node head) {
		if(head==null) {
			return;
		}
		posOrderRecur(head.left);
		posOrderRecur(head.right);
		System.out.print(head.value+" ");
	}
	
	
	/**
	 * 使用非递归实现先序遍历
	 */
	public static void preOrderUnRecur(Node head) {
		System.out.print("pre-order: ");
		if(head!=null) {
			Stack<Node> stack = new Stack<>();
			stack.add(head);
			while(!stack.isEmpty()) {
				head = stack.pop();
				System.out.print(head.value+" ");
				if(head.right!=null) {
					stack.push(head.right);
				}
				if(head.left!=null){
					stack.push(head.left);
				}
			}
		}
		System.out.println();
	}
	/**
	 * 使用非递归实现中序遍历
	 */
	public static void inOrderUnRecur(Node head) {
		System.out.print("in-order: ");
		if(head != null) {
			Stack<Node> stack = new Stack<>();
			while(!stack.isEmpty() || head !=null) {
				if(head != null) {
					stack.push(head);
					head = head.left;
				}else {
					head = stack.pop();
					System.out.print(head.value+" ");
					head = head.right;
				}
			}
		}
		System.out.println();
	}
	/**
	 * 使用非递归实现后序遍历
	 */
	public static void posOrderUnRecur(Node head) {
		System.out.print("pos-order: ");
		if(head!=null) {
			Stack<Node> s1 = new Stack<>();
			Stack<Node> s2 = new Stack<>();
			s1.push(head);
			while(!s1.isEmpty()) {
				head = s1.pop();
				s2.push(head);
				if(head.left!=null) {
					s1.push(head.left);
				}
				if(head.right!=null) {
					s1.push(head.right);
				}
			}
			while(!s2.isEmpty()) {
				System.out.print(s2.pop().value + " ");
			}
		}
		System.out.println();
	}
	
	//测试代码
	public static void main(String[] args) {
		Node head = new Node(5);
		head.left = new Node(3);
		head.right = new Node(8);
		head.left.left = new Node(2);
		head.left.right = new Node(4);
		head.left.left.left = new Node(1);
		head.right.left = new Node(7);
		head.right.left.left = new Node(6);
		head.right.right = new Node(10);
		head.right.right.left = new Node(9);
		head.right.right.right = new Node(11);

		// recursive
		System.out.println("==============recursive==============");
		System.out.print("pre-order: ");
		preOrderRecur(head);
		System.out.println();
		System.out.print("in-order: ");
		inOrderRecur(head);
		System.out.println();
		System.out.print("pos-order: ");
		posOrderRecur(head);
		System.out.println();

		// unrecursive
		System.out.println("============unrecursive=============");
		preOrderUnRecur(head);
		inOrderUnRecur(head);
		posOrderUnRecur(head);
		

	}
}



```
