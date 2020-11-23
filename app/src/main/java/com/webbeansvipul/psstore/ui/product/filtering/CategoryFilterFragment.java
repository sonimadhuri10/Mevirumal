package com.webbeansvipul.psstore.ui.product.filtering;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.binding.FragmentDataBindingComponent;
import com.webbeansvipul.psstore.databinding.TypeFilterBinding;
import com.webbeansvipul.psstore.ui.common.DataBoundListAdapter;
import com.webbeansvipul.psstore.ui.common.PSFragment;
import com.webbeansvipul.psstore.ui.product.filtering.adapter.CategoryAdapter;
import com.webbeansvipul.psstore.utils.AutoClearedValue;
import com.webbeansvipul.psstore.utils.Constants;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.category.CategoryViewModel;
import com.webbeansvipul.psstore.viewmodel.subcategory.SubCategoryViewModel;
import com.webbeansvipul.psstore.viewobject.Category;
import com.webbeansvipul.psstore.viewobject.SubCategory;
import com.webbeansvipul.psstore.viewobject.common.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class CategoryFilterFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface{

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private CategoryViewModel categoryViewModel;
    private SubCategoryViewModel subCategoryViewModel;
    private String catId,subCatId,subcatname;
    public Intent intent = new Intent();

    @VisibleForTesting
    private AutoClearedValue<TypeFilterBinding> binding;
    private AutoClearedValue<CategoryAdapter> adapter;
    private AutoClearedValue<List<Category>> lastCategoryData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        TypeFilterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.type_filter, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

       // initToolBar();

    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.clear_button,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.clear)
        {
            this.catId = "";
            this.subCatId = "";
            this.subcatname = "";

            initializeAdapter();

            initData();

            navigationController.navigateBackToHomeFeaturedFragment(CategoryFilterFragment.this.getActivity(), this.catId, this.subCatId,this.subcatname);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewModels() {
        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
        subCategoryViewModel = ViewModelProviders.of(this,viewModelFactory).get(SubCategoryViewModel.class);

    }

    @Override
    protected void initAdapters() {

        try {
            if(getActivity() != null) {

                intent = getActivity().getIntent();

                this.catId = intent.getStringExtra(Constants.CATEGORY_ID);
                this.subCatId = intent.getStringExtra(Constants.SUBCATEGORY_ID);
                this.subcatname = intent.getStringExtra(Constants.SUBCATEGORY_NAME);

            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        initializeAdapter();
    }

    private void initializeAdapter()
    {
        CategoryAdapter nvAdapter = new CategoryAdapter(dataBindingComponent, (catId, subCatId ,subcatname) -> {

            CategoryFilterFragment.this.assignCategoryId(catId, subCatId,subcatname);

            navigationController.navigateBackToHomeFeaturedFragment(CategoryFilterFragment.this.getActivity(), catId, subCatId , subcatname);

            if(getActivity()!= null)
            {
                CategoryFilterFragment.this.getActivity().finish();
            }

        },this.catId,this.subCatId,this.subcatname);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().CategoryList.setAdapter(nvAdapter);
    }

    private void assignCategoryId(String catId,String subCatId,String subcatname)
    {
        this.catId = catId;
        this.subCatId = subCatId;
        this.subcatname = subcatname;

    }

    @Override
    protected void initData() {

        categoryViewModel.setCategoryListObj(loginUserId, categoryViewModel.categoryParameterHolder, String.valueOf(Config.LIST_CATEGORY_COUNT),String.valueOf(categoryViewModel.offset));
        subCategoryViewModel.setAllSubCategoryListObj();

        LiveData<Resource<List<Category>>> categories = categoryViewModel.getCategoryListData();
        LiveData<Resource<List<SubCategory>>> subCategories = subCategoryViewModel.getAllSubCategoryListData();


        if (categories != null) {

            categories.observe(this, listResource -> {
                if (listResource != null) {

                    if(listResource.data != null && listResource.data.size() > 0) {

                        lastCategoryData = new AutoClearedValue<>(this, listResource.data);
                        replaceCategory(lastCategoryData.get());

                    }

                } else {

                    // Init Object or Empty Data

                    if (categoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        categoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        if (subCategories != null) {

            subCategories.observe(this, listResource -> {
                if (listResource != null) {


                    if (listResource.data != null && listResource.data.size() > 0) {

                        replaceSubCategory(listResource.data);
                    }



                } else {

                    // Init Object or Empty Data

                    if (subCategoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        subCategoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

    }


    private void replaceCategory(List<Category> CategoryList) {

        adapter.get().replaceCategory(CategoryList);
        adapter.get().notifyDataSetChanged();
        binding.get().executePendingBindings();

    }

    private void replaceSubCategory(List<SubCategory> subCategoryList) {

        adapter.get().replaceSubCategory(subCategoryList);
        adapter.get().notifyDataSetChanged();
        binding.get().executePendingBindings();

    }

    @Override
    public void onDispatched() {

//      if  (categoryViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
////            if (binding.get().CategoryList != null) {
////
////                LinearLayoutManager layoutManager = (LinearLayoutManager)
////                        binding.get().CategoryList.getLayoutManager();
////
////
////                if (layoutManager != null) {
////                    layoutManager.scrollToPosition(0);
////                }
////            }
//        }
//
        }
}
