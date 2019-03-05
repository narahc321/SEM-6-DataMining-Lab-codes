/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori;

import java.io.*;
import java.util.*;

/**
 *
 * @author Narahc
 */
public class Apriori {

    /**
     * @param args the command line arguments
     */
    public static void find_confidence(ArrayList <ArrayList <Integer>> frequent_itemset,
            ArrayList <Integer> support_frequent_itemset, float confidence){//,ArrayList <ArrayList <Integer>> confidence_itemset,
//            ArrayList <Float> confidence_values){
        ArrayList <Integer> a;
        int a_support=0;
        for(int i=0;i<frequent_itemset.size();i++){//
            a = frequent_itemset.get(i);
            a_support = support_frequent_itemset.get(i);
            int nn =a.size();
//            for(int j=0;j<nn;j++)
//                System.out.print(a.get(j)+" ");
//            System.out.println("");
            if(nn>1){
                int[] n = new int[nn];
                boolean flag=true;
                while(flag){
                    for(int j=0;j<nn;j++){
                        if(n[j]==0){
                            n[j]=1;
                            break;
                        }
                        else{
                            n[j]=0;
                            if(j==nn-1){
                                flag=false;
                                break;
                            }
                        }
                    }
                    if(!flag)
                        break;
                    ArrayList <Integer> b = new ArrayList<>(),c=new ArrayList<>(),d;
                    int b_support=0,c_support=0;
                    for(int j=0;j<nn;j++){
                        if(n[j]==0)
                            b.add(a.get(j));
                        else
                            c.add(a.get(j));
                    }
                    if(b.size()==0 || c.size()==0)
                        break;
                    for(int j=0;j<frequent_itemset.size();j++){
                        d = frequent_itemset.get(j);
                        if(d.size()>b.size() && d.size()>b.size())
                            break;
                        if(b_support==0 && b.size()==d.size()){
                            int k;
                            for(k=0;k<d.size();k++){
                                if(d.get(k)!=b.get(k))
                                    break;
                            }
                            if(d.size()==k){
                                b_support = support_frequent_itemset.get(j);
                            }
                        }
                        if(c_support==0 && c.size()==d.size()){
                            int k;
                            for(k=0;k<d.size();k++){
                                if(d.get(k)!=c.get(k))
                                    break;
                            }
                            if(d.size()==k){
                                c_support = support_frequent_itemset.get(j);
                            }
                        }
                    }
                    if((float)a_support*100.0 /(float) b_support >= confidence){
                        for(int j=0;j<b.size();j++)
                            System.out.print(b.get(j)+" ");
                        System.out.print("-> ");
                        for(int j=0;j<c.size();j++)
                            System.out.print(c.get(j)+" ");
                        System.out.println(" : " + (float)a_support /(float) b_support);
                    }
//                    for(int j=0;j<c.size();j++)
//                        System.out.print(c.get(j)+"  ");
//                    System.out.print("   ->   ");
//                    for(int j=0;j<b.size();j++)
//                        System.out.print(b.get(j)+"  ");
//                    System.out.println(" : " + a_support +" / "+ c_support);
                }
            }
        }
    }
    public static void generate(int support,ArrayList <ArrayList <Integer>> transactions,ArrayList <ArrayList <Integer>> frequent_itemset,
            ArrayList <Integer> support_frequent_itemset,ArrayList <ArrayList <Integer>> closed_frequent_itemset,int start){
        ArrayList <ArrayList <Integer>> temp = new ArrayList <>();
        int end =frequent_itemset.size();
        for(int i=start;i<frequent_itemset.size();i++){
            for(int j=i+1;j<frequent_itemset.size();j++){
//                System.out.println(i+" "+j);
                ArrayList <Integer> a=frequent_itemset.get(i),b=frequent_itemset.get(j),c=new ArrayList<Integer>();
                boolean flag = true;
//                System.out.println(a.size()+" "+b.size());
//                if(a.size()!=b.size())
//                    System.out.println(a+" "+b);
                for(int k=0;k<a.size()-1;k++){
                    if(a.get(k)!=b.get(k)){
                        flag=false;
                        break;
                    }
                }
                if(flag){
                    for(int k=0;k<a.size();k++){
                        c.add(a.get(k));
//                        System.out.print(a.get(k) + " ");
                    }
                    c.add(b.get(b.size()-1));
//                    System.out.println(b.get(b.size()-1));
                    temp.add(c);
                }
                
//                for(int k=0;k<c.size();k++)
//                    System.out.print(c.get(k)+" ");
//                System.out.println("");
            }
        }
        
        ArrayList <Integer> c=new ArrayList<Integer>();
//        verify each generated case
        for(int i=0;i<temp.size();i++){
//            System.out.println(i);
            boolean flag=true;
            ArrayList <Integer> a = temp.get(i);
//            for(int k=0;k<a.size();k++)
//                    System.out.print(a.get(k)+" ");
//                System.out.println("");
            for(int j=0;j<a.size();j++){
                int x=a.get(j),k;
//                System.out.println(x);
                for(k=start;k<end;k++){
                    ArrayList <Integer> b=frequent_itemset.get(k);
                    boolean tflag=true;
                    int l,m;
                    for(l=0,m=0;l<b.size();l++,m++){
//                        System.out.print(a.get(m));
                        if(a.get(m)==x)
                            m++;
                        if(a.get(m)!=b.get(l)){
                            tflag=false;
                            break;
                        }
                    }
                    if(tflag)
                        break;
                }
                if(k==end){
                    flag=false;
                    break;
                }
            }
            if(flag){
//                for(int k=0;k<a.size();k++)
//                    System.out.print(a.get(k)+" ");
//                System.out.println("");
                int cnt=0;
                for(int j=0;j<transactions.size();j++){
                    int l,m;
                    ArrayList <Integer> b=transactions.get(j);
                    for(l=0,m=0;l<b.size() && m<a.size();l++){
                        if(a.get(m)==b.get(l))
                            m++;
                    }
                    if(m==a.size()){
                        cnt++;
                    }
                }
                if(cnt>=support){
//                        System.out.println("jabdwyjhagdba");
                    frequent_itemset.add(a);
                    support_frequent_itemset.add(cnt);
//                            System.out.println("qwe");
                }
            }
        }
        for(int i=start;i<end;i++){
            ArrayList <Integer> a = frequent_itemset.get(i);
            boolean flag=true;
            for(int j=end;j<frequent_itemset.size();j++){
                if(support_frequent_itemset.get(i)==support_frequent_itemset.get(j)){
                    ArrayList <Integer> b = frequent_itemset.get(j);
                    int l,m;
                    for(l=0,m=0;l<b.size() && m<a.size();l++){
                        if(b.get(l)==a.get(m))
                            m++;
                    }
                    if(m==a.size()){
                        flag=false;
                        break;
                    }
                }
            }
            if(flag)
                closed_frequent_itemset.add(a);
        }
//        for(int i=0;i<frequent_itemset.size();i++){
//            ArrayList <Integer> a = frequent_itemset.get(i);
//            for(int j=0;j<a.size();j++){
//                System.out.print(a.get(j)+" ");
//            }
//            System.out.println("");
//        }
//        System.out.println("size" +frequent_itemset.size()+" end"+end+"        ");
        if(end==frequent_itemset.size()){
            for(int j=end;j<frequent_itemset.size();j++){
                closed_frequent_itemset.add(frequent_itemset.get(j));
            }
            return;
        }
        generate(support,transactions,frequent_itemset,support_frequent_itemset,closed_frequent_itemset,end);
        return;
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("\\Users\\Narahc\\Documents\\NetBeansProjects\\apriori\\data\\chess.dat"));
        String s=" ";
        int t_count=0,support=3037;
        float confidence=0;
        ArrayList <ArrayList <Integer>> transactions= new ArrayList<ArrayList <Integer>>();
        while((s=br.readLine())!=null){
            t_count++;
            String[] splited = s.split("\\s+");
            ArrayList<Integer> arr = new ArrayList<Integer>();
            for(int i=0;i<splited.length;i++)
                arr.add(Integer.parseInt(splited[i]));
            transactions.add(arr);
        }
//        for(int i=0;i<transactions.size();i++){
//            int a[]=transactions.get(i);
//            for(int j=0;j<100;j++){
//                System.out.print(a[j]+" " );
//            }
//            System.out.println("");
//        }
        Map< Integer,Integer> map =  new HashMap< Integer,Integer>();
        for(int i=0;i<transactions.size();i++){
            ArrayList a=transactions.get(i);
            for(int j=0;j<a.size();j++){
//                System.out.print(a.get(j)+" " );
//                    System.out.println(map.containsKey(j));
                if(!map.containsKey(a.get(j)))
                    map.put((Integer)a.get(j), 0);
                int x=map.get(a.get(j))+1;
//                    System.out.println(x+" "+j);
                map.replace((Integer)a.get(j), x);
//                    System.out.print(j+ " ");
            }
//            System.out.println(" ");
//            break;
        }
        ArrayList <ArrayList <Integer>> frequent_itemset =  new ArrayList <ArrayList <Integer>>();
        ArrayList <ArrayList <Integer>> closed_frequent_itemset =  new ArrayList <ArrayList <Integer>>();
        ArrayList <Integer> support_frequent_itemset =  new ArrayList <>();
        for(Map.Entry m:map.entrySet()){  
            if(((int)m.getValue()>=support)){
//                System.out.println(m.getKey()+" "+m.getValue());
//                System.out.println("");
                ArrayList <Integer> lis = new ArrayList<>();
                lis.add((Integer)m.getKey());
                frequent_itemset.add(lis);
                support_frequent_itemset.add((int)m.getValue());
            }
        }
        
        generate(support,transactions,frequent_itemset,support_frequent_itemset,closed_frequent_itemset,0);
        System.out.println("*********************** FRQUENT ITEM SETS ***********************");
        for(int i=0;i<frequent_itemset.size();i++){
            ArrayList <Integer> a = frequent_itemset.get(i);
            for(int j=0;j<a.size();j++){
                System.out.print(a.get(j)+" ");
            }
            System.out.println(" : "+support_frequent_itemset.get(i));
        }
        System.out.println("\n\n\n *********************** CLOSED FRQUENT ITEM SETS ***********************");
        for(int i=0;i<closed_frequent_itemset.size();i++){
            ArrayList <Integer> a = closed_frequent_itemset.get(i);
            for(int j=0;j<a.size();j++){
                System.out.print(a.get(j)+" ");
            }
            System.out.println("");
        }
//        ArrayList <ArrayList <Integer>> confidence_itemset = new ArrayList<>();
//        ArrayList <Float> confidence_values = new ArrayList<Float>();
        System.out.println("\n\n\n *********************** CONFIDENCE ***********************");
        find_confidence(frequent_itemset,support_frequent_itemset,confidence);//,confidence_itemset,confidence_values,confidence);
    }
}