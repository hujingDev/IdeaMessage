package com.hujing.ideamessage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
       String[] ch={"bss","ass","ess","gss","css","dss","fss"};
        List<String>  list=new ArrayList<>();
//        Arrays.sort(ch);
       for (int i=0;i<ch.length;i++){
           list.add(ch[i]);
//           System.out.println(ch[i]);
       }
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (String s:list){
            System.out.println(s);
        }
    }
}