package gui.interactivepane;


public interface CablePoint  {
	public int getXOnScreen();
	public int getYOnScreen();
	public InteractiveCable getCable();
	public void setCable(InteractiveCable cable);
	public boolean isConnected();
	public CablePointType getType();
	public void setHost(CablePointHost host);
	public CablePointHost getHost();
	public void disconnect();
	public void setIndex(int i);
	public int getIndex();
	

}

