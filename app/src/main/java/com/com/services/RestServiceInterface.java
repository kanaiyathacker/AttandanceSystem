package com.com.services;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public interface RestServiceInterface {
    @GET("/")
    Object getPanchang(@Query("cityname") String cityName , @Query("yr") int year  , @Query("mn") int month , @Query("dt") int date);
}
