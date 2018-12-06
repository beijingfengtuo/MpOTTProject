package cn.cibnmp.ott.ui.detail;

import java.util.EmptyStackException;

/**
 * Created by axl on 2018/1/10.
 */

public class Main<E> {
    //输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，
    // 所有的偶数位于位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。

    protected Object[] elementData;

    protected int capacityIncrement;

    protected int elementCount;
    private static final int DEFAULT_SIZE = 10;

    private int mobcount = 0;//记录操作次数的


    private Main() {
    }

    //private E[] new
    private E[] newElmentArray(int size) {
        return (E[]) new Object[size];
    }


    public void add(int loaction, E object) {
        //   insertElementAt(object,loaction);
    }
    //数组结构会有相应的迭代器


    @Override
    public synchronized int hashCode() {

        int result = 1;
        for (int i = 0; i < elementCount; i++) {
            result = (31 * result) * (elementData[i].hashCode());
        }

        return result;
    }


    public synchronized E peek() {
        try {
            return (E) elementData[elementCount - 1];
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }


    public synchronized E pop() {
        if (elementCount == 0) {
            throw new EmptyStackException();
        }
        //这写的十分精妙  既获取到索引 有将元素总数--
        final int index = --elementCount;
        final E obj = (E) elementData[index];
        //没有缩容
        elementData[index] = null;
        mobcount++;
        return obj;
    }

    public synchronized  void addElement(Object obj){
        if(elementCount==elementData.length){
            growByOne();
        }
        elementData[elementCount++] =obj;
        mobcount++;
    }

    private void growByOne() {
        int adding = 0;
        if(capacityIncrement<=0){
            if((adding=elementData.length)==0){
                adding=1;
            }
        }else{
            adding= capacityIncrement;
        }
        E[] newData= (E[]) new Object[elementData.length+adding];
        System.arraycopy(elementData,0,newData,0,elementCount);
        elementData= newData;
    }



    public synchronized  int search(Object o){
        //利用elementData 容易出现安全问题 所以需要赋值
        final Object[] dumpArray = elementData;
        final int size = elementCount;
        if(o!=null){

            for (int i = size-1; i >=0 ; i--) {
                if(o.equals(dumpArray[i])){
                    return i;
                }
            }
        }else{
            for (int i = size-1; i >=0; i--) {
                if(dumpArray[i]==null){
                    return i;
                }
            }
        }
        //波兰表达式
        int i = 9+(3-1)*3+10/2;

        String s = "9+(3-1)*3+10/2";

        for (int j = 0; j < s.length(); j++) {
           char c = s.charAt(j);

        }


        return  -1;
    }


    //private final int

//    private static void int print(String s){
//
//
//    }

     class MyStack{

        private static final  char c1 = '1';


        private static final char c9='9';
        private static final char c11 ='(';
        private static final  char c12 =')';
        private static final char c13='+';
         private static final char c14= '-';

      private char[]data = new char[100];

      public void  push(){

      }

      public void pop(){

      }


    }

}

