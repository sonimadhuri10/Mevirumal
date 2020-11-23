package com.webbeansvipul.psstore.ui.checkout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webbeansvipul.psstore.Config;
import com.webbeansvipul.psstore.R;
import com.webbeansvipul.psstore.binding.FragmentDataBindingComponent;
import com.webbeansvipul.psstore.databinding.CheckoutFragmentStatusBinding;
import com.webbeansvipul.psstore.ui.common.DataBoundListAdapter;
import com.webbeansvipul.psstore.ui.common.PSFragment;
import com.webbeansvipul.psstore.utils.AutoClearedValue;
import com.webbeansvipul.psstore.utils.Constants;
import com.webbeansvipul.psstore.utils.Utils;
import com.webbeansvipul.psstore.viewobject.User;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class CheckoutStatusFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private AutoClearedValue<CheckoutFragmentStatusBinding> binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CheckoutFragmentStatusBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_status, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {
        try
        {
            if (getActivity() != null) {

                String currencySymbol = ((CheckoutActivity) getActivity()).transactionObject.currencySymbol;

                binding.get().reviewCardView.setOnClickListener(view -> navigationController.navigateToTransactionDetail(getActivity(), ((CheckoutActivity) getActivity()).transactionObject));

                binding.get().transactionNumberTextView.setText(((CheckoutActivity) Objects.requireNonNull(getActivity())).transactionObject.transCode);

                binding.get().imageView24.setOnClickListener(v -> {
                    ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(Constants.TRANSACTION_NUMBER, ((CheckoutActivity) Objects.requireNonNull(getActivity())).transactionObject.transCode);

                    if (clipboard != null && clip != null) {
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                    }
                });


                if (!((CheckoutActivity) Objects.requireNonNull(getActivity())).transactionObject.totalItemCount.equals(Constants.ZERO)) {
                    binding.get().totalItemCountValueTextView.setText(((CheckoutActivity) getActivity()).transactionObject.totalItemCount);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.totalItemAmount.equals(Constants.ZERO)) {

                    String totalValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.totalItemAmount));
                    binding.get().totalValueTextView.setText(totalValueString);

                }

                if (!((CheckoutActivity) getActivity()).transactionObject.discountAmount.equals(Constants.ZERO)) {

                    String discountValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.discountAmount));
                    binding.get().discountValueTextView.setText(discountValueString);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.subTotalAmount.equals(Constants.ZERO)) {
                    String subTotalValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.subTotalAmount));
                    binding.get().subtotalValueTextView.setText(subTotalValueString);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.taxAmount.equals(Constants.ZERO)) {
                    String taxValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.taxAmount));
                    binding.get().taxValueTextView.setText(taxValueString);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.shippingAmount.equals(Constants.ZERO)) {
                    String shippingTaxValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.shippingAmount));
                    binding.get().shippingTaxValueTextView.setText(shippingTaxValueString);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.balanceAmount.equals(Constants.ZERO)) {
                    String finalTotalValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.balanceAmount));
                    binding.get().finalTotalValueTextView.setText(finalTotalValueString);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.shippingMethodAmount.equals(Constants.ZERO)) {
                    String shippingCostValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.shippingMethodAmount));
                    binding.get().shippingCostValueTextView.setText(shippingCostValueString);
                }

                if (!((CheckoutActivity) getActivity()).transactionObject.couponDiscountAmount.equals(Constants.ZERO)) {
                    String couponDiscountValueString = currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(((CheckoutActivity) getActivity()).transactionObject.couponDiscountAmount));
                    binding.get().couponDiscountValueTextView.setText(couponDiscountValueString);
                }

                if (!overAllTaxLabel.equals(Constants.ZERO)) {

                    try {
                        binding.get().textView22.setText(getString(R.string.tax, overAllTaxLabel));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                if (!shippingTaxLabel.equals(Constants.ZERO)) {

                    try {
                        binding.get().textView24.setText(getString(R.string.shipping_tax, shippingTaxLabel));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {



            binding.get().refDiscountValueTextView.setText(Config.refDiscount);
        }
        catch (Exception e)
        {

        }

    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (this.getActivity() != null) {

            User user = ((CheckoutActivity) this.getActivity()).getCurrentUser();


          try {
              String text = getString(R.string.checkout_status__thank_you) + Constants.SPACE_STRING + user.userName;
              binding.get().nameTitleTextView.setText(text);
              binding.get().shippingPhoneValueTextView.setText(user.shippingPhone);
              binding.get().shippingEmailValueTextView.setText(user.shippingEmail);
              binding.get().shippingAddressValueTextView.setText(user.shippingAddress1);

              binding.get().billingphoneValueTextView.setText(user.billingPhone);
              binding.get().billingEmailValueTextView.setText(user.billingEmail);
              binding.get().billingAddressValueTextView.setText(user.billingAddress1);

          }
          catch (Exception e)
          {
              e.printStackTrace();
          }





        }
    }
}
