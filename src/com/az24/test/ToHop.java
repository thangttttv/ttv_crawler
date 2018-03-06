package com.az24.test;

import java.awt.List;
import java.util.ArrayList;
import java.util.Scanner;

//http://congdongjava.com/forum/threads/thu%E1%BA%ADt-to%C3%A1n-sinh-nh%E1%BB%8B-ph%C3%A2n-t%E1%BB%95-h%E1%BB%A3p-ho%C3%A1n-v%E1%BB%8B-c%C6%A1-b%E1%BA%A3n.17405/
//java game combinatorial from alphabet

public class ToHop {
    private int i, n, k, a[];

    public void Init() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.print("Nhập vào số phần tử n >=0:");
            this.n = input.nextInt();
            System.out.print("Nhập vào số tổ hợp  k <= n:");
            this.k = input.nextInt();
        } while (this.n < 0 || this.k > this.n);

        a = new int[n+1];
        for (int j = 1; j <= this.k; j++)
            a[j] = j;
    }

    //Hiển thị kết quả
    public void Result() {
        for (int j = 1; j <= k; j++)
            System.out.print(a[j] + "  ");
        System.out.println();
    }
    //Sinh tổ hợp
    public void sinhToHop() {
        do {
            Result();
          this.i = this.k;
          while (this.i > 0 && a[i] == this.n -this.k + i) -- i;
          if (this.i > 0) {
              a[i]++;
              for (int j = i + 1; j <= this.k; j++) {
                  a[j] = a[j-1] + 1;
              }
          }

        } while (this.i != 0);

    }
    
    public void generateCombinations(int arraySize, ArrayList<String> possibleValues)
    {
        int carry;
        int[] indices = new int[arraySize];
        do
        {
            for(int index : indices)
                System.out.print(possibleValues.get(index) + " ");
            System.out.println("");

            carry = 1;
            for(int i = indices.length - 1; i >= 0; i--)
            {
                if(carry == 0)
                    break;

                indices[i] += carry;
                carry = 0;

                if(indices[i] == possibleValues.size())
                {
                    carry = 1;
                    indices[i] = 0;
                }
            }
        }
        while(carry != 1); // Call this method iteratively until a carry is left over
    }

    public static void main(String[] agrs) {
        ToHop tohop = new ToHop();
       // tohop.Init();
       // tohop.sinhToHop();
        ArrayList<String> ab = new ArrayList<String>();
        ab.add("a");ab.add("g");ab.add("l");ab.add("q");ab.add("v");
        ab.add("b");ab.add("h");ab.add("m");ab.add("x");
        ab.add("c");ab.add("i");ab.add("n");ab.add("y");
        ab.add("d");ab.add("j");ab.add("o");ab.add("z");
        ab.add("e");ab.add("k");ab.add("p");ab.add("u");
        tohop.generateCombinations(5, ab);
        System.gc();
    }
}
