package com.product.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.product.model.Category;
import com.product.model.Product;
import com.product.serviceimpl.CategoryServiceImpl;

@Component
public class Helper {
	
	@Autowired
	CategoryServiceImpl categoryServiceImpl;
	
	public static boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
        
    }
    
    public List<Product> convertExcelToListOfProduct(InputStream is) {
    	
    	
        List<Product> list = new ArrayList<>();

        try {


            XSSFWorkbook workbook= new XSSFWorkbook(is);

            XSSFSheet sheet = workbook.getSheet("data");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cid = 0;

                Product p = new Product();

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            p.setName(cell.getStringCellValue());
                            break;
                        case 1:
                        	int categoryId=(int)cell.getNumericCellValue();
                        	Optional<Category> category=categoryServiceImpl.findById(categoryId);
                            p.setCategory(category.get());
                            break;
                        case 2:
                            p.setDescription(cell.getStringCellValue());
                            break;
                        case 3:
                        	p.setPrice(cell.getNumericCellValue());
                            break;
                        case 4:
                        	p.setStock((int)cell.getNumericCellValue());
                        	break;
                        case 5:
                        	p.setImage(cell.getStringCellValue());
                        	break;
                        default:
                            break;
                    }
                    cid++;

                }

                list.add(p);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
}
