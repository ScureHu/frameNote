# 打印两个有序链表的公共部分

## 题目：
 给定两个有序链表的头指针head1和head2,打印两个链表的公共部分


## 解题思路

 - 因为是有序链表,所以两个链表可以比较往下循环
 - 当1链表的值小于2链表的值，1链表的头结点往下移动
 - 同理，当2链表的值小于1链表的值，2链表的头结点往下移动
 - 当相等的时候打印相同的部分
 
### 代码

```java

public class PrintCommonPart {
	//节点类
	public static class Node {
		public int value;
		public Node next;
		public Node(int data) {
			this.value = data;
		}
	}
	//打印公共部分
	public static void printCommonPart(Node head1,Node head2) {
		System.out.println("Common Part: ");
		while(head1 !=null && head2 !=null) {
			if(head1.value<head2.value) {
				head1 = head1.next;
			}else if(head1.value>head2.value) {
				head2 = head2.next;
			}else {
				System.out.println(head1.value);
				head1 = head1.next;
				head2 = head2.next;
			}
		}
		System.out.println();
	}
	//打印列表
	public static void printLinkedList(Node node) {
		System.out.print("Linked List: ");
		while (node != null) {
			System.out.print(node.value + " ");
			node = node.next;
		}
		System.out.println();
	}
	
	//测试数据

	public static void main(String[] args) {
		Node node1 = new Node(2);
		node1.next = new Node(3);
		node1.next.next = new Node(5);
		node1.next.next.next = new Node(6);

		Node node2 = new Node(1);
		node2.next = new Node(2);
		node2.next.next = new Node(5);
		node2.next.next.next = new Node(7);
		node2.next.next.next.next = new Node(8);

		printLinkedList(node1);
		printLinkedList(node2);
		printCommonPart(node1, node2);

	}
}

```
