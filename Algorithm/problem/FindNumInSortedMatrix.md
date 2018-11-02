# 在行列都排好序的矩阵中找数

## 题目：
  给定一个N*M的整形矩阵matrix和一个整数K,matrix的每一行和每一列都是排好序的
 例如: 
 - 0,1,2,5
 - 2,3,4,7
 - 4,4,4,8
 - 5,7,7,9
  如果K为7，返回true
  如果K为6，返回false

## 要求：时间复杂度为O(M+N),额外空间复杂度为O(1)

## 解题思路

 - 因为行列都是排好序的,所以我们取右上角的点为起始点
 - 当进来的数与左上角的点进行比较，如果大于左上角的点，就往下移
 - 如果小于左上角的点，就往左移
 - 直到移动到左下角停止,没有找到就返回false,找到就返回true
 
### 代码

```java

public class FindNumInSortedMatrix {
	
	public static boolean isContains(int[][] matrix,int k) {
		int row = 0;
		int col = matrix[0].length-1;
		while(row<matrix.length && col>-1) {
			if(matrix[row][col]==k) {
				return true;
			//如果坐标点大于k,则往左移动
			}else if(matrix[row][col]>k) {
				col--;
			}else {
			//如果坐标点小于k,则往下移动
				row++;
			}
		}
		return false;
	}
	//测试代码
	public static void main(String[] args) {
		int[][] matrix = new int[][] { { 0, 1, 2, 3, 4, 5, 6 },// 0
				{ 10, 12, 13, 15, 16, 17, 18 },// 1
				{ 23, 24, 25, 26, 27, 28, 29 },// 2
				{ 44, 45, 46, 47, 48, 49, 50 },// 3
				{ 65, 66, 67, 68, 69, 70, 71 },// 4
				{ 96, 97, 98, 99, 100, 111, 122 },// 5
				{ 166, 176, 186, 187, 190, 195, 200 },// 6
				{ 233, 243, 321, 341, 356, 370, 380 } // 7
		};
		int K = 233;
		System.out.println(isContains(matrix, K));
	}
}

```
