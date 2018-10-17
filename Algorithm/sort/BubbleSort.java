public class BubbleSort {
	//时间复杂度为O(N^2) 空间复杂度为O（1）稳定：稳定
	public static void bubbleSort(int[] arr) {
		if(arr ==null || arr.length<2) {
			return ;
		}
		for(int i = arr.length-1;i>=0;i--) {
			for(int j = 0;j<i;j++) {
				if(arr[j]>arr[i]) {
					swap(arr, i, j);
				}
			}
		}
	}
	
	public static void swap(int[] arr,int i,int j) {
		int temp = arr[i];
		arr[i]=arr[j];
		arr[j]=temp;
	}
	
	public static void main(String[] args) {
    //测试用例
		int[] arr = { 6,7,6, 9, 3, 10, 8, 2, 7, 8 };
		bubbleSort(arr);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(arr);
	}
}
