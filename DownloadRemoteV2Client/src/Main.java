import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class Main {

	public static void main(String[] args) throws IOException {
		
		// Get All links
		String links = new String();
		if(args.length == 0)
			new InterfaceLiens();
		else
		{
			// Else, we concate all links
			for (int i = 0; i < args.length ; i++)
				links+=" " + args[i];
			System.out.println(links);
			new remoteClient(links);
		}

	}

}
