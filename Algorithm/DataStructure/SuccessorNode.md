# 在二叉树中找到一个节点的后继结点


### 题目描述
- 该结构比普通二叉树节点结构多了一个指向父节点的parent指针
- 假设有一棵Node类型的节点组成的二叉树,数中每个节点的parent指针都正确的指向自己的父节点node,请实现返回node的后继节点的函数。
- 在二叉树的中序遍历的序列中，node的下一个节点叫做node的后继节点
  
### 要求：
请实现返回node的后继节点的函数。

### 解题思路：

 * 当传入的这个节点有右孩子，那么后继节点肯定是右孩子最左边的孩子
 * 当传入的这个节点没有右孩子，那么后继节点肯定是往上找，找到父节点的左孩子是这个节点
### 代码

```java
public class SuccessorNode {
	public static class Node {
		public int value;
		public Node left;
		public Node right;
		public Node parent;
		
		public Node(int data) {
			this.value = data;
		}
	}
	//主思路
	public static Node getSuccessorNode(Node node) {
		if(node == null) {
			return node;
		}
		if(node.right!=null) {
			return getLeftMost(node.right);
		}else {
			Node parent = node.parent;
			while(parent!=null && parent.left != node) {
				node = parent;
				parent = node.parent;
			}
			return parent;
		}
	}
	//寻找当前节点最左边的孩子
	public static Node getLeftMost(Node node) {
		if(node == null) {
			return node;
		}
		while(node.left!=null) {
			node = node.left;
		}
		return node;
	}
	//测试代码
	public static void main(String[] args) {
		Node head = new Node(6);
		head.parent = null;
		head.left = new Node(3);
		head.left.parent = head;
		head.left.left = new Node(1);
		head.left.left.parent = head.left;
		head.left.left.right = new Node(2);
		head.left.left.right.parent = head.left.left;
		head.left.right = new Node(4);
		head.left.right.parent = head.left;
		head.left.right.right = new Node(5);
		head.left.right.right.parent = head.left.right;
		head.right = new Node(9);
		head.right.parent = head;
		head.right.left = new Node(8);
		head.right.left.parent = head.right;
		head.right.left.left = new Node(7);
		head.right.left.left.parent = head.right.left;
		head.right.right = new Node(10);
		head.right.right.parent = head.right;

		Node test = head.left.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left.left.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.left.right.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right.left.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right.left;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right;
		System.out.println(test.value + " next: " + getSuccessorNode(test).value);
		test = head.right.right; // 10's next is null
		System.out.println(test.value + " next: " + getSuccessorNode(test));
	}
}
```
