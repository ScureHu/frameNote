## 小和问题

### 问题描述

> 在一个数组中,每一个数左边比当前数小的数累加起来,叫做这个数组的小和。求一个数组的小和

### 例如

###### 例子 [1,3,4,2,5]
 
 1左边比1小的数，没有
 3左边比3小的数，1
 4左边比4小的数，1,3
 2左边比2小的数，1
 5左边比5小的数，1,3,4,2
 所以小和为1+1+3+1+1+3+4+2=16
 
### 解决思路

之前我们我们写了归并排序的算法,在归并排序上进行细化,可得解

每次归并的时候，我们都会让两边的数字（已经排序好两边）进行比较,那一边的小就会先进辅助数组中，这时候我们就可以判断出进辅助数组的那个数比较小，所以那个数要乘上左边没进的数的个数+右边后面的个数。

### 代码
```java
package cn.zcmu.J20181017;

public class SmallSum {

	public static int smallSum(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		return mergeSort(arr, 0, arr.length - 1);
	}

	public static int mergeSort(int[] arr, int l, int r) {
		if (l == r) {
			return 0;
		}
		int mid = l + ((l - r) >> 1);
		return mergeSort(arr, l, mid) + mergeSort(arr, mid + 1, r) + merge(arr, l, mid, r);
	}

	public static int merge(int[] arr, int l, int mid, int r) {
		int[] help = new int[l - r + 1];
		int p1 = l;
		int p2 = mid + 1;
		int res = 0;
		while (p1 < l && p2 < r) {
			res += arr[p1] < arr[p2] ? (r - p2 + 1) * arr[p1] : 0;
		}
		return 0;
	}

}

```
