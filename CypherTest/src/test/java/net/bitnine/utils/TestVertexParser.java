package net.bitnine.utils;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
import org.postgresql.util.PGtokenizer;

import net.bitnine.domain.Vertex;

public class TestVertexParser {
    public static void main(String[] args) throws SQLException, ParseException {
        String result = "[production[4.7058]{\"id\": 51, \"kind\": \"episode\", \"title\": \"Haunted House\", \"md5sum\": \"9fae28455fdcdbcb6a725e501abea51a\", \"full_info\": [{\"tech info\": \"CAM:Arri Alexa\"}, {\"tech info\": \"CAM:Canon 5D Mark II SLR Camera, Canon Lenses\"}, {\"release dates\": \"Australia:26 November 2013\"}], \"season_nr\": 1, \"episode_nr\": 6, \"phonetic_code\": \"H532\", \"production_year\": 2013},company[5.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"},movie[2.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"}]";
        VertexParser vertexParser = new VertexParser();

        TopCommaTokenizer topCommaTokenizer = new TopCommaTokenizer(result);
        
        for (int i = 0; i < topCommaTokenizer.getSize(); i++) {
            System.out.println("topCommaTokenizer.getToken: " + topCommaTokenizer.getToken(i));
            

            String p = PGtokenizer.removeBox(topCommaTokenizer.getToken(i));
            
            Vertex vertex = vertexParser.createParsedVertext(p);

//            rowJsonObject.put(columnName, vertex);
        }
        
//        vertexParser.setValue(result);     
    }
}
