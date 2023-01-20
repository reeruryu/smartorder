package com.example.smartorder.common.component;

import static com.example.smartorder.common.error.ErrorCode.FAIL_CALL_KAKAO_API;
import static com.example.smartorder.common.error.ErrorCode.INVALID_ADDRESS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.*;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.model.KakaoApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class LocationComponents {

//	@Value("${kakao.key}")
	private String apiKey = "5639e87e58d677766b2f3146fd14828b";

	public KakaoApi.Documents getKakaoApiDouments(String address) {
		List<KakaoApi.Documents> documents =
			getKakaoApi(address).getDocuments();

		if (documents.size() == 0) {
			throw new CustomException(INVALID_ADDRESS);
		}
		return documents.get(0);
	}

	public KakaoApi getKakaoApi(String address) {
		// request url
		URI apiUrl = UriComponentsBuilder.newInstance()
			.scheme("https").host("dapi.kakao.com")
			.path("/v2/local/search/address.json")
			.query("query={queryValue}")
			.encode(Charset.forName("UTF-8"))
			.buildAndExpand(address).toUri();

		// RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// header
		HttpHeaders header = new HttpHeaders();
		header.add(AUTHORIZATION, "KakaoAK " + apiKey);

		// build the request
		HttpEntity request = new HttpEntity<>(header);

		// HTTP GET request with header
		ResponseEntity<KakaoApi> response = restTemplate.exchange(
			apiUrl, HttpMethod.GET, request, KakaoApi.class);

		int responseCode = response.getStatusCodeValue();
		if (responseCode != 200) {
			throw new CustomException(FAIL_CALL_KAKAO_API);
		}

		return response.getBody();
	}

}
