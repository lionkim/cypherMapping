package net.bitnine.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.util.GT;
import org.postgresql.util.PGtokenizer;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import net.bitnine.domain.Edge;

public class EdgeParser extends DomainParser{
    private static Pattern _pattern;
    static {
        _pattern = Pattern.compile("(.+)\\[(\\d+)\\.(\\d+)\\]\\[(\\d+)\\.(\\d+),(\\d+)\\.(\\d+)\\](.*)");
    }
    
    public List<Edge> createParsedVertextList(String result) throws SQLException, ParseException {
        TopCommaTokenizer topCommaTokenizer = new TopCommaTokenizer(result);
        List<Edge> edgeList = new ArrayList<>();
         
        for (int i = 0; i < topCommaTokenizer.getSize(); i++) {
            System.out.println("edgeToken: " + topCommaTokenizer.getToken(i));

            String p = PGtokenizer.removeBox(topCommaTokenizer.getToken(i));
            
            Edge edge = createParsedEdge(p);
            edgeList.add(edge);
        }
        return edgeList;
    }

    private Edge createParsedEdge(String result) throws SQLException, ParseException {

        String id;
        String type = "edge"; 
        String name;
        String source;
        String target;
        JSONObject  props;

        JSONParser parser = new JSONParser();
        
        Matcher m = _pattern.matcher(result);
        if (m.find()) {
            name = m.group(1);
            id = m.group(2) + "." + m.group(3);
            source = m.group(4) + "." + m.group(5);
            target = m.group(6) + "." + m.group(7);
            String properties = m.group(8);
            if (properties == null)
                props = null;
            else
                props = (JSONObject) parser.parse(properties);
        }
        else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, result}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }
        
        return new Edge(id, type, name, source, target, props);
    }
    
    /*public List<Edge> createParsedEdge(String result) throws ParseException {
        // [distributed[8.965184][5.3494,4.7058]{}]
        // distributed[8.965184][5.3494,4.7058]{}
        // distributed[   8.965184][   5.3494,4.7058]{}
//        result = "[distributed[8.965184][5.3494,4.7058]{},friend[2.965184][5.3494,4.7058]{},like[3.963384][5.3294,4.7511]{}]";
        String strPattern = "([a-zA-Z]+)\\[([0-9]+\\.[0-9]+)\\]\\[([0-9]+\\.[0-9]+)\\,([0-9]+\\.[0-9]+)\\]"; // node를 파싱하기위한 정규식. ex) 'distributed[8.965184][5.3494,4.7058]' 을 검색함.

        String squareRemovedResult = result.substring(1, result.length()-1);      // distributed[8.965184][5.3494,4.7058]{}
        Pattern pattern = Pattern.compile(strPattern);

        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.

        // [distributed[8.965184][5.3494,4.7058], {}, distributed[8.965184][5.3494,4.7058], {}, distributed[8.965184][5.3494,4.7058], {}]
        String[] edges = createNodes (squareRemovedResult, countMatcherNum, matcherLocate);
        int edgesLength = edges.length;
        
        List<Edge> edgeList = testingCreateMatcherLocate(squareRemovedResult, pattern, countMatcherNum);
        
        return edgeList;
    }
    
    *//**
     * 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성
     * @param node
     * @param pattern
     * @param countMatcherNum
     * @return
     *//*
    private List<Edge> testingCreateMatcherLocate(String node, Pattern pattern, int countMatcherNum) {
        Matcher matcher = pattern.matcher(node);

        JSONParser parser = new JSONParser();
        
        List<Edge> edgeList = new ArrayList<>();
        
        String[] edgeStringFields = new String[10];
        int[] matcherLocate = new int[countMatcherNum + 1];     // countMatcherNum에는 마지막 props는 포함되지 않으므로 하나 큰 배열을 생성
        
        // 정규 표현에 검색된 문자열 구하기
        // find() 메소드가 false 반환할 때까지 반복
        int stringFieldCount = 0;
        while (matcher.find()) {
            matcher.group(0);
            edgeStringFields[stringFieldCount++] = matcher.group(1);
            edgeStringFields[stringFieldCount++] = matcher.group(2); 
            edgeStringFields[stringFieldCount++] = matcher.group(3);        // 5.3494
            edgeStringFields[stringFieldCount++] = matcher.group(4);        // 4.7058
        }
        int cnt = 0;
        while (matcher.find()) {
            matcher.group(0);

            matcherLocate[cnt++] = matcher.start();       // 정규식으로 발견된 처음 위치
            matcherLocate[cnt++] = matcher.end();       // 정규식으로 발견된 처음 위치
        }
        
        node.substring(

        JSONObject props = new JSONObject();
//                (JSONObject) parser.parse(sourceAndTargetAndProps[1]);       // {}

        Edge edge = new Edge(id, "Edge", name, source, target, props); // Edge 객체 생성

        edgeList.add(edge);
        
        
        matcherLocate[countMatcherNum] = node.length();         // 배열의 마지막에는 node의 끝위치를 저장.
        return edgeList;
    }
    
    
    public List<Edge> createParsedEdge(String result) throws ParseException {
        // [distributed[8.965184][5.3494,4.7058]{}]
        // distributed[8.965184][5.3494,4.7058]{}
        // distributed[   8.965184][   5.3494,4.7058]{}
//        result = "[distributed[8.965184][5.3494,4.7058]{},friend[2.965184][5.3494,4.7058]{},like[3.963384][5.3294,4.7511]{}]";
        String strPattern = "([a-zA-Z]+)(\\[[0-9]+\\.[0-9]+\\])(\\[[0-9]+\\.[0-9]+\\,[0-9]+\\.[0-9]+\\])"; // node를 파싱하기위한 정규식. ex) 'distributed[8.965184][5.3494,4.7058]' 을 검색함.

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
    }*/
}
