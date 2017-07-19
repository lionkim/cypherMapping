package net.bitnine.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import net.bitnine.domain.Vertex;

public class JsonObjectTest {
    
    public List<Vertex> parseVertext(String result) throws ParseException {

        String node = result.substring(1, result.length()-1);      // distributed[8.965184][5.3494,4.7058]{}
        
        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]"; // nodes에서 파싱하기위한 정규식. ex) 'production[4.1111...]{' 을 검색함.

        Pattern pattern = Pattern.compile(strPattern);

        Matcher matcher = pattern.matcher(node);

        int[] matcherLocate = new int[10];
        
        // 정규 표현에 검색된 문자열 구하기
        // find() 메소드가 false 반환할 때까지 반복
        int cnt = 0;
        while (matcher.find()) {
            matcher.group(0);

            matcherLocate[cnt++] = matcher.start();       // 정규식으로 발견된 처음 위치
            matcherLocate[cnt++] = matcher.end();     // 정규식으로 발견된  끝 위치
        }
        matcherLocate[cnt] = node.length();

        String[] vertexes = new String[10];         // vertex들을 담을 배열, ex) { production[4.812332],  company[3.4444],  movie[4.444234], .... }
        
        for (int i = 0; i < cnt; i++) {
            
            int first = matcherLocate[i];
            int j = i+1;
            int second = matcherLocate[j];
            if ( ((i % 2) == 1) && (i != (cnt - 1)) ) {          // props 맨뒤의 ","를 없애기 위해
                second = second - 1;
            }
            vertexes [i] = node.substring(first, second);   
        }
        
        List<Vertex> vertextList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        
        String[] vertexNames = new String[2];   
        
        for (int k = 0; k < cnt; k += 2) {              // vertex는 인덱스 2증가로 저장되있음.
            vertexNames = vertexes[k].split("\\[");     // ex) vertexes[k] = production[4.812332],   vertexNames = { production, 4.812332] }
            String name = vertexNames[0];
            String id = vertexNames[1].substring(0, vertexNames[1].length() - 1);       // 맨뒤에 "]"가 있으므로 전체길이에서 -1를 해줌
            int propsNum = k + 1;       // 해당 vertex의 props. 
            
            JSONObject  props = (JSONObject) parser.parse(vertexes[propsNum]);
            Vertex vertex = new Vertex(name, "Vertex", id, props);
            
            vertextList.add(vertex);
        }

        return vertextList;
    }
    
    public static void main(String[] args) throws ParseException {
        JsonObjectTest jsonObjectTest = new JsonObjectTest();
        
        String result = "[production[4.7058]{\"id\": 51, \"kind\": \"episode\", \"title\": \"Haunted House\", \"md5sum\": \"9fae28455fdcdbcb6a725e501abea51a\", \"full_info\": [{\"tech info\": \"CAM:Arri Alexa\"}, {\"tech info\": \"CAM:Canon 5D Mark II SLR Camera, Canon Lenses\"}, {\"release dates\": \"Australia:26 November 2013\"}], \"season_nr\": 1, \"episode_nr\": 6, \"phonetic_code\": \"H532\", \"production_year\": 2013},company[5.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"},movie[2.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"}]";
        
        jsonObjectTest.parseVertext(result);
    }

}
