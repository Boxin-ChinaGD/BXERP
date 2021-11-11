package wpos.model;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public class Intent {
    Map<String,Integer> intMap = new HashMap<String, Integer>();
    Map<String,String> stringMap = new HashMap<String, String>();


    public void putExtra(String name, int value){
        intMap.put(name,value);
    }

    public void putExtra(String name, String value){
        stringMap.put(name,value);
    }

    public int getIntExtra(String name, int defaultValue){
        if (intMap.get(name)!=null){
            return intMap.get(name);
        }else {
            return defaultValue;
        }

    }

    public String getStringExtra(String name){
        if (stringMap.get(name) == null){
            return "";
        }else {
            return stringMap.get(name);
        }
    }


}
