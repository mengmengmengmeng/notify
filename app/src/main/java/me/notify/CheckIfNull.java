package me.notify;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class CheckIfNull {

    public String check_if_null(String string){
        if(string.equals("null")){
            string = "";
        }
        return string;
    }
}