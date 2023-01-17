package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> 
{
	static String _type = "nlug";
	static String _desc = "Newton's law universal gravitation";
	
	public NewtonUniversalGravitationBuilder()
	{
		super(_type, _desc);
	}

	protected ForceLaws createTheInstance(JSONObject js)	// Comprueba que los datos son válidos. Si lo son crea un nuevo Body, de lo contrario devolverá null/producirá una excepción
	{
		if(!js.getString("type").equals(_type)) {return null;}
		js = js.getJSONObject("data");  // Para acceder a los valores de "data" fácilmente

		try
		{
			double gConst = js.has("g") ? js.getDouble("g") : 6.67E-11;
			return new NewtonUniversalGravitation(gConst);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Illegal argument");
		}

	}
	
	protected JSONObject createData()  
	{
		JSONObject js = new JSONObject();
		
		js.put("g", "gravitation constant");
		
		return js;
	}
}
