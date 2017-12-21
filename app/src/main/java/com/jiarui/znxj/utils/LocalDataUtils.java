package com.jiarui.znxj.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LocalDataUtils
{
    public static String getFromAssets(Context context, String fileName)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getFromRaw(Context context, int rawId)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(rawId));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
