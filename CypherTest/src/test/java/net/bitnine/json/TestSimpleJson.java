package net.bitnine.json;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.Yytoken;
import org.junit.Test;

public class TestSimpleJson {

    public static void main(String[] args) {
        String s = "{"
    + "\"title\": \"Person\","
    + "\"type\": \"object\","
    + "\"properties\": {"
        + "\"firstName\": {"
            + "\"type\": \"string\""
            + "},"
            + "\"lastName\": {"
            + "\"type\": \"string\" "
            + "}, "
            + "\"age\": { "
            + "\"description\": \"Age in years\", "
            + "\"type\": \"integer\", "
            + "\"minimum\": 0 "
            + "} "
            + "}, "
    + "\"required\": [\"firstName\", \"lastName\"] "
    + "}";
    
        s = JSONObject.escape(s);
        
        String str = "[\"firstName\", \"lastName\"]"; 
        
        Yytoken yytoken = new Yytoken(3, str);
        
        System.out.println("yytoken: " + yytoken.toString());
    
        System.out.println(s);
    }

}
