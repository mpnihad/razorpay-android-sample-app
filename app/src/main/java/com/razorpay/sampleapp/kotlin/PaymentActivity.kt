package com.razorpay.sampleapp.kotlin

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.razorpay.sampleapp.R
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class PaymentActivity: Activity(), PaymentResultListener {

    val TAG:String = PaymentActivity::class.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */
        Checkout.preload(applicationContext)

        var button: Button = findViewById(R.id.btn_pay)
        button.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity:Activity = this
        val co = Checkout()

        co.setFullScreenDisable(false)

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency","INR")
            options.put("amount","100")


            // if we want upi as prefered methord but other payment will be available
//            val prefill = JSONObject()
//            prefill.put("email","test@razorpay.com")
//            prefill.put("contact","9876543210")
//            prefill.put("method","upi")
//
//            options.put("prefill",prefill)




           // if we want to remove all the payment except upi
            val methord = JSONObject()

            methord.put("netbanking", false)
            methord.put("card", false)
            methord.put("upi", true)
            methord.put("wallet", false)

            options.put("method",methord)


            Log.d(TAG, "startPayment: ${options.toString()}")
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        try{
            Toast.makeText(this,"Payment failed $errorCode \n $response",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        try{
            Toast.makeText(this,"Payment Successful $razorpayPaymentId",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }


}