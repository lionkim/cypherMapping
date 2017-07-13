package net.bitnine;

import static org.mockito.Matchers.matches;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexFindExam {
	public static void main(String[] args) {

//		String content = "\n[errorcode] = ERR0001" + "\n[errordescription] = 접속 실패!";
		String content = "[production[4.7058]{\"id\": 51, \"kind\": \"episode\", \"title\": \"Haunted House\", \"md5sum\": \"9fae28455fdcdbcb6a725e501abea51a\", \"full_info\": [{\"tech info\": \"CAM:Arri Alexa\"}, {\"tech info\": \"CAM:Canon 5D Mark II SLR Camera, Canon Lenses\"}, {\"release dates\": \"Australia:26 November 2013\"}], \"season_nr\": 1, \"episode_nr\": 6, \"phonetic_code\": \"H532\", \"production_year\": 2013},company[5.3494]{\"id\": 5, \"name\": \"Australian Broadcasting Corporation (ABC)\", \"md5sum\": \"3543eb7b85c34815894baad029499929\", \"country_code\": \"[au]\", \"name_pcode_nf\": \"A2364\", \"name_pcode_sf\": \"A2364\"}]";
		System.out.println(content);

		System.out.println("\n========== 정규표현으로 문자열 검색 ==================");

		// [name] = value 와 같은 패턴으로 되어 있는 문자열을 검색하는 정규표현
//		Pattern pattern = Pattern.compile("\\[\\w+] = .+");

//		String strPattern = "^[a-zA-Z]+\\[+[0-9]+\\]\\{+$";
		String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]\\{";
		
		Pattern pattern = Pattern.compile(strPattern);
		
		Matcher matcher = pattern.matcher(content);

		int[] matcherLocate = new int[2];
		
		// 정규 표현에 검색된 문자열 구하기
		// find() 메소드가 false 반환할 때까지 반복
		int cnt = 0;
		while (matcher.find()) {
			matcher.group(0);
			int end = matcher.end();
			
			System.out.println("end: " + end + "\n");
			matcherLocate[cnt] = end;
			// group() 메소드를 호출하고 정규 표현에 일치된 문자열을 꺼냄
//			System.out.println(matcher.group(0) + " => 시작위치: " + matcher.start() + ", 끝위치: " +matcher.end());
			cnt++;
		}
		
		for (int i : matcherLocate) {
			System.out.println("matcherLocate: " + i + "\n");
		}
		
		
		

		/*System.out.println("\n========== 일치된 문자열의 일부를 구하기 ============");

		// 정규표현식에서는 괄호 () 로 둘러싼 범위를 그룹화 할 수 있다.
		// Matcher#group(int) 메소드에 인수를 넘겨줌으로 써 특정 그룹에 일치된 부분의 문자열을 구할수있다.

		// 2개의 그룹을 포함한 정규표현식
		Pattern groupPattern = Pattern.compile("\\[(\\w+)] = (.+)"); // [name] =
																		// value
																		// 와 같은
																		// 패턴
		Matcher groupMatcher = groupPattern.matcher(content);

		while (groupMatcher.find()) {
			// System.out.println(groupMatcher.group()); // 정규표현식에 일치한 전체 문자열
			System.out.println(groupMatcher.group(0)); // 정규표현식에 일치한 전체 문자열

			String name = groupMatcher.group(1); // 첫번째 그룹에 일치한 문자열을 구함
			String value = groupMatcher.group(2); // 두번째 그룹에 일치한 문자열을 구함
			System.out.println(String.format("이름:%s, 값:%s%n", name, value));
		}*/
	}

}
