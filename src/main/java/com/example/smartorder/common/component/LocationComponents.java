package com.example.smartorder.common.component;

import static com.example.smartorder.common.error.ErrorCode.FAIL_CALL_KAKAO_API;
import static com.example.smartorder.common.error.ErrorCode.INVALID_ADDRESS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.model.KakaoApi;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class LocationComponents {

	@Value("${kakao.key}")
	private String apiKey;

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
