import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    Integer[][] matrix;

    public CSVReader() {
        String file = "src/resources/csv/map.csv";
        List<Integer[]> rowList = new ArrayList<Integer[]>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                Integer[] intArray = new Integer[lineItems.length];
                for (int i = 0; i < lineItems.length; i++) {
                    intArray[i] = Integer.parseInt(lineItems[i]);
                }
                rowList.add(intArray);
            }
            br.close();
        } catch (Exception e) {

        }
        matrix = new Integer[rowList.size()][];
        for (int i = 0; i < rowList.size(); i++) {
            Integer[] row = rowList.get(i);
            matrix[i] = row;
        }
    }

    public Integer[][] export() {
        return matrix;
    }
}
