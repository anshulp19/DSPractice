
// migght contain practice problems of strings and DP

public class DS_Arrays {

    private static void printArray(int[] arr) {
        for(int ele: arr)
            System.out.print(ele + " ");
        System.out.println();
    }

    private void reverse(int[] arr, int begin, int end) {
        while (begin < end) {
            int temp = arr[begin];
            arr[begin] = arr[end];
            arr[end] = temp;
            begin++;
            end--;
        }
    }

    public void rotate(int[] arr, int k) {
        if (k < 0)
            return;
        if (k > arr.length)
            rotate(arr, k % arr.length);
        else {
            reverse(arr, 0, k - 1);
            reverse(arr, k, arr.length - 1);
            reverse(arr, 0, arr.length - 1);
        }
    }

    public static void main(String[] args) {
        DS_Arrays ds = new DS_Arrays();
        int[] arr1 = {1, 2, 3, 4, 5, 6};

        ds.rotate(arr1, 3);
        printArray(arr1);

    }
}
