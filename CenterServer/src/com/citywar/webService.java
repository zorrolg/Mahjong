package com.citywar;




import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/** 
* Java6寮�鍙慦ebService鍏ラ棬 
* 
* @author leizhimin 2009-11-13 16:10:44 
*/ 
@WebService 
public class webService { 
        /** 
         * Web鏈嶅姟涓殑涓氬姟鏂规硶 
         * 
         * @return 涓�涓瓧绗︿覆 
         */ 
        public String doSomething() { 
                return "Hello Java6 WebService!"; 
        } 

        public static void main(String[] args) { 

        	Endpoint.publish("http://localhost:8080/com.citywar.dice.webService", new webService());  
        	
        } 
}