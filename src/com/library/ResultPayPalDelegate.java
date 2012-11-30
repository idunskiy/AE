package com.library;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;

import com.assignmentexpert.NewMessageActivity;
import com.paypal.android.MEP.PayPalResultDelegate;

public class ResultPayPalDelegate implements PayPalResultDelegate, Serializable {
	Activity ctx;
	String resultTitle;
	String resultInfo;
	String resultExtra;
	public String getResultTitle()
	{
		return this.resultTitle;
	}
	public String getResultInfo()
	{
		return this.resultInfo;
	}
	public String getResultExtra()
	{
		return this.resultExtra;
	}
	public void setResultTitle(String title)
	{
		this.resultTitle = title;
	}
	public void setResultInfo(String info)
	{
		this.resultInfo = info;
	}
	public void setResultExtra(String extra)
	{
		this.resultExtra = extra;
	}
	private static final long serialVersionUID = 10001L;
	/**
	 * Notification that the payment has been completed successfully.
	 * 
	 * @param payKey			the pay key for the payment
	 * @param paymentStatus		the status of the transaction
	 */
	public void onPaymentSucceeded(String payKey, String paymentStatus) {
		setResultTitle("SUCCESS");
		setResultInfo("You have successfully completed your transaction.");
		setResultExtra("Key: " + payKey);
	}
	/**
	 * Notification that the payment has failed.
	 * 
	 * @param paymentStatus		the status of the transaction
	 * @param correlationID		the correlationID for the transaction failure
	 * @param payKey			the pay key for the payment
	 * @param errorID			the ID of the error that occurred
	 * @param errorMessage		the error message for the error that occurred
	 */
	public void onPaymentFailed(String paymentStatus, String correlationID,
			String payKey, String errorID, String errorMessage) {
		resultTitle = "FAILURE";
		resultInfo = errorMessage;
		resultExtra = "Error ID: " + errorID + "\nCorrelation ID: "
				+ correlationID + "\nPay Key: " + payKey;
	}
	/**
	 * Notification that the payment was canceled.
	 * 
	 * @param paymentStatus		the status of the transaction
	 */
	public void onPaymentCanceled(String paymentStatus) {
		resultTitle = "CANCELED";
		resultInfo = "The transaction has been cancelled.";
		resultExtra = "";
	}
}