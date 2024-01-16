package com.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.MeetingDto;
import com.app.emun.MeetingStatus;

public class ExcelHepler {

	public static String[] getHeader(Object object) {
		Class<?> class1 = object.getClass();

		Field[] declaredFields = class1.getDeclaredFields();

		String setFlides[] = new String[declaredFields.length];
		for (int i = 0; i < setFlides.length; i++) {
			setFlides[i] = declaredFields[i].getName();
		}

		return setFlides;
	}

	public static ByteArrayInputStream dataToExcel(List<MeetingDto> meetingsByPagination) {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Meetings Data");

			CellStyle greenStyle = createCellStyle(workbook, IndexedColors.GREEN.getIndex());
			CellStyle orangeStyle = createCellStyle(workbook, IndexedColors.ORANGE.getIndex());
			CellStyle yellowStyle = createCellStyle(workbook, IndexedColors.YELLOW.getIndex());
			CellStyle RedStyle = createCellStyle(workbook, IndexedColors.RED.getIndex());
			CellStyle blueStyle = createCellStyle(workbook, IndexedColors.LIGHT_BLUE.getIndex());
			CellStyle lightRedStyle = createCellStyle(workbook, IndexedColors.LIGHT_TURQUOISE.getIndex());

			Font boldFont = workbook.createFont();
			boldFont.setBold(true);

			CellStyle boldStyle = createCellStyle(workbook, IndexedColors.GREY_25_PERCENT.getIndex());
			boldStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			boldStyle.setFont(boldFont);

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("S.No");
			headerRow.createCell(1).setCellValue("Visitor Name");
			headerRow.createCell(2).setCellValue("Employee Name");
			headerRow.createCell(3).setCellValue("Employee Company");

			headerRow.createCell(4).setCellValue("Employee Department");
			headerRow.createCell(5).setCellValue("Visitor Company ");
			headerRow.createCell(6).setCellValue("Visitor Email");
			headerRow.createCell(7).setCellValue("Visitor Phone");
			headerRow.createCell(8).setCellValue("Room Name");

			headerRow.createCell(9).setCellValue("Context");
			headerRow.createCell(10).setCellValue("Remarks");
			headerRow.createCell(11).setCellValue("Meeting Start Date/Time");
			headerRow.createCell(12).setCellValue("Meeting End Date/Time");
			headerRow.createCell(13).setCellValue("Check-In Date/Time");
			headerRow.createCell(14).setCellValue("Check-Out Date/Time");
			headerRow.createCell(15).setCellValue("Meeting Duration");
			headerRow.createCell(16).setCellValue("Status");

			for (int i = 0; i <= 16; i++) {
				headerRow.getCell(i).setCellStyle(boldStyle);
			}

			int rowNum = 1;
			for (MeetingDto meeting : meetingsByPagination) {
				Row dataRow = sheet.createRow(rowNum++);
				dataRow.createCell(0).setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(meeting.getVisitor() != null ? meeting.getVisitor().getName() : "");
				dataRow.createCell(2)
						.setCellValue(meeting.getUser() != null
								? meeting.getUser().getFirstName() + " " + meeting.getUser().getLastName()
								: "NA");

				dataRow.createCell(3)
						.setCellValue(meeting.getUser().getCompany().getName() != null
								? meeting.getUser().getCompany().getName()
								: "NA");

				dataRow.createCell(4)
						.setCellValue(meeting.getUser().getDepartmentDto().getName() != null
								? meeting.getUser().getDepartmentDto().getName()
								: "NA");
				dataRow.createCell(5)
						.setCellValue(meeting.getVisitor().getVisitorCompanyDto() != null
								? meeting.getVisitor().getVisitorCompanyDto().getName()
								: "NA");

				dataRow.createCell(6).setCellValue(meeting.getVisitor().getEmail());
				dataRow.createCell(7).setCellValue(meeting.getVisitor().getPhoneNumber());
				String roomName = meeting.getRoom() != null ? meeting.getRoom().getRoomName() : "NA";
				dataRow.createCell(8).setCellValue(roomName);

				dataRow.createCell(9)
						.setCellValue(meeting.getContext() != null ? meeting.getContext().toString() : "NA");
				dataRow.createCell(10).setCellValue(meeting.getRemarks() != null ? meeting.getRemarks() : "NA");

				dataRow.createCell(11)
						.setCellValue(meeting.getMeetingStartDateTime() != null
								? convertDateToFormattedString(meeting.getMeetingStartDateTime().getTime())
								: "NA");

				dataRow.createCell(12)
						.setCellValue(meeting.getMeetingEndDateTime() != null
								? convertDateToFormattedString(meeting.getMeetingEndDateTime().getTime())
								: "NA");

				dataRow.createCell(13)
						.setCellValue(meeting.getCheckInDateTime() != null
								? convertDateToFormattedString(meeting.getCheckInDateTime().getTime())
								: "NA");

				dataRow.createCell(14)
						.setCellValue(meeting.getCheckOutDateTime() != null
								? convertDateToFormattedString(meeting.getCheckOutDateTime().getTime())
								: "NA");

				dataRow.createCell(15).setCellValue(meeting.getDuration() != null ? meeting.getDuration() : "NA");

				Cell statusCell = dataRow.createCell(16);
				String statusValue = (meeting.getStatus() != null) ? meeting.getStatus().toString() : "NA";
				String updatedByValue = (meeting.getUpdatedBy() != null) ? meeting.getUpdatedBy().toString() : "NA";

				statusCell.setCellValue(statusValue + "    (" + updatedByValue + ")");

				if (meeting.getStatus() == MeetingStatus.COMPLETED) {
					statusCell.setCellStyle(greenStyle);
				} else if (meeting.getStatus() == MeetingStatus.APPROVED) {
					statusCell.setCellStyle(orangeStyle);
				} else if (meeting.getStatus() == MeetingStatus.PENDING) {
					statusCell.setCellStyle(yellowStyle);
				} else if (meeting.getStatus() == MeetingStatus.CANCELLED) {
					statusCell.setCellStyle(RedStyle);
				} else if (meeting.getStatus() == MeetingStatus.INPROCESS) {
					statusCell.setCellStyle(blueStyle);
				}
			}

			for (int i = 0; i <= 16; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static CellStyle createCellStyle(Workbook workbook, short colorIndex) {
		CellStyle style = workbook.createCellStyle();
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(colorIndex);
		return style;
	}

	public static String convertDateToFormattedString(Long dateString) {
		try {
			if (dateString != null) {

//	            	System.out.println(anew D);
//	                long milliseconds = dateString + (5L * 60L * 60L * 1000L) + (30L * 60L * 1000L);
				Date resultDate = new Date(dateString);

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy , hh:mm a");
				String formattedDate = sdf.format(resultDate);

				System.out.println(formattedDate);
				return formattedDate;
			} else {
				// Handle the case when dateString is null
				return null;
			}
		} catch (Exception e) {
			// Handle the exception appropriately, e.g., log it
			e.printStackTrace();
			return null;
		}
	}

}
