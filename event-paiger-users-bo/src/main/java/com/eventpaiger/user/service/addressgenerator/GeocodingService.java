package com.eventpaiger.user.service.addressgenerator;

import com.eventpaiger.openmapsobjects.NominatinSearchQueryDto;
import com.eventpaiger.openmapsobjects.NominatinSearchResponse;
import com.eventpaiger.user.helper.NominatimApiHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    @Value("${geocoding.nominatin.path}")
    private String basicPath;

    public NominatinSearchResponse getFirstResult(NominatinSearchQueryDto query) throws IOException {
        TypeReference<List<NominatinSearchResponse>> responseType = new TypeReference<>() {};

        String forObject = restTemplate.getForObject(basicPath + NominatimApiHelper.SEARCH_PATH_Q + buildQuery(query), String.class);

        List<NominatinSearchResponse> nominatinSearchResponses = objectMapper.readValue(forObject, responseType);

            return nominatinSearchResponses.isEmpty()
                    ? new NominatinSearchResponse()
                    : nominatinSearchResponses.get(0);
    }


    private String buildQuery(NominatinSearchQueryDto dto){
        StringBuilder buildQuery = new StringBuilder();
        Field[] declaredFields = dto.getClass().getDeclaredFields();

        for (Field field : declaredFields){
            try {
                field.setAccessible(true);
                Object value = field.get(dto);
                if(value != null && StringUtils.hasText(value.toString())){
                    buildQuery.append(value).append(" ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return buildQuery.toString();
    }
}
