package com.library;

import java.io.Serializable;

import android.app.Activity;

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
	 * Уведомление, что оплата прошла успешно
	 * 
	 * @param payKey			ключ оплаты
	 * @param paymentStatus		статус транзакции
	 */
	public void onPaymentSucceeded(String payKey, String paymentStatus) {
		setResultTitle("SUCCESS");
		setResultInfo("You have successfully completed your transaction.");
		setResultExtra("Key: " + payKey);
	}
	/**
	 * Уведомление, что оплата закончилась провалом. 
	 * 
	 * @param paymentStatus		статус транзакции
	 * @param correlationID		correlationID провала оплаты
	 * @param payKey			ключ оплаты
	 * @param errorID			ID ошибки
	 * @param errorMessage		сообщение ошибки
	 */
	public void onPaymentFailed(String paymentStatus, String correlationID,
			String payKey, String errorID, String errorMessage) {
		resultTitle = "FAILURE";
		resultInfo = errorMessage;
		resultExtra = "Error ID: " + errorID + "\nCorrelation ID: "
				+ correlationID + "\nPay Key: " + payKey;
	}
	/**
	 *	Уведомление, что оплату отменили.
	 * 
	 * @param paymentStatus		статус транзакции
	 */
	public void onPaymentCanceled(String paymentStatus) {
		resultTitle = "CANCELED";
		resultInfo = "The transaction has been cancelled.";
		resultExtra = "";
	}
}