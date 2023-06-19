package csvservices.impl;

import net.datafaker.Faker;

public class DataGenerator {
    public static void main(String args[]) {
        System.out.println("main");
    }

    public DataGenerator() {
    }

    public String generate(String expression) {
        SequenceNumberFaker numberFaker = new SequenceNumberFaker();
        System.out.println(String.format("generate: %s",expression));
        String data = null;
        try {
            data = numberFaker.expression(expression);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return data;

    }

    public String generate(String header, String columnExpressions, String delimiter, Long recordCount) {
        String cols = "";
        String[] patterns = columnExpressions.replace("\n","").split(delimiter);
        String[] headers = header.replace("\n","").split(delimiter);
        for (int i = 0; i < headers.length; i++) {
            if (i != 0) {
                cols += ",";
            }
            cols += "'" + headers[i] + "','" + patterns[i] + "'";
        }
        String dataFakerExpression = String.format("#{csv '%s','\"','true','%d',%s}", delimiter,recordCount, cols);
        return this.generate(dataFakerExpression);
    }
}
