package com.webbeansvipul.psstore.viewmodel.collection;

import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.repository.collection.ProductCollectionRepository;
import com.webbeansvipul.psstore.utils.AbsentLiveData;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.common.PSViewModel;
import com.webbeansvipul.psstore.viewobject.Product;
import com.webbeansvipul.psstore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductCollectionProductViewModel extends PSViewModel {


    // for ProductCollectionHeader

    private final LiveData<Resource<List<Product>>> productCollectionProductListData;
    private MutableLiveData<ProductCollectionProductViewModel.TmpDataHolder> productCollectionProductListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageLoadingStateData;
    private MutableLiveData<ProductCollectionProductViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    //region Constructor

    @Inject
    ProductCollectionProductViewModel(ProductCollectionRepository repository) {
        Utils.psLog("Inside ProductViewModel");


        // Latest ProductCollectionHeader List
        productCollectionProductListData = Transformations.switchMap(productCollectionProductListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getProductCollectionProducts(Config.API_KEY, obj.limit, obj.offset, obj.id);
        });

        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductCollectionProduct(obj.limit, obj.offset, obj.id);
        });


    }

    //endregion

    //region ProductCollectionHeader

    // Get ProductCollectionHeader
    public void setProductCollectionProductListObj(String limit, String offset, String id) {
        if (!isLoading) {
            ProductCollectionProductViewModel.TmpDataHolder tmpDataHolder = new ProductCollectionProductViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.id = id;
            productCollectionProductListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Product>>> getProductCollectionProductListData() {
        return productCollectionProductListData;
    }

    //Get Latest ProductCollectionHeader Next Page
    public void setNextPageLoadingStateObj(String limit, String offset, String id) {

        if (!isLoading) {
            ProductCollectionProductViewModel.TmpDataHolder tmpDataHolder = new ProductCollectionProductViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.id = id;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageLoadingStateData;
    }

    //endregion

    //region Holder

    class TmpDataHolder {

        public String offset = "";
        String limit = "";
        String id = "";
        public Boolean isConnected = false;
        public String shopId = "";

    }

    //endregion
}
