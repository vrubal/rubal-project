import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemoveDuplicates {

    /**
     * Remove duplicates from linkedList
     * 1->2->3->4->2->4
     * 1->3
     *
     * @param head
     */
    public Node deleteDuplicates(Node head) {
        Node first = head;
        Map<Integer, Integer> map = new HashMap<>();

        while (first.getNext() == null) {
            if (map.containsKey(first.getData())) {
                map.put(first.getData(), map.get(first.getData()) + 1);
            } else {
                map.put(first.getData(), 1);
            }
            first = first.getNext();
        }
        first = head;
        Node prev = new Node(0, head);
        Node next = null;
        while (first.getNext() == null) {

            if(map.get(first.getData()) > 1){
               next = first.getNext();
               first.setNext(null);

            }
            first=next;
        }
        if(prev.getNext() != null){
            return prev.getNext();
        }
        return null;

    }
}
