## "之"字形打印矩阵

### 问题描述

给定一个矩阵matrix,按照"之"字型的方式打印这个矩阵,
  例如:
  - 1  2  3  4 
  - 5  6  7  8
  - 9  10 11 12
  打印结果为1,2,5,9,6,3,4,7,10,11,8,12

### 要求

额外空间复杂度O(1)

### 解题思路

- 根据题目要求,我们可以斜着打印所有的点，加一个flag来判断打印的次序，是从右上打印到左下，还是从左下打印到右上

### 代码实现

```java
public class ZigZagPrintMatrix {
	//主思路
	public static void printMatrixZigZag(int[][] matrix) {
		int tR = 0;
		int tC = 0;
		int dR = 0;
		int dC = 0;
		int endR = matrix.length - 1;
		int endC = matrix[0].length - 1;
		boolean fromUp = false;
		//直到（tR,tC）移动矩阵的最右下角的点
		while (tR != endR + 1) {
			printLevel(matrix, tR, tC, dR, dC, fromUp);
			//(tR,tC)坐标一直沿着tC往左边走，走到底往下走
			//(dC,dR)坐标一直沿着tR往下走，走到底往右走
			tR = tC == endC ? tR + 1 : tR;
			tC = tC == endC ? tC : tC + 1;
			dC = dR == endR ? dC + 1 : dC;
			dR = dR == endR ? dR : dR + 1;
			fromUp = !fromUp;
		}
	}
	/**
	 * 打印点
	 * @param m 矩阵
	 * @param tR 
	 * @param tC 
	 * @param dR 
	 * @param dC 
	 * @param f 打印顺序
	 */
	public static void printLevel(int[][] m, int tR, int tC, int dR, int dC, boolean f) {
		// 打印的顺序
		if (f) {
			// 移动的点能斜着移动到左下角
			while (tR != dR + 1) {
				System.out.print(m[tR++][tC--] + " ");
			}
		} else {
			// 移动的点能斜着移动右上角
			while (dR != tR - 1) {
				System.out.print(m[dR--][dC++] + " ");
			}
		}
	}
	//测试代码
	public static void main(String[] args) {
		int[][] matrix = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 } };
		printMatrixZigZag(matrix);

	}
}


```
