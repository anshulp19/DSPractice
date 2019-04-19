import java.util.*;

public class DS_Heap {
    private int capacity = 100;
    private int size = 0;

    private int[] items = new int[capacity];
    private PriorityQueue<Integer> minHeap;
    private int cap = 0;

    private int getLeftChildIndex(int pIndex) { return pIndex * 2 + 1; }
    private int getRightChildIndex(int pIndex) { return pIndex * 2 + 2; }
    private int getParentIndex(int cIndex) { return (cIndex - 2) / 2; }

    private boolean hasLeftChild(int index) { return getLeftChildIndex(index) < size; }
    private boolean hasRightChild(int index) { return getRightChildIndex(index) < size; }
    private boolean hasParent(int index) { return getParentIndex(index) >= 0; }

    private int leftChild(int index) { return items[getLeftChildIndex(index)]; }
    private int rightChild(int index) { return items[getRightChildIndex(index)]; }
    private int parent(int index) { return items[getParentIndex(index)]; }

    private void swap(int indexOne, int indexTwo) {
        int temp = items[indexOne];
        items[indexOne] = items[indexTwo];
        items[indexTwo] = temp;
    }

    private void ensureCapacity() {
        if (size == capacity) {
            items = Arrays.copyOf(items, capacity * 2);
            capacity *= 2;
        }
    }


    public int peek() {
        if (size == 0) throw  new IllegalStateException();

        int temp = items[0];
        items[0] = items[size - 1];

        size--;
        heapifyDown();

        return temp;
    }

    public void add(int item) {
        ensureCapacity();

        items[size] = item;
        size++;
        heapifyUp();
    }

    public void heapifyUp() {
        int index = size - 1;

        while (hasParent(index) && parent(index) > items[index]) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    public void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index) < leftChild(index))
                smallerChildIndex = getRightChildIndex(index);

            if (items[index] < items[smallerChildIndex]) {
                break;
            } else {
                swap(index, smallerChildIndex);
            }
            index = smallerChildIndex;
        }
    }

    public static void addNumber(int number, PriorityQueue<Integer> lowers, PriorityQueue<Integer> highers) {
        if (lowers.size() == 0 || number < lowers.peek())
            lowers.add(number);
        else
            highers.add(number);
    }

    public static void rebalance(PriorityQueue<Integer> lowers, PriorityQueue<Integer> highers) {
        PriorityQueue<Integer> biggerHeap = lowers.size() > highers.size() ? lowers : highers;
        PriorityQueue<Integer> smallerHeap = lowers.size() > highers.size() ? highers : lowers;

        if (biggerHeap.size() - smallerHeap.size() > 1) {
            smallerHeap.add(biggerHeap.poll());
        }
    }

    public static double getMedian(PriorityQueue<Integer> lowers, PriorityQueue<Integer> highers) {
        PriorityQueue<Integer> biggerHeap = lowers.size() > highers.size() ? lowers : highers;
        PriorityQueue<Integer> smallerHeap = lowers.size() > highers.size() ? highers : lowers;

        if (biggerHeap.size() == smallerHeap.size())
            return ((double)biggerHeap.peek() + smallerHeap.peek()) / 2;
        else
            return biggerHeap.peek();
    }

    public static double[] runningMedian(int[] array) {
        PriorityQueue<Integer> lowers = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -1 * o1.compareTo(o2);
            }
        }); // max heap of lowers
        PriorityQueue<Integer> highers = new PriorityQueue<>(); // min heap of highers
        double[] medians = new double[array.length];

        for (int i = 0; i < array.length; i++) {
            addNumber(array[i], lowers, highers);
            rebalance(lowers, highers);
            medians[i] = getMedian(lowers, highers);
        }

        return medians;
    }

    public void kthLargest(int k, int[] nums) {
        minHeap = new PriorityQueue<>();
        cap = k;
        for (int elem: nums) {
            minHeap.offer(elem);
            if (minHeap.size() > cap)
                minHeap.poll();
        }
    }

    public int addToHeap(int num) {
        minHeap.offer(num);
        if (minHeap.size() > cap)
            minHeap.poll();

        return minHeap.peek();
    }

    public int[][] kClosest(int[][] points, int K) {
        int[][] res = new int[K][2];
        PriorityQueue<int[]> dist = new PriorityQueue<>((p1, p2) -> p2[0] * p2[0] + p2[1] * p2[1] -
                p1[0] * p1[0] - p1[1] * p1[1] );

        for (int[] p: points) {
            dist.offer(p);
            if (dist.size() > K)
                dist.poll();
        }

        while (K > 0)
            res[--K] = dist.poll();

        return res;

    }

    public String frequencySort(String s) {
        /*
        * https://leetcode.com/problems/sort-characters-by-frequency/
        * Given a string, sort it in decreasing order based on the frequency of characters.
        *
        * Input: "tree"
        * Output: "eert"
        *
        * */

        Map<Character, Integer> map = new HashMap<>();
        for (char c: s.toCharArray())
            map.put(c, map.getOrDefault(c, 0) + 1);

        PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>((p, q) -> q.getValue() - p.getValue());
        pq.addAll(map.entrySet());

        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            Map.Entry e = pq.poll();
            for (int i = 0; i < (int)e.getValue(); i++)
                sb.append(e.getKey());
        }

        return sb.toString();

    }

    public static void main(String[] args) {
        DS_Heap mHeap = new DS_Heap();

        /*
        mHeap.kthLargest(3, new int[]{4, 5, 8, 2});
        System.out.println(mHeap.addToHeap(3));
        System.out.println(mHeap.addToHeap(5));
        System.out.println(mHeap.addToHeap(10));
        System.out.println(mHeap.addToHeap(9));
        System.out.println(mHeap.addToHeap(4));
        */

       /*
        System.out.println(Arrays.deepToString(mHeap.kClosest(new int[][]{{1, 3}, {-2, 2}}, 1)));
        System.out.println(Arrays.deepToString(mHeap.kClosest(new int[][]{{3, 3}, {-1, 5}, {-2, 4}}, 2)));
       */

        /*
         System.out.println(mHeap.frequencySort("tree"));
         System.out.println(mHeap.frequencySort("cccaaa"));
         System.out.println(mHeap.frequencySort("Aabb"));
        */
    }

}
