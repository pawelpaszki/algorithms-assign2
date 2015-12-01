package utils;


/**
 * 
 * @author Eamonn DeLeastar
 *
 */
public interface Serializer {
	/**
	 * pushes an object to the stack
	 */
	void push(Object o);

	/**
	 * pops an object from the stack
	 */
	Object pop();

	/**
	 * reads the file into the stack
	 */
	void write() throws Exception;

	/**
	 * writes objects to the stack
	 */
	void read() throws Exception;
}