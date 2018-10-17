/**
 * 堆排序 
 * 1.堆是一个完全二叉树的结构，可以使用数组的形式去存
 * 2.如果父节点是i 那么子节点就是左边(2i+1),右边就是(2i+2)
 * 3.如果子节点是i 那么父节点就是(i-2)/2 
 * 4.大根堆,根节点最大 
 * 5.小根堆,根节点最小
 * 时间复杂度为 O(N*logN)
 * 空间复杂度为O(1)
 * 不稳定
 * @author Scure
 *
 */
public class HeapSort {

	public static void heapSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			heapInsert(arr, i);
		}
		int size = arr.length;
		swap(arr, 0, --size);
		while (size > 0) {
			heapify(arr, 0, size);
			swap(arr, 0, --size);
		}
	}

	/**
	 * 根节点数据发生变化,重新调整二叉树
	 * 
	 * @param arr
	 * @param index
	 *            改变的二叉树的数组下标
	 * @param size
	 *            现在二叉树的大小
	 */
	public static void heapify(int[] arr, int index, int size) {
		int left = index * 2 + 1;
		while (left < size) {
			int largest = left + 1 < size && arr[left + 1] > arr[left] ? left + 1 : left;
			largest = arr[largest] > arr[index] ? largest : index;
			if (largest == index) {
				break;
			}
			swap(arr, largest, index);
			index = largest;
			left = index * 2 + 1;
		}

	}

	/**
	 * 插入节点 放在节点的末尾,然后再跟父节点比较,如果大于父节点 就调整位置
	 * 
	 * @param arr
	 *            数组
	 * @param i
	 *            插入节点的位置
	 */
	public static void heapInsert(int[] arr, int index) {
		while (arr[index] > arr[(index - 1) / 2]) {
			swap(arr, index, (index - 1) / 2);
			index = (index - 1) / 2;
		}
	}

	/*
	 * 交换位置
	 */
	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	 public static void main(String[] args) {
			int[] arr = { 5, 3, 8, 2, 4, 6, 9, 1 };
			heapSort(arr);
			for (int i : arr) {
				System.out.print(i + " ");
			}
	}
}
