import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
class Connection{
   Integer weight = null;
   ArrayList<String> dest = new ArrayList<String>();
   String stdest = null;
   public Connection(){
   
   }
   public Connection( Integer weight,String stdest) {
      this.weight =  weight;
      this.stdest = stdest;
      dest.add(this.stdest);
      Collections.sort(dest);     
   }
   @Override//override equals method to check if objects of the connection class are equal.
   public boolean equals(Object obj){   
      if(obj instanceof Connection){
         Connection connection = (Connection) obj;
         if(connection != null && this.weight == connection.weight){  
            if( connection.dest.contains(this.stdest) ){
               return true;                  
            }
         }
      }
      return false;
   }
}

class nfa{
   Map<String,ArrayList<Connection>> nfa = new LinkedHashMap<String,ArrayList<Connection>>();
   Map<String,ArrayList<Connection>> dfa = new LinkedHashMap<String,ArrayList<Connection>>();
   Set<String>  skip = new HashSet<String>();
   boolean convnfa = false;
   
   public Map<String, ArrayList<Connection>> getDfa(){
      return dfa;
   }
   
   public boolean canConvert(){
      return convnfa;
   }
   
   public boolean isSameArray(ArrayList<Connection> checkarray, Connection checkhold){
      for(Connection tmpcheck : checkarray){
         if(tmpcheck.dest == null && checkhold.dest == null){
            return true;         
         }
         if((tmpcheck.dest == null && checkhold.dest != null) || tmpcheck.dest != null && checkhold.dest == null || tmpcheck.dest.size() != checkhold.dest.size()){
            continue;
         }
         tmpcheck.dest = new ArrayList<String>(tmpcheck.dest);
         checkhold.dest = new ArrayList<String>(checkhold.dest);
         Collections.sort(tmpcheck.dest);
         Collections.sort(checkhold.dest);
         if(tmpcheck.dest.equals(checkhold.dest)){
            return true;
         }
      }
      return false;
   } 
   
   public void transform(){
      //transforms the nfa to dfa and stores it in a map.
      //calls the print method to rint it out.
      
      String source;
      
      ArrayList<Connection> hold = new ArrayList<Connection>();
      for(String key : nfa.keySet()){//loops through nfa map
         source = key;                     
         if(!skip.contains(key)){
         dfa.put(key,nfa.get(key)); 
         ArrayList<Connection> getcon = new ArrayList<Connection>(nfa.get(key));//gets the connection of key
         for(ListIterator<Connection> place = getcon.listIterator(); place.hasNext();){
            skip.add(key);
            String adding = new String(); 
            Connection tmp = place.next();
            for(String express : tmp.dest){//append dest arraylist to string and check if the key exists in nfa 
               adding += express;
            }
            if(!nfa.containsKey(adding)|| !dfa.containsKey(adding)){
               for(String layer :  tmp.dest){
                  skip.add(layer);
                  if(nfa.containsKey(layer)){
                     ArrayList<Connection>  tmpconc = new ArrayList<Connection>(nfa.get(layer));
                     //loop through arraylist connection tmpconc and add to dfa
                     for(Connection putdfa :  tmpconc){
                        ArrayList<String>  holddestdfa = new ArrayList<String>(putdfa.dest);
                        for(String putdestdfa :  holddestdfa){
                           addLink(adding, putdestdfa, putdfa.weight, dfa);
                        }
                     }
                  }
               }
               if(dfa.get(adding) != null){
                  for(Connection listadd : dfa.get(adding)){
                     if(!isSameArray(hold,listadd)){
                        Collections.sort(listadd.dest);
                        hold.add(listadd);
                        place.add(listadd);
                        place.previous();                           
                     }                                             
                  }
               }                  
            }
         }
      }
   }      
   //print(dfa);  
}
   //overloaded method might replace this later
   public void addLink(String source, String dest, Integer weight){
      addLink(source, dest,weight,nfa);
   }
   public void addLink(String source, String dest, Integer weight, Map<String,ArrayList<Connection>> map){
      if(map.containsKey(source)){//checks if source entered already exists if it does adds the connection to the source
         ArrayList<Connection>  conc = new ArrayList<Connection>(map.get(source));
         
         for(Iterator<Connection> iterator = conc.iterator(); iterator.hasNext();){//updates the dest arraylist of the connection when weight exists at the source.
            Connection c = iterator.next();
            if(c.weight == (weight)&& !c.dest.contains(dest)){
               convnfa = true;
               c.dest.add(dest);
               break;
            }
         }
         if(!conc.contains(new Connection(weight, dest))){//doesn't add same conneciton of the same source
            conc.add(new Connection(weight, dest));
            map.put(source,conc);
         }
      }
      else{//else add a new source with new connection
         Connection newc = new Connection(weight,dest);
         ArrayList<Connection> newcon = new ArrayList<Connection>();
         newcon.add(newc);
         map.put(source, newcon);
         //iterator.remove();
      }
   }
   //Overloaded method to print map
   public void print(){
      print(nfa);
   }
   public void print(Map<String,ArrayList<Connection>> map){
      //code to print map 
      for(String key : map.keySet()){
         ArrayList<Connection> holdcon = new ArrayList<Connection>(map.get(key)); //puts the connection from specific key
         System.out.print("Key = " + key);
         System.out.print("-->" );
         for(Connection printcon : holdcon){
            System.out.print( "[" + printcon.weight + "|");            
            ArrayList<String> holddest = new ArrayList<String>(printcon.dest);
            for(String printdest : holddest){ // goes through arraylist of dest and prints
               System.out.print( printdest);
            }
            System.out.print("]");
         }
      }
     //System.out.println("Maping ------------");
     //System.out.println(map.entrySet());
   }
}
/*
public class nfaconverter {
   public static void  main(String[] args) {
      nfa test = new nfa();
      
      //while loop to input values into map
      String source, dest;
      int weight, count = 0;
      Scanner scan = new Scanner(System.in);
      
      while(count< 4){
         System.out.println("Enter the source");
         source = scan.next();
          
         System.out.println("Now the connection first the weight");
         weight = scan.nextInt();
         
         System.out.println("Now the destination");
         dest = scan.next();
         test.addLink(source,dest,weight);
         count++;
      }
      test.print();
      System.out.println("-----------------DFA------------");
      test.transform();
   }
}*/