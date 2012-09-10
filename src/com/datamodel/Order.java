package com.datamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Order {

	@SerializedName("files")
	List<File> files;
	@SerializedName("customer_deadline_sent")
	boolean customer_deadline_sent;
	@SerializedName("payed")
	boolean payed;
	@SerializedName("payment_failed")
	boolean payment_failed;
	@SerializedName("not_payed_sent")
	boolean not_payed_sent;
	@SerializedName("info")
	String info;
	@SerializedName("id")
	int id;
	@SerializedName("category")
	Category category;
	@SerializedName("timezone")
	String timezone;
	@SerializedName("title")
	String title;
	@SerializedName("level")
	Level level;
	@SerializedName("updated_at")
	DateTime updated_at;
	@SerializedName("price")
	float price;
	@SerializedName("checkpoint_deadline")
	DateTime checkpoint_deadline;
	@SerializedName("h_notified")
	boolean h_notified;
	@SerializedName("refund")
	float refund;
	@SerializedName("special_info")
	String special_info;
	@SerializedName("checkpoint_deadline_sent")
	boolean checkpoint_deadline_sent;
	@SerializedName("process_status")
	ProcessStatus process_status;
	@SerializedName("created_at")
	DateTime created_at;
	@SerializedName("customer_thread")
	CustomerThread cus_thread;
	@SerializedName("dtl_expl")
	boolean den;
	@SerializedName("deadline")
	DateTime deadline;
	List<Order> orders;
	public Order()
	{}
	public Order(List<File> files, boolean customer_deadline_sent,
			boolean payed, boolean payment_failed, boolean not_payed_sent,
			String info, int id, Category category, String timezone,
			String title, Level level, DateTime updated_at, float price,
			DateTime checkpoint_deadline, boolean h_notified, float refund,
			String special_info, boolean checkpoint_deadline_sent,
			ProcessStatus process_status, DateTime created_at,
			CustomerThread cus_thread, boolean den, DateTime deadline) {
		this.files = files;
		this.customer_deadline_sent = customer_deadline_sent;
		this.payed = payed;
		this.payment_failed = payment_failed;
		this.not_payed_sent = not_payed_sent;
		this.info = info;
		this.id = id;
		this.category = category;
		this.timezone = timezone;
		this.title = title;
		this.level = level;
		this.updated_at = updated_at;
		this.price = price;
		this.checkpoint_deadline = checkpoint_deadline;
		this.h_notified = h_notified;
		this.refund = refund;
		this.special_info = special_info;
		this.checkpoint_deadline_sent = checkpoint_deadline_sent;
		this.process_status = process_status;
		this.created_at = created_at;
		this.cus_thread = cus_thread;
		this.den = den;
		this.deadline = deadline;
	}

	public List<File> getOrderFiles() {
		return this.files;
	}

	public boolean getOrderDeadline() {
		return this.checkpoint_deadline_sent;
	}

	public boolean getPayed() {
		return this.payed;
	}

	public boolean getPayment_failed() {
		return this.payment_failed;
	}

	public boolean getNot_payed_sent() {
		return this.not_payed_sent;
	}

	public String getOrderinfo() {
		return this.info;
	}

	public int getOrderid() {
		return this.id;
	}

	public Category getCategory() {
		return this.category;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public String getTitle() {
		return this.title;
	}

	public Level getLevel() {
		return this.level;
	}

	public DateTime getUpdated_at() {
		return this.updated_at;
	}

	public float getPrice() {
		return this.price;
	}

	public DateTime getChetcheckpoint_deadline() {
		return this.checkpoint_deadline;
	}

	public boolean getH_notified() {
		return this.h_notified;
	}

	public float getRefund() {
		return this.refund;
	}

	public String getSpecInfo() {
		return this.special_info;
	}

	public boolean getCheckpoint_deadline_sent() {
		return this.checkpoint_deadline_sent;
	}

	public ProcessStatus getProcess_status() {
		return this.process_status;
	}

	public DateTime getCreated_at() {
		return this.created_at;
	}

	public CustomerThread getCusThread() {
		return this.cus_thread;
	}

	public boolean getden() {
		return this.den;
	}

	public DateTime getDeadline() {
		return this.deadline;
	}

	// setters

	public void setOrderId(List<File> files) {
		this.files = files;
	}

	public void setOrderDeadline(boolean checkpoint_deadline_sent) {
		this.checkpoint_deadline_sent = checkpoint_deadline_sent;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public void setPayment_failed(boolean payment_failed) {
		this.payment_failed = payment_failed;
	}

	public void setNot_payed_sent(boolean not_payed_sent) {
		this.not_payed_sent = not_payed_sent;
	}

	public void getOrderinfo(String info) {
		this.info = info;
	}

	public void setOrderid(int id) {
		this.id = id;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public void getTitle(String title) {
		this.title = title;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setUpdated_at(DateTime updated_at) {
		this.updated_at = updated_at;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setChetcheckpoint_deadline(DateTime checkpoint_deadline) {
		this.checkpoint_deadline = checkpoint_deadline;
	}

	public void setH_notified(boolean h_notified) {
		this.h_notified = h_notified;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}

	public void setSpecInfo(String special_info) {
		this.special_info = special_info;
	}

	public void setCheckpoint_deadline_sent(boolean checkpoint_deadline_sent) {
		this.checkpoint_deadline_sent = checkpoint_deadline_sent;
	}

	public void setProcess_status(ProcessStatus process_status) {
		this.process_status = process_status;
	}

	public void setCreated_at(DateTime created_at) {
		this.created_at = created_at;
	}

	public void setCusThread(CustomerThread cus_thread) {
		this.cus_thread = cus_thread;
	}

	public void setden(boolean den) {
		this.den = den;
	}

	public void setDeadline(DateTime deadline) {
		this.deadline = deadline;
	}

	public List<Order> getOrders()
	{
		return this.orders;
	}
	public void setOrders(List<Order> orders)
	{
		this.orders  = orders;;
	}
	@Override
	public String toString() {
		return "id=" + id + " " + "title=" + title
				+ " " + "price=" + price + " " + "title= "+process_status.title
				+ "}";
	}

}
