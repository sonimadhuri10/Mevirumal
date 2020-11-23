package com.webbeansvipul.psstore.ui.checkout;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.webbeansvipul.psstore.R;

import org.json.JSONObject;

public class CheckoutActivityR extends AppCompatActivity implements PaymentResultListener {

    String name="unknown";
    String desc="Welcome In Razorpay";
    String pay="1";
    String email="unknown@gmail.com";
    String contact="9838109180";

    private Button buttonConfirmOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_r);


        try {

            if(getIntent()!=null && getIntent().getStringExtra("name")!=null && !getIntent().getStringExtra("name").equalsIgnoreCase(""))
            {
               name= getIntent().getStringExtra("name");
            }

            if(getIntent()!=null && getIntent().getStringExtra("desc")!=null && !getIntent().getStringExtra("desc").equalsIgnoreCase(""))
            {
                desc= getIntent().getStringExtra("desc");
            }

            if(getIntent()!=null && getIntent().getStringExtra("pay")!=null && !getIntent().getStringExtra("pay").equalsIgnoreCase(""))
            {
                pay= getIntent().getStringExtra("pay");
            }


            if(getIntent()!=null && getIntent().getStringExtra("email")!=null && !getIntent().getStringExtra("email").equalsIgnoreCase(""))
            {
                email= getIntent().getStringExtra("email");
            }

            if(getIntent()!=null && getIntent().getStringExtra("contact")!=null && !getIntent().getStringExtra("contact").equalsIgnoreCase(""))
            {
                contact= getIntent().getStringExtra("contact");
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        listeners();
    }


    public void listeners() {

        startPayment();
    }


    public void startPayment() {
        final Activity activity = this;
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
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}