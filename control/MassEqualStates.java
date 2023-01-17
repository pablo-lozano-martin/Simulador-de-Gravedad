package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;


public class MassEqualStates implements StateComparator // Comparador de cuerpos
{
	public boolean equal(JSONObject s1, JSONObject s2)
	{
		if(s1.getDouble("time") == s2.getDouble("time"))
		{
			JSONArray js1 = s1.getJSONArray("bodies");
		    JSONArray js2 = s2.getJSONArray("bodies");

		    if(js1.length() == js2.length())	
		    {
		    	for(int i = 0; i < js1.length(); i++)
			    {
			    	if (!js1.getJSONObject(i).getString("id").equals(js2.getJSONObject(i).getString("id") ) || js1.getJSONObject(i).getDouble("m") != js2.getJSONObject(i).getDouble("m")) 
			    	{
			    		return false;
			    	}
			    }
		    	return true;
		    }
		}
		return false;
	}
}

