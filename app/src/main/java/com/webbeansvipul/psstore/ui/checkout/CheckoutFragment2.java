package com.webbeansvipul.psstore.ui.checkout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonElement;
import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.api.ApiClient;
import com.webbeansvipul.psstore.api.PSApiService;
import com.webbeansvipul.psstore.binding.FragmentDataBindingComponent;
import com.webbeansvipul.psstore.databinding.CheckoutFragment2Binding;
import com.webbeansvipul.psstore.ui.checkout.adapter.ShippingMethodsAdapter;
import com.webbeansvipul.psstore.ui.common.DataBoundListAdapter;
import com.webbeansvipul.psstore.ui.common.PSFragment;
import com.webbeansvipul.psstore.utils.AutoClearedValue;
import com.webbeansvipul.psstore.utils.Constants;
import com.webbeansvipul.psstore.utils.PSDialogMsg;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.coupondiscount.CouponDiscountViewModel;
import com.webbeansvipul.psstore.viewmodel.product.BasketViewModel;
import com.webbeansvipul.psstore.viewmodel.shippingmethod.ShippingMethodViewModel;
import com.webbeansvipul.psstore.viewobject.Basket;
import com.webbeansvipul.psstore.viewobject.ShippingCostContainer;
import com.webbeansvipul.psstore.viewobject.ShippingMethod;
import com.webbeansvipul.psstore.viewobject.ShippingProductContainer;
import com.webbeansvipul.psstore.viewobject.common.Status;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.webbeansvipul.psstore.utils.Utils.isCheckReferral;

public class CheckoutFragment2 extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private BasketViewModel basketViewModel;
    private ShippingMethodViewModel shippingMethodViewModel;
    private CouponDiscountViewModel couponDiscountViewModel;

    @VisibleForTesting
    private AutoClearedValue<CheckoutFragment2Binding> binding;
    private AutoClearedValue<ShippingMethodsAdapter> adapter;

    private PSDialogMsg psDialogMsg;

    String referral_value = "";
    String youTotalRewardPoints = "";
    String razorpayCharge = "";
    String main_price = "";


    SharedPreferences prefs;
    SharedPreferences.Editor editor ;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isCheckReferral = false;

         prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
          editor = prefs.edit();


        editor.putString("couponcode", "");
        editor.commit();

        CheckoutFragment2Binding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_2, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);
        getReferalCode();

        if (getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
            ((CheckoutActivity) getActivity()).progressDialog.setCancelable(false);
        }

        binding.get().couponDiscountButton.setOnClickListener(v -> {
            if ((CheckoutFragment2.this.getActivity()) != null) {
                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.couponDiscountText = binding.get().couponDiscountValueEditText.getText().toString();
            }
            couponDiscountViewModel.setCouponDiscountObj(binding.get().couponDiscountValueEditText.getText().toString());

            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                ((CheckoutActivity) getActivity()).progressDialog.setMessage(getString(R.string.check_coupon));
                ((CheckoutActivity) getActivity()).progressDialog.show();
            }


        });

        if (!overAllTaxLabel.isEmpty()) {
            binding.get().overAllTaxTextView.setText(getString(R.string.tax, overAllTaxLabel));
        }

        if (!shippingTaxLabel.isEmpty()) {
            binding.get().shippingTaxTextView.setText(getString(R.string.shipping_tax, shippingTaxLabel));
        }


        binding.get().refCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                 @Override
                                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                     if (isChecked) {

                                                                         try {
                                                                             isCheckReferral = true;
                                                                             float ttl_amt = 0;
                                                                             float a = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);
                                                                             String origpoint = binding.get().finalTotalValueTextView.getText().toString();

                                                                             if (Float.valueOf(origpoint) >= a) {
                                                                                 ttl_amt = Float.valueOf(origpoint) - a;
                                                                             } else {
                                                                                 ttl_amt = 0;
                                                                             }
                                                                             binding.get().finalTotalValueTextView.setText(String.valueOf(ttl_amt));
                                                                         } catch (Exception e) {
                                                                             e.printStackTrace();
                                                                         }

                                                                     } else {
                                                                         try {

                                                                             isCheckReferral = false;
                                                                             updateUI();
                                                                         } catch (Exception e) {
                                                                             e.printStackTrace();
                                                                         }

                                                                     }
                                                                 }
                                                             }
        );
    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        shippingMethodViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShippingMethodViewModel.class);
        couponDiscountViewModel = ViewModelProviders.of(this, viewModelFactory).get(CouponDiscountViewModel.class);
    }

    @Override
    protected void initAdapters() {

        if (getActivity() != null) {

            ShippingMethodsAdapter nvAdapter = new ShippingMethodsAdapter(dataBindingComponent, shippingMethod -> {

                if (CheckoutFragment2.this.getActivity() != null) {

                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shippingMethodName = shippingMethod.name;
                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId = shippingMethod.id;

                    CheckoutFragment2.this.calculateTheBalance();
                }
            }, shippingId, ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId);

            adapter = new AutoClearedValue<>(this, nvAdapter);
            binding.get().shippingMethodsRecyclerView.setAdapter(adapter.get());
        }

    }

    @Override
    protected void initData() {

        if (shopNoShippingEnable.equals(Constants.ONE)) {
            binding.get().shippingMethodsCardView.setVisibility(View.GONE);
        }

        if (shopStandardShippingEnable.equals(Constants.ONE)) {
            binding.get().shippingMethodsCardView.setVisibility(View.VISIBLE);
            shippingMethodViewModel.setShippingMethodsObj();
        }

        shippingMethodViewModel.getshippingCostByCountryAndCityData().observe(this, result -> {

            if (result != null) {
                if (result.status == Status.SUCCESS) {
//                    progressDialog.get().cancel();
                    if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                        ((CheckoutActivity) getActivity()).progressDialog.hide();
                    }

                    if (result.data != null) {

                        if (CheckoutFragment2.this.getActivity() != null) {
                            ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shippingMethodName = result.data.shippingZone.shippingZonePackageName;
                            ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Float.parseFloat(result.data.shippingZone.shippingCost);

                            calculateTheBalance();
                        }
                    }
                } else if (result.status == Status.ERROR) {
                    if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                        ((CheckoutActivity) getActivity()).progressDialog.hide();
                    }

                }
            }

        });

        shippingMethodViewModel.getShippingMethodsData().observe(this, result -> {

            if (result.data != null) {
                switch (result.status) {

                    case SUCCESS:
                        CheckoutFragment2.this.replaceShippingMethods(result.data);

                        for (ShippingMethod shippingMethod : result.data) {

                            if ((CheckoutFragment2.this.getActivity()) != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id.equals(shippingId) && ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId.isEmpty()) {
                                        if (CheckoutFragment2.this.getActivity() != null) {
                                            if (shopNoShippingEnable.equals(Constants.ONE)) {
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = 0;
                                            } else {
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                                            }
                                            CheckoutFragment2.this.calculateTheBalance();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;

                    case LOADING:
                        CheckoutFragment2.this.replaceShippingMethods(result.data);

                        for (ShippingMethod shippingMethod : result.data) {

                            if ((CheckoutFragment2.this.getActivity()) != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id.equals(shippingId) && ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId.isEmpty()) {
                                        if (CheckoutFragment2.this.getActivity() != null) {
                                            if (shopNoShippingEnable.equals(Constants.ONE))
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = 0;
                                            else {
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                                            }
                                            CheckoutFragment2.this.calculateTheBalance();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        });

        couponDiscountViewModel.getCouponDiscountData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case ERROR:

                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

                        psDialogMsg.showErrorDialog(getString(R.string.error_coupon), getString(R.string.app__ok));
                        psDialogMsg.show();

                        break;

                    case SUCCESS:

                        if (result.data != null) {

                            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                                ((CheckoutActivity) getActivity()).progressDialog.hide();
                            }

                            editor.putString("couponcode", binding.get().couponDiscountValueEditText.getText().toString());
                            editor.putString("userid", CheckoutActivity.user_id);
                            editor.commit();
                            psDialogMsg.showSuccessDialog(getString(R.string.checkout_detail__claimed_coupon), getString(R.string.app__ok));
                            psDialogMsg.show();

                            try {
                                if (getActivity() != null) {
                                    ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount = Utils.round(Float.parseFloat(result.data.couponAmount), 2);
                                    Utils.psLog("coupon5" + ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount + "");
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            calculateTheBalance();
                        }

                        break;
                }
            }
        });

        basketViewModel.setBasketListWithProductObj();
        basketViewModel.getAllBasketWithProductList().observe(this, baskets -> {
            if (baskets != null && baskets.size() > 0) {

                if (getActivity() != null) {
                    ((CheckoutActivity) getActivity()).transactionValueHolder.resetValues();

                    for (Basket basket : baskets) {

                        ((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count += basket.count;
                        ((CheckoutActivity) getActivity()).transactionValueHolder.total += Utils.round((basket.basketOriginalPrice) * basket.count, 2);

                        ((CheckoutActivity) getActivity()).transactionValueHolder.discount += Utils.round(basket.product.discountAmount * basket.count, 2);
                        ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol = basket.product.currencySymbol;


                        ShippingProductContainer shippingProductContainer = new ShippingProductContainer(
                                basket.product.id,
                                basket.count
                        );
                        shippingMethodViewModel.shippingProductContainer.add(shippingProductContainer);

                    }

                    if (shopZoneShippingEnable.equals(Constants.ONE)) {
                        binding.get().shippingMethodsCardView.setVisibility(View.GONE);

                        if (getActivity() != null) {
                            //progressDialog.get().show();
                            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                                ((CheckoutActivity) getActivity()).progressDialog.show();
                                Utils.psLog(((CheckoutActivity) getActivity()).user.country.id + " - " + ((CheckoutActivity) getActivity()).user.city.id + " - " + shopId);


                                shippingMethodViewModel.setshippingCostByCountryAndCityObj(new ShippingCostContainer(
                                        ((CheckoutActivity) getActivity()).user.country.id, ((CheckoutActivity) getActivity()).user.city.id, shopId,
                                        shippingMethodViewModel.shippingProductContainer));
                            }
                        }
                    }
                }
                calculateTheBalance();
            }
        });

        basketViewModel.getWholeBasketDeleteData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    Utils.psLog("Success");
                } else if (result.status == Status.ERROR) {
                    Utils.psLog("Fail");
                }
            }
        });
    }

    private void replaceShippingMethods(List<ShippingMethod> shippingMethods) {
        this.adapter.get().replace(shippingMethods);
        this.binding.get().executePendingBindings();
    }

    private void calculateTheBalance() {

        if (getActivity() != null) {

            ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.total - (((CheckoutActivity) getActivity()).transactionValueHolder.discount), 2);

            if (((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount > 0) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total - ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount, 2);
            }

            if (!overAllTax.isEmpty()) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.tax = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total * Float.parseFloat(overAllTax), 2);
            }

            if (!shippingTax.isEmpty() && ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost > 0) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost * Float.parseFloat(shippingTax), 2);
            }

            ((CheckoutActivity) getActivity()).transactionValueHolder.final_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total + ((CheckoutActivity) getActivity()).transactionValueHolder.tax +
                    ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax + ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost, 2);
            updateUI();
        }

    }

    private void updateUI() {

        if (getActivity() != null) {

            if (((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count > 0) {
                binding.get().totalItemCountValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.total > 0) {
                String totalValueString = ((CheckoutActivity) getActivity()).
                        transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.total);
                binding.get().totalValueTextView.setText(totalValueString);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount > 0) {
                String couponDiscountValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount);
                binding.get().couponDiscountValueTextView.setText(couponDiscountValueString);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.discount > 0) {
                String discountValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.discount);
                binding.get().discountValueTextView.setText(discountValueString);
            }

            if (!((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText.isEmpty()) {
                String couponDiscountValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText;
                binding.get().couponDiscountValueEditText.setText(couponDiscountValueString);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.sub_total > 0) {
                String subTotalValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total);
                binding.get().subtotalValueTextView.setText(subTotalValueString);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.tax > 0) {
                String taxValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.tax);
                binding.get().taxValueTextView.setText(taxValueString);
            }

            if (!shippingTax.equals("0.0") && !shippingTax.equals(Constants.RATING_ZERO)) {
                String shippingTaxValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(Utils.round((((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax), 2));
                binding.get().shippingTaxValueTextView.setText(shippingTaxValueString);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.final_total > 0.0) {
                String finalTotalValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.final_total);


                binding.get().finalTotalValueTextView.setText(finalTotalValueString);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost > 0) {
                String shippingCostValueString = ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost);
                binding.get().shippingCostValueTextView.setText(shippingCostValueString);
            }

            try {

                float a = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);
                binding.get().refCheckBox.setText("Use Your Referral Points " + "(Rs" + String.format("%.2f", a) + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void getReferalCode() {
        Context context = getContext();
        try {
            PSApiService apiRequests = ApiClient.getInstance().create(PSApiService.class);
            Call<JsonElement> aboutCall = apiRequests.getReferralCode(Config.API_KEY, pref.getString(Constants.USER_ID, Constants.EMPTY_STRING));
            aboutCall.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        referral_value = jsonObject.getString("referral_value");
                        youTotalRewardPoints = jsonObject.getString("youTotalRewardPoints");
                        razorpayCharge = jsonObject.getString("razorpayCharge");
                        float a = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);
                        binding.get().refCheckBox.setText("Use Your Referral Points " + "(Rs" + String.format("%.2f", a) + ")");
                        try
                        {
                            binding.get().referralDiscount.setText(String.valueOf(a));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Toast.makeText(context, "Oops Went Something wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Oops Went Something wrong...", Toast.LENGTH_SHORT).show();
        }
    }


}
