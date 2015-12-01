package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

/**
 * 
 * @author Pawel Paszki 
 * XMLSerializer used to manage serialisation
 */
public class XMLSerializer implements Serializer {

	@SuppressWarnings("rawtypes")
	private Stack stack = new Stack();
	private File file;

	/**
	 * XMLSerializer constructor
	 * 
	 * @param file
	 *            passed to specify the file to be written to and read from
	 */
	public XMLSerializer(File file) {
		this.file = file;
	}

	/**
	 * pushes an object to the stack
	 */
	@SuppressWarnings("unchecked")
	public void push(Object o) {
		stack.push(o);
	}

	/**
	 * pops an object from the stack
	 */
	public Object pop() {
		return stack.pop();
	}

	/**
	 * reads the file into the stack
	 */
	@SuppressWarnings("rawtypes")
	public void read() throws Exception {
		ObjectInputStream is = null;

		try {
			XStream xstream = new XStream(new DomDriver());
			is = xstream.createObjectInputStream(new FileReader(file));
			stack = (Stack) is.readObject();
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
			XStream xstream = new XStream(new DomDriver());
			os = xstream.createObjectOutputStream(new FileWriter(file));
			os.writeObject(stack);
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
}