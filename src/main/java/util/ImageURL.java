package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

//Classe responsavel por baixar as imagens via URL
public class ImageURL {
	
	int i;
	public void viewImage(String local,String urlParam) throws Exception{

		URL url = new URL(urlParam);
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n=in.read(buf)))
		{
		   out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		
		if(local.contains("/")) {
			local = local.replace("/", " ");
		}
        //Variavel de ambiente
		FileOutputStream fos = new FileOutputStream("C:\\Users\\daniel\\Desktop\\foto\\"+local+"!..!"+(i++)+".png");
		fos.write(response);
		fos.close();
	}
	

}
