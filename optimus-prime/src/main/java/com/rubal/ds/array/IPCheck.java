package com.rubal.ds.array;

import java.util.Arrays;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class IPCheck {

    public boolean checkIpSegment(String str){

        if(str.length() == 0 || (str.charAt(0)=='0' && str.length() > 1) || str.length() > 3 ||
                Integer.valueOf(str) > 255)
            return false;
        return true;

    }
    public boolean isValid(String s) {
        String[] ips = s.split("\\.");
        System.out.println(ips.length);

        //Arrays.stream(ips).forEach(System.out::println);
        if(ips.length != 4)
            return false;
        for(String ip: ips){
            //System.out.println(ip);
            if(!checkIpSegment(ip))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        IPCheck ipCheck = new IPCheck();
        ipCheck.isValid("12.23.23.43.");
    }
}
