package net.bitnine;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJackson {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	public static void main(String [] args) {
		Map<String, String> dummyData1 = new HashMap<>();
		dummyData1.put("value1", "값1");
		dummyData1.put("value2", "값2");
		 
		ObjectMapper om = new ObjectMapper();
		 
		try {  			
		    System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(dummyData1));
		} catch (IOException e) {  
		    e.printStackTrace();
		}


		/*try {
			ObjectMapper mapper = new ObjectMapper();
			
			String user_json = "{'name' : { 'first' : 'Joe', 'last' : 'Sixpack' }, "
					+ " 'gender' : 'Male', "
					+ " 'verified' : false, "
					+ " 'userImage' : 'Rm9vYmFyIQ==' " + "   }";
			
			User user = mapper.readValue(user_json, User.class);
		}*/
	}
	

}

class User {
	public enum Gender {Male, Female};
	
	public static class Name {
		private String _first, _last;
	}
}


























