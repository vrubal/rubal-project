package com.rubal.ds.tree.traversal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 *              1
 *            /     \
 *         2          3
 *      /  \        /   \
 *     4   5        6   7
 *     /    \      /     \
 *    8     9     10      11
 */
public class AggregateCostTree {


    public static void printTrie(TrieNode node){
        if(node==null)
            return;
        for (TrieNode e : node.children) {
            printTrie(e);
        }
        System.out.println("data:"+node.data+" cost:"+node.cost);

    }
    public static void buildCost(TrieNode node){
        if(node==null)
            return;
        for (TrieNode e : node.children)
            buildCost(e);
        node.cost=sum(node);
    }

    private static int sum(TrieNode node) {
        return node.data + node.children.stream().mapToInt(e->e.cost).sum();
    }

    public static void main(String[] args) {
        TrieNode one = new TrieNode(1, new ArrayList<>());
        TrieNode two = new TrieNode(2, new ArrayList<>());
        TrieNode three = new TrieNode(3, new ArrayList<>());

        TrieNode four = new TrieNode(4, new ArrayList<>());
        TrieNode five = new TrieNode(5, new ArrayList<>());
        TrieNode six = new TrieNode(6, new ArrayList<>());
        TrieNode seven = new TrieNode(7, new ArrayList<>());

        TrieNode eight = new TrieNode(8, new ArrayList<>());
        TrieNode nine = new TrieNode(9, new ArrayList<>());
        TrieNode ten = new TrieNode(10, new ArrayList<>());
        TrieNode eleven = new TrieNode(11, new ArrayList<>());

        one.getChildren().add(two);
        one.getChildren().add(three);

        two.getChildren().add(four);
        two.getChildren().add(five);

        three.getChildren().add(six);
        three.getChildren().add(seven);

        four.getChildren().add(eight);
        five.getChildren().add(nine);
        six.getChildren().add(ten);
        seven.getChildren().add(eleven);

        //printTrie(one);
        buildCost(one);
        printTrie(one);



    }


}
