package edu.cuny.brooklyn.tandem.helper;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class IOUtil
{
    public static int[] parseRepeatString(String s)
    {
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter("\\D+");
        int[] points = new int[3];
        points[0] = scanner.nextInt();
        points[1] = scanner.nextInt();
        try
        {
            points[2] = scanner.nextInt();
        }
        catch (Exception e)
        {
        }
        return points;
    }

    public static String getStringFromFile(String inputFile)
    {
        InputStream input = SwingUtil.class.getClassLoader().getResourceAsStream(inputFile);
        String fileString = new Scanner(input).useDelimiter("\\Z").next();
        return fileString;
    }

    public static String getStringFromFile(String inputFile, int offset, int length) throws IOException
    {
        FileReader reader = new FileReader(inputFile);
        char[] cbuf = new char[length];
        reader.read(cbuf, offset, length);
        return new String(cbuf);
    }
}
