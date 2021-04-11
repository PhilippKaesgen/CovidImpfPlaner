/**
 * CovidImpfplaner
 * Dieses Programm unterstützt Arztpraxen bei der Organisation der Impfungen
 * gegen COVID-19
 *
 * CovidImpfPlaner  Copyright (C) 2021  Philipp Käsgen
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *
 * pkaesgen(AT)freenet.de
 */
package kaesgen.med;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FileHandlerTest {


    @Test    
    public void testEncryptingAndDecryptingStringInFile() {

        FileHandler dut = new FileHandler();
        String fileName = "newFile.txt";
        String password = "key";

        List<Serializable> golden = new ArrayList<>();
        golden.add("content");

        try{
            dut.saveFile(password,fileName,golden);
        }
        catch(Exception e){
            fail("Exception during saving");
        }



        try{
            List<String> result = dut.loadFile(String.class, password,fileName);
           
            assertEquals(golden.get(0),result.get(0));
        }
        catch(FileNotFoundException e){
            fail("File not found");
        }
        catch(IOException e){
            e.printStackTrace();
            fail("File couldn't be read");
        }
        catch(NoSuchAlgorithmException e){
            fail();
        }
        catch(InvalidKeySpecException e){
            fail("Couldn't generaet secret key");
        }
        catch(InvalidKeyException e){
            fail("Secret key didn't work");
        }
        catch(Exception e){
            fail("You messed up");
        }
    }


    @Test
    public void encryptAndDecryptPatientInFile(){

        FileHandler dut = new FileHandler();
        String fileName = "newFile.txt";
        String password = "key";

        PatientEntry p = new PatientEntry(5,"Schmidt","Hans",LocalDate.of(1970,1,1),"COPD","01234/234512","",null,false,VaccineBrand.Astrazeneca,null,false,VaccineBrand.JohnsonNJohnson);
        
        List<PatientEntry> golden = new ArrayList<>();

        golden.add(p);
       

        try{
            dut.saveFile(password,fileName,golden);
        }
        catch(Exception e){
            fail("Exception during saving");
        }



        try{
            List<PatientEntry> result = dut.loadFile(PatientEntry.class, password,fileName);

                      
            assertEquals(golden.get(0).getLastName(),result.get(0).getLastName());
        }
        catch(FileNotFoundException e){
            fail("File not found");
        }
        catch(IOException e){
            e.printStackTrace();
            fail("File couldn't be read");
        }
        catch(NoSuchAlgorithmException e){
            fail();
        }
        catch(InvalidKeySpecException e){
            fail("Couldn't generaet secret key");
        }
        catch(InvalidKeyException e){
            fail("Secret key didn't work");
        }
        catch(Exception e){
            e.printStackTrace();
            fail("You messed up");
        }

    }


    @Test
    public void encryptAndDecryptListInFile(){

        FileHandler dut = new FileHandler();
        String fileName = "newFile.txt";
        String password = "key";

        List<PatientEntry> golden = new ArrayList<>();
        PatientEntry patient1 = new PatientEntry(5,"Schmidt","Hans",LocalDate.of(1970,1,1),"COPD","01234/234512","",null,false,VaccineBrand.Moderna,null,false,VaccineBrand.JohnsonNJohnson);
        PatientEntry patient2 = new PatientEntry(5,"Ludwig","Karl",LocalDate.of(1950,5,4),"Krebs","01234/234512","",null,false,VaccineBrand.Novavax,null,false,VaccineBrand.JohnsonNJohnson);
        golden.add(patient1);
        golden.add(patient2);


        try{
            dut.saveFile(password,fileName,golden);
        }
        catch(Exception e){
            fail("Exception during saving");
        }



        try{
            List<PatientEntry> result = dut.loadFile(PatientEntry.class, password,fileName);

            System.out.println(golden.get(0) + " " +result.get(1));
            
            assertEquals(golden.get(0).getFirstName(), result.get(0).getFirstName());
            assertEquals(golden.get(1).getFirstName(), result.get(1).getFirstName());
            
            
        }
        catch(FileNotFoundException e){
            fail("File not found");
        }
        catch(IOException e){
            e.printStackTrace();
            fail("File couldn't be read");
        }
        catch(NoSuchAlgorithmException e){
            fail();
        }
        catch(InvalidKeySpecException e){
            fail("Couldn't generaet secret key");
        }
        catch(InvalidKeyException e){
            fail("Secret key didn't work");
        }
        catch(Exception e){
            e.printStackTrace();
            fail("You messed up");
        }

    }


    @Test
    public void encryptAndDecryptShouldFailDueToWrongPassword(){

        FileHandler dut = new FileHandler();
        String fileName = "newFile.txt";
        String passwordCorrect = "key";
        String passwordFail = "key2";

        List<Serializable> golden = new ArrayList<>();
        PatientEntry patient1 = new PatientEntry(5,"Schmidt","Hans",LocalDate.of(1970,1,1),"COPD","01234/234512","",null,false,VaccineBrand.JohnsonNJohnson,null,false,VaccineBrand.JohnsonNJohnson);
        PatientEntry patient2 = new PatientEntry(5,"Ludwig","Karl",LocalDate.of(1950,5,4),"Krebs","01234/234512","",null,false,VaccineBrand.JohnsonNJohnson,null,false,VaccineBrand.JohnsonNJohnson);
        golden.add(patient1);
        golden.add(patient2);


        try{
            dut.saveFile(passwordCorrect,fileName,golden);
        }
        catch(Exception e){
            fail("Exception during saving");
        }



        try{
            List<PatientEntry> result =dut.loadFile(PatientEntry.class, passwordFail,fileName);
            System.out.println(result);

            fail("File opening successful although password wrong");
        }
        catch(FileNotFoundException e){
            fail("File not found");
        }
        catch(IOException e){
            e.printStackTrace();
            //fail("File couldn't be read");
        }
        catch(NoSuchAlgorithmException e){
            fail();
        }
        catch(InvalidKeySpecException e){
            fail("Couldn't generaet secret key");
        }
        catch(InvalidKeyException e){
            fail("Secret key didn't work");
        }
        catch(Exception e){
            e.printStackTrace();
            fail("You messed up");
        }

    }


 }