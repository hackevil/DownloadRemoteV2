import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class remoteClient {
	public remoteClient(String links) throws IOException
	{
		String username = System.getProperties().getProperty ("user.name");
		String message = "{\"user\": \""+username+"\", \"args\":\""+links+"\"}";
		
		URLConnection connection = new URL("http://manfroi.fr:5000/remote").openConnection();
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Length", "" + message.length());
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Cache-Control", "no-cache");
		OutputStream stream = connection.getOutputStream();
		stream.write(message.getBytes());
		stream.flush();
		stream.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String str = br.readLine();
		 while(str != null){
		       sb.append(str);
		       str = br.readLine();
		 }
		 br.close();
		 String responseString = sb.toString();
		 System.out.println(responseString);
	}
}
