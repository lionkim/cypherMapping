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
    
    /**
     * vertex들과 props들을 담을 배열 생성
     * @param node
     * @param countMatcherNum
     * @param matcherLocate
     * @return
     */
    protected String[] createNodes   (String beforeParsingString, int countMatcherNum, int[] matcherLocate) {
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
     * 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기를 반환
     * @param node
     * @param pattern
     * @return
     */
    protected int getMatchersLength(String node, Pattern pattern) {
        
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
    protected int[] createMatcherLocate(String node, Pattern pattern, int countMatcherNum) {
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
