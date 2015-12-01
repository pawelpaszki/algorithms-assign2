package utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * 
 * @author Pawel Paszki
 * this class is a JSONSerializer. it is currently resting and not 
 * being used due to lower performance comparing to XMLSerializer.
 * it is however fully tested and can be easily used by changing 
 * type of Serializer in RecommenderAPI class
 *
 */
public class JSONSerializer implements Serializer {

	private Stack<Object> stack = new Stack<Object>();
	private File file;

	/**
	 * JSONSerializer constructor
	 * @param file passed to specify the file to be written to and read from
	 */
	public JSONSerializer(File file) {
		this.file = file;
	}

	/**
	 * pushes an object to the stack
	 */
	@Override
	public void push(Object o) {
		stack.push(o);

	}

	/**
	 * pops an object from the stack
	 */
	@Override
	public Object pop() {
		return stack.pop();
	}

	/**
	 * reads the file into the stack
	 */
	@SuppressWarnings("unchecked")
	public void read() throws Exception {
		ObjectInputStream is = null;

		try {
			XStream xstream = new XStream(new JettisonMappedXmlDriver());
			is = xstream.createObjectInputStream(new FileReader(file));
			stack = (Stack<Object>) is.readObject();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * writes objects to the stack
	 */
	public void write() throws Exception {
		ObjectOutputStream os = null;
		try {
			XStream xstream = new XStream(new JettisonMappedXmlDriver());
			os = xstream.createObjectOutputStream(new FileWriter(file));
			xstream.setMode(XStream.NO_REFERENCES);
			os.writeObject(stack);
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

}
