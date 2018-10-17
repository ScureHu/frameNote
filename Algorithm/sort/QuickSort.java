/**
 * 快速排序 
 * 思路1:找到最后一个数跟 跟前面的数进行比较，大的放在前面，小的放在后面，最后一个数在中间 (金典快速排序)，之后进行递归调用
 * 思路2:找到最后一个数跟前面的数进行比较,大的放在前面,小的放在后面,中间都是最后一个数(改进1),之后进行递归调用
 * 思路3:随机找到一个数，大的放在前面,小的放在后面,中间都是随机的那个数,之后进行递归调用
 * 
 * 随机快排的期望空间复杂度为O(N*logN)
 * 空间复杂度为O(logN)
 * @author Scure
 *
 */
public class QuickSort {

	public static void quickSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		quickSort(arr, 0, arr.length - 1);
	}

	public static void quickSort(int[] arr, int l, int r) {
		if (l < r) {
			swap(arr, l + (int) (Math.random() * (r - l + 1)), r);
			int[] p = partition(arr, l, r);
			quickSort(arr, l, p[0]-1);
			quickSort(arr,p[p.length-1],r);
		}
	}
	/**
	 * 取r位置的数为中间数,
	 * @param arr
	 * @param l
	 * @param r
	 * @return
	 */
	public static int[] partition(int[] arr,int l,int r) {
		int less = l-1;
		int more = r;
		while(l<more) {
			if(arr[l]<arr[r]) {
				swap(arr, ++less, l++);
			}else if(arr[l]>arr[r]) {
				swap(arr, --more, l);
			}else {
				l++;
			}
		} 
		swap(arr, more, r);
		return new int[] {less+1,more};
	}

	// 交换两个数
	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	public static void main(String[] args) {
		int[] arr = { 5, 3, 8, 2, 4, 6, 9, 1 };
		quickSort(arr);
		for (int i : arr) {
			System.out.print(i + " ");
		}
	}

}
