package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> 
{
	static String _type = "basic";
	static String _desc = "default body";
	
	public BasicBodyBuilder()
	{
		super(_type, _desc);
	}
	
	protected Body createTheInstance(JSONObject js)	// Comprueba que los datos son válidos. Si lo son crea un nuevo Body, de lo contrario devolverá null/producirá una excepción
	{
		if(!js.getString("type").equals(_type)) {return null;}
		js = js.getJSONObject("data"); // Para acceder a los valores de "data" fácilmente

		try
		{
			double m = js.getDouble("m");
			String id = js.getString("id");

			JSONArray p = js.getJSONArray("p");
			JSONArray v = js.getJSONArray("v");

			Vector2D pos = new Vector2D(p.getDouble(0), p.getDouble(1));
			Vector2D vel = new Vector2D(v.getDouble(0), v.getDouble(1));
			return new Body(id, vel, pos, m);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Illegal argument");
		}
	}
	
	protected JSONObject createData() 
	{
        JSONObject js = new JSONObject();
        js.put("id", "the identifier");
        js.put("v", "the velocity");
        js.put("p"," the position");
        js.put("m", "the mass");
        return js;
    }
}
