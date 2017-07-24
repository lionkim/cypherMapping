package net.bitnine.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
import org.postgresql.util.GT;
import org.postgresql.util.PGtokenizer;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import net.bitnine.domain.Path;
import net.bitnine.domain.Edge;
import net.bitnine.domain.Vertex;

public class PathParser extends DomainParser {
    public List<Path> createParsedPathList(String result) {
        String squareRemovedResult = result.substring(1, result.length()-1);      // 결과물 양쪽 끝의 [, ] 를 제거 함.  ex) [distributed[8.965184][5.3494,4.7058]{}] => distributed[8.965184][5.3494,4.7058]{}

        String strPattern = "w*\\[[0-9]*\\.[0-9]*\\]\\[[0-9]*\\.[0-9]*\\,[0-9]*\\.[0-9]*\\]"; // result 를 파싱하기위한 정규식. ex) Edge 'distributed[8.965184][5.3494,4.7058]' 를 검색함.

        Pattern pattern = Pattern.compile(strPattern);
        
        int countMatcherNum = getMatchersLength(squareRemovedResult, pattern);                               // 해당 패턴의 시작위치와 끝위치를 저장하는 배열의 크기 계산.

        int[] matcherLocate = createMatcherLocate(squareRemovedResult, pattern, countMatcherNum);      // 패턴에 일치하는 시작위치와 끝위치를 저장하는 배열을 생성. 이 위치를 사용하여 문자열을 짜를것임.
        return null;
    }
    
    public Path createParsedPath (String result) throws SQLException, ParseException {
        String type = "path";
        List<Vertex> vertexs = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        VertexParser vertexParser = new VertexParser();
        
        
        String p = PGtokenizer.removeBox(result);
        TopCommaTokenizer t;
        try {
            t = new TopCommaTokenizer(p);
        }
        catch (Exception e) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}."
                        , new Object[]{type, result})
                    , PSQLState.DATA_TYPE_MISMATCH);
        }
        for (int i = 0; i < t.getSize(); ++i) {
            if (i % 2 == 0) {
                vertexs.add(vertexParser.createParsedVertext(t.getToken(i)));
            }
            else {
                edges.add(new Edge());
            }
        }
        return new Path("", "", vertexs, edges);
    }
}
