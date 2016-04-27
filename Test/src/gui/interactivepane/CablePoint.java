package gui.interactivepane;


public interface CablePoint  {
	public int getXOnPane();
	public int getYOnPane();
	public InteractiveCable getCable();
	public void setCable(InteractiveCable cable);
	public boolean isConnected();
	public CablePointType getType();
	public void setHost(CablePointHost host);
	public void disconnect();
	

}

