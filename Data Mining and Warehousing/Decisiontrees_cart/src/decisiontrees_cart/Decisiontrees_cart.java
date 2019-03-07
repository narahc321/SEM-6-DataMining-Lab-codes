/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees_cart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Narahc
 */
class sel_col_vals{
    int sel_col=-1;
    ArrayList<String> s1=new ArrayList<>();
    ArrayList<String> s2=new ArrayList<>();
}
class Node{
    String name="None";
    sel_col_vals sel_col;
    int maxi=0,cnt;
    boolean leaf=false;
    Node child1,child2;
    Map<String,Integer> m1 = new HashMap<>();
    Map<String,Integer> m2 = new HashMap<>();
    public void print(ArrayList<String> prin){
        if(this.leaf==true){
            for(int i=0;i<prin.size();i++){
                System.out.print(prin.get(i));
//                i++;
//                System.out.print(" -"+prin.get(i)+"-> ");
            }
            System.out.println(this.name);
        }
        else{
            prin.add(this.name);
            prin.add(" --{ ");
            for(int i=0;i<sel_col.s1.size();i++)
                prin.add(sel_col.s1.get(i)+" ");
            prin.add("}--> ");
            child1.print(prin);
            for(int i=0;i<sel_col.s1.size()+2;i++)
                prin.remove(prin.size()-1);
            prin.add(" --{ ");
            for(int i=0;i<sel_col.s2.size();i++)
                prin.add(sel_col.s2.get(i)+" ");
            prin.add("}--> ");
            child2.print(prin);
            for(int i=0;i<sel_col.s2.size()+2;i++)
                prin.remove(prin.size()-1);
            prin.remove(prin.size()-1);
        }
    }
    public void calculate(String[] heads,ArrayList<String[]> Data,ArrayList<Integer> rows,ArrayList<Integer> columns,ArrayList <Map<String,Integer>> columns_set,boolean split_info){
        Map< String,Integer> m = new HashMap<String,Integer>();
        for(int i=0;i<rows.size();i++){
//            System.out.println(Data.get(rows.get(i))[columns.get(columns.size()-1)]);
            if(m.containsKey(Data.get(rows.get(i))[columns.get(columns.size()-1)])==false){
                m.put(Data.get(rows.get(i))[columns.get(columns.size()-1)],0);
            }
            m.put(Data.get(rows.get(i))[columns.get(columns.size()-1)],m.get(Data.get(rows.get(i))[columns.get(columns.size()-1)])+1);
        }
        Set< Map.Entry< String,Integer> > st = m.entrySet();
        for (Map.Entry< String,Integer> me:st) { 
            cnt++;
            if(me.getValue()>maxi){
                maxi=me.getValue();
                name=me.getKey();
            }
        } 
        if(columns.size()==1 || cnt==1){
            leaf=true;
//            System.out.println(this.name);
            return;
        }
        st = m.entrySet();
        double x,tot_infog=1.0;
        for (Map.Entry< String,Integer> me:st) 
        { 
            x= (double)me.getValue()/(double)rows.size();
            tot_infog-=(double)(x*x);
        } 
//        System.out.println(tot_infog);
        sel_col = select(Data,columns,rows,columns_set,tot_infog,split_info);
        name = heads[sel_col.sel_col];
        child1 = new Node();
        child2 = new Node();
        for(int i=0;i<sel_col.s1.size();i++){
//            if(!m1.containsKey(sel_col.s1.get(i)))
                m1.put(sel_col.s1.get(i), m1.size());
//                System.out.print("hello "+sel_col.s1.get(i)+" "+m1.size()+" ");
        }
//        System.out.println("");
        for(int i=0;i<sel_col.s2.size();i++){
//            if(!m2.containsKey(sel_col.s2.get(i)))
                m2.put(sel_col.s2.get(i), m2.size());
//                System.out.print("hello "+sel_col.s2.get(i)+" "+m2.size()+" ");
        }
//        System.out.println("");
        ArrayList ncolumns = (ArrayList)columns.clone();//new ArrayList<>();
        ArrayList nrows = new ArrayList<>();
        if(m1.size()==1){
            ncolumns = new ArrayList<>();
            for(int i=0;i<columns.size();i++){
//                System.out.print(columns.get(i)+" ");
                if(columns.get(i)!=sel_col.sel_col){
                    ncolumns.add(columns.get(i));
//                    System.out.print(columns.get(i)+" ");
                }
            }
        }
//        System.out.println("");
        for(int i=0;i<rows.size();i++){
            if(m1.containsKey(Data.get(rows.get(i))[sel_col.sel_col])){
                nrows.add(rows.get(i));
//                System.out.print(Data.get(rows.get(i))[0]);
            }
        }
        ArrayList<Map<String,Integer >> ncolumns_set = (ArrayList<Map<String,Integer >>)columns_set.clone();
        ncolumns_set.set(sel_col.sel_col,m1);
        child1.calculate(heads, Data,nrows,ncolumns,ncolumns_set,split_info);
        
        ncolumns = (ArrayList)columns.clone();//new ArrayList<>();
        nrows = new ArrayList<>();
        if(m2.size()==1){
            ncolumns = new ArrayList<>();
            for(int i=0;i<columns.size();i++){
                if(columns.get(i)!=sel_col.sel_col){
                    ncolumns.add(columns.get(i));
//                    System.out.print(columns.get(i)+" ");
                }
            }
        }
//        System.out.println("");
        for(int i=0;i<rows.size();i++){
            if(m2.containsKey(Data.get(rows.get(i))[sel_col.sel_col])){
                nrows.add(rows.get(i));
//                System.out.print(Data.get(rows.get(i))[0]);
            }
        }
        ncolumns_set = (ArrayList<Map<String,Integer >>)columns_set.clone();
        ncolumns_set.set(sel_col.sel_col,m2);
        child2.calculate(heads, Data,nrows,ncolumns,ncolumns_set,split_info);
        return;
    }
//    
    public sel_col_vals select(ArrayList<String[]> Data,ArrayList<Integer> columns,ArrayList<Integer> rows,ArrayList <Map<String,Integer>> columns_set,double tot_infog,boolean split_info){
        int col=-1,col_sub=-1;
        double max_score=-1.00,score;
        for(int i=0;i<columns.size()-1;i++){
            int [][] tot_tab= new int[(columns_set.get(columns.get(i))).size()][columns_set.get(columns.get(columns.size()-1)).size()];
            for(int j=0;j<rows.size();j++){
                tot_tab[columns_set.get(columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)])][columns_set.get(columns.get(columns.size()-1)).get(Data.get(rows.get(j))[columns.get(columns.size()-1)])]++;
            }
            for(int k=1;k<1<<columns_set.get(columns.get(i)).size();k++){
                int [][] tab= new int[2][columns_set.get(columns.get(columns.size()-1)).size()];
                for(int j=0;j<rows.size();j++){
//                    System.out.println(columns_set.get(columns.get(columns.size()-1)).get(Data.get(rows.get(j))[columns.get(columns.size()-1)]));
//                    System.out.println(1<<columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)])));
                    if((1<<columns_set.get(columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)]) & k) >0){
                        tab[0][columns_set.get(columns.get(columns.size()-1)).get(Data.get(rows.get(j))[columns.get(columns.size()-1)])]++;  
                    }
                    else{
                        tab[1][columns_set.get(columns.get(columns.size()-1)).get(Data.get(rows.get(j))[columns.get(columns.size()-1)])]++;  
                    }       
                }
                score = cal_score(tab,2, columns_set.get(columns.get(columns.size()-1)).size(),tot_infog,split_info);
                if(score>max_score){
                    max_score = score;
                    col=columns.get(i);
                    col_sub=k;
                }
            }
        }
        sel_col_vals col_obj = new sel_col_vals();
        col_obj.sel_col=col;
        Map<String,Integer> m= columns_set.get(col);
        Iterator iterator = m.entrySet().iterator(); 
        while (iterator.hasNext()) { 
            Map.Entry mapele = (Map.Entry)iterator.next();
//            System.out.println((int)mapele.getValue()+" "+(String)mapele.getKey()+" "+(1<<(int)mapele.getValue() & col_sub));
            if((1<<(int)mapele.getValue() & col_sub)>0)
                col_obj.s1.add((String)mapele.getKey());
            else
                col_obj.s2.add((String)mapele.getKey());
        } 
        return col_obj;
    }
    public double cal_score(int[][] tab,int rows,int columns,double tot_infog,boolean split_info){
        int[] sumr= new int[rows];
        int sum_all=0;
        double x,entro=0.0,tot_entro=0.0;
        for(int i=0;i<rows;i++){
            sumr[i]=0;
            for(int j=0;j<columns;j++)
                sumr[i]+=tab[i][j];
            sum_all+=sumr[i];
        }
        for(int i=0;i<rows;i++){
            entro=1.0;
            for(int j=0;j<columns;j++){
                if(tab[i][j]!=0){
                    x = (double)tab[i][j]/(double)sumr[i];
                    entro-=(x*x);
                }
            }
            tot_entro+=((double)sumr[i]/(double)sum_all)*(entro);
        }
//        System.out.println(tot_infog-tot_entro);
        return (tot_infog-tot_entro);
    }
    public String predict(String[] predi){
        if(this.leaf==true)
            return this.name;
//        System.out.println(m1);
//        System.out.println(m2);
//        System.out.println(predi[sel_col.sel_col]);
        if(m1.containsKey(predi[sel_col.sel_col]))
            return child1.predict(predi);
        else if(m2.containsKey(predi[sel_col.sel_col]))
            return child2.predict(predi);
        return "Unable to predict";
    }
}
public class Decisiontrees_cart {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Narahc\\Documents\\NetBeansProjects\\decisiontrees_id3_cart\\cardata.txt"));
        String s=" ";
        boolean split_info = true;
        ArrayList <Map<String,Integer>> columns_set = new ArrayList<>();
        Map<String,Integer> temp_m;
        s=br.readLine();
        String[] heads = s.split("\\s+");
        ArrayList<Integer> columns=new ArrayList<Integer>();
        ArrayList<Integer> rows=new ArrayList<Integer>();
        int i;
        for(i=0;i<heads.length;i++){
            columns.add(i);
            columns_set.add(new HashMap());
        }
        i=0;
        ArrayList<String[]> Data = new ArrayList<>();
        while((s=br.readLine())!=null){
            String[] splited = s.split("\\s+"); 
            if(splited.length==heads.length){ 
                for(int j=0;j<splited.length;j++){
                    temp_m = columns_set.get(j);
                    if(!temp_m.containsKey(splited[j])){
                        temp_m.put(splited[j],temp_m.size());
                    }
                    columns_set.set(j,temp_m);
                }
                Data.add(splited);
                rows.add(i);
                i++;
            }
        }
//        for(i=0;i<columns_set.size();i++){
//            Map<String,Integer> mm = columns_set.get(i);
//            Set< Map.Entry< String,Integer> > st = mm.entrySet();
//            for (Map.Entry< String,Integer> me:st) { 
//                System.out.print(me.getKey()+":"); 
//                System.out.println(me.getValue());
//            } 
//        }
//        for(int i=0;i<heads.length;i++)
//            System.out.print(heads[i]+"\t");
//        System.out.println("");
//        for(int i=0;i<Data.size();i++){
//            for(int j=0;j<Data.get(i).length;j++)
//                System.out.print(Data.get(i)[1]+"\t\t");
//            System.out.println("");
//        }
        Node obj = new Node();
        obj.calculate(heads, Data,rows,columns,columns_set,split_info);
        ArrayList <String> prin=new ArrayList<>();
        obj.print(prin);
        String [] predi;
        int cor=0;
        for(i=0;i<Data.size();i++){
            predi=Data.get(i);
//            System.out.println(obj.predict(predi)+" "+predi[predi.length-1]);
            if(obj.predict(predi).equals(predi[predi.length-1]))
                cor++;
        }
        System.out.println(cor+ " / "+ Data.size());
    }

}
