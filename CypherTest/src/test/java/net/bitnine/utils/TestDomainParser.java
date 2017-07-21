package net.bitnine.utils;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestDomainParser {

    public static void main(String[] args) {
        String content = "\n[errorcode] = ERR0001"
                + "\n[errordescription] = 접속 실패!"; 
        
        System.out.println(content);
                
        System.out.println("\n========== 정규표현으로 문자열 검색 ==================");
        
        // [name] = value 와 같은 패턴으로 되어 있는 문자열을 검색하는 정규표현
        Pattern pattern = Pattern.compile("\\[\\w+] = .+"); 
        Matcher matcher = pattern.matcher(content);
        
        // 정규 표현에 검색된 문자열 구하기
        // find() 메소드가 false 반환할 때까지 반복 
        while(matcher.find()) {
            //group() 메소드를 호출하고 정규 표현에 일치된 문자열을 꺼냄
            System.out.println(matcher.group()); 
        }
        
        
        System.out.println("\n========== 일치된 문자열의 일부를 구하기 ============");
        
        // 정규표현식에서는 괄호 () 로 둘러싼 범위를 그룹화 할 수 있다. 
        // Matcher#group(int) 메소드에 인수를 넘겨줌으로 써 특정 그룹에 일치된 부분의 문자열을 구할수있다.
        
        // 2개의 그룹을 포함한 정규표현식 
        Pattern groupPattern = Pattern.compile("\\[(\\w+)] = (.+)"); //[name] = value 와 같은 패턴
        Matcher groupMatcher = groupPattern.matcher(content);
        
        while(groupMatcher.find()) {
            //System.out.println(groupMatcher.group());  // 정규표현식에 일치한 전체 문자열
            System.out.println("group(0) " +groupMatcher.group(0)); // 정규표현식에 일치한 전체 문자열
            
            /*String name = groupMatcher.group(1);  // 첫번째 그룹에 일치한 문자열을 구함
            String value = groupMatcher.group(2); // 두번째 그룹에 일치한 문자열을 구함
            System.out.println(String.format("이름:%s, 값:%s%n", name, value));      */  
            System.out.println("group(1) " +groupMatcher.group(1)); // 첫번째 그룹에 일치한 문자열을 구함
            System.out.println("group(2) " +groupMatcher.group(2)); // 두번째 그룹에 일치한 문자열을 구함    
        }       
        
       /* String squareRemovedResult = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}

        String strPattern = "w*\\[[0-9]*\\.[0-9]*\\]\\[[0-9]*\\.[0-9]*\\,[0-9]*\\.[0-9]*\\]"; // result 를 파싱하기위한 정규식. ex) Edge 'distributed[8.965184][5.3494,4.7058]' 를 검색함.

        Pattern pattern = Pattern.compile(strPattern);*/
        
        
    }

}




























