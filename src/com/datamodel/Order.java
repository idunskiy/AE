package com.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/** *класс обьекта заказа. Используется сериализация.*/
public class Order implements Parcelable{
	public static final String STATUS_ID = "id";
	public static final String STATUS_TITLE = "title";
	public static final String SUBJECT_ID = "subject_id";
	public static final String CATEGORY_ID = "category_id";
	public static final String LEVEL_ID = "level_id";
	/** *поле списка файлов.*/
	@SerializedName("files")
	List<Files> files;
	/** *поле флага, определяющего был ли оплачен заказ или нет. */
	@SerializedName("payed")
	int payed;
	/** *поле флага, определяющего была ли провалена попытка оплатить заказ. */
	@SerializedName("payment_failed")
	int payment_failed;
	/** *поле информации по заказу. */
	@SerializedName("task_info")
	String info;
	/** *id заказа */
	@SerializedName("id")
	int id;
	/** *категория заказа */
	@SerializedName("category")
	Category category;
	/** *временная зона, где был сделан заказ */
	@SerializedName("gnt")
	String timezone;
	/** *название заказа */
	@SerializedName("order_title")
	String title;
	/** *уровень заказа */
	@SerializedName("level")
	Level level;
	/** *время обновления заказа */
	@SerializedName("updated_at")
	DateTime updated_at;
	/** *цена заказа */
	@SerializedName("price")
	int price;
	/** *время чекпоинта заказа */
	@SerializedName("checkpoint_deadline")
	DateTime checkpoint_deadline;
	@SerializedName("h_notified")
	boolean h_notified;
	/** *количество возвращаемых денег */
	@SerializedName("refund")
	float refund;
	/** *поле специальной информации */
	@SerializedName("special_info")
	String special_info;
	/** *флаг о посылке уведомления о чекпоинте заказа */
	@SerializedName("checkpoint_deadline_sent")
	boolean checkpoint_deadline_sent;
	/** *статус заказа */
	@SerializedName("process_status")
	ProcessStatus process_status;
	/** *время создания заказа */
	@SerializedName("placement_date")
	DateTime created_at;
	/** *флаг детального обьяснения по заказу*/
	@SerializedName("messages")
	ArrayList<Messages> messages;
	@SerializedName("dtl_expl")
	boolean den;
	/** *время выполнения заказа*/
	@SerializedName("deadline")
	DateTime deadline;
	/** *флаг активности заказа*/
	@SerializedName("status")
	String is_active;
	/** *продукт заказа*/
	@SerializedName("product")
	Product product;
	/** *тема заказа*/
	@SerializedName("subject")
	Subject subject;
	/** *список заказов*/
	List<Order> orders;
	/** *конструктор для ORMLite*/
	public Order()
	{}
	/** *основной конструктор*/
	public Order(ArrayList<Files> files, 
			int payed, int payment_failed,
			String info, int id, Category category, String timezone,
			String title, Level level, DateTime updated_at, int price,
			DateTime checkpoint_deadline, boolean h_notified, float refund,
			String special_info, boolean checkpoint_deadline_sent,
			ProcessStatus process_status, DateTime created_at,
			ArrayList<Messages> messages, boolean den, DateTime deadline , String is_active,
			Product product, Subject subject) {
		this.files = files;
		this.payed = payed;
		this.payment_failed = payment_failed;
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
		this.messages = messages;
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
	public Order(int i) {
		this.id = i;
	}
	public List<Files> getOrderFiles() {
		return this.files;
	}

	public boolean getOrderDeadline() {
		return this.checkpoint_deadline_sent;
	}

	public int getPayed() {
		return this.payed;
	}

	public int getPayment_failed() {
		return this.payment_failed;
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

	public int getPrice() {
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

	public ArrayList<Messages> getMessages() {
		return this.messages;
	}

	public boolean getden() {
		return this.den;
	}

	public DateTime getDeadline() {
		return this.deadline;
	}
	
	public String getIsActive() {
		return this.is_active;
	}
	public Product getProduct() {
		return this.product;
	}
	public Subject getSubject() {
		return this.subject;
	}

	// setters

	public void setOrderFiles(List<Files> files) {
		this.files = files;
	}

	public void setOrderDeadline(boolean checkpoint_deadline_sent) {
		this.checkpoint_deadline_sent = checkpoint_deadline_sent;
	}

	public void setPayed(int payed) {
		this.payed = payed;
	}

	public void setPayment_failed(int payment_failed) {
		this.payment_failed = payment_failed;
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

	public void setPrice(int price) {
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


	public void setden(boolean den) {
		this.den = den;
	}

	

	public void setDeadline(DateTime deadline) {
		this.deadline = deadline;
	}
	
	public void setIsActive(String is_active) {
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
				+ timezone+level+"price= "+"process status = "+ process_status+"product " + product.getProduct() + "}";
				//+ product +" "+ product.getProduct().toString()+"" +"process status = "+ process_status+"}";
		
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	private void readFromParcel(Parcel in) {
		files = in.readArrayList(Files.class.getClassLoader());
		checkpoint_deadline_sent = in.readByte() == 1; 
		payed = in.readInt(); 
		payment_failed= in.readInt();  
		den = in.readByte() == 1;
		h_notified = in.readByte() == 1; 
//		checkpoint_deadline_sent = myBooleanArr[0];
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
		price = in.readInt();
		refund = in.readFloat();
		
	}
	public void writeToParcel(Parcel par, int arg1) {
		par.writeList(files);
		par.writeByte((byte) (checkpoint_deadline_sent ? 1 : 0)); 
		par.writeInt(payed);
		par.writeInt(payment_failed); 
		par.writeByte((byte) (den ? 1 : 0));
		par.writeByte((byte) (h_notified ? 1 : 0)); 
//		par.writeBooleanArray(new boolean[] {checkpoint_deadline_sent});
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Order))
			return false;
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public void addMessage(Messages message)
	  {messages.add(message);}
	

}
