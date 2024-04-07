package p3;


import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
public class FriendShipGraph {
    private Person[] personList;
    private int[][] personRelation;
    // 是否是有向图
    private boolean isDirect;
    HashMap<Character, Integer> map;

    public FriendShipGraph(int size, boolean isDirect){
        personList = new Person[size];
        personRelation = new int[size][size];
        // 初始化关系数组
        for(int i=0; i<size; i++){
            Arrays.fill(personRelation[i], Integer.MAX_VALUE);
        }
        this.isDirect = isDirect;

    }

    public void initPersonList(Person[] array) {

        for (int i = 0; i < array.length; i++) {
            Scanner sc = new Scanner(System.in);
            System.out.println("输入名字：");
            String personName = sc.next();
            personList[i] = new Person(personName);
        }
    }

    public void addEdge(){

    }


}
