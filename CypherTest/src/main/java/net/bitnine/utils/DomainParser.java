package net.bitnine.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.bitnine.domain.Edge;
import net.bitnine.domain.Path;
import net.bitnine.domain.Vertex;

public class DomainParser {


    public List<Path> createParsedPathList(String result) {
        String squareRemovedResult = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}

        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]\\[[0-9]\\.[0-9]*\\,[0-9]\\.[0-9]*\\]"; // result 를 파싱하기위한 정규식. ex) Edge 'distributed[8.965184][5.3494,4.7058]' 를 검색함.

        Pattern pattern = Pattern.compile(strPattern);
        
        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.
        return null;
    }
    
    public List<Edge> createParsedEdge(String result) throws ParseException {
        // [distributed[8.965184][5.3494,4.7058]{}]
        // distributed[8.965184][5.3494,4.7058]{}
        // distributed[   8.965184][   5.3494,4.7058]{}
//        result = "[distributed[8.965184][5.3494,4.7058]{},friend[2.965184][5.3494,4.7058]{},like[3.963384][5.3294,4.7511]{}]";
        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]\\[[0-9]\\.[0-9]*\\,[0-9]\\.[0-9]*\\]"; // node를 파싱하기위한 정규식. ex) 'distributed[8.965184][5.3494,4.7058]' 을 검색함.

        String squareRemovedResult = result.substring(1, result.length()-1);      // distributed[8.965184][5.3494,4.7058]{}
        Pattern pattern = Pattern.compile(strPattern);

        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.

        // [distributed[8.965184][5.3494,4.7058], {}, distributed[8.965184][5.3494,4.7058], {}, distributed[8.965184][5.3494,4.7058], {}]
        String[] edges = createNodes (squareRemovedResult, countMatcherNum, matcherLocate);
        int edgesLength = edges.length;
        
        List<Edge> edgeList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        
        String[] edgeNames = new String[edgesLength];   
        
        for (int k = 0; k < edgesLength; k++) {
            edgeNames = edges[k].split("\\[");      // ex) edges[k] = distributed[8.965184][5.3494,4.7058]{}
            String name = edgeNames[0];
            String id = edgeNames[1].substring(0, edgeNames[1].length() - 1);       // 맨뒤에 "]"가 있으므로 전체길이에서 -1를 해줌   [ 8.965184 ]

            String[] sourceAndTargetAndProps = edgeNames[2].split("\\]");           // 5.3494,4.7058, {}
            String[] sourceAndTargets = sourceAndTargetAndProps[0].split("\\,");                // 5.3494, 4.7058 

            String source = sourceAndTargets[0];        // 5.3494
            String target = sourceAndTargets[1];        // 4.7058

            JSONObject props = (JSONObject) parser.parse(sourceAndTargetAndProps[1]);       // {}

            Edge edge = new Edge(id, "Edge", name, source, target, props); // Edge 객체 생성

            edgeList.add(edge);
        }
        
        return edgeList;
    }
    
    /**
     * vertex들과 props들을 담을 배열 생성
     * @param node
     * @param countMatcherNum
     * @param matcherLocate
     * @return
     */
    private String[] createNodes   (String beforeParsingString, int countMatcherNum, int[] matcherLocate) {
        String[] nodes = new String[countMatcherNum / 2];        
        
        for (int i = 0, k = 0; i < countMatcherNum; i += 2, k++) {
            
            // edge의 시작 위치 값.  ex) // distributed[8.965184][5.3494,4.7058]{},friend[8.965184][5.3494,4.7058]{},like[8.965184][5.3494,4.7058]{} 이라면
            int start = matcherLocate[i];          // d, f, l 위치값
            int j = i+2;        // ,
            
            int end = 0;
            // edge의 끝 위치 값.      ex)   } 위치값
            if (i != countMatcherNum - 2) {     
                end = matcherLocate[j] - 1;     // props 맨뒤의 "," 하나 이전으로 짜르기 위해
            } else {        // 마지막일 때
                end = matcherLocate[j];         // 마지막일 경우 "," 가 없으므로
            }
            nodes [k] = beforeParsingString.substring(start, end);   
        }
        return nodes;
    }
    


    /**
     * 실제 Vertex List를 생성하는 메소드
     * @param countMatcherNum
     * @param vertexes                      // 결과 문자열을 vertext 메타정보와 props를 파싱하여 저장된 배열 ex) { production[4.812332], {"id": 51, "kind": "episode", ..},  company[3.4444],  {"id": 15, "kind": "episode", ..}, .... }
     * @return
     * @throws ParseException
     */
    private List<Vertex> getVertexList(int countMatcherNum, String[] vertexes) throws ParseException {
        List<Vertex> vertextList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        
        String[] vertexNames = new String[2];   
        
        for (int k = 0; k < countMatcherNum; k += 2) {              // vertex는 인덱스 2증가로 저장되있음.
            vertexNames = vertexes[k].split("\\[");     // ex) vertexes[k] = production[4.812332],   vertexNames = { production, 4.812332] }
            String name = vertexNames[0];
            String id = vertexNames[1].substring(0, vertexNames[1].length() - 1);       // 맨뒤에 "]"가 있으므로 전체길이에서 -1를 해줌
            int propsNum = k + 1;       // 해당 vertex의 props. 
            
            JSONObject  props = (JSONObject) parser.parse(vertexes[propsNum]);
            
            Vertex vertex = new Vertex(id, "Vertex", name, props);          // Vertex 객체 생성
            
            vertextList.add(vertex);
        }
        return vertextList;
    }
    
    /**
     * 파싱되지 않은 쿼리결과문자열을 파싱하여 VertextList를 생성하는 메소드
     * @param result
     * @return
     * @throws ParseException
     */
    public List<Vertex> createParsedVertextList(String result) throws ParseException {
        
        String squareRemovedResult = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}
        
        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]"; // node를 파싱하기위한 정규식. ex) 'production[4.1111...]' 을 검색함.

        Pattern pattern = Pattern.compile(strPattern);
        
        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.

        // vertex들과 props들을 담을 배열, ex) { production[4.812332], {"id": 51, "kind": "episode", ..},  company[3.4444],  {"id": 5, "kind": "episode", ..}, movie[4.444234], {"id": 15, "kind": "episode", ..}, .... }
        String[] vertexes = createVertexes(squareRemovedResult, countMatcherNum, matcherLocate);
        
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
    private String[] createVertexes(String node, int countMatcherNum, int[] matcherLocate) {
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
    private int getMatchersLength(String node, Pattern pattern) {
        
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
    private int[] createMatcherLocate(String node, Pattern pattern, int countMatcherNum) {
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
}
