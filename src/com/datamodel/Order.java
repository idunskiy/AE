package com.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable{

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
	@SerializedName("owc_thread")
	CustomerThread cus_thread;
	@SerializedName("dtl_expl")
	boolean den;
	@SerializedName("deadline")
	DateTime deadline;
	@SerializedName("is_active")
	boolean is_active;
	@SerializedName("product")
	Product product;
	@SerializedName("subject")
	Subject subject;
	List<Order> orders;
	public Order()
	{}
	public Order(ArrayList<File> files, boolean customer_deadline_sent,
			boolean payed, boolean payment_failed, boolean not_payed_sent,
			String info, int id, Category category, String timezone,
			String title, Level level, DateTime updated_at, float price,
			DateTime checkpoint_deadline, boolean h_notified, float refund,
			String special_info, boolean checkpoint_deadline_sent,
			ProcessStatus process_status, DateTime created_at,
			CustomerThread cus_thread, boolean den, DateTime deadline , boolean is_active,
			Product product, Subject subject) {
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
		this.is_active = is_active;
		this.product = product;
		this.subject = subject;
	}

	public Order(Parcel in) {
		this();
		readFromParcel(in);
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
	
	public boolean getIsActive() {
		return this.is_active;
	}
	public Product getProduct() {
		return this.product;
	}
	public Subject getSubject() {
		return this.subject;
	}

	// setters

	public void setOrderFiles(List<File> files) {
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

	public void setTitle(String title) {
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
	
	public void setIsActive(boolean is_active) {
		this.is_active = is_active;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
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
				+ " " + "price=" + price + " " 
				+ timezone+level+"price= "+"process status = "+ process_status+"}";
				//+ product +" "+ product.getProduct().toString()+"" +"process status = "+ process_status+"}";
		
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	private void readFromParcel(Parcel in) {
		files = in.readArrayList(File.class.getClassLoader());
		checkpoint_deadline_sent = in.readByte() == 1; 
		customer_deadline_sent = in.readByte() == 1; 
		payed = in.readByte() == 1; 
		payment_failed = in.readByte() == 1; 
		not_payed_sent = in.readByte() == 1; 
		den = in.readByte() == 1;
		h_notified = in.readByte() == 1; 
//		checkpoint_deadline_sent = myBooleanArr[0];
//		customer_deadline_sent =  myBooleanArr[1];
//		payed = myBooleanArr[2];
//		payment_failed = myBooleanArr[3];
//		not_payed_sent = myBooleanArr[4];
//		den = myBooleanArr[5];
//		h_notified = myBooleanArr[6];
	//	in.readBooleanArray(myBooleanArr);
		special_info = in.readString();
		title = in.readString();
		timezone = in.readString();
		info = in.readString();
		id = in.readInt();
		category = in.readParcelable(Category.class.getClassLoader());
		level = in.readParcelable(Level.class.getClassLoader());
		updated_at =  in.readParcelable(DateTime.class.getClassLoader());
		deadline =  in.readParcelable(DateTime.class.getClassLoader());
		checkpoint_deadline =  in.readParcelable(DateTime.class.getClassLoader());
		created_at =  in.readParcelable(DateTime.class.getClassLoader());
		process_status =  in.readParcelable(ProcessStatus.class.getClassLoader());
		cus_thread =  in.readParcelable(CustomerThread.class.getClassLoader());
		price = in.readFloat();
		refund = in.readFloat();
		
	}
	public void writeToParcel(Parcel par, int arg1) {
		par.writeList(files);
		par.writeByte((byte) (checkpoint_deadline_sent ? 1 : 0)); 
		par.writeByte((byte) (customer_deadline_sent ? 1 : 0)); 
		par.writeByte((byte) (payed ? 1 : 0)); 
		par.writeByte((byte) (payment_failed ? 1 : 0)); 
		par.writeByte((byte) (not_payed_sent ? 1 : 0));
		par.writeByte((byte) (den ? 1 : 0));
		par.writeByte((byte) (h_notified ? 1 : 0)); 
//		par.writeBooleanArray(new boolean[] {checkpoint_deadline_sent});
//		par.writeBooleanArray(new boolean[] {customer_deadline_sent});
//		par.writeBooleanArray(new boolean[] {payed});
//		par.writeBooleanArray(new boolean[] {payment_failed});
//		par.writeBooleanArray(new boolean[] {not_payed_sent});
//		par.writeBooleanArray(new boolean[] {den});
//		par.writeBooleanArray(new boolean[] {h_notified});
		par.writeString(special_info);
		par.writeString(info);
		par.writeString(timezone);
		par.writeString(title);
		par.writeInt(id);
		par.writeParcelable(category, arg1);
		par.writeParcelable(level, arg1);
		par.writeParcelable(checkpoint_deadline, arg1);
		par.writeParcelable(created_at, arg1);
		par.writeParcelable(deadline, arg1);
		par.writeParcelable(updated_at, arg1);
		par.writeParcelable(cus_thread, arg1);
		par.writeParcelable(process_status, arg1);
		par.writeFloat(price);
		par.writeFloat(refund);
	}

	public static final Parcelable.Creator<Order> CREATOR =
	    	new Parcelable.Creator<Order>() {
	            public Order createFromParcel(Parcel in) {
	                return new Order(in);
	            }
	 
	            public Order[] newArray(int size) {
	            	//throw new UnsupportedOperationException();
	            	return new Order[size];
	            }
	        };
	  @Override
	  public boolean equals(Object other){
		  boolean result = false;
		  if (other instanceof Order)
		  {
			  if (((Order) other).getOrderid() == this.getOrderid())
				 result = true;
		  }
		  else
			  result = false;
		  
		  return result;
	  }
	

}
