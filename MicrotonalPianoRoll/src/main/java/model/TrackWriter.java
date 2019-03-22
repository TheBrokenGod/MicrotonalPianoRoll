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
			document.writeStartElement("track");
			document.writeAttribute("lower", Double.toString(track.lowerFrequency));
			document.writeAttribute("higher", Double.toString(track.higherFrequency));
			document.writeAttribute("steps", Integer.toString(track.stepsInBetween));
			document.writeAttribute("start", Integer.toString(track.lowestOffset));
			document.writeAttribute("keys", Integer.toString(track.numKeys));
			document.writeAttribute("bpm", Integer.toString(track.firstMeasure().getBPM()));
			Integer bpm = track.firstMeasure().getBPM();
			boolean writeBPM = false;
			for(Measure measure : track) {
				if(measure.getBPM() != bpm) {
					bpm = measure.getBPM();
					writeBPM = true;
				}
				for(Note note : measure) {
					document.writeStartElement("note");
					document.writeAttribute("length", note.length.name());
					if(writeBPM) {
						document.writeAttribute("bpm", Integer.toString(bpm));						
						writeBPM = false;
					}
					document.writeCharacters(note.values().map(v -> v.toString()).collect(Collectors.joining(" ")));
					document.writeEndElement();
					
				}
			}
			document.writeEndElement();
			document.writeEndDocument();
			serializer.serializeNode(document.getDocumentNode());
			document.close();
			serializer.close();
		}
		catch (XMLStreamException | SaxonApiException e) {
			throw new RuntimeException(e);
		}
	}
}
