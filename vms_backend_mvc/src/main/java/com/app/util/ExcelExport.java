package com.app.util;

import java.io.ByteArrayInputStream;
import org.apache.poi.ss.usermodel.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.app.dto.CompanyDTO;
import com.app.dto.ExcelExportDtoForUser;

@Component
public class ExcelExport {

	public String[] getHeader(Object object) {
		Class<?> class1 = object.getClass();

		Field[] declaredFields = class1.getDeclaredFields();

		String setFlides[] = new String[declaredFields.length];
		for (int i = 0; i < setFlides.length; i++) {
			setFlides[i] = declaredFields[i].getName();
		}

		return setFlides;
	}

	public ByteArrayInputStream datatoexcel(List<?> objlist) throws IOException {

		String[] HEADERS = getHeader(objlist.get(0));

		Workbook workbook = new XSSFWorkbook();

		CellStyle greenStyle = createCellStyle(workbook, IndexedColors.GREEN.getIndex());
		CellStyle orangeStyle = createCellStyle(workbook, IndexedColors.ORANGE.getIndex());
		CellStyle yellowStyle = createCellStyle(workbook, IndexedColors.YELLOW.getIndex());
		CellStyle RedStyle = createCellStyle(workbook, IndexedColors.RED.getIndex());
		Font boldFont = workbook.createFont();
		boldFont.setBold(true);

		// Set the bold Font to the yellowStyle

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			Sheet createSheet = workbook.createSheet("data");

			Row row = createSheet.createRow(0);

			for (int i = 0; i < HEADERS.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(HEADERS[i].toUpperCase());
				yellowStyle.setFont(boldFont);
				cell.setCellStyle(yellowStyle);
			}

			int rowIndex = 1;
			DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Define a format without trailing zeros

			for (Object obj : objlist) {
				Field[] declaredFields = obj.getClass().getDeclaredFields();
				Row createRow = createSheet.createRow(rowIndex);

				for (int i = 0; i < declaredFields.length; i++) {
					Field field = declaredFields[i];
					Object object = field.get(obj);
					Cell cell = createRow.createCell(i);
					if (object != null) {
						if (object instanceof Double) {
							double doubleValue = (Double) object;
							if (doubleValue == (int) doubleValue) {

								cell.setCellValue(Integer.toString((int) doubleValue));
							} else {

								cell.setCellValue(decimalFormat.format(object));
							}
						} else {

							cell.setCellValue(object.toString());
						}

					} else {
						cell.setCellValue("NULL");
						cell.setCellStyle(orangeStyle);
					}
				}
				rowIndex++;

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());

		} catch (Exception e) {

			e.printStackTrace();

			System.out.println("falid to export datas");

		} finally {
			workbook.close();
		}

		return null;
	}

	private static CellStyle createCellStyle(Workbook workbook, short colorIndex) {
		CellStyle style = workbook.createCellStyle();
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(colorIndex);
		return style;
	}

	public ByteArrayInputStream datatoexcelforUser(List<ExcelExportDtoForUser> convertUsersToExcelExportDto) {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Employee Data");

			CellStyle greenStyle = createCellStyle(workbook, IndexedColors.GREEN.getIndex());
			CellStyle orangeStyle = createCellStyle(workbook, IndexedColors.ORANGE.getIndex());
			CellStyle yellowStyle = createCellStyle(workbook, IndexedColors.YELLOW.getIndex());
			CellStyle RedStyle = createCellStyle(workbook, IndexedColors.RED.getIndex());
			CellStyle blueStyle = createCellStyle(workbook, IndexedColors.LIGHT_BLUE.getIndex());
			CellStyle lightRedStyle = createCellStyle(workbook, IndexedColors.LIGHT_TURQUOISE.getIndex());

			Font boldFont = workbook.createFont();
			boldFont.setBold(true);

//			CellStyle boldStyle = createCellStyle(workbook, IndexedColors.GREY_25_PERCENT.getIndex());
//	        boldStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//	        boldStyle.setFont(boldFont);
	        
	        CellStyle boldStyle = workbook.createCellStyle();
	        boldStyle.setFont(boldFont);
	        boldStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        boldStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("S.No");
			headerRow.createCell(1).setCellValue("First Name ");
			headerRow.createCell(2).setCellValue("Last Name ");
			headerRow.createCell(3).setCellValue("Email ");
			headerRow.createCell(4).setCellValue("Phone ");
			headerRow.createCell(5).setCellValue("Gender ");
			headerRow.createCell(6).setCellValue("Pincode ");
			headerRow.createCell(7).setCellValue("Company Name ");
			headerRow.createCell(8).setCellValue("Role");
			headerRow.createCell(9).setCellValue("State ");
			headerRow.createCell(10).setCellValue("City ");
			headerRow.createCell(11).setCellValue("Department ");
			headerRow.createCell(12).setCellValue("Emp Code ");
			headerRow.createCell(13).setCellValue("DOB ");

			for (int i = 0; i <= 13; i++) {
				headerRow.getCell(i).setCellStyle(boldStyle);
			}

			int rowNum = 1;
			for (ExcelExportDtoForUser user : convertUsersToExcelExportDto) {
				Row dataRow = sheet.createRow(rowNum++);
				dataRow.createCell(0).setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(user.getFirstName() != null ? user.getFirstName() : "");
				dataRow.createCell(2).setCellValue(user.getLastName() != null ? user.getLastName() : "NA");

				dataRow.createCell(3).setCellValue(user.getEmail() + " ");

				dataRow.createCell(4).setCellValue(user.getPhone() + " ");
				dataRow.createCell(5).setCellValue(user.getGender());

				dataRow.createCell(6).setCellValue(user.getPincode());

				dataRow.createCell(7).setCellValue(user.getCompanyName());

				dataRow.createCell(8).setCellValue(user.getRoleName());

				dataRow.createCell(9).setCellValue(user.getStateName());
				dataRow.createCell(10).setCellValue(user.getCityName());
				dataRow.createCell(11).setCellValue(user.getDepartmentName());
				dataRow.createCell(12).setCellValue(user.getEmpCode());

				dataRow.createCell(13).setCellValue(user.getDob());

			}

			for (int i = 0; i <= 13; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static ByteArrayInputStream companydataToExcel(List<CompanyDTO> allCompanies) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Companies Data");

			Row headerRow = sheet.createRow(0);

			String[] headers = { "S.No ", "Name ", "Email ", "Phone Number ", "Address ", "Industry ", "About Us ", "State ",
					"City ", "Pincode ", "User Limit ", "Building ID " ,"Building " };

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowNum = 1;
			for (CompanyDTO company : allCompanies) {
				Row dataRow = sheet.createRow(rowNum++);

				dataRow.createCell(0).setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(company.getName());
				dataRow.createCell(2).setCellValue(company.getEmail());
				dataRow.createCell(3).setCellValue(company.getPhoneNumber());
				dataRow.createCell(4).setCellValue(company.getAddress());
				// Assuming MultipartFile image is stored as a string in your CompanyDTO
				dataRow.createCell(5).setCellValue(company.getIndustry());
				dataRow.createCell(6).setCellValue(company.getAboutUs());
				dataRow.createCell(7).setCellValue(company.getState() != null ? company.getState().getName() : "");
				dataRow.createCell(8).setCellValue(company.getCity() != null ? company.getCity().getName() : "");
				dataRow.createCell(9).setCellValue(company.getPincode());
				dataRow.createCell(10).setCellValue(company.getUserLimit());
				dataRow.createCell(11)
				.setCellValue(company.getBuilding().getBuildingId());
				dataRow.createCell(12)
						.setCellValue(company.getBuilding().getName() != null ? company.getBuilding().getName() : "");

			}
			
			  for (int i = 0; i <= headers.length; i++) {
		            sheet.autoSizeColumn(i);
		        }

			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
