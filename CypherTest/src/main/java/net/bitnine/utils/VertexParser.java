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

import net.bitnine.domain.Vertex;

public class VertexParser  extends DomainParser {
    private static final Pattern _pattern;
    static {
        _pattern = Pattern.compile("(.+)\\[(\\d+)\\.(\\d+)\\](.*)");
//        _pattern = Pattern.compile("([a-zA-Z]*)\\[[0-9]\\.[0-9]*\\](.*)");     // node를 파싱하기위한 정규식. ex) 'production[4.1111...]' 을 검색함.
        /*_pattern = Pattern.compile("(.+)\\[([\\d+]\\.[\\d+])\\](.*)");
        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\](.*)"; // node를 파싱하기위한 정규식. ex) 'production[4.1111...]' 을 검색함.
*/    }


    public List<Vertex> createParsedVertextList(String result) throws SQLException, ParseException {
        TopCommaTokenizer topCommaTokenizer = new TopCommaTokenizer(result);
        List<Vertex> vertextList = new ArrayList<>();
         
        for (int i = 0; i < topCommaTokenizer.getSize(); i++) {
            System.out.println("topCommaTokenizer.getToken: " + topCommaTokenizer.getToken(i));

            String p = PGtokenizer.removeBox(topCommaTokenizer.getToken(i));
            
            Vertex vertex = createParsedVertext(p);
            vertextList.add(vertex);
        }
        return vertextList;
    }
    
    /**
     * 파싱되지 않은 쿼리결과문자열을 파싱하여 VertextList를 생성하는 메소드
     * @param result
     * @return
     * @throws SQLException 
     * @throws ParseException 
     */
    public Vertex createParsedVertext(String result) throws SQLException, ParseException {
        String id;
        String name;
        String type = "vertex";
        JSONObject props;

        JSONParser parser = new JSONParser();
        
        Matcher m = _pattern.matcher(result);
        if (m.find()) {
            name = m.group(1);
            id = m.group(2) + "." + m.group(3);
            String property = m.group(4);
            if (property == null)
                props = null;
            else
                props = (JSONObject) parser.parse(property);
        } else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, result}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }

        return new Vertex(id, type, name, props);
    }
    
    
    
    
    
    public List<Vertex> createParsedVertextList2(String result) throws ParseException {
        
        String squareRemovedResult = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}

        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]"; // node를 파싱하기위한 정규식. ex) 'production[4.1111...]' 을 검색함.
//        String strPattern = "[a-zA-Z]*\\[[0-9]+(.[0-9])\\]"; // node를 파싱하기위한 정규식. ex) 'production[4.1111...]' 을 검색함.
        
        Pattern pattern = Pattern.compile(strPattern);
        
        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.

        // vertex들과 props들을 담을 배열, ex) { production[4.812332], {"id": 51, "kind": "episode", ..},  company[3.4444],  {"id": 5, "kind": "episode", ..}, movie[4.444234], {"id": 15, "kind": "episode", ..}, .... }
        String[] vertexes = createVertexes(squareRemovedResult, countMatcherNum, matcherLocate);
        
        List<Vertex> vertextList = getVertexList(countMatcherNum, vertexes);

        return vertextList;
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
            
            //Vertex vertex = new Vertex(id, "Vertex", name, props);          // Vertex 객체 생성
            
            //vertextList.add(vertex);
        }
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
}
