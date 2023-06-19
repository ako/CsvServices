package tests.impl;

import csvservices.impl.DataGenerator;
import csvservices.impl.SequenceNumberFaker;
import net.datafaker.Faker;

public class TestMain {
    public static void main(String arg[]) {
        System.out.println("main");
        DataGenerator dg = new DataGenerator();
        SequenceNumberFaker numberFaker = new SequenceNumberFaker();
        //String result = dg.generate("#{csv ';','\"','true','10','Firstname','#{Name.first_name}','Lastname','#{Name.last_name}'}");
        //System.out.print(result);
        //    String result = dg.generate("#{csv ';','\"','true','10','EmployeeId','#{Number.number_between ''1'',''10''}','Firstname','#{Name.first_name}','Lastname','#{Name.last_name}'}");
        //  System.out.print(result);
        //String result2 = dg.generate("#{csv ';','\"','true','10','EmployeeId','#{SequenceNumber.next ''empId''}','Firstname','#{Name.first_name}','Lastname','#{Name.last_name}'}");
        //String data = dg.generate("#{csv ';','\"','true','10','EmployeeId*','#{sequenceNumber.next ''empId''}','Firstname','#{Name.first_name}','Lastname','#{Name.last_name}'}");
        //System.out.print(data);
        String data2 = dg.generate("EmployeeId*,Firstname,Lastname",
                "#{sequenceNumber.next ''empId''},#{Name.first_name},#{Name.last_name}",",",10L);
        System.out.print(data2);
    }
}
