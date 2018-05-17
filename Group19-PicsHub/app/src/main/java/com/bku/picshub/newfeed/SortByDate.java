package com.bku.picshub.newfeed;

import com.bku.picshub.info.PostInfo;

import java.util.Comparator;

/**
 * Created by Welcome on 5/17/2018.
 */

public class SortByDate implements Comparator<PostInfo> {
    public int compare(PostInfo first,PostInfo second){
        String strTimeStamp1=first.getTimeStamp();
        String strTimeStamp2=second.getTimeStamp();
        String [] DateTime1=strTimeStamp1.split(" ");
        String [] DateTime2=strTimeStamp2.split(" ");

        String year1=DateTime1[0].split("/")[0];
        String year2=DateTime2[0].split("/")[0];
        String month1=DateTime1[0].split("/")[1];
        String month2=DateTime2[0].split("/")[1];
        String day1=DateTime1[0].split("/")[2];
        String day2=DateTime2[0].split("/")[2];

        String hour1=DateTime1[1].split(":")[0];
        String hour2=DateTime2[1].split(":")[0];
        String minute1=DateTime1[1].split(":")[1];
        String minute2=DateTime2[1].split(":")[1];
        String second1=DateTime1[1].split(":")[2];
        String second2=DateTime2[1].split(":")[2];

        if(Integer.parseInt(year1)>Integer.parseInt(year2)){
            return Integer.parseInt(year1)-Integer.parseInt(year2);
        }
        else if(Integer.parseInt(month1)>Integer.parseInt(month2)){
            return Integer.parseInt(month1)-Integer.parseInt(month2);
        }
        else if(Integer.parseInt(day1)>Integer.parseInt(day2)){
            return Integer.parseInt(day1)-Integer.parseInt(day2);
        }
        else if(Integer.parseInt(hour1)>Integer.parseInt(hour2)){
            return Integer.parseInt(hour1)-Integer.parseInt(hour2);
        }
        else if(Integer.parseInt(minute1)>Integer.parseInt(minute2)){
            return Integer.parseInt(minute1)-Integer.parseInt(minute2);
        }
        else{
            return Integer.parseInt(second1)-Integer.parseInt(second2);
        }
    }
}
