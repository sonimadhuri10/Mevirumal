package com.webbeansvipul.psstore.ui.checkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.api.ApiClient;
import com.webbeansvipul.psstore.api.PSApiService;
import com.webbeansvipul.psstore.binding.FragmentDataBindingComponent;
import com.webbeansvipul.psstore.databinding.CheckoutFragment3Binding;
import com.webbeansvipul.psstore.ui.common.DataBoundListAdapter;
import com.webbeansvipul.psstore.ui.common.PSFragment;
import com.webbeansvipul.psstore.utils.AutoClearedValue;
import com.webbeansvipul.psstore.utils.Constants;
import com.webbeansvipul.psstore.utils.PSDialogMsg;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewmodel.paypal.PaypalViewModel;
import com.webbeansvipul.psstore.viewmodel.product.BasketViewModel;
import com.webbeansvipul.psstore.viewmodel.shop.ShopViewModel;
import com.webbeansvipul.psstore.viewmodel.transaction.TransactionListViewModel;
import com.webbeansvipul.psstore.viewobject.Basket;
import com.webbeansvipul.psstore.viewobject.BasketProductListToServerContainer;
import com.webbeansvipul.psstore.viewobject.BasketProductToServer;
import com.webbeansvipul.psstore.viewobject.TransactionHeaderUpload;
import com.webbeansvipul.psstore.viewobject.User;
import com.webbeansvipul.psstore.viewobject.common.Status;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment3 extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface, PaymentResultListener {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    public String razorpayCharge = "";
    public String youTotalRewardPoints = "";
    public String referral_value = "";
    public Boolean isCheck = false;
    public String is_referral_points = "0";
    AutoClearedValue<CheckoutFragment3Binding> binding;
    String paymentMethod = Constants.PAYMENT_CASH_ON_DELIVERY;
    //    private ProgressDialog progressDialog;
    boolean clicked = false;
    private PaypalViewModel paypalViewModel;
    private BasketViewModel basketViewModel;
    private TransactionListViewModel transactionListViewModel;
    private ShopViewModel shopViewModel;
    private User user;
    private List<Basket> basketList;
    private BasketProductListToServerContainer basketProductListToServerContainer = new BasketProductListToServerContainer();
    private String clientTokenString;
    private String payment_method_nonce;
    private CardView oldCardView;
    private TextView oldTextView;
    private ImageView oldImageView;
    private PSDialogMsg psDialogMsg;

    String name="unknown";
    String desc="Welcome In Razorpay";
    String pay="1";
    String email="unknown@gmail.com";
    String contact="9838109180";

    private Button buttonConfirmOrder;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CheckoutFragment3Binding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_3, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        Config.refDiscount = "";

        getReferalCode();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();

        return binding.get().getRoot();
    }


    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        binding.get().cashImageView.setColorFilter(getResources().getColor(R.color.md_grey_500));

        if (getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.setMessage((Utils.getSpannableString(getContext(), getString(R.string.com_facebook_loading), Utils.Fonts.MM_FONT)));
            ((CheckoutActivity) getActivity()).progressDialog.setCancelable(false);
        }

        binding.get().cashCardView.setOnClickListener(v -> {

            if (!clicked) {
                clicked = true;

                oldCardView = binding.get().cashCardView;
                oldImageView = binding.get().cashImageView;
                oldTextView = binding.get().cashTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);
            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().cashCardView, binding.get().cashTextView, binding.get().cashImageView);

                    oldCardView = binding.get().cashCardView;
                    oldImageView = binding.get().cashImageView;
                    oldTextView = binding.get().cashTextView;
                }
            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__COD);

            paymentMethod = Constants.PAYMENT_CASH_ON_DELIVERY;

        });

        binding.get().cardCardView.setOnClickListener(v -> {

            if (!clicked) {
                clicked = true;

                oldCardView = binding.get().cardCardView;
                oldImageView = binding.get().cardImageView;
                oldTextView = binding.get().cardTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);
            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().cardCardView, binding.get().cardTextView, binding.get().cardImageView);

                    oldCardView = binding.get().cardCardView;
                    oldImageView = binding.get().cardImageView;
                    oldTextView = binding.get().cardTextView;
                }
            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__STRIPE);
            paymentMethod = Constants.PAYMENT_STRIPE;

        });

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);

        binding.get().paypalCardView.setOnClickListener(v -> {

            if (!clicked) {
                clicked = true;

                oldCardView = binding.get().paypalCardView;
                oldImageView = binding.get().paypalImageView;
                oldTextView = binding.get().paypalTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);
            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().paypalCardView, binding.get().paypalTextView, binding.get().paypalImageView);

                    oldCardView = binding.get().paypalCardView;
                    oldImageView = binding.get().paypalImageView;
                    oldTextView = binding.get().paypalTextView;
                }
            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__PAYPAL);
            paymentMethod = Constants.PAYMENT_PAYPAL;

        });

        binding.get().bankCardView.setOnClickListener(v -> {
            if (!clicked) {

                clicked = true;

                oldCardView = binding.get().bankCardView;
                oldImageView = binding.get().bankImageView;
                oldTextView = binding.get().bankTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);

            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().bankCardView, binding.get().bankTextView, binding.get().bankImageView);

                    oldCardView = binding.get().bankCardView;
                    oldImageView = binding.get().bankImageView;
                    oldTextView = binding.get().bankTextView;
                }

            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__PAYPAL);
            paymentMethod = Constants.PAYMENT_BANK;

        });


        binding.get().razorPayView.setOnClickListener(v -> {
            if (!clicked) {

                clicked = true;

                oldCardView = binding.get().razorPayView;
                oldImageView = binding.get().razorPayImageView;
                oldTextView = binding.get().razorPayTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);

            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().razorPayView, binding.get().razorPayTextView, binding.get().razorPayImageView);
                    oldCardView = binding.get().razorPayView;
                    oldImageView = binding.get().razorPayImageView;
                    oldTextView = binding.get().razorPayTextView;
                }

            }

            binding.get().warningTitleTextView.setText("Checkout from Razorpay");
            paymentMethod = Constants.PAYMENT_RAZORPAY;

        });


        if (cod.equals(Constants.ONE)) {
            binding.get().cashCardView.setVisibility(View.VISIBLE);

            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }

        if (paypal.equals(Constants.ONE)) {
            binding.get().paypalCardView.setVisibility(View.VISIBLE);
            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }

        if (stripe.equals(Constants.ONE)) {
            binding.get().cardCardView.setVisibility(View.VISIBLE);
            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }


        //binding.get().razorPayView.setVisibility(View.VISIBLE);

        if (razorpay.equals(Constants.ONE)) {
            binding.get().razorPayView.setVisibility(View.VISIBLE);
            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }


    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel.class);
        paypalViewModel = ViewModelProviders.of(this, viewModelFactory).get(PaypalViewModel.class);
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    void sendData() {

        if (getActivity() != null) {
            user = ((CheckoutActivity) CheckoutFragment3.this.getActivity()).getCurrentUser();
            if (user.city.id.isEmpty()) {
                psDialogMsg.showErrorDialog(getString(R.string.error_message__select_city), getString(R.string.app__ok));
                psDialogMsg.show();
                return;
            } else if (user.shippingAddress1.isEmpty()) {
                psDialogMsg.showErrorDialog(getString(R.string.shipping_address_one_error_message), getString(R.string.app__ok));
                psDialogMsg.show();
                return;
            } else if (user.billingAddress1.isEmpty()) {
                psDialogMsg.showErrorDialog(getString(R.string.billing_address_one_error_message), getString(R.string.app__ok));
                psDialogMsg.show();
                return;
            }
        }

        if (basketList != null) {

            if (basketList.size() > 0) {

                if (getActivity() != null) {
                    switch (paymentMethod) {

                        case Constants.PAYMENT_PAYPAL:
                            if (Utils.isCheckReferral) {
                                is_referral_points = "1";

                            } else {
                                is_referral_points = "0";

                            }
                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ONE,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            shopZoneShippingEnable,
                                            basketProductListToServerContainer.productList,
                                            "0",
                                            is_referral_points
                                    )
                            );
                            break;

                        case Constants.PAYMENT_RAZORPAY:
                            try {
                                float ttl_sum = 0;
                                if (Utils.isCheckReferral) {
                                    is_referral_points = "1";
                                    float famount = Float.valueOf(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total));
                                    float famountt = famount + ((famount * Float.valueOf(razorpayCharge)) / 100);
                                    float a = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);
                                    if (a >= famountt) {
                                        float aab = famountt;
                                        ttl_sum = aab;
                                    } else {
                                        float aab = famountt - a;
                                        ttl_sum = aab;
                                    }
                                    Config.refDiscount = String.valueOf(a);

                                } else {
                                    is_referral_points = "0";
                                    float famount = Float.valueOf(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total));
                                    float famountt = famount + ((famount * Float.valueOf(razorpayCharge)) / 100);
                                    float aab = famountt;
                                    ttl_sum = aab;
                                }
                                float ab = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);

                                if (ab >= ttl_sum && Utils.isCheckReferral) {
                                    transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                                    user.userId,
                                                    prefs.getString("couponcode",String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText)),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                                    String.valueOf((int) Math.round(ttl_sum)),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                                    user.userName,
                                                    user.userPhone,
                                                    Constants.RATING_ZERO,
                                                    Constants.RATING_ZERO,
                                                    Constants.RATING_ZERO,
                                                    Constants.RATING_ZERO,
                                                    payment_method_nonce,
                                                    Constants.RATING_ONE,
                                                    basketList.get(0).product.currencySymbol,
                                                    basketList.get(0).product.currencyShortForm,
                                                    user.billingFirstName,
                                                    user.billingLastName,
                                                    user.billingCompany,
                                                    user.billingAddress1,
                                                    user.billingAddress2,
                                                    user.billingCountry,
                                                    user.billingState,
                                                    user.billingCity,
                                                    user.billingPostalCode,
                                                    user.billingEmail,
                                                    user.billingPhone,
                                                    user.shippingFirstName,
                                                    user.shippingLastName,
                                                    user.shippingCompany,
                                                    user.shippingAddress1,
                                                    user.shippingAddress2,
                                                    user.shippingCountry,
                                                    user.shippingState,
                                                    user.shippingCity,
                                                    user.shippingPostalCode,
                                                    user.shippingEmail,
                                                    user.shippingPhone,
                                                    shippingTax,
                                                    overAllTax,
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                                    binding.get().memoEditText.getText().toString(),
                                                    String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                                    shopZoneShippingEnable,
                                                    basketProductListToServerContainer.productList,
                                                    "1",
                                                    is_referral_points
                                            )
                                    );
                                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                } else {
                                    int rounded_a = (int) Math.round(ttl_sum);
                                    name = user.userName;
                                    email = user.userEmail;
                                    contact = user.userPhone;
                                    pay = String.valueOf(rounded_a);
                                    startPayment();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            break;

                        case Constants.PAYMENT_STRIPE:

                            if (Utils.isCheckReferral) {
                                is_referral_points = "1";

                            } else {
                                is_referral_points = "0";
                            }
                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ONE,
                                            Constants.RATING_ZERO,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            shopZoneShippingEnable,
                                            basketProductListToServerContainer.productList,
                                            "0",
                                            is_referral_points
                                    )
                            );

                            break;


                        case Constants.PAYMENT_CASH_ON_DELIVERY:
                            float ttl_sums = 0;
                            if (Utils.isCheckReferral) {
                                is_referral_points = "1";
                                float famount = Float.valueOf(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total));
                                float a = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);
                                if(a>=famount)
                                {
                                    float aab = famount;
                                    ttl_sums = aab;
                                }
                                else
                                {
                                    float aab = famount - a;
                                    ttl_sums = aab;
                                }
                                Config.refDiscount = String.valueOf(a);

                            } else {
                                is_referral_points = "0";
                                float famount = Float.valueOf(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total));
                                float aab = famount;
                                ttl_sums = aab;
                            }
                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            prefs.getString("couponcode",String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText)),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(ttl_sums),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ONE,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            shopZoneShippingEnable,
                                            basketProductListToServerContainer.productList,
                                            "0",
                                            is_referral_points
                                    )
                            );
                            break;

                        case Constants.PAYMENT_BANK:

                            if (Utils.isCheckReferral) {
                                is_referral_points = "1";
                            } else {
                                is_referral_points = "0";
                            }
                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ONE,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            shopZoneShippingEnable,
                                            basketProductListToServerContainer.productList,
                                            "0",
                                            is_referral_points
                                    )
                            );
                            break;
                    }
                }

            } else {
                psDialogMsg.showErrorDialog(getString(R.string.basket__no_item_desc), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE__PAYPAL) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                if (result.getPaymentMethodNonce() != null) {
                    onPaymentMethodNonceCreated(result.getPaymentMethodNonce());
                }
            }
//            else {
//                // handle errors here, an exception may be available in
////                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
//            }
        } else if (requestCode == Constants.REQUEST_CODE__STRIPE_ACTIVITY && resultCode == Constants.RESULT_CODE__STRIPE_ACTIVITY) {
            if (this.getActivity() != null) {

                payment_method_nonce = data.getStringExtra(Constants.PAYMENT_TOKEN);

//                progressDialog.show();
                if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                    ((CheckoutActivity) getActivity()).progressDialog.show();
                }
                sendData();

            }
        }
    }


    @Override
    protected void initData() {

        shopViewModel.setShopObj(Config.API_KEY);

        shopViewModel.getShopData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        if (result.data != null) {
                            if (result.data.paypalEnabled.equals(Constants.ONE)) {
                                binding.get().paypalCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().paypalCardView.setVisibility(View.GONE);
                            }

                            if (result.data.codEnabled.equals(Constants.ONE)) {
                                binding.get().cashCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().cashCardView.setVisibility(View.GONE);
                            }

                            if (result.data.stripeEnabled.equals(Constants.ONE)) {
                                binding.get().cardCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().cardCardView.setVisibility(View.GONE);
                            }

                            if (result.data.banktransferEnabled.equals(Constants.ONE)) {
                                binding.get().bankCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().bankCardView.setVisibility(View.GONE);
                            }
                        }

                        break;

                    case LOADING:

                        if (result.data != null) {
                            if (result.data.paypalEnabled.equals(Constants.ONE)) {
                                binding.get().paypalCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().paypalCardView.setVisibility(View.GONE);
                            }

                            if (result.data.codEnabled.equals(Constants.ONE)) {
                                binding.get().cashCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().cashCardView.setVisibility(View.GONE);
                            }

                            if (result.data.stripeEnabled.equals(Constants.ONE)) {
                                binding.get().cardCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().cardCardView.setVisibility(View.GONE);
                            }

                            if (result.data.banktransferEnabled.equals(Constants.ONE)) {
                                binding.get().bankCardView.setVisibility(View.VISIBLE);
                            } else {
                                binding.get().bankCardView.setVisibility(View.GONE);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        paypalViewModel.getPaypalTokenData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case SUCCESS:

                        clientTokenString = result.message;

                        onBraintreeSubmit();

                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

                        break;

                    case ERROR:

                        Toast.makeText(getContext(), result.message, Toast.LENGTH_SHORT).show();

                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

                        break;
                }
            }
        });

        basketViewModel.setBasketListWithProductObj();
        basketViewModel.getAllBasketWithProductList().observe(this, baskets -> {
            if (baskets != null && baskets.size() > 0) {
                basketList = baskets;

//                String currency = "";
                for (Basket basket : baskets) {
                    //                    currency = basket.product.currencyShortForm;
                    HashMap<String, String> map = new Gson().fromJson(basket.selectedAttributes, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    Iterator<String> keyIterator = map.keySet().iterator();
                    StringBuilder keyStr = new StringBuilder();
                    StringBuilder nameStr = new StringBuilder();
                    while (keyIterator.hasNext()) {
                        String key = keyIterator.next();
                        if (!key.equals("-1")) {
                            if (map.containsKey(key)) {

                                if (!keyStr.toString().equals("")) {
                                    keyStr.append(Config.ATTRIBUTE_SEPARATOR);
                                    nameStr.append(Config.ATTRIBUTE_SEPARATOR);
                                }
                                keyStr.append(key);
                                nameStr.append(map.get(key));

                            }
                        }

                    }

                    HashMap<String, String> priceMap = new Gson().fromJson(basket.selectedAttributesPrice, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    Iterator<String> priceKeyIterator = priceMap.keySet().iterator();
                    StringBuilder priceStr = new StringBuilder();
                    while (priceKeyIterator.hasNext()) {
                        String key = priceKeyIterator.next();

                        if (!key.equals("-1")) {
                            if (priceMap.containsKey(key)) {

                                if (!priceStr.toString().equals("")) {
                                    priceStr.append(Config.ATTRIBUTE_SEPARATOR);
                                }
                                priceStr.append(priceMap.get(key));
                            }
                        }
                    }

                    Utils.psLog("Data for map" + basket.selectedAttributes);

                    BasketProductToServer basketProductToServer = new BasketProductToServer(
                            "",
                            basket.productId,
                            basket.product.name,
                            keyStr.toString(),
                            nameStr.toString(),
                            priceStr.toString(),
                            basket.selectedColorId,
                            basket.selectedColorValue,
                            basket.product.productUnit,
                            basket.product.productMeasurement,
                            basket.product.shippingCost,
                            String.valueOf(basket.product.unitPrice),
                            String.valueOf(basket.basketOriginalPrice),
                            String.valueOf(basket.basketOriginalPrice - basket.product.unitPrice),
                            String.valueOf(basket.product.discountAmount),
                            String.valueOf(basket.count),
                            String.valueOf(basket.product.discountValue),
                            String.valueOf(basket.product.discountPercent),
                            basket.product.currencyShortForm,
                            basket.product.currencySymbol);

                    basketProductListToServerContainer.productList.add(basketProductToServer);
                }
            }

        });

        transactionListViewModel.getSendTransactionDetailData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (CheckoutFragment3.this.getActivity() != null) {

                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).number = 4;

                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).transactionObject = result.data;

                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).goToFragment4();

                        basketViewModel.setWholeBasketDeleteStateObj();

                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

//                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).progressDialog.cancel();
                    }

                } else if (result.status == Status.ERROR) {
                    if (CheckoutFragment3.this.getActivity() != null) {

                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

//                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).progressDialog.cancel();

                        psDialogMsg.showErrorDialog(result.message, getString(R.string.app__ok));

                        psDialogMsg.show();
                    }
                }
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

    void getToken() {

        paypalViewModel.setPaypalTokenObj();
//        progressDialog.show();
        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.show();
        }

    }

    private void onBraintreeSubmit() {
        if (getActivity() != null) {
            DropInRequest dropInRequest = new DropInRequest()
                    .clientToken(clientTokenString);
            this.getActivity().startActivityForResult(dropInRequest.getIntent(this.getActivity()), Constants.REQUEST_CODE__PAYPAL);
        }
    }

    private void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
//        if (paymentMethodNonce instanceof PayPalAccountNonce) {
//            PayPalAccountNonce paypalAccountNonce = (PayPalAccountNonce) paymentMethodNonce;

//            PayPalCreditFinancing creditFinancing = paypalAccountNonce.getCreditFinancing();
//            if (creditFinancing != null) {
//                // PayPal Credit was accepted
//            }

        // Access additional information

        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.show();
        }

        payment_method_nonce = paymentMethodNonce.getNonce();

        sendData();

//        }
        /*else {
            // Send nonce to server
            String nonce = paymentMethodNonce.getNonce();
        }*/
    }

    private void changeToOrange(CardView cardView, TextView textView, ImageView imageView) {

        cardView.setCardBackgroundColor(getResources().getColor(R.color.global__primary));
        imageView.setColorFilter(getResources().getColor(R.color.md_white_1000));
        textView.setTextColor(getResources().getColor(R.color.md_white_1000));
    }

    private void changeToWhite(CardView cardView, TextView textView, ImageView imageView) {

        cardView.setCardBackgroundColor(getResources().getColor(R.color.md_white_1000));
        imageView.setColorFilter(getResources().getColor(R.color.md_grey_500));
        textView.setTextColor(getResources().getColor(R.color.md_grey_700));
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
                        //Toast.makeText(context,jsonObject.getString("referral_code"),Toast.LENGTH_LONG).show();
                        referral_value = jsonObject.getString("referral_value");
                        youTotalRewardPoints = jsonObject.getString("youTotalRewardPoints");
                        razorpayCharge = jsonObject.getString("razorpayCharge");
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


    public void startPayment() {


        try {


            final Activity activity = getActivity();
            final Checkout co = new Checkout();
            try {
                JSONObject options = new JSONObject();
                options.put("name", name);
                options.put("description", desc);
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
                options.put("currency", "INR");
                double total = Double.parseDouble(pay);
                total = total * 100;
                options.put("amount", total);
                JSONObject preFill = new JSONObject();
                preFill.put("email", email);
                preFill.put("contact", contact);
                options.put("prefill", preFill);
                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            float ttl_sum = 0;
            if (Utils.isCheckReferral) {
                is_referral_points = "1";
                float famount = Float.valueOf(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total));
                float famountt = famount + ((famount * Float.valueOf(razorpayCharge)) / 100);
                float a = Float.valueOf(referral_value) * Float.valueOf(youTotalRewardPoints);
                if (a >= famountt) {
                    float aab = famountt;
                    ttl_sum = aab;
                } else {
                    float aab = famountt - a;
                    ttl_sum = aab;
                }
                Config.refDiscount = String.valueOf(a);

            } else {
                is_referral_points = "0";
                float famount = Float.valueOf(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total));
                float famountt = famount + ((famount * Float.valueOf(razorpayCharge)) / 100);
                float aab = famountt;
                ttl_sum = aab;
            }
            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                            user.userId,
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                            String.valueOf((int) Math.round(ttl_sum)),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                            user.userName,
                            user.userPhone,
                            Constants.RATING_ZERO,
                            Constants.RATING_ZERO,
                            Constants.RATING_ZERO,
                            Constants.RATING_ZERO,
                            payment_method_nonce,
                            Constants.RATING_ONE,
                            basketList.get(0).product.currencySymbol,
                            basketList.get(0).product.currencyShortForm,
                            user.billingFirstName,
                            user.billingLastName,
                            user.billingCompany,
                            user.billingAddress1,
                            user.billingAddress2,
                            user.billingCountry,
                            user.billingState,
                            user.billingCity,
                            user.billingPostalCode,
                            user.billingEmail,
                            user.billingPhone,
                            user.shippingFirstName,
                            user.shippingLastName,
                            user.shippingCompany,
                            user.shippingAddress1,
                            user.shippingAddress2,
                            user.shippingCountry,
                            user.shippingState,
                            user.shippingCity,
                            user.shippingPostalCode,
                            user.shippingEmail,
                            user.shippingPhone,
                            shippingTax,
                            overAllTax,
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                            binding.get().memoEditText.getText().toString(),
                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                            shopZoneShippingEnable,
                            basketProductListToServerContainer.productList,
                            "1",
                            is_referral_points
                    )
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getActivity(), "Error in payment: " +s, Toast.LENGTH_SHORT).show();
    }
}
