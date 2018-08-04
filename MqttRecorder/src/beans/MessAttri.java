package beans;

public class MessAttri {
	private int mqttid;
	private String topic; 
	private boolean duplicate;
	private boolean retained;
	private String receivedtime;
	
	public MessAttri(int mqtt_id,String topic, boolean duplicate, boolean retained, String receivedtime)
	{
		this.mqttid = mqtt_id;
		this.topic = topic;
		this.duplicate = duplicate;
		this.retained = retained;
		this.receivedtime = receivedtime;
		
	}
	
	public int getMqttid() {
		return mqttid;
		
	}
	
	
	public String getTopic() {
		return topic;
		
	}
	
	public boolean getDuplicate() {
		return duplicate;
	}
	
	public boolean getRetained(){
		return retained;
	}
	
	public String getReceivedtime(){
		return receivedtime;
		
	}
	
	
	public void setMqttid(int mqttid){
		this.mqttid = mqttid;
	}
	
	
	public void setTopic(String topic){
		this.topic = topic;
	}
	
	public void setDuplicate(boolean duplicate){
	    this.duplicate = duplicate;
	}
	
	public void setRetained(boolean retained){
		this.retained = retained;
		
	}
	
	public void setReceivedtime(String receivedtime)
	{
		this.receivedtime = receivedtime;
	}
}
