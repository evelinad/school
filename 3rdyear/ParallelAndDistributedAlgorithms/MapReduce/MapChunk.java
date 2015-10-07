import java.util.HashMap;

//retine un hashmap pt fiecare cuvand + nr de aparitii al acestuia
public class MapChunk {
	public String content;
public	HashMap<String, Integer> wordhashmap;
	public MapChunk(String content)
	{
		this.content=content;
		wordhashmap= new HashMap<String, Integer>();
	}
}
