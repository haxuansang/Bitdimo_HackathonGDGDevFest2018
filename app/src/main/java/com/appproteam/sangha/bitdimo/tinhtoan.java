package com.appproteam.sangha.bitdimo;

import java.util.Random;

public class tinhtoan {
    public int count = 0;
    Random random = new Random();
    public void main(String[] Args) {
        while (count<20){
            int optimistic = (int)(Math.random() * 4 + 1);
            int pesstimictic = (int) (Math.random() * 15 + 9);
            int mostlikely = (int) (Math.random() * 9 + 4);
            if (((pesstimictic-((optimistic+mostlikely*4+pesstimictic)/6))/pesstimictic)>0.7){
                System.out.println("op:" + optimistic +"\t most:"+ mostlikely + "\t pes:" + pesstimictic);
                count++;
            }
        }
    }
}
