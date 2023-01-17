package simulator.control;

import org.json.JSONObject;

public class NotEqualStatesException extends Exception 
{
	private static final long serialVersionUID = 1L;
	private JSONObject _actual, _expected, _ob1, _ob2;
	private int _step;

	NotEqualStatesException(JSONObject exp, JSONObject act, JSONObject ob1, JSONObject ob2, int step) // Excepción que salta cuando el cuerpo actual difiere del esperado
	{
		super("States are different at step " + step + System.lineSeparator() + 
			" Actual: " + act + System.lineSeparator() + 
			" Expected: " + exp + System.lineSeparator() +
			" Actual Body: " + ob1 + System.lineSeparator() +
			" Expected Body: " + ob2 + System.lineSeparator() );
		_actual = act;
		_expected = exp;
		_step = step;
		_ob1 = ob1;
		_ob2 = ob2;
	}
	
	public JSONObject getBodyActual()
	{
		return _ob1;
	}

	public JSONObject getBodyExpected()
	{
		return _ob2;
	}

	public JSONObject getActual()
	{
		return _actual;
	}

	public JSONObject getExpected()
	{
		return _expected;
	}

	public int getStep() 
	{
		return _step;
	}
}
