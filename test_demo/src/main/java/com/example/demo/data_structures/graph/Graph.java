package com.example.demo.data_structures.graph;

import java.util.LinkedList;

public class Graph {
    private int     vertexSize;
    private int[]   vertexs;
    private int[][] matrix;
    private static final int MAX_WEIGHT = 1000;
    private boolean[] isVisited;

    public Graph(int vertextSize) {
        this.vertexSize = vertextSize;
        matrix = new int[vertextSize][vertextSize];
        vertexs = new int[vertextSize];
        for (int i = 0; i < vertextSize; i++) {
            vertexs[i] = i;
        }
        isVisited = new boolean[vertextSize];
    }


    /**
     * ��ȡĳ������ĳ���
     *
     * @return
     */
    public int getOutDegree(int index) {
        int degree = 0;
        for (int j = 0; j < matrix[index].length; j++) {
            int weight = matrix[index][j];
            if (weight != 0 && weight != MAX_WEIGHT) {
                degree++;
            }
        }
        return degree;
    }


    /**
     * ���
     * @return
     */

    /**
     * ��ȡĳ������ĵ�һ���ڽӵ�
     */
    public int getFirstNeighbor(int index) {
        for (int j = 0; j < vertexSize; j++) {
            if (matrix[index][j] > 0 && matrix[index][j] < MAX_WEIGHT) {
                return j;
            }
        }
        return -1;
    }

    /**
     * ����ǰһ���ڽӵ���±���ȡ����һ���ڽӵ�
     *
     * @param v1��ʾҪ�ҵĶ���
     * @param v2           ��ʾ�ö���������ĸ��ڽӵ�ȥ��ȡ��һ���ڽӵ�
     */
    public int getNextNeighbor(int v, int index) {
        for (int j = index + 1; j < vertexSize; j++) {
            if (matrix[v][j] > 0 && matrix[v][j] < MAX_WEIGHT) {
                return j;
            }
        }
        return -1;
    }

    /**
     * ͼ��������ȱ����㷨
     */
    private void depthFirstSearch(int i) {
        isVisited[i] = true;
        int w = getFirstNeighbor(i);//
        while (w != -1) {
            if (!isVisited[w]) {
                //��Ҫ�����ö���
                System.out.println("���ʵ��ˣ�" + w + "����");
                depthFirstSearch(w);
            }
            w = getNextNeighbor(i, w);//��һ�������w���ڽӵ�
        }
    }

    /**
     * ���⹫����������ȱ���
     */

    public void depthFirstSearch() {
        isVisited = new boolean[vertexSize];
        for (int i = 0; i < vertexSize; i++) {
            if (!isVisited[i]) {
                System.out.println("���ʵ��ˣ�" + i + "����");
                depthFirstSearch(i);
            }
        }
        isVisited = new boolean[vertexSize];
    }

    public void broadFirstSearch() {
        isVisited = new boolean[vertexSize];
        for (int i = 0; i < vertexSize; i++) {
            if (!isVisited[i]) {
                broadFirstSearch(i);
            }
        }
    }

    /**
     * ʵ�ֹ�����ȱ���
     *
     * @param i
     */
    private void broadFirstSearch(int i) {
        int u, w;
        LinkedList<Integer> queue = new LinkedList<Integer>();
        System.out.println("���ʵ���" + i + "����");
        isVisited[i] = true;
        queue.add(i);//��һ�ΰ�v0�ӵ�����
        while (!queue.isEmpty()) {
            u = (Integer) (queue.removeFirst()).intValue();
            w = getFirstNeighbor(u);
            while (w != -1) {
                if (!isVisited[w]) {
                    System.out.println("���ʵ��ˣ�" + w + "����");
                    isVisited[w] = true;
                    queue.add(w);
                }
                w = getNextNeighbor(u, w);
            }
        }
    }

    /**
     * 普里姆算法
     */
    public void prim() {
        int[] lowcost = new int[vertexSize];//
        int[] adjvex = new int[vertexSize];//
        int min, minId, sum = 0;
        for (int i = 1; i < vertexSize; i++) {
            lowcost[i] = matrix[0][i];//每次复制出一个一维数组
        }
        for (int i = 1; i < vertexSize; i++) {//循环n次
            min = MAX_WEIGHT;
            minId = 0;
            for (int j = 1; j < vertexSize; j++) {
                if (lowcost[j] < min && lowcost[j] > 0) {
                    min = lowcost[j];//拿到其中最小的那个值,那个权值
                    minId = j;//最小的那个值的下标
                }
            }
            System.out.println("这是啥 ? " + adjvex[minId] + " - 最小值 : " + min);
            sum += min;
            lowcost[minId] = 0;
            for (int j = 1; j < vertexSize; j++) {
                if (lowcost[j] != 0/*排除已经找到的*/ && matrix[minId][j] < lowcost[j]) {
                    lowcost[j] = matrix[minId][j];
                    adjvex[j] = minId;
                }
            }
        }
        System.out.println("所有权值的和为:" + sum);
    }

    /**
     * ͼ�Ĺ�����������㷨
     */

    /**
     * ��ȡ��������֮���Ȩֵ
     *
     * @return
     */
    public int getWeight(int v1, int v2) {
        int weight = matrix[v1][v2];
        return weight == 0 ? 0 : (weight == MAX_WEIGHT ? -1 : weight);
    }


    public int[] getVertexs() {
        return vertexs;
    }

    public void setVertexs(int[] vertexs) {
        this.vertexs = vertexs;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(9);

        int[] a1 = new int[]{0, 10, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 11, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT};
        int[] a2 = new int[]{10, 0, 18, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 16, MAX_WEIGHT, 12};
        int[] a3 = new int[]{MAX_WEIGHT, MAX_WEIGHT, 0, 22, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 8};
        int[] a4 = new int[]{MAX_WEIGHT, MAX_WEIGHT, 22, 0, 20, MAX_WEIGHT, MAX_WEIGHT, 16, 21};
        int[] a5 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 20, 0, 26, MAX_WEIGHT, 7, MAX_WEIGHT};
        int[] a6 = new int[]{11, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 26, 0, 17, MAX_WEIGHT, MAX_WEIGHT};
        int[] a7 = new int[]{MAX_WEIGHT, 16, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 17, 0, 19, MAX_WEIGHT};
        int[] a8 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 16, 7, MAX_WEIGHT, 19, 0, MAX_WEIGHT};
        int[] a9 = new int[]{MAX_WEIGHT, 12, 8, 21, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 0};

        graph.matrix[0] = a1;
        graph.matrix[1] = a2;
        graph.matrix[2] = a3;
        graph.matrix[3] = a4;
        graph.matrix[4] = a5;
        graph.matrix[5] = a6;
        graph.matrix[6] = a7;
        graph.matrix[7] = a8;
        graph.matrix[8] = a9;

//		int degree = graph.getOutDegree(4);
//		System.out.println("vo�ĳ���:"+degree);
//		System.out.println("Ȩֵ��"+graph.getWeight(2,3));
//		graph.depthFirstSearch();
//		graph.broadFirstSearch();
        graph.prim();
    }
}
