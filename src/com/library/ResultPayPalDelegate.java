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
	 * �����������, ��� ������ ������ �������
	 * 
	 * @param payKey			���� ������
	 * @param paymentStatus		������ ����������
	 */
	public void onPaymentSucceeded(String payKey, String paymentStatus) {
		setResultTitle("SUCCESS");
		setResultInfo("You have successfully completed your transaction.");
		setResultExtra("Key: " + payKey);
	}
	/**
	 * �����������, ��� ������ ����������� ��������. 
	 * 
	 * @param paymentStatus		������ ����������
	 * @param correlationID		correlationID ������� ������
	 * @param payKey			���� ������
	 * @param errorID			ID ������
	 * @param errorMessage		��������� ������
	 */
	public void onPaymentFailed(String paymentStatus, String correlationID,
			String payKey, String errorID, String errorMessage) {
		resultTitle = "FAILURE";
		resultInfo = errorMessage;
		resultExtra = "Error ID: " + errorID + "\nCorrelation ID: "
				+ correlationID + "\nPay Key: " + payKey;
	}
	/**
	 *	�����������, ��� ������ ��������.
	 * 
	 * @param paymentStatus		������ ����������
	 */
	public void onPaymentCanceled(String paymentStatus) {
		resultTitle = "CANCELED";
		resultInfo = "The transaction has been cancelled.";
		resultExtra = "";
	}
}