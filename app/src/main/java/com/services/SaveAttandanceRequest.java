package com.services;

import com.google.inject.Singleton;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.inject.ContextSingleton;

/**
 * Created by kanaiyalalt on 11/11/2014.
 */
public class SaveAttandanceRequest extends RetrofitSpiceRequest<Object, RestServiceInterface> {

    private String companyId;
    private String personId;
    private String fName;
    private String mName;
    private String lName;
    private String date;
    private String time;
    private String desc;

    public SaveAttandanceRequest( String companyId , String personId , String fName , String mName ,
                                  String lName , String date ,String time ,String desc ) {
        super(Object.class, RestServiceInterface.class);

        this.companyId = companyId;
        this.personId = personId;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.date = date;
        this.time = time;
        this.desc = desc;

    }

    @Override
    public Object loadDataFromNetwork() throws java.lang.Exception{
        getService().save(companyId , personId , fName , mName , lName , date , time , desc);
        return null;
    }
}
