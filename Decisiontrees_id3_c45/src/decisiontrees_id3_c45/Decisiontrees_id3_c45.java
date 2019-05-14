/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees_id3_c45;

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
class Node{
    String name="None";
    int sel_col=-1;
    int maxi=0,cnt;
    boolean leaf=false;
    ArrayList<String> paths = new ArrayList<>();
    ArrayList<Node> child = new ArrayList<>();
    public void print(ArrayList<String> prin){
        if(this.leaf==true){
            for(int i=0;i<prin.size();i++){
                System.out.print(prin.get(i));
                i++;
                System.out.print(" --{ "+prin.get(i)+" }--> ");
            }
            System.out.println(this.name);
        }
        else{
            prin.add(this.name);
            for(int i=0;i<child.size();i++){
                prin.add(paths.get(i));
                child.get(i).print(prin);
                prin.remove(prin.size()-1);
            }
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
        double x,tot_infog=0;;
        for (Map.Entry< String,Integer> me:st) 
        { 
            x= (double)me.getValue()/(double)rows.size();
            tot_infog+=(-x)*(double)Math.log((double)x)/(double)Math.log((double)2);
//            System.out.println((-x)*(double)Math.log((double)x)/(double)Math.log((double)2));
        } 
//        System.out.println("tot infog : "+tot_infog);
        sel_col = select(Data,columns,rows,columns_set,tot_infog,split_info);
        name = heads[sel_col];
//        System.out.println(name);
        Map<String, Integer> link=columns_set.get(sel_col);
        Iterator iterator = link.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)iterator.next();
//            System.out.println(mapElement.getKey());
            paths.add((String)mapElement.getKey());
//            System.out.println((String)mapElement.getKey());
            Node nnode = new Node();
            ArrayList ncolumns = new ArrayList<>();
            ArrayList nrows = new ArrayList<>();
            for(int i=0;i<columns.size();i++){
                if(columns.get(i)!=sel_col)
                    ncolumns.add(columns.get(i));
            }
            for(int i=0;i<rows.size();i++){
//                System.out.println(Data.get(rows.get(i))[sel_col]);
                if((Data.get(rows.get(i))[sel_col]).equals((String)mapElement.getKey())){
                    nrows.add(rows.get(i));
                }
            }
            nnode.calculate(heads, Data,nrows,ncolumns,columns_set,split_info);
            child.add(nnode);
//            break;
        }
//        System.out.println(this.name);
//        for(int a=0;a<rows.size();a++){
//            for(int y=0;y<columns.size();y++){
//                System.out.print(Data.get(rows.get(a))[columns.get(y)]+ " ");
//            }
//            System.out.println("");
//        }
//        System.out.println("");
        return;
    }
    public int select(ArrayList<String[]> Data,ArrayList<Integer> columns,ArrayList<Integer> rows,ArrayList <Map<String,Integer>> columns_set,double tot_infog,boolean split_info){
        int col=-1;
        double max_score=-1.00,score;
        for(int i=0;i<columns.size()-1;i++){
//            System.out.println(columns.get(i));
//            System.out.println(columns_set.get(columns.get(i)).size());
//            int [] tab= new int[columns_set.get(columns.get(i)).size()];
            int [][] tab= new int[(columns_set.get(columns.get(i))).size()][columns_set.get(columns.get(columns.size()-1)).size()];
            for(int j=0;j<rows.size();j++){
//                System.out.println();
                tab[columns_set.get(columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)])][columns_set.get(columns.get(columns.size()-1)).get(Data.get(rows.get(j))[columns.get(columns.size()-1)])]++;
//                System.out.println(columns_set.get(columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)]));
//                System.out.println(columns_set.get(columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)]));
//                System.out.println(columns_set.get(columns.get(i)).get(Data.get(rows.get(j))[columns.get(i)]));
            }
            
//            for(int x=0;x<(columns_set.get(columns.get(i))).size();x++){
//                for(int y=0;y<columns_set.get(columns.get(columns.size()-1)).size();y++){
//                    System.out.print(tab[x][y]+" ");
//                }
//                System.out.println("");
//            }
            score = cal_score(tab,(columns_set.get(columns.get(i))).size(), columns_set.get(columns.get(columns.size()-1)).size(),tot_infog,split_info);
//            System.out.println(columns.get(i)+" "+score);
            if(score>max_score){
                max_score = score;
                col=columns.get(i);
            }
//            break;
        }
//        System.out.println(max_score+" "+col);
//        System.out.println(max_score);
        return col;
    }
    public double cal_score(int[][] tab,int rows,int columns,double tot_infog,boolean split_info){
        int[] sumr= new int[rows];
        int sum_all=0;
        double x,entro=0.0,tot_entro=0.0,split_in=1.0;
        for(int i=0;i<rows;i++){
            sumr[i]=0;
            for(int j=0;j<columns;j++)
                sumr[i]+=tab[i][j];
            sum_all+=sumr[i];
        }
        for(int i=0;i<rows;i++){
            entro=0.0;
            for(int j=0;j<columns;j++){
                if(tab[i][j]!=0){
                    x = (double)tab[i][j]/(double)sumr[i];
                    entro+=(-x)*(double)Math.log((double)x)/(double)Math.log((double)2);
//                    System.out.println((-x)*(double)Math.log((double)x)/(double)Math.log((double)2));
                }
            }
            tot_entro+=((double)sumr[i]/(double)sum_all)*(entro);
        }
        if(split_info==true){
            split_in=0.0;
            for(int i=0;i<rows;i++){
                x=(double)sumr[i]/(double)sum_all;
                split_in+=(-x)*(double)Math.log((double)x)/(double)Math.log((double)2);
            }
        }
        return (tot_infog-tot_entro)/split_in;
    }
    public String predict(String[] predi){
        if(this.leaf==true)
            return this.name;
        for(int i=0;i<paths.size();i++){
            if(paths.get(i).equals(predi[sel_col])){
                return child.get(i).predict(predi);
            }
        }
        return "Unable to predict";
    }
}
public class Decisiontrees_id3_c45 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Narahc\\Documents\\NetBeansProjects\\decisiontrees_id3_cart\\cardata.txt"));
        String s=" ";
        boolean split_info = false;
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
//        System.out.println(obj.predict(Data.get(0))+" "+Data.get(0)[Data.get(0).length-1]);
//        System.out.println(obj.predict(Data.get(1))+" "+Data.get(1)[Data.get(1).length-1]);
        String [] predi;
        int cor=0;
        for(i=0;i<Data.size();i++){
            predi=Data.get(i);
            if(obj.predict(predi).equals(predi[predi.length-1]))
                cor++;
        }
        System.out.println(cor+ " / "+ Data.size());
    }

}

