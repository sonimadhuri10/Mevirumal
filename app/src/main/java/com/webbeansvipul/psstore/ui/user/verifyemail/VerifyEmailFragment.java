package com.webbeansvipul.psstore.ui.user.verifyemail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.webbeansvipul.psstore.MainActivity;
import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.binding.FragmentDataBindingComponent;
import com.webbeansvipul.psstore.ui.common.DataBoundListAdapter;
import com.webbeansvipul.psstore.ui.common.PSFragment;
import com.webbeansvipul.psstore.utils.AutoClearedValue;
import com.webbeansvipul.psstore.utils.PSDialogMsg;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.user.UserViewModel;
import com.webbeansvipul.psstore.viewobject.UserLogin;
import com.webbeansvipul.psstore.viewobject.common.Resource;

public class VerifyEmailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables
    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    @VisibleForTesting
    private AutoClearedValue<com.webbeansvipul.psstore.databinding.FragmentVerifyEmailBinding> binding;

//endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.webbeansvipul.psstore.databinding.FragmentVerifyEmailBinding dataBinding;
        dataBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

//        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
//            ((MainActivity) getActivity()).updateMenuIconWhite();
//            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
//
//        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().emailTextView.setText(userEmailToVerify);

        binding.get().submitButton.setOnClickListener(v -> {
            if (validateInput()) {
                binding.get().submitButton.setEnabled(false);
                binding.get().submitButton.setText(getResources().getString(R.string.message__loading));
                userViewModel.setEmailVerificationUser(Utils.checkUserId(userIdToVerify), binding.get().enterCodeEditText.getText().toString());
            } else {
                Toast.makeText(getContext(), getString(R.string.verify_email__enter_code), Toast.LENGTH_SHORT).show();
            }
        });

        binding.get().resentCodeButton.setOnClickListener(v -> userViewModel.setResentVerifyCodeObj(userEmailToVerify));

        binding.get().changeEmailButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                navigationController.navigateToUserRegister((MainActivity) getActivity());
            } else {
                if(getActivity() != null) {
                    navigationController.navigateToUserRegisterActivity(getActivity());
                    getActivity().finish();
                }
            }
        });
    }

    private boolean validateInput() {
        return !binding.get().enterCodeEditText.getText().toString().isEmpty();
    }

    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        LiveData<Resource<UserLogin>> itemList = userViewModel.getEmailVerificationUser();

        if (itemList != null) {

            itemList.observe(this, listResource -> {
                if (listResource != null) {

                    binding.get().submitButton.setEnabled(true);
                    binding.get().submitButton.setText(getResources().getString(R.string.verify_email__submit));
                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

//                            if (listResource.data != null) {
//                                //fadeIn Animation
//                                fadeIn(binding.get().getRoot());
//
//                                // Update the data
//                                Toast.makeText(getContext(), "Loading Loading", Toast.LENGTH_SHORT).show();
//
//                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                try {
                                    if (getActivity() != null) {
                                        Utils.updateUserLoginData(pref, listResource.data.user);
                                        Utils.navigateAfterUserLogin(getActivity(),navigationController);

                                    }

                                } catch (NullPointerException ne) {
                                    Utils.psErrorLog("Null Pointer Exception.", ne);
                                } catch (Exception e) {
                                    Utils.psErrorLog("Error in getting notification flag data.", e);
                                }


                            }

                            break;

                        case ERROR:
                            // Error State
                            psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
//                            psDialogMsg.showErrorDialog(getString(R.string.error_message__code_not_verify), getString(R.string.app__ok));
                            psDialogMsg.show();

                            break;
                        default:
                            // Default

                            break;
                    }

                }

            });
        }


        //For resent code
        userViewModel.getResentVerifyCodeData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "Success Success", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Fail resent code again", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


}


