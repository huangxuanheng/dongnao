package com.dongnao.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test2 {
    public static void main(String args[]){
        ArrayList<String> list = new ArrayList<String>();
        list.add("this is a GoldenKey");
        list.add("hahaha");
        list.add("fsdfad1");
        list.add("fsdfad2");
        list.add("fsdfad3");
        list.add("fsdfad4");


        Iterator<String> itr =  list.iterator();

        while (itr.hasNext()){
            //list.remove("fsdfad1");
            System.out.println(itr.next());;
            itr.remove();
        }
    }
}
