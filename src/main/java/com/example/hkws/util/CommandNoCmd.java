package com.example.hkws.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandNoCmd
{
    public static String exeCmd(String commandStr)
    {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            Process p = Runtime.getRuntime().exec("cmd /c start /b "+commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    // public static void main(String[] args) {
    // String commandStr = "ipconfig";
    // System.out.println(Command.exeCmd(commandStr));
    // }
}