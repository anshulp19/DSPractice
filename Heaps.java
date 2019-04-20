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

    public List<Integer> topKFrequent(int[] nums, int k) {
        /*
        * https://leetcode.com/problems/top-k-frequent-elements/
        * Given a non-empty array of integers, return the k most frequent elements.
        *
        * Input: nums = [1,1,1,2,2,3], k = 2
        * Output: [1,2]
        * */
        Map<Integer, Integer> map = new HashMap<>();
        for (int elem: nums)
            map.put(elem, map.getOrDefault(elem, 1) + 1);

        PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<Integer, Integer> e: map.entrySet()) {
            pq.add(e);
            if (pq.size() > k)
                pq.poll();
        }

        List<Integer> l = new ArrayList<>();
        while(pq.size() > 0)
            l.add(pq.poll().getKey());
        Collections.reverse(l);

        return l;
    }

    public int findKthLargest(int[] nums, int k) {
        /*
        * https://leetcode.com/problems/kth-largest-element-in-an-array/
        * Find the kth largest element in an unsorted array.
        * Note that it is the kth largest element in the sorted order, not the kth distinct element.
        *
        * Input: [3,2,1,5,6,4] and k = 2
        * Output: 5
        * */

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int elem : nums) {
            pq.offer(elem);
            if (pq.size() > k)
                pq.poll();
        }

        return pq.peek();

    }

    public List<String> topKFrequent(String[] words, int k) {
        /*
        * https://leetcode.com/problems/top-k-frequent-words
        * Given a non-empty list of words, return the k most frequent elements.
        * Your answer should be sorted by frequency from highest to lowest.
        * If two words have the same frequency, then the word with the lower alphabetical order comes first.
        *
        * Input: ["i", "love", "leetcode", "i", "love", "coding"], k = 2
        * Output: ["i", "love"]
        *
        * */
        Map<String, Integer> map = new HashMap<>();
        for (String elem: words)
            map.put(elem, map.getOrDefault(elem, 0) + 1);

        PriorityQueue<String> pq = new PriorityQueue<>((w1, w2) -> map.get(w1).equals(map.get(w2))
            ? w2.compareTo(w1) : map.get(w1) - map.get(w2));

        for (String word: map.keySet()) {
            pq.offer(word);
            if (pq.size() > k)
                pq.poll();
        }

        List<String> ans = new ArrayList<>();
        while (!pq.isEmpty())
            ans.add(pq.poll());
        Collections.reverse(ans);

        return ans;

    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        /*
        * https://leetcode.com/problems/sliding-window-maximum/
        * Given an array nums, there is a sliding window of size k which is moving from
        * the very left of the array to the very right.
        * You can only see the k numbers in the window. Each time the
        * sliding window moves right by one position. Return the max sliding window.
        *
        * Input: nums = [1,3,-1,-3,5,3,6,7], and k = 3
        * Output: [3,3,5,5,6,7]
        *
        * */

        if (nums.length == 0 || nums.length < k)
            return new int[0];

        Deque<Integer> dq = new LinkedList<>();
        List<Integer> l = new ArrayList<>();

        int i;
        for (i = 0; i < k; ++i) {
            while(!dq.isEmpty() && nums[i] >= nums[dq.peekLast()])
                dq.removeLast();
            dq.addLast(i);
        }

        for (; i < nums.length; i++ ) {
            l.add(nums[dq.peek()]);

            while(!dq.isEmpty() && dq.peek() <= i - k)
                dq.removeFirst();

            while(!dq.isEmpty() && nums[i] >= nums[dq.peekLast()])
                dq.removeLast();
            dq.addLast(i);
        }
        l.add(nums[dq.peek()]);

        int[] res = new int[l.size()];
        for (int j = 0; j < l.size(); j++)
            res[j] = l.get(j).intValue();

        return res;
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

        /*
         System.out.println(mHeap.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));
         System.out.println(mHeap.topKFrequent(new int[]{1}, 1));
        */

        /*
         System.out.println(mHeap.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
         System.out.println(mHeap.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
        */

        /*
         System.out.println(mHeap.topKFrequent(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2));
         System.out.println(mHeap.topKFrequent(new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"}, 4));
        */

        /*
         System.out.println(Arrays.toString(mHeap.maxSlidingWindow(new int[]{12, 1, 78, 90, 57, 89, 56}, 3)));
         System.out.println(Arrays.toString(mHeap.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
        */

    }

}
