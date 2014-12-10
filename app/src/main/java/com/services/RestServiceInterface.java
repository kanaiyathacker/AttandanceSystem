package com.services;

import com.bean.AttandanceTransaction;
import com.bean.User;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public interface RestServiceInterface {

    @POST("/ScanAttandanceApp/saveDetails/{companyId}/{personId}/{fName}/{mName}/{lName}/{date}/{time}/{desc}")
    Object save(@Path("companyId") String companyId , @Path("personId") String personId ,@Path("fName") String fName,
                @Path("mName") String mName,@Path("lName") String lName,@Path("date") String date,
                @Path("time") String time,@Path("desc") String desc );

    @POST("/ScanAttandanceApp/saveDetails")
    Object save(@Body AttandanceTransaction attandanceTransaction);

    @POST("/ScanAttandanceApp/login/{loginId}/{password}/{deviceId}")
    Object login(@Path("loginId") String loginId , @Path("password") String password ,@Path("deviceId") String deviceId);

    @GET("/ScanAttandanceApp/getUserInfo/{cardId}")
    Object getUserInfo(@Path("cardId") String cardId);

    @GET("/ScanAttandanceApp/viewReport/{adminId}/{orgId}")
    Object viewReport(@Path("adminId") String adminId , @Path("orgId") String orgId);

    @GET("/ScanAttandanceApp/viewAbsenteeDetails/{adminId}/{orgId}")
    Object viewAbsenteeDetailsRequest(@Path("adminId") String adminId  , @Path("orgId") String orgId);



//    @POST("/DeviceTokenApplicaion-0.0.1-SNAPSHOT/ScanAttandanceApp/saveDetails")
//    Object save(@Body AttandanceTransaction attandanceTransaction);
//
//    @POST("/DeviceTokenApplicaion-0.0.1-SNAPSHOT/ScanAttandanceApp/login/{loginId}/{password}/{deviceId}")
//    Object login(@Path("loginId") String loginId , @Path("password") String password ,@Path("deviceId") String deviceId);
//
//    @GET("/DeviceTokenApplicaion-0.0.1-SNAPSHOT/ScanAttandanceApp/getUserInfo/{cardId}")
//    Object getUserInfo(@Path("cardId") String cardId);
//
//    @GET("/DeviceTokenApplicaion-0.0.1-SNAPSHOT/ScanAttandanceApp/viewReport/{adminId}/{orgId}")
//    Object viewReport(@Path("adminId") String adminId , @Path("orgId") String orgId);
//
//    @GET("/DeviceTokenApplicaion-0.0.1-SNAPSHOT/ScanAttandanceApp/viewAbsenteeDetails/{adminId}/{orgId}")
//    Object viewAbsenteeDetailsRequest(@Path("adminId") String adminId  , @Path("orgId") String orgId);


}
