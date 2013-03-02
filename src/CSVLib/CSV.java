package CSVLib;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.*;

public class CSV{
    protected ArrayList<ArrayList<Object>> table = new ArrayList<ArrayList<Object>>();//table of the valueszz
    //Read csv. In input values file's name and csv file's separator
    public static CSV readFromFile(String fileName, String separator, IProgresible callback){
        String expr = "\"[^\"]*\"" + separator;
        String expr2 = "\"[^\"]*\"";
        Pattern pattern = Pattern.compile(expr);
        Pattern pattern2 = Pattern.compile(expr2);
        long totalLength = (new File(fileName)).length();
        long currentLength = 0;

        Matcher matcher;

        CSV csv = new CSV();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            int index;
            String line;
            String buf = "";
            while ((line = br.readLine()) != null){
                if((int)line.charAt(0) == 65279) line = line.substring(1);
                csv.addLine();
                currentLength += line.getBytes().length + 1;
                while ((index = line.indexOf(separator)) >= 0){
                    buf +=  line.substring(0, index + 1);
                    line = line.substring(index + 1);

                    if(buf.length() > 1){
                        matcher = pattern.matcher(buf);

                        if(matcher.matches()){
                            buf = buf.substring(1, buf.length() - 2);
                            csv.addColumn(buf);
                            buf = "";
                        }else{
                            if(buf.charAt(buf.length() - 2) == '\"'){
                                if(line.length() > 0)
                                    if(line.charAt(0) != '\"'){
                                        csv.addColumn(buf.substring(0, buf.length() - 1));
                                        buf = "";
                                    }
                            }else{
                                buf = buf.replaceAll("\"" + separator + "\"", separator);
                                csv.addColumn(buf.substring(0, buf.length() - 1));
                                buf = "";
                            }
                        }
                    }
                    else{
                        csv.addColumn("");
                        buf = "";
                    }
                }
                buf += line;
                matcher = pattern2.matcher(buf);
                if(matcher.matches()){
                    buf = buf.substring(1, buf.length() - 1);
                    csv.addColumn(buf);
                    buf = "";
                }
                else{
                    csv.addColumn(buf);
                    buf = "";
                }
                callback.setProgress((int)(100 * currentLength / totalLength));
            }
            br.close();
            fr.close();
            return csv;
        }catch (Exception e) {
            return csv;
        }
    }

    private void addLine(){
        table.add(new ArrayList<Object>());
    }

    private void addColumn(Object ob){
        table.get(table.size() - 1).add(ob);
    }




    public static CSV readFromFile(File file, String separator, IProgresible callback){
        return readFromFile(file.getAbsolutePath(), separator, callback);
    }

    public void setCell(Object ob, int line, int column){
        if (line >= table.size() - 1) addLines(line);
        if (column >= table.get(line).size() - 1) addColumns(line, column);
        table.get(line).set(column, ob);
    }

    public String getCell(int line, int column){
        return table.get(line).get(column).toString();
    }

    public int getLinesNumber(){
        return table.size();
    }

    public int getColumnsNumber(int line){
        return table.get(line).size();
    }

    public void writeToFile(String fileName, String separator) throws Exception{
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        for (ArrayList<Object> line : table){
            for (Object column : line){
                String buf = column.toString();
                buf = buf.replaceAll(separator,"\"" + separator + "\"");

                bw.write(buf + separator);
            }
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }

    public void writeToFile(File file, String separator) throws Exception{
        writeToFile(file.getAbsolutePath(), separator);
    }

    private void addLines(int line){
        for (int i = table.size(); i <= line; i++)
            table.add(new ArrayList<Object>());
    }

    private void addColumns(int line, int column){
        for (int i = table.get(line).size(); i <= column; i++)
            table.get(line).add(null);
    }
}