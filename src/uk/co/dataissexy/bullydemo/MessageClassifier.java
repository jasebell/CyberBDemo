package uk.co.dataissexy.bullydemo;

import net.sf.classifier4J.ClassifierException;
import net.sf.classifier4J.vector.HashMapTermVectorStorage;
import net.sf.classifier4J.vector.TermVectorStorage;
import net.sf.classifier4J.vector.VectorClassifier;

public class MessageClassifier {
	public double rateBullyLevel(String message) throws ClassifierException {
		TermVectorStorage storage = new HashMapTermVectorStorage();
		VectorClassifier vc = new VectorClassifier(storage);

		// this list could be got from anywhere.
		vc.teachMatch("blacklist", "why hurt harm ugly kill die you");
		return vc.classify("blacklist", message);

	}

	public static void main(String[] args) {
		try {
			MessageClassifier mc = new MessageClassifier();
			String message = "You're ugly why don't you harm yourself?";
			
			System.out.println("Rating for: " + message + " is " + mc.rateBullyLevel(message));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
