package com.example.smartorder.controller.smartorder;

import com.example.smartorder.common.dto.ApiResponse;
import com.example.smartorder.dto.LocationDto;
import com.example.smartorder.model.LocationParam;
import com.example.smartorder.service.smartorder.LocationService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/location")
public class LocationController {

	private final LocationService locationService;

	@PostMapping
	public ApiResponse updateLocation(
		@RequestBody @Valid LocationParam.Customer parameter,
		Principal principal) {

		locationService.updateLocation(parameter.getLat(), parameter.getLnt(),
			principal.getName());

		return ApiResponse.OK();
	}

	@GetMapping("/store")
	public ApiResponse getNearStoreList(Principal principal) {

		List<LocationDto> storeList =
			locationService.getNearStoreList(principal.getName());

		return ApiResponse.OK(storeList);
	}

}
