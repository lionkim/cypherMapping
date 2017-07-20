package net.bitnine.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.bitnine.domain.Vertex;

public class DomainParser {
    public static List<Vertex> createParsedVertextList(String result) throws ParseException {
        result = "[production[4.7058]{\"id\": 51, \"kind\": \"episode\", \"title\": \"Haunted House\", \"md5sum\": \"9fae28455fdcdbcb6a725e501abea51a\", \"full_info\": [{\"tech info\": \"CAM:Arri Alexa\"}, {\"tech info\": \"CAM:Canon 5D Mark II SLR Camera, Canon Lenses\"}, {\"release dates\": \"Australia:26 November 2013\"}], \"season_nr\": 1, \"episode_nr\": 6, \"phonetic_code\": \"H532\", \"production_year\": 2013},company[5.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"},movie[2.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"}]";
        
        String node = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}
        
        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]"; // nodes에서 파싱하기위한 정규식. ex) 'production[4.1111...]' 을 검색함.

        Pattern pattern = Pattern.compile(strPattern);

        int countMatcherNum = getMatchersLength(node, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(node, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.

        // vertex들과 props들을 담을 배열, ex) { production[4.812332], {"id": 51, "kind": "episode", ..},  company[3.4444],  {"id": 5, "kind": "episode", ..}, movie[4.444234], {"id": 15, "kind": "episode", ..}, .... }
        String[] vertexes = createVertexes(node, countMatcherNum, matcherLocate);
        
        List<Vertex> vertextList = getVertexList(countMatcherNum, vertexes);

        return vertextList;
    }

    /**
     * vertex들과 props들을 담을 배열 생성
     * @param node
     * @param countMatcherNum
     * @param matcherLocate
     * @return
     */
    private static String[] createVertexes(String node, int countMatcherNum, int[] matcherLocate) {
        String[] vertexes = new String[countMatcherNum];        
        
        for (int i = 0; i < countMatcherNum; i++) {
            
            int first = matcherLocate[i];
            int j = i+1;
            int second = matcherLocate[j];
            if ( ((i % 2) == 1) && (i != (countMatcherNum - 1)) ) {          // props 맨뒤의 ","를 없애기 위해
                second = second - 1;
            }
            vertexes [i] = node.substring(first, second);   
        }
        return vertexes;
    }


    /**
     * 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기를 반환
     * @param node
     * @param pattern
     * @return
     */
    private static int getMatchersLength(String node, Pattern pattern) {
        
        int countMatcherNum = 0;        // matcherLocate 배열 크기를 정하기 위한 수.
        
        Matcher countMatcher = pattern.matcher(node);
        while (countMatcher.find()) {
            countMatcher.group(0);

            countMatcher.start();       // 정규식으로 발견된 처음 위치
            countMatcherNum++;
            countMatcher.end();     // 정규식으로 발견된  끝 위치
            countMatcherNum++;
        }
        return countMatcherNum;
    }
    
    /**
     * 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성
     * @param node
     * @param pattern
     * @param countMatcherNum
     * @return
     */
    private static int[] createMatcherLocate(String node, Pattern pattern, int countMatcherNum) {
        Matcher matcher = pattern.matcher(node);
        int[] matcherLocate = new int[countMatcherNum + 1];     // countMatcherNum에는 마지막 props는 포함되지 않으므로 하나 큰 배열을 생성
        
        // 정규 표현에 검색된 문자열 구하기
        // find() 메소드가 false 반환할 때까지 반복
        int cnt = 0;
        while (matcher.find()) {
            matcher.group(0);

            matcherLocate[cnt++] = matcher.start();       // 정규식으로 발견된 처음 위치
            matcherLocate[cnt++] = matcher.end();     // 정규식으로 발견된  끝 위치
        }
        matcherLocate[countMatcherNum] = node.length();         // 배열의 마지막에는 node의 끝위치를 저장.
        return matcherLocate;
    }

    /**
     * 실제 Vertex List를 생성하는 메소드
     * @param countMatcherNum
     * @param vertexes                      // 결과 문자열을 vertext 메타정보와 props를 파싱하여 저장된 배열 ex) { production[4.812332], {"id": 51, "kind": "episode", ..},  company[3.4444],  {"id": 15, "kind": "episode", ..}, .... }
     * @return
     * @throws ParseException
     */
    private static List<Vertex> getVertexList(int countMatcherNum, String[] vertexes) throws ParseException {
        List<Vertex> vertextList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        
        String[] vertexNames = new String[2];   
        
        for (int k = 0; k < countMatcherNum; k += 2) {              // vertex는 인덱스 2증가로 저장되있음.
            vertexNames = vertexes[k].split("\\[");     // ex) vertexes[k] = production[4.812332],   vertexNames = { production, 4.812332] }
            String name = vertexNames[0];
            String id = vertexNames[1].substring(0, vertexNames[1].length() - 1);       // 맨뒤에 "]"가 있으므로 전체길이에서 -1를 해줌
            int propsNum = k + 1;       // 해당 vertex의 props. 
            
            JSONObject  props = (JSONObject) parser.parse(vertexes[propsNum]);
            Vertex vertex = new Vertex(id, "Vertex", name, props);
            
            vertextList.add(vertex);
        }
        return vertextList;
    }
}
