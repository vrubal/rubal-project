package com.rubal.ds.company;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class RateLimiter {
    static class URLReq {
        private int count;
        private int idx;
        URLReq(int count, int idx){
            this.count = count;
            this.idx = idx;
        }
    }
    public static void main(String[] args) {
        //String[] req = new String[]{"a.com","b.com","b.com","a.com","b.com","b.com","b.com","b.com","b.com"};
        String[] req = new String[]{"www.xyz.com"," www.abc.com","www.xyz.com","www.pqr.com","www.abc.com","www.xyz.com","www.xyz.com","www.abc.com","www.xyz.com"};

        Map<String, URLReq> map = new HashMap<>();
        for (int i = 0; i < req.length; i++) {
            if(map.containsKey(req[i])){
                URLReq urlReq = map.get(req[i]);
                if(urlReq.idx > i-4){
                    if(urlReq.count == 2){
                        System.out.println("Too many request "+req[i]);
                    }else{
                        System.out.println("Request Accepted "+req[i]);
                        urlReq.count = 2;
                        urlReq.idx = i;
                    }
                }else{
                    System.out.println("Request Accepted "+req[i]);
                    urlReq.count = 1;
                    urlReq.idx = i;
                }

            }else{
                System.out.println("Request Accepted "+req[i]);
                map.put(req[i], new URLReq(1, i));
            }
        }

    }
}
