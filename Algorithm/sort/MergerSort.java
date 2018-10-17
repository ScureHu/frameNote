/**
 * 归并排序
 * 时间复杂度为O(N*logN)
 * 空间复杂度为O(N)
 * 思路 1.把数组拆成两部分,然后两部分内自己排序,排序完成后 在根据两边的数组不相同 进行排序
 * @author Scure 
 */
public class MergerSort {
	/**
	 * 假设排序的数组为[5,3,8,2,4,6,9,1] 第一次取中间值为 mid=0+3=3 数组下标为3 实际为第四个数 mid=3,l=0,r=7;
	 * 1.进入递归函数 l=0,r=3 mid=1+0=2 l=0 2.再次进入递归 l=0,r=2 mid=0 l=0 进入第二递归mid+1=1,r=2
	 * ------>l=1,r=2--->mid=1,l=1,r=2---> 3.进入递归 被弹出 ↑↑↑↑↑↑↑↑
	 * 
	 * @param arr
	 * @param L
	 * @param R
	 */
	public static void mergeSort(int[] arr, int L, int R) {
		// 1.如果两边都相等了 肯定是达到最后一个数,直接返回
		if (R == L) {
			return;
		} 
		// 2.取中间的数字 ,采用不会溢出的方法
		int mid = L + ((R - L) >> 1);
		// 3.递归调用取得左边的中间值
		mergeSort(arr, L, mid);
		// 4.递归调用取得右边的中间值
		mergeSort(arr, mid + 1, R);
		// 5.排序,拷贝到辅助空间中
		merge(arr, L, mid, R);
	}

	public static void merge(int[] arr, int L, int m, int R) {
		int[] help = new int[R - L + 1];
		int i = 0;
		int p1 = L;
		int p2 = m + 1;
		while (p1 <= m && p2 <= R) {
			help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= R) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
	}

	public static void margeSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		mergeSort(arr, 0, arr.length - 1);
	}

	public static void main(String[] args) {
		int[] arr = { 6,7,6, 9, 3, 10, 8, 2, 7, 8 };
		margeSort(arr);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(arr);
		
	}
}
