package model;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import org.xml.sax.SAXException;

import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmValue;

public class SongReader {

	private XPathCompiler xpath;
	private XdmItem document;
	
	public SongReader(File file) throws SAXException {
		Processor proc = new Processor(new Configuration());
		xpath = proc.newXPathCompiler();
		try {
			document = proc.newDocumentBuilder().build(file);
			document = xpath.evaluate("track", document).itemAt(0);
		}
		catch(SaxonApiException | IndexOutOfBoundsException e) {
			throw new SAXException(e);
		}
	}
	
	public Track read() throws SAXException {
		try {
			// Audio synthesis
			Track track = new Track(
				getReal("@lower", 1),
				getReal("@higher", 1),
				getInt("@steps", 1),
				getInt("@start", Integer.MIN_VALUE),
				getInt("@keys", 1)
			);
			AtomicInteger bpm = new AtomicInteger(getInt("@bpm", 1));
			// Read notes
			getList("note").forEach(n -> {
				// Change BPM
				if(getInt("count(@bpm)", n) == 1) {
					bpm.set(getInt("@bpm", n, 1));
				}
				Note note = new Note(getString("@length", n, 1), bpm.get());
				// Read values
				getList(n, "tokenize(text(), '\\s')").forEach(v -> {
					note.add(getInt("", v, 0));
				});
				track.add(note);
			});
			return track;
		}
		catch(SaxonApiUncheckedException e) {
			throw new SAXException(e);
		}
	}
	
	private XdmValue getList(XdmItem context, String expression) {
		try {
			return xpath.evaluate(expression, context);
		} 
		catch(SaxonApiException e) {
			throw new RuntimeException(e);
		}
	}
	
	private XdmValue getList(String expression) {
		return getList(document, expression);
	}
	
	private String getString(String expression, XdmItem context, int minLength) {
		expression = "string(" + expression + ")";
		try {
			String value = ((XdmAtomicValue)xpath.evaluate(expression, context).itemAt(0)).getStringValue();
			if(value.length() < minLength) {
				throw new SaxonApiException(expression);
			}
			return value;
		}
		catch(SaxonApiException e) {
			throw new SaxonApiUncheckedException(e);
		}
	}
	
//	private String getString(String expression, int minLength) {
//		return getString(expression, document, minLength);
//	}
	
	private int getInt(String expression, XdmItem context, int minValue) {
		return (int) getReal(expression, context, minValue);
	}
	
	private int getInt(String expression, XdmItem context) {
		return (int) getReal(expression, context, Integer.MIN_VALUE);
	}
	
	private int getInt(String expression, int minValue) {
		return getInt(expression, document, minValue);
	}
	
	private double getReal(String expression, XdmItem context, double minValue) {
		expression = "number(" + expression + ")";
		try {
			double value = ((XdmAtomicValue)xpath.evaluate(expression, context).itemAt(0)).getDoubleValue();	
			if(!Double.isFinite(value) || value < minValue) {
				throw new SaxonApiException(expression + " is " + value);
			}
			return value;
		}
		catch(SaxonApiException e) {
			throw new SaxonApiUncheckedException(e);
		}
	}
	
	private double getReal(String expression, double minValue) {
		return getReal(expression, document, minValue);
	}
}
