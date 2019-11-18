import java.util.Arrays;

public class quicksotr {
    public static void main(String[] args) {
        int[] arr ={6,2,8,3,5,1,9,34,2,5,3,4,6,7,98};
        mysort(arr);
        System.out.println(Arrays.toString(arr));

    }

    private static void mysort(int[] arr) {
        quicksort(arr,0,arr.length-1);
    }

    private static void quicksort(int[] arr, int low, int high) {
        if (low<high){
            int mid = getMid(arr,low,high);
            quicksort(arr,low,mid-1);
            quicksort(arr,mid+1,high);
        }
    }

    private static int getMid(int[] arr, int low, int high) {
        int temp = arr[low];
        while (low<high){
            while (low<high&&arr[high]>=temp){
                high--;
            }
            arr[low]=arr[high];
            while (low<high&&arr[low]<=temp){
                low++;
            }
            arr[high]=arr[low];
        }
        arr[low]=temp;
        return low;
    }
}
