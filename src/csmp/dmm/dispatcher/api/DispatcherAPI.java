package csmp.dmm.dispatcher.api;


public class DispatcherAPI {	
	public static DispatcherConnection getConnection(String url){
		DispatcherConnection DispConn = new DispatcherConnection();
		DispConn.setConnUrl(url);
		return DispConn;		
	}

}
