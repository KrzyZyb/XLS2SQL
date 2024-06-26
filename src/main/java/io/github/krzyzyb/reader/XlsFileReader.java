package io.github.krzyzyb.reader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.github.krzyzyb.reader.entities.ImportedFile;

public class XlsFileReader {

  public static ImportedFile read(Path path, String tableName) throws FileNotFoundException {
    byte[] fileContent =  loadExcelFile(path);
    ImportedFile persistedFile;
    try {
      assert fileContent != null;
      try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
        persistedFile = new ImportedFile(new XSSFWorkbook(inputStream), tableName);
      }
    } catch (Exception ex) {
      throw new NotOfficeXmlFileException("Not XML file");
    }
    return persistedFile;
  }

  private static byte[] loadExcelFile(Path path) throws FileNotFoundException {
    InputStream inputStream = new FileInputStream(path.toFile());
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int bytesRead;
      byte[] data = new byte[1024];
      while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, bytesRead);
      }
      buffer.flush();
      return buffer.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  public static ImportedFile readFile(Path inputFile, String tableName){
    try {
      return read(inputFile, tableName);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Xls file not found");
    }
  }

  public static String readFileName(Path path){
    String fileName = path.getFileName().toString();
    int lastDotIndex = fileName.lastIndexOf('.');
    if (lastDotIndex != -1) {
      return fileName.substring(0, lastDotIndex);
    }
    return fileName;
  }
}
