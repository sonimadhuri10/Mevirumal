package com.webbeansvipul.psstore.repository.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.webbeansvipul.psstore.api.PSApiService;
import com.webbeansvipul.psstore.db.PSCoreDb;
import com.webbeansvipul.psstore.viewobject.User;
import com.webbeansvipul.psstore.viewobject.common.Resource;


/**
 * General Delete Task Sample
 * Created by Panacea-Soft on 12/14/17.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class DeleteTask implements Runnable {


    //region Variables

    private final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

    public final PSApiService service;
    private final PSCoreDb db;
    private final Object obj;

    //endregion


    //region Constructor

    DeleteTask(PSApiService service, PSCoreDb db, Object obj) {
        this.service = service;
        this.db = db;
        this.obj = obj;
    }

    //endregion


    //region Override Methods

    @Override
    public void run() {
        try {
            try{
                db.beginTransaction();

                if(obj instanceof User) {
                    db.userDao().deleteUserLogin();

                    db.productDao().deleteAllFavouriteProducts();

                    db.transactionDao().deleteAllTransactionList();

                    db.transactionOrderDao().deleteAll();

                    db.basketDao().deleteAllBasket();

                    db.historyDao().deleteHistoryProduct();

                    db.setTransactionSuccessful();

                }

            }finally {
                db.endTransaction();
            }
            statusLiveData.postValue(Resource.success(true));
        } catch (Exception e) {
            statusLiveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    //endregion


    //region public SyncCategory Methods

    /**
     * This function will return Status of Process
     * @return statusLiveData
     */
    public LiveData<Resource<Boolean>> getStatusLiveData() { return statusLiveData; }

    //endregion
}