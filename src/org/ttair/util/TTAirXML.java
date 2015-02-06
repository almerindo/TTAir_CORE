package org.ttair.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.ttair.util.xml.XMLTypeBehavior;

import com.thoughtworks.xstream.XStream;




public class TTAirXML {

	private static XStream xstream = new XStream();
	private static final TTAirXML INSTANCE = new TTAirXML();

	
	public TTAirXML(){
		xstream.processAnnotations(XMLTypeBehavior.class);
	}
	
	public XMLTypeBehavior loader (String filePath) throws IOException {
		FileInputStream fi = new FileInputStream(filePath);
		try {
			return (XMLTypeBehavior) xstream.fromXML(fi);
		} finally {
			fi.close();
		}
	}
	
	public XMLTypeBehavior loaderXML (String xml)  {
		return (XMLTypeBehavior) xstream.fromXML(xml);
	}

	public void generate( XMLTypeBehavior obj, String filename) throws IOException{
		FileOutputStream os = new FileOutputStream(filename);
		xstream.toXML(obj, os);
		os.close();
	}

	public void writeToStream(XMLTypeBehavior obj, OutputStream out){
		xstream.toXML(obj, out);
	}
	
	public String getXML(XMLTypeBehavior obj){
		return xstream.toXML(obj);
	}
	
	
	public Object loadClass(String fullNameClass){
		Object ret = null;
		try {
			String whatClass = fullNameClass;
			Class<?> exampleClass = Class.forName(whatClass);
			ret = exampleClass.newInstance();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static TTAirXML getINSTANCE() {
		return INSTANCE;
	}
	
	

		

}
