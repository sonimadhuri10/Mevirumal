package com.webbeansvipul.psstore.viewmodel.country;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.repository.country.CountryRepository;
import com.webbeansvipul.psstore.utils.AbsentLiveData;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.common.PSViewModel;
import com.webbeansvipul.psstore.viewobject.Country;
import com.webbeansvipul.psstore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class CountryViewModel extends PSViewModel {
    private LiveData<Resource<List<Country>>> countryListData;
    private MutableLiveData<CountryViewModel.TmpDataHolder> countryListObj = new MutableLiveData<>();

    private LiveData<Resource<Boolean>> nextPageCountryListData;
    private MutableLiveData<CountryViewModel.TmpDataHolder> nextPageCountryListObj = new MutableLiveData<>();

    @Inject
    CountryViewModel(CountryRepository repository) {
        Utils.psLog("Inside CountryViewModel");

        countryListData = Transformations.switchMap(countryListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getCountryListWithShopId(Config.API_KEY, obj.shopId, obj.limit, obj.offset);
        });

        nextPageCountryListData = Transformations.switchMap(nextPageCountryListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageCityListWithShopId(Config.API_KEY, obj.shopId, obj.limit, obj.offset);
        });
    }

    public void setCountryListObj(String shopId, String limit, String offset) {
        CountryViewModel.TmpDataHolder tmpDataHolder = new CountryViewModel.TmpDataHolder();
        tmpDataHolder.shopId = shopId;
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;

        countryListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Country>>> getCountryListData() {

        return countryListData;
    }

    public void setNextPageCountryListObj(String shopId, String limit, String offset) {
        CountryViewModel.TmpDataHolder tmpDataHolder = new CountryViewModel.TmpDataHolder();
        tmpDataHolder.shopId = shopId;
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;

        nextPageCountryListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageCountryListData() {

        return nextPageCountryListData;
    }

    class TmpDataHolder {
        public String loginUserId = "";
        public String limit = "";
        public String offset = "";
        public String shopId = "";

    }
}
