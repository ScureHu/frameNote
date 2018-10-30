## 反转单双向链表

### 问题描述

分别实现反转单向链表和反转双向链表的函数

### 要求

如果链表长度为N，时间复杂度要求为O(N)，额外空间复杂度要求为O(1)

### 解题思路

#### 单向链表

- 设置两个节点，分别为next，pre
- 每次遍历节点的时候，首先保存下一个节点，存在next节点中
- 让遍历到的节点的下一个节点指向pre
- 再改变pre节点为当前节点
- 再把遍历的节点改为下一个节点（next节点）

#### 双向链表

- 设置两个节点，分别为next，pre
- 每次遍历节点的时候，首先保存下一个节点，存在next节点中
- 让遍历到的节点的下一个节点指向pre
- 让遍历到的节点的上一个节点指向next
- 再改变pre节点为当前节点
- 再把遍历的节点改为下一个节点（next节点）

### 代码实现

```java

//反转单双向链表
public class ReverseList {
	//单链表节点结构
	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}
	//反转单向链表
	public static Node reverseList(Node head) {
		Node pre = null;
		Node next = null;
		while(head != null) {
			next = head.next;
			head.next = pre;
			pre = head;
			head = next;
		}
		return pre;
	}
	
	//双向链表节点结构
	public static class DoubleNode {
		public int value;
		public DoubleNode last;
		public DoubleNode next;

		public DoubleNode(int data) {
			this.value = data;
		}
	}
	//反转双向链表
	public static DoubleNode reverseDoubleList(DoubleNode head) {
		DoubleNode pre = null;
		DoubleNode next = null;
		while(head!=null) {
			next = head.next;
			head.next = pre;
			head.last = next;
			pre  = head;
			head = next;
		}
		return pre;
	}
	//打印单向链表
	public static void printLinkedList(Node head) {
		System.out.print("Linked List: ");
		while (head != null) {
			System.out.print(head.value + " ");
			head = head.next;
		}
		System.out.println();
	}
	//打印双向链表
	public static void printDoubleLinkedList(DoubleNode head) {
		System.out.print("Double Linked List: ");
		DoubleNode end = null;
		while (head != null) {
			System.out.print(head.value + " ");
			end = head;
			head = head.next;
		}
		System.out.print("| ");
		while (end != null) {
			System.out.print(end.value + " ");
			end = end.last;
		}
		System.out.println();
	}
	//测试代码
	public static void main(String[] args) {
		Node head1 = new Node(1);
		head1.next = new Node(2);
		head1.next.next = new Node(3);
		printLinkedList(head1);
		head1 = reverseList(head1);
		printLinkedList(head1);

		DoubleNode head2 = new DoubleNode(1);
		head2.next = new DoubleNode(2);
		head2.next.last = head2;
		head2.next.next = new DoubleNode(3);
		head2.next.next.last = head2.next;
		head2.next.next.next = new DoubleNode(4);
		head2.next.next.next.last = head2.next.next;
		printDoubleLinkedList(head2);
		printDoubleLinkedList(reverseDoubleList(head2));

	}
}


```
