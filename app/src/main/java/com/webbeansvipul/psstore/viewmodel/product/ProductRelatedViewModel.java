package com.webbeansvipul.psstore.viewmodel.product;

import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.repository.product.ProductRepository;
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

public class ProductRelatedViewModel extends PSViewModel {
    //for product detail list

    private final LiveData<Resource<List<Product>>> productRelatedData;
    private MutableLiveData<ProductRelatedViewModel.TmpDataHolder> productRelatedListObj = new MutableLiveData<>();

    //region Constructor

    @Inject
    ProductRelatedViewModel(ProductRepository productRepository) {
        //  product detail List
        productRelatedData = Transformations.switchMap(productRelatedListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("ProductRelatedViewModel.");
            return productRepository.getRelatedList(Config.API_KEY, obj.productId, obj.catId);
        });

    }

    //endregion
    //region Getter And Setter for product detail List

    public void setProductRelatedListObj(String productId, String catId) {
        if (!isLoading) {
            ProductRelatedViewModel.TmpDataHolder tmpDataHolder = new ProductRelatedViewModel.TmpDataHolder();
            tmpDataHolder.productId = productId;
            tmpDataHolder.catId = catId;
            productRelatedListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Product>>> getProductRelatedData() {
        return productRelatedData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String offset = "";
        public String productId = "";
        public String catId = "";
        public String shopId = "";
        public Boolean isConnected = false;
    }
    //endregion
}
