package net.bitnine.utils;

import java.util.List;
import java.util.regex.Pattern;

import net.bitnine.domain.Path;

public class PathParser extends DomainParser {
    public List<Path> createParsedPathList(String result) {
        String squareRemovedResult = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}

        String strPattern = "w*\\[[0-9]*\\.[0-9]*\\]\\[[0-9]*\\.[0-9]*\\,[0-9]*\\.[0-9]*\\]"; // result 를 파싱하기위한 정규식. ex) Edge 'distributed[8.965184][5.3494,4.7058]' 를 검색함.

        Pattern pattern = Pattern.compile(strPattern);
        
        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.
        return null;
    }
}
