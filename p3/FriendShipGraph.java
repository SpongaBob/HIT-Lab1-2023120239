package p3;


import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.util.*;

public class FriendShipGraph {
    private ArrayList<Person> personList;
    private int[][] personRelation;
    // 是否是有向图
    private boolean isDirect;
    HashMap<Character, Integer> map;

    public FriendShipGraph(int size, boolean isDirect){
        personList = new ArrayList<Person>();
        personRelation = new int[size][size];
        // 初始化关系数组
        for(int i=0; i<size; i++){
            Scanner sc = new Scanner(System.in);
            System.out.println("输入名字：");
            Person p = new Person(sc.next());
            personList.add(p);
            Arrays.fill(personRelation[i], Integer.MAX_VALUE);
        }
        this.isDirect = isDirect;

    }


    public void addEdge(Person p0, Person p1, int weight){
        int n0Index = getIndex(p0.name);
        int n1Index = getIndex(p1.name);
        personRelation[n0Index][n1Index] = weight;
        if(!isDirect){
            personRelation[n1Index][n0Index] = weight;
        }

    }

    public void addVertex(Person person){
        personList.add(person);
        int[][] temp = new int[personRelation.length+1][personRelation.length+1];
        for(int i=0; i <personRelation.length; i++){
            for(int j=0; j <personRelation.length; j++){
                temp[i][j] = personRelation[i][j];
            }
        }
        for(int i=0; i<personRelation.length+1; i++){
            temp[i][personRelation.length] = Integer.MAX_VALUE;
            temp[personRelation.length][i] = Integer.MAX_VALUE;
        }
        personRelation = temp;
    }

    public int getIndex(String name){
        for(int i=0; i<personList.size(); i++){
            if(personList.get(i).name.equals(name)){
                return i;
            }
        }
        System.out.println("查无此人");
        return -1;
    }

    public int getDevOfV(String name) {
        int count = 0;
        int nameIndex = getIndex(name);
        for (int i = 0; i < personRelation.length; i++) {
            if (personRelation[nameIndex][i] != Integer.MIN_VALUE) {
                count++;
            }
        }
        //计算有向图的出度
        if (isDirect) {
            for (int i = 0; i < personRelation[0].length; i++) {
                if (personRelation[i][nameIndex] != Integer.MIN_VALUE) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getDistance(Person p0, Person p1){
        int p0Index = getIndex(p0.name);
        int p1Index = getIndex(p1.name);
        if (personRelation[p0Index][p1Index] == 1 ){
            return 1;
        }
        else if(p0Index == p1Index){
            return 0;
        }
        for(int i= 0; i<personList.size(); i++){
            if(personRelation[p0Index][i] == 1 && personRelation[i][p1Index] == 1){
                return 2;
            }
        }
        return -1;
    }


    public void printGraph() {
        for (int i = 0; i < personRelation.length; i++) {
            for (int j = 0; j < personRelation[0].length; j++) {
                if (personRelation[i][j] != Integer.MIN_VALUE) {
                    System.out.print(personRelation[i][j] + " ");
                } else {
                    System.out.print("∞ ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        FriendShipGraph graph = new FriendShipGraph(0, true);
        Person rache1 = new Person("rache1");
        Person ross = new Person("ross");
        Person ben = new Person("ben");
        Person kramer = new Person("kramer");
        graph.addVertex(rache1);
        graph.addVertex(ross);
        graph.addVertex(kramer);
        graph.addVertex(ben);
        graph.addEdge(rache1, ross, 1);
        graph.addEdge(ross, rache1, 1);
        graph.addEdge(ross, ben, 1);
        graph.addEdge(ben, ross, 1);
        System.out.println(graph.getDistance(rache1, ross));
        System.out.println(graph.getDistance(rache1, ben));
        System.out.println(graph.getDistance(rache1, rache1));
        System.out.println(graph.getDistance(rache1, kramer));
        graph.printGraph();

    }

}
