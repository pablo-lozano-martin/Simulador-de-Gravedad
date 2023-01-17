package simulator.factories;

import org.json.JSONObject;
import simulator.control.StateComparator;
import simulator.control.MassEqualStates;

public class MassEqualStateBuilder extends Builder<StateComparator> 
{
	static String _type = "masseq";
	static String _desc = "mass equal";
	
	public MassEqualStateBuilder()
	{
		super(_type, _desc);
	}
	
	protected StateComparator createTheInstance(JSONObject js) // Comprueba que los datos son válidos. Si lo son crea un nuevo Body, de lo contrario devolverá null/producirá una excepción
	{
		if(!js.getString("type").equals(_type)) {return null;} 
		return new MassEqualStates();
	}
	
	protected JSONObject createData() 	
	{
		JSONObject data = new JSONObject();
		return data;
	}
}
