package model;

import java.io.File;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.BuildingStreamWriter;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;

public class TrackWriter {
	
	private final BuildingStreamWriter document;
	private final Serializer serializer;
	
	public TrackWriter(File file) {
		Processor proc = new Processor(new Configuration());
		try {
			document = proc.newDocumentBuilder().newBuildingStreamWriter();
			serializer = proc.newSerializer(file);
			serializer.setOutputProperty(Serializer.Property.INDENT, "yes");
		}
		catch(SaxonApiException | IndexOutOfBoundsException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void write(Track track) {
		try {
			document.writeStartDocument();
			startElement("track");
			// Audio synthesis
			setAttribute("lower", track.lowerFrequency);
			setAttribute("higher", track.higherFrequency);
			setAttribute("steps", track.stepsInBetween);
			setAttribute("start", track.lowestOffset);
			setAttribute("keys", track.numKeys);
			int bpm = track.firstMeasure().getBPM();
			setAttribute("bpm", bpm);
			// Measure does not get written to file
			for(Measure measure : track) {
				for(Note note : measure) {
					startElement("note");
					setAttribute("length", note.length.name());
					// Save tempo change in the first note of the measure
					if(measure.getBPM() != bpm) {
						bpm = measure.getBPM();
						setAttribute("bpm", bpm);						
					}
					// Write note values as a string of space separated integers
					setValue(note.values().map(v -> v.toString()).collect(Collectors.joining(" ")));
					endElement();
				}
			}
			endElement();
			document.writeEndDocument();
			serializer.serializeNode(document.getDocumentNode());
			document.close();
			serializer.close();
		}
		catch (XMLStreamException | SaxonApiException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void startElement(String element) throws XMLStreamException {
		document.writeStartElement(element);
	}
	
	private void endElement() throws XMLStreamException {
		document.writeEndElement();
	}
	
	private void setAttribute(String name, double value) throws XMLStreamException {
		document.writeAttribute(name, Double.toString(value));
		
	}
	
	private void setAttribute(String name, int value) throws XMLStreamException {
		document.writeAttribute(name, Integer.toString(value));		
	}
	
	private void setAttribute(String name, String value) throws XMLStreamException {
		document.writeAttribute(name, value);
	}
	
	private void setValue(String string) throws XMLStreamException {
		document.writeCharacters(string);
	}
}
