/**
 * 插入排序
 * 时间复杂度为O(N^2)
 * 空间复杂度为O(1)
 * 稳定
 * @author Scure
 *
 */
public class InsertSort {
	public static void insertSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		for (int i = 1; i < arr.length; i++) {
			//如果j>=0 和 arr[j]>arr[j+1] 那么执行下一条语句
			for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
					swap(arr, j, j + 1);
			}
		}
	}

	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public static void main(String[] args) {
		int[] arr = { 6, 7, 6, 9, 3, 10, 8, 2, 7, 8 };
		insertSort(arr);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(arr);
	}
}
