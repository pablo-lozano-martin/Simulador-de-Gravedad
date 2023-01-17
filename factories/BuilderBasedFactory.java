
package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class BuilderBasedFactory<T> implements Factory<T>
{
	List<JSONObject> factoryElements;
	List<Builder<T>> builderList;
	
	public BuilderBasedFactory(List<Builder<T>> builderList) 
	{
		this.builderList = new ArrayList<>(builderList);
		factoryElements = new ArrayList<>();

		for (Builder<T> bu : builderList)
		{
			factoryElements.add(bu.getBuilderInfo());
		}
	}

	public T createInstance(JSONObject js) throws IllegalArgumentException	// Si la información suministrada por info es correcta, entonces crea un objeto de tipo T. En otro caso devuelve null 
	{
		T object = null;
		
		if (js != null)
		{
			boolean done = false;
			int i = 0;
			
			while(!done && i < builderList.size())
			{
				object = builderList.get(i).createInstance(js);
				if (object != null)
				{
					done = true;
				}
				i++;
			}
			if (object != null)
			{
				return object;
			}
			else
			{
				throw new IllegalArgumentException("Null object");
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid value of createInstance: Null");
		}
	}
	
    public List<JSONObject> getInfo()
    {
        return factoryElements;
    }
}

