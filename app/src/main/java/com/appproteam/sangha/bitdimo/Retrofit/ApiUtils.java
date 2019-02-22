package com.appproteam.sangha.bitdimo.Retrofit;

public class ApiUtils {

    public static final String BASE_URL = "http://192.168.137.252:8000";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}