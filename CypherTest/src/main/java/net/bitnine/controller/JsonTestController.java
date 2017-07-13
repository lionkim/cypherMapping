package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bitnine.domain.dto.DataSourceDTO;

@RestController
public class JsonTestController {

	@RequestMapping("/testJson")
	public void findPath(HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		Map<String, String> dummyData1 = new HashMap<>();
		dummyData1.put("value1", "값1");
		dummyData1.put("value2", "값2");

		ObjectMapper om = new ObjectMapper();
		om.writeValue(response.getWriter(), dummyData1);
	}

	@RequestMapping("/listJson")
	public void listJson(HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		// List 에 맵이 들어가 있는 형태
		List<Map<String, String>> dummyData2 = new ArrayList<>();

		Map<String, String> m = new HashMap<>();
		m.put("bonjovi", "singer");
		m.put("Hugh Jackman", "actor");
		m.put("Bill Nighy", "actor");
		m.put("Leonardo", "actor");
		dummyData2.add(m);

		m = new HashMap<>();
		m.put("Bill Gates", "programmer");
		m.put("Lee", "developer");
		dummyData2.add(m);

		ObjectMapper om = new ObjectMapper();

		om.writeValue(response.getWriter(), dummyData2);
	}

	@RequestMapping("json")
	public String jacksonJson() {
		String jsonStr = "";
		try {
			ObjectMapper mapper = new ObjectMapper();

			String userJson = "{\"name\" : { \"first\" : \"Joe\", \"last\" : \"Sixpack\" }, "
					+ " \"gender\" : \"MALE\", " + " \"verified\" : false, " + " \"userImage\" : \"Rm9vYmFyIQ==\" "
					+ "      } ";

			User user = mapper.readValue(userJson, User.class);

			System.out.println("First name: " + user.getName().getFirst());
			System.out.println("Last name: " + user.getName().getLast());
			System.out.println("Gender: " + user.getGender());
			System.out.println("Verified: " + user.isVerified());

			user.getName().setFirst("Lion");
			user.getName().setLast("Kim");

			jsonStr = mapper.writeValueAsString(user);
			System.out.println("Simple Binding: " + jsonStr);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonStr;
	}

}

class User {
	public enum Gender {
		MALE, FEMALE
	};

	public static class Name {
		private String first, last;

		public String getFirst() {
			return first;
		}

		public void setFirst(String first) {
			this.first = first;
		}

		public String getLast() {
			return last;
		}

		public void setLast(String last) {
			this.last = last;
		}
	}

	private Gender gender;
	private Name name;
	private boolean isVerified;
	private byte[] userImage;

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public byte[] getUserImage() {
		return userImage;
	}

	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}

}
