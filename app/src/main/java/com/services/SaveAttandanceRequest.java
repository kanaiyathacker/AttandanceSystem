package com.services;

import com.bean.User;
import com.google.inject.Singleton;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.io.Serializable;

import roboguice.inject.ContextSingleton;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class SaveAttandanceRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> implements Serializable {

//    private String companyId;
//    private String personId;
//    private String fName;
//    private String mName;
//    private String lName;
//    private String date;
//    private String time;
//    private String desc;
//

//    public SaveAttandanceRequest( String companyId , String personId , String fName , String mName ,
//                                  String lName , String date ,String time ,String desc ) {
//        super(Object.class, RestServiceInterface.class);
//
//        this.companyId = companyId;
//        this.personId = personId;
//        this.fName = fName;
//        this.mName = mName;
//        this.lName = lName;
//        this.date = date;
//        this.time = time;
//        this.desc = desc;
//
//    }

    private String cardId;
    private String adminId;
    private String date;
    private String time;


    public SaveAttandanceRequest(String adminId , String cardId , String date , String time) {
        super(Object.class, RestServiceInterface.class);
        this.adminId = adminId;
        this.cardId = cardId;
        this.date = date;
        this.time = time;
    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception{
        User user = new User();
        return getService().save(user);
    }
}
