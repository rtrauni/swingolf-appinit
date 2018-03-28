package at.swingolf.appinit.neo4jconverter;


import org.apache.commons.text.StringEscapeUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Neo4jBaseDto {
    abstract Object[] getKeyInternal();
    public StringBuffer createStringBuffer(){
        return new StringBuffer("// #### "+getClass().getSimpleName()+"\n");
    }
    public String getKey(){
        return getClass().getSimpleName()+"_"+Arrays.stream(getKeyInternal()).map(object -> object.toString()).map(string -> StringEscapeUtils.escapeHtml4(string.replaceAll(" ","_").replaceAll("-","_"))).collect(Collectors.joining("_"));
    }


}
