package net.bitnine.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTopCommaTokenizer {

	public static void main(String[] args) {
//		String result = \"[distributed[8.965184][5.3494,4.7058]{},friend[2.965184][5.3494,4.7058]{},like[3.963384][5.3294,4.7511]{}]\";
//				String result = "production[4.3875135]{\"id\": 3898645, \"kind\": \"movie\", \"title\": \"Swallow\", \"md5sum\": \"c706cc6d6a60f2765f4038fb1e0020e2\", \"full_info\": [{\"plot\": \"Swallow is the exploration of a man, whose idea of the world was shaped through guilt and shame. Swallow explores this man's life by means of an organic stream of consciousness where all things have already happened and represent the flashes of images that would likely happen in a single moment.\"}, {\"color info\": \"Color\"}, {\"countries\": \"USA\"}, {\"genres\": \"Fantasy\"}, {\"genres\": \"Short\"}, {\"languages\": \"English\"}, {\"runtimes\": \"14\"}, {\"release dates\": \"USA:21 September 2013\"}], \"imdb_index\": \"IV\", \"phonetic_code\": \"S4\", \"production_year\": 2013}";
//				String result = "person[3.14]{\"id\": 1370, \"name\": \"Aaron, Alex\", \"gender\": \"m\", \"md5sum\": \"4dd5ad09d650f8c04f7e88035fb12512\", \"name_pcode_cf\": \"A6542\", \"name_pcode_nf\": \"A4265\", \"surname_pcode\": \"A65\"},actor_in[30.6642][3.14,4.3875135]{\"md5sum\": \"6e7ad9b8cbd15010cb39e80d80d7e753\", \"role_name\": \"Arthur\", \"name_pcode_nf\": \"A636\", \"surname_pcode\": null},production[4.3875135]{\"id\": 3898645, \"kind\": \"movie\", \"title\": \"Swallow\", \"md5sum\": \"c706cc6d6a60f2765f4038fb1e0020e2\", \"full_info\": [{\"plot\": \"Swallow is the exploration of a man, whose idea of the world was shaped through guilt and shame. Swallow explores this man's life by means of an organic stream of consciousness where all things have already happened and represent the flashes of images that would likely happen in a single moment.\"}, {\"color info\": \"Color\"}, {\"countries\": \"USA\"}, {\"genres\": \"Fantasy\"}, {\"genres\": \"Short\"}, {\"languages\": \"English\"}, {\"runtimes\": \"14\"}, {\"release dates\": \"USA:21 September 2013\"}], \"imdb_index\": \"IV\", \"phonetic_code\": \"S4\", \"production_year\": 2013}";
				
		String result = "[production[4.7058]{\"id\": 51, \"kind\": \"episode\", \"title\": \"Haunted House\", \"md5sum\": \"9fae28455fdcdbcb6a725e501abea51a\", \"full_info\": [{\"tech info\": \"CAM:Arri Alexa\"}, {\"tech info\": \"CAM:Canon 5D Mark II SLR Camera, Canon Lenses\"}, {\"release dates\": \"Australia:26 November 2013\"}], \"season_nr\": 1, \"episode_nr\": 6, \"phonetic_code\": \"H532\", \"production_year\": 2013}";
		
		TopCommaTokenizer topCommaTokenizer = new TopCommaTokenizer(result);
		
		for (int i = 0; i < topCommaTokenizer.getSize(); i++) {
			System.out.println("topCommaTokenizer.getToken: " + topCommaTokenizer.getToken(i));
		}
	}

}
