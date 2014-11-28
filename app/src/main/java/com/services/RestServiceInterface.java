package com.services;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public interface RestServiceInterface {

//    @POST("/ScanAttandanceApp/saveDetails/{companyId}/{personId}/{fName}/{mName}/{lName}/{date}/{time}/{desc}")
//    Object save(@Path("companyId") String companyId , @Path("personId") String personId ,@Path("fName") String fName,
//                @Path("mName") String mName,@Path("lName") String lName,@Path("date") String date,
//                @Path("time") String time,@Path("desc") String desc );

    @POST("/ScanAttandanceApp/saveDetails")
    Object save(@Body SaveAttandanceRequest saveAttandanceRequest);


    @POST("/ScanAttandanceApp/login/{loginId}/{password}/{deviceId}")
    Object login(@Path("loginId") String loginId , @Path("password") String password ,@Path("deviceId") String deviceId);

}
