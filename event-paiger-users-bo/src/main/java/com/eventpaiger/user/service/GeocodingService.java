package com.eventpaiger.user.service;

import com.eventpaiger.openmapsobjects.NominatinSearchQueryDto;
import com.eventpaiger.openmapsobjects.NominatinSearchResponse;
import com.eventpaiger.user.helper.NominatimApiHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
@Slf4j
public class GeocodingService {

    private final OkHttpClient httpClient = new OkHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    public JsonObject search(String query) throws IOException {
        Request request = new Request.Builder()
                .url(NominatimApiHelper.SEARCH_PATH_Q + query)
                .build();

        try(Response response = httpClient.newCall(request).execute()) {
            if(!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(response.body().string());

            if(!jsonArray.isEmpty()){
                return jsonArray.get(0).getAsJsonObject();
            }
            return null;
        }
    }

    public List<NominatinSearchResponse> search(NominatinSearchQueryDto query) throws IOException {

        Request request = new Request.Builder()
                .url(NominatimApiHelper.SEARCH_PATH + buildQuery(query))
                .build();

        try(Response response = httpClient.newCall(request).execute()) {
            if(!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }

            TypeReference<List<NominatinSearchResponse>> responseType = new TypeReference<>() {
            };

            return objectMapper.readValue(response.body().bytes(), responseType);
        }
    }

    public NominatinSearchResponse getFirstResult(NominatinSearchQueryDto query) throws IOException {

        Request request = new Request.Builder()
                .url(NominatimApiHelper.SEARCH_PATH_Q + buildQuery(query))
                .build();

        try(Response response = httpClient.newCall(request).execute()) {
            if(!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }

            TypeReference<List<NominatinSearchResponse>> responseType = new TypeReference<>() {
            };

            if(response.body() == null){
                return new NominatinSearchResponse();
            }

            List<NominatinSearchResponse> nominatinSearchResponses = objectMapper.readValue(response.body().bytes(), responseType);

            return nominatinSearchResponses.isEmpty()
                    ? new NominatinSearchResponse()
                    : nominatinSearchResponses.get(0);
        }
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
