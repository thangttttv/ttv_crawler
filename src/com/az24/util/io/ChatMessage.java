package com.az24.util.io;

import java.util.Calendar;

import net.sf.json.JSONObject;

public class ChatMessage {
	public int id;
	public int fuID;
	public int tuID;
	public String content="";
	public int pID=1;
	public double price=0;
	public int quantity=0;
	public int ship=0;
	public int type=0;
	public String time_receive;
	public String file="";
	public long file_size = 0;
	public String p_swap_id="";
	
	public String getP_swap_id() {
		return p_swap_id;
	}

	public void setP_swap_id(String p_swap_id) {
		this.p_swap_id = p_swap_id;
	}

	private String fuName;
	private String tuName;
	private String time;
	private String fuAvatar;
	private String tuAvatar;
	
	public ChatMessage() {
	
	}
	
	public ChatMessage(int _fuID,int _tuID,int _pID,String _content,int _type) {
		this.fuID = _fuID;
		this.tuID = _tuID;
		this.pID = _pID;
		this.content = _content;
		this.type = _type;
	}
	
	public ChatMessage(int _fuID,int _tuID,int _pID,String _content,int _type,String _file) {
		this.fuID = _fuID;
		this.tuID = _tuID;
		this.pID = _pID;
		this.content = _content;
		this.type = _type;
		this.file = _file;
	}
	
	public ChatMessage(int _fuID,int _tuID,int _pID,String _content,int _type,String _file,long _file_size) {
		this.fuID = _fuID;
		this.tuID = _tuID;
		this.pID = _pID;
		this.content = _content;
		this.type = _type;
		this.file = _file;
		this.file_size = _file_size;
	}
	
	public ChatMessage(int _fuID,int _tuID,int _pID,String _content,int _type,double _price,int _quantity,int _ship) {
		this.fuID = _fuID;
		this.tuID = _tuID;
		this.pID = _pID;
		this.content = _content;
		this.type = _type;
		this.price = _price;
		this.quantity = _quantity;
		this.ship = _ship;
		
	}
	
	public ChatMessage(int _fuID,int _tuID,int _pID,String _content,int _type,double _price,int _quantity,int _ship,String _p_swap_id) {
		this.fuID = _fuID;
		this.tuID = _tuID;
		this.pID = _pID;
		this.content = _content;
		this.type = _type;
		this.price = _price;
		this.quantity = _quantity;
		this.ship = _ship;
		this.p_swap_id = _p_swap_id;
	}
	
	public ChatMessage(int _fuID,int _tuID,String _content,String _fuName,String _tuName,String _fuAvatar,String _tuAvatar,String _time){
		this.fuID = _fuID;
		this.tuID = _tuID;
		this.content = _content;
		this.fuName = _fuName;
		this.tuName = _tuName;
		this.fuAvatar = _fuAvatar;
		this.tuAvatar = _tuAvatar;
		this.time = _time;
	}
	
	public int getpID() {
		return pID;
	}

	public void setpID(int pID) {
		this.pID = pID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuatity() {
		return quantity;
	}

	public void setQuatity(int quatity) {
		this.quantity = quatity;
	}

	public int getShip() {
		return ship;
	}

	public void setShip(int ship) {
		this.ship = ship;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime_receive() {
		return time_receive;
	}

	public void setTime_receive(String time_receive) {
		this.time_receive = time_receive;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	
	public int getFuID() {
		return fuID;
	}
	
	public void setFuID(int fuID) {
		this.fuID = fuID;
	}
	public int getTuID() {
		return tuID;
	}
	
	public void setTuID(int tuID) {
		this.tuID = tuID;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getFuName() {
		return fuName;
	}
	
	public void setFuName(String fuName) {
		this.fuName = fuName;
	}
	
	public String getTuName() {
		return tuName;
	}
	
	public void setTuName(String tuName) {
		this.tuName = tuName;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getFuAvatar() {
		return fuAvatar;
	}
	
	public void setFuAvatar(String fuAvatar) {
		this.fuAvatar = fuAvatar;
	}
	
	public String getTuAvatar() {
		return tuAvatar;
	}
	
	public void setTuAvatar(String tuAvatar) {
		this.tuAvatar = tuAvatar;
	}
	
	public String toJSonString(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("fuName",fuName);
	        formDetailsJson.put("tuName",tuName);
	        formDetailsJson.put("fuAvatar",fuAvatar);
	        formDetailsJson.put("tuAvatar",tuAvatar);
	        formDetailsJson.put("time",time);
	        formDetailsJson.put("content",content);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageText(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("content",content);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageFileHeader(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("size",file_size);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageOfferBuy(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("content",content);
	        formDetailsJson.put("price",price);
	        formDetailsJson.put("quantity",quantity);
	        formDetailsJson.put("ship",ship);
	        strJson=formDetailsJson.toString();
		return strJson;
	}
	
	public String toJSonStringMessageOfferSwap(){
		String strJson = "";
		   JSONObject formDetailsJson = new JSONObject();
	        formDetailsJson.put("fuID", fuID);
	        formDetailsJson.put("tuID", tuID);
	        formDetailsJson.put("pID",pID);
	        formDetailsJson.put("psID",p_swap_id);
	        formDetailsJson.put("content",content);
	        formDetailsJson.put("price",price);
	        formDetailsJson.put("quantity",quantity);
	        formDetailsJson.put("ship",ship);
	        strJson=formDetailsJson.toString();
	        return strJson;
	}	
	
	public static void main(String[] args) {
		ChatMessage chatMessage = new ChatMessage(1, 2, "Test", "thangtt", "van", "1.jpg", "2.jpg", Calendar.getInstance().getTime().toString());
		System.out.println(chatMessage.toJSonString());
	}
}
