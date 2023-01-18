package com.example.smartorder.common.component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationComponents {

//	@Value("${kakao.key}")
	private String apiKey = "5639e87e58d677766b2f3146fd14828b";

	public Map<String, Object> getXY(String address) {
		String jsonString = getLocation(address);
		return parseLocation(jsonString);
	}

	public String getLocation(String address) {

		try {
			String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json?"
				+ "query=" + URLEncoder.encode(address, "UTF-8");

			URL url = new URL(apiUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "KakaoAK " + apiKey);

			BufferedReader br;
			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();

			return response.toString();
		} catch (Exception e) {
			return "fail to get location";
		}
	}

	public Map<String, Object> parseLocation(String jsonString) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;

		try {
			jsonObject = (JSONObject) jsonParser.parse(jsonString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Map<String, Object> resultMap = new HashMap<>();

		JSONArray documentsArr = (JSONArray) jsonObject.get("documents");
		JSONObject documents = (JSONObject) documentsArr.get(0);
		resultMap.put("x", documents.get("x"));
		resultMap.put("y", documents.get("y"));

		return resultMap;
	}

}
