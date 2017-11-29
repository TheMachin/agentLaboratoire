package miage.m2.sid.excel.model;

import miage.m2.sid.EntityManager;
import miage.m2.sid.model.Vaccin;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PoiVaccinDao {

    private String fileName;
    private javax.persistence.EntityManager em = EntityManager.getInstance();


    public PoiVaccinDao(String fileName) {
        this.fileName = fileName;
    }

    public void setAllMaladie() {

        URL url = this.getClass().getResource(fileName);
        File file=null;
        file = new File(url.getPath());
        try{
            final Workbook workbook = WorkbookFactory.create(file);
            final Sheet sheet = workbook.getSheetAt(0);

            int index = 0;
            Row row = sheet.getRow(index++);
            while(row != null){
                rowToMaladieToDb(row);
                row = sheet.getRow(index++);
            }


        }catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }


    }

    private void rowToMaladieToDb(Row row) {

        Vaccin vaccin = new Vaccin();
        vaccin.setNom(row.getCell(0).getStringCellValue());
        vaccin.setVolume(Double.parseDouble(row.getCell(1).getStringCellValue()));
        vaccin.setPrix(Double.parseDouble(row.getCell(2).getStringCellValue().toString()));

        em.getTransaction().begin();
        em.merge(vaccin);
        em.getTransaction().commit();


    }
}
