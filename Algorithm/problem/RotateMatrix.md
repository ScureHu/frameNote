## 旋转正方形矩阵

### 问题描述

给定一个整形正方形矩阵matrix，请把该矩阵调整成顺时针旋转90度的样子

### 要求

额外空间复杂度为O（1）

### 解题思路

- 取矩阵的左上角，矩阵的右下角，每次最边圈的矩阵进行旋转
- 然后每次两个坐标都缩小，直到两个坐标的横坐标相交

### 代码实现


    public class RotateMatrix {
    	//主思路
    	public static void rotate(int[][] matrix) {
    		int tR = 0;
    		int tC = 0;
    		int dR = matrix.length-1;
    		int dC = matrix[0].length-1;
    		while(tR<dR) {
    			rotateEdge(matrix, tR++, tC++, dR--, dC--);
    		}
    	}
    	//旋转最外圈的矩阵
    	public static void rotateEdge(int[][] m, int tR, int tC, int dR, int dC) {
    		int times = dC-tC;
    		int tmp = 0;
    		for (int i = 0; i !=times; i++) {
    			tmp = m[tR][tC+i];
    			m[tR][tC+i] = m[dR-i][tC];
    			m[dR-i][tC] = m[dR][dC-i];
    			m[dR][dC-i] = m[tR+i][dC];
    			m[tR+i][dC] = tmp;
    		}
    	}
    	//打印方法
    	public static void printMatrix(int[][] matrix) {
    		for (int i = 0; i != matrix.length; i++) {
    			for (int j = 0; j != matrix[0].length; j++) {
    				System.out.print(matrix[i][j] + " ");
    			}
    			System.out.println();
    		}
    	}
    	//测试代码
    	public static void main(String[] args) {
    		int[][] matrix = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 },
    				{ 13, 14, 15, 16 } };
    		printMatrix(matrix);
    		rotate(matrix);
    		System.out.println("=========");
    		printMatrix(matrix);
    
    	}
    }


