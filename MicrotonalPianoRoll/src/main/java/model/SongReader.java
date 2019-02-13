package model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SongReader {
	
	private final Document song;
	private final XPath path;
	
	public SongReader(File file) throws SAXException {
		try {
			song = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			path = XPathFactory.newInstance().newXPath();
		}
		catch (ParserConfigurationException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Song read() throws SAXException {
		System.out.println(new Audio(
			findDouble("number(/song/audio/lower/@hz)", true),
			findDouble("number(/song/audio/higher/@hz)", true),
			findInt("number(/song/audio/distance/@steps)", true),
			findInt("number(/song/audio/lowest/@offset)", false, 0),
			findInt("number(/song/audio/keys/@count)", true, 0),
			findString("/song/audio/lowest/@note")
		));
		return null;
	}
	
	private String findString(String expression) throws SAXException {
		return findString(expression, null);
	}
	
	private String findString(String expression, String orElse) throws SAXException {
		String string;
		try {
			string = (String) path.evaluate(expression, song, XPathConstants.STRING);
		}
		catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		if(string.isEmpty() && orElse != null) {
			return orElse;
		}
		else if(string.isEmpty()) {
			throw new SAXException("XPath '" + expression + "' is empty");
		}
		return string;
	}
	
	private double findDouble(String expression, boolean positive) throws SAXException {
		return findDouble(expression, positive, null);
	}

	private double findDouble(String expression, boolean positive, Number orElse) throws SAXException {
		double value;
		try {
			value = (double) path.evaluate(expression, song, XPathConstants.NUMBER);
		} 
		catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		if(!Double.isFinite(value) && orElse != null) {
			return orElse.doubleValue();
		}
		else if(!Double.isFinite(value) || positive && value < 0) {
			throw new SAXException("XPath '" + expression + "' is '" + value + "'");
		}
		return value;
	}
	
	private int findInt(String expression, boolean positive) throws SAXException {
		return findInt(expression, positive, null);
	}
	private int findInt(String expression, boolean positive, Integer orElse) throws SAXException {
		return (int) findDouble(expression, positive, orElse);
	}
}
