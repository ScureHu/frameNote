/**
 * 选择排序 
 * 从第二元素找到最后一个元素,寻找最大的元素 
 * 时间复杂度为O(N^2) 
 * 空间复杂度为O(1)
 * 不稳定
 * @author Scure
 *
 */
public class SelectionSort {

	public static void selectionSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		for (int i = 0; i < arr.length - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				minIndex = arr[j] < arr[minIndex] ? j : minIndex;
			}
			swap(arr, minIndex, i);
		}
	}

	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public static void main(String[] args) {
		int[] arr = { 6, 7, 6, 9, 3, 10, 8, 2, 7, 8 };
		selectionSort(arr);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(arr);
	}
}
