package net.bitnine.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import net.bitnine.domain.Vertex;
import net.bitnine.utils.DomainParser;

public class JsonObjectTest {
    
    public static void main(String[] args) throws ParseException {
        DomainParser domainParser = new DomainParser();
        
//        String result = "[production[4.7058]{\"id\": 51, \"kind\": \"episode\", \"title\": \"Haunted House\", \"md5sum\": \"9fae28455fdcdbcb6a725e501abea51a\", \"full_info\": [{\"tech info\": \"CAM:Arri Alexa\"}, {\"tech info\": \"CAM:Canon 5D Mark II SLR Camera, Canon Lenses\"}, {\"release dates\": \"Australia:26 November 2013\"}], \"season_nr\": 1, \"episode_nr\": 6, \"phonetic_code\": \"H532\", \"production_year\": 2013},company[5.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"},movie[2.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"}]";
        String result = "[person[3.14]{\"id\": 1370, \"name\": \"Aaron, Alex\", \"gender\": \"m\", \"md5sum\": \"4dd5ad09d650f8c04f7e88035fb12512\", \"name_pcode_cf\": \"A6542\", \"name_pcode_nf\": \"A4265\", \"surname_pcode\": \"A65\"},actor_in[30.6642][3.14,4.3875135]{\"md5sum\": \"6e7ad9b8cbd15010cb39e80d80d7e753\", \"role_name\": \"Arthur\", \"name_pcode_nf\": \"A636\", \"surname_pcode\": null},production[4.3875135]{\"id\": 3898645, \"kind\": \"movie\", \"title\": \"Swallow\", \"md5sum\": \"c706cc6d6a60f2765f4038fb1e0020e2\", \"full_info\": [{\"plot\": \"Swallow is the exploration of a man, whose idea of the world was shaped through guilt and shame. Swallow explores this man's life by means of an organic stream of consciousness where all things have already happened and represent the flashes of images that would likely happen in a single moment.\"}, {\"color info\": \"Color\"}, {\"countries\": \"USA\"}, {\"genres\": \"Fantasy\"}, {\"genres\": \"Short\"}, {\"languages\": \"English\"}, {\"runtimes\": \"14\"}, {\"release dates\": \"USA:21 September 2013\"}], \"imdb_index\": \"IV\", \"phonetic_code\": \"S4\", \"production_year\": 2013}]";
//        List<Vertex> vertexList = domainParser.createParsedVertextList(result);
//        System.out.println("vertexList: " + vertexList);
        domainParser.createParsedPathList(result);
        
        
    }

}
