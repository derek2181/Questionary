package com.piaweb.data;

import java.io.UnsupportedEncodingException;

public class Encoder {
	
		public String encodeString(String elemento) {
			String valueUTF8=null;
			if(elemento!=null) {
			try {
				valueUTF8 = new String(elemento.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			return valueUTF8;
		}
		
		
}
