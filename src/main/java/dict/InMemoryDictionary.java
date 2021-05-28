package dict;

import java.io.*;
import java.util.TreeMap;


/**
 * Implements a persistent dictionary that can be held entirely in memory.
 * When flushed, it writes the entire dictionary back to a file.
 * 
 * The file format has one keyword per line:
 * <pre>word:def1:def2:def3,...</pre>
 * 
 * Note that an empty definition list is allowed (in which case the entry would have the form: <pre>word:</pre> 
 * 
 * @author talm
 *
 */
public class InMemoryDictionary extends TreeMap<String,String> implements PersistentDictionary  {
	private static final long serialVersionUID = 1L; // (because we're extending a serializable class)

	private File file;


	public InMemoryDictionary(File dictFile) {
		super();
		this.file = dictFile;
	}
	
	@Override
	public void open() throws IOException {
			/* Stop if the file is empty (-nothing to load in)*/
		if(!this.file.canRead()) return;

		FileReader fr = null;
		BufferedReader br = null;
		String key, value , line;
		int colonIndex;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
				/* read from file to map
			     * word to key . def to value*/
			while ((line = br.readLine()) != null) {
				colonIndex = line.indexOf(':');
				key = line.substring(0, colonIndex);
				value = line.substring(colonIndex + 1);
				this.put(key, value);
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}finally {
			br.close();
			fr.close();
		}

	}

	@Override
	public void close() throws IOException {
		FileWriter fw = null;
		BufferedWriter bw = null;
		String endLine = System.lineSeparator();
		String word, def;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
				/* write back from map to file
				*  key to word . value to def */
			for (String key : this.keySet()) {
				word = key;
				def = this.get(key);
				bw.write(word + ":" + def + endLine);
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}finally {
			bw.close();
			fw.close();
		}
	}
	
}
