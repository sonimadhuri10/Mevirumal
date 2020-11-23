package com.webbeansvipul.psstore.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.MainActivity;
import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.binding.FragmentDataBindingComponent;
import com.webbeansvipul.psstore.databinding.FragmentProfileBinding;
import com.webbeansvipul.psstore.ui.common.DataBoundListAdapter;
import com.webbeansvipul.psstore.ui.common.PSFragment;
import com.webbeansvipul.psstore.ui.transaction.list.adapter.TransactionListAdapter;
import com.webbeansvipul.psstore.utils.AutoClearedValue;
import com.webbeansvipul.psstore.utils.Constants;
import com.webbeansvipul.psstore.utils.PSDialogMsg;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.transaction.TransactionListViewModel;
import com.webbeansvipul.psstore.viewmodel.user.UserViewModel;
import com.webbeansvipul.psstore.viewobject.TransactionObject;
import com.webbeansvipul.psstore.viewobject.User;
import com.webbeansvipul.psstore.viewobject.common.Resource;

import java.util.List;

/**
 * ProfileFragment
 */
public class ProfileFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;
    private TransactionListViewModel transactionListViewModel;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileBinding> binding;
    private AutoClearedValue<TransactionListAdapter> adapter;


    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileBinding dataBinding = (FragmentProfileBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().transactionList.setNestedScrollingEnabled(false);
        binding.get().editTextView.setOnClickListener(view -> navigationController.navigateToProfileEditActivity(getActivity()));
        binding.get().seeAllTextView.setOnClickListener(view -> navigationController.navigateToTransactionDetailActivity(getActivity()));
        binding.get().userHistoryTextView.setOnClickListener(view -> navigationController.navigateToUserHistoryActivity(getActivity()));
        binding.get().favouriteTextView.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().heartImageView.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().settingTextView.setOnClickListener(view -> navigationController.navigateToSettingActivity(getActivity()));
        binding.get().userTransactionTextView.setOnClickListener(view -> navigationController.navigateToTransactionDetailActivity(getActivity()));




    }

    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel.class);
    }

    @Override
    protected void initAdapters() {

        TransactionListAdapter nvAdapter = new TransactionListAdapter(dataBindingComponent, new TransactionListAdapter.TransactionClickCallback() {
            @Override
            public void onClick(TransactionObject transaction) {

                System.out.print(transaction);

                navigationController.navigateToTransactionDetail(getActivity(), transaction);
            }

            @Override
            public void onCopyClick() {

                psDialogMsg.showSuccessDialog(getString(R.string.copied_to_clipboard), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        },this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().transactionList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        //User
        userViewModel.getUser(loginUserId).observe(this, listResource -> {

//            transactionListViewModel.setTransactionListObj(String.valueOf(Config.LOAD_FROM_DB), String.valueOf(transactionListViewModel.offset), loginUserId);

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            //fadeIn Animation
                            fadeIn(binding.get().getRoot());

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                            replaceUserData(listResource.data);

                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            //fadeIn Animation
                            //fadeIn(binding.get().getRoot());

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                            replaceUserData(listResource.data);
                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        userViewModel.isLoading = false;

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");


            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");

            }
        });

        //Transaction
        transactionListViewModel.setTransactionListObj(String.valueOf(Config.LOAD_FROM_DB),String.valueOf(transactionListViewModel.offset), loginUserId);

        LiveData<Resource<List<TransactionObject>>> news = transactionListViewModel.getTransactionListData();

        if (news != null) {
            news.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            transactionListViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            transactionListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (transactionListViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        transactionListViewModel.forceEndLoading = true;
                    }

                }

            });
        }
    }

    @Override
    public void onDispatched() {

    }

    //endregion
    private void replaceData(List<TransactionObject> transactionList) {
        adapter.get().replace(transactionList);
        binding.get().executePendingBindings();

    }

    private void replaceUserData(User user) {

        binding.get().editTextView.setText(binding.get().editTextView.getText().toString());
        binding.get().userTransactionTextView.setText(binding.get().userTransactionTextView.getText().toString());
        binding.get().userHistoryTextView.setText(binding.get().userHistoryTextView.getText().toString());
        binding.get().favouriteTextView.setText(binding.get().favouriteTextView.getText().toString());
        binding.get().settingTextView.setText(binding.get().settingTextView.getText().toString());
        binding.get().ordersTextView.setText(binding.get().ordersTextView.getText().toString());
        binding.get().seeAllTextView.setText(binding.get().seeAllTextView.getText().toString());
        binding.get().joinedDateTitleTextView.setText(binding.get().joinedDateTitleTextView.getText().toString());
        binding.get().joinedDateTextView.setText(user.addedDate);
        binding.get().nameTextView.setText(user.userName);
        binding.get().phoneTextView.setText(user.userPhone);
        binding.get().statusTextView.setText(user.userAboutMe);
        binding.get().referralCode.setText("Referral Code : "+user.referral_code);

        if(user.reward_points!=null)
        {
            binding.get().pointValue.setText("Your Rewards : "+user.reward_points);
        }

        binding.get().tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.webbeansvipul.psstore&hl=en_IN\n"+"Share Your Referral Code With others to win Rewards!\n"+user.referral_code);
                startActivity(Intent.createChooser(intent, "Share Your Referral Code With others to win Rewards!"));

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE__PROFILE_FRAGMENT
                && resultCode == Constants.RESULT_CODE__LOGOUT_ACTIVATED) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                //navigationController.navigateToUserFBRegister((MainActivity) getActivity());
                navigationController.navigateToUserLogin((MainActivity) getActivity());
            }
        }

    }

}
