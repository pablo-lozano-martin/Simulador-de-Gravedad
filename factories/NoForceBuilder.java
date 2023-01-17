package simulator.factories;

import org.json.JSONObject;
import simulator.model.NoForce;
import simulator.model.ForceLaws;


public class NoForceBuilder extends Builder<ForceLaws>
{	
	static String _type = "nf";
	static String _desc = "no force";
	
	public NoForceBuilder()
	{
		super(_type, _desc);
	}

	protected ForceLaws createTheInstance(JSONObject js) // Comprueba que los datos son válidos. Si lo son crea un nuevo Body, de lo contrario devolverá null/producirá una excepción
	{
		if(!js.getString("type").equals(_type)) {return null;} 
		return new NoForce();
	}
}
