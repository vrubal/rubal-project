import java.util.ArrayList;
import java.util.List;

public class SmallerTripplet {



    public static List<List<Integer>> findCombination(int [] arr, int count, int sum){
        List<List<Integer>> finalList = new ArrayList<>();
        List<Integer> t = new ArrayList<>();
        getAllTripplets(finalList, t, arr, count, sum, 0);
        return finalList;
    }

    public static void getAllTripplets(List<List<Integer>> l, List<Integer> t, int[] arr, int count, int sum, int idx) {

        if(count == 0 && sum > 0){
            l.add(new ArrayList<>(t));
            return;
        }
        for(int i=idx;i<arr.length;i++){
            t.add(arr[i]);
            getAllTripplets(l, t, arr, count-1, sum-arr[i], idx+1);
            t.remove(t.size()-1);
        }

    }

    public static void main(String[] args) {
        int[] a = new int[]{5,1,3,4,7};
        System.out.println(findCombination(a, 3, 12));
    }
}
