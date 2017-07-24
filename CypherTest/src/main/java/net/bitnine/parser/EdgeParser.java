package net.bitnine.parser;

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
import net.bitnine.utils.DomainParser;
import net.bitnine.utils.TopCommaTokenizer;

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

    public Edge createParsedEdge(String result) throws SQLException, ParseException {

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
}
