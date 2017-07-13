package net.bitnine.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestJacksonJSON {

	public static void main(String[] args) {

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
			
			String jsonStr = mapper.writeValueAsString(user);
			System.out.println("Simple Binding: " + jsonStr);
			
			

			// 직접 raw 데이터를 입력해서 JSON형태로 출력하는 방법.
			Map<String, Object> userData = new HashMap<>();
			Map<String, Object> nameStruct = new HashMap<>();
			
			nameStruct.put("first", "Jon");
			nameStruct.put("last", "BonJovi");
			
			userData.put("name", nameStruct);
			userData.put("gender", "MALE");
			userData.put("verified", Boolean.FALSE);
			userData.put("userImage", "Rm9vYmFyIQ==");
			
			jsonStr = mapper.writeValueAsString(userData);
			System.out.println("Raw Data: " + jsonStr);
			
			
			ObjectMapper m = new ObjectMapper();
			
			JsonNode rootNode = m.readTree(userJson);
			
			JsonNode nameNode = rootNode.path("name");
			String lastName = nameNode.path("last").textValue();
			((ObjectNode) nameNode).put("last", "inputLast");
			
			jsonStr = m.writeValueAsString(rootNode);
			System.out.println("Tree Model: " + jsonStr);
			
			

			// Streaming API 예제
			JsonFactory factory = new JsonFactory();
			
			OutputStream outStream = System.out;
			
			JsonGenerator jsonGenerator = factory.createJsonGenerator(outStream);
			
			jsonGenerator.writeStartObject();
			jsonGenerator.writeObjectFieldStart("name");
			jsonGenerator.writeStringField("first", "StreamAPIFirst");
			jsonGenerator.writeStringField("last", "Sixpack");
			jsonGenerator.writeEndObject();
			jsonGenerator.writeStringField("gender", "MALE");
			jsonGenerator.writeBooleanField("verified",  false);
			jsonGenerator.writeFieldName("userImage");
			jsonGenerator.writeEndObject();
			jsonGenerator.close();
			
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

	}

}




































