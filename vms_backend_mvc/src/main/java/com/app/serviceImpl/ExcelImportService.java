package com.app.serviceImpl;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.Dao.CityDao;
import com.app.Dao.CompanyDao;
import com.app.Dao.DepartmentDao;
import com.app.Dao.RoleDao;
import com.app.Dao.StateDao;
import com.app.Dao.UserDao;
import com.app.dto.CompanyDTO;
import com.app.dto.DepartmentDto;
import com.app.dto.UserDto;
import com.app.entity.City;
import com.app.entity.Company;
import com.app.entity.Department;
import com.app.entity.Role;
import com.app.entity.State;
import com.app.entity.User;
import com.app.response.Response;

@Service
public class ExcelImportService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StateDao stateDao;
	@Autowired
	private CityDao cityDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DepartmentDao departmentDao;

//	check file is  excel  or  not 
	public static Boolean checkExcelFormat(MultipartFile file) {
		String contentType = file.getContentType();

		if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		} else {
			return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		}

	}

	@SuppressWarnings("resource")
	public Response<?> convertExceltoList(InputStream inputStream, Integer companyId) {
		List<UserDto> successUser = new ArrayList();
		List<UserDto> unsuccessUser = new ArrayList();
		DataFormatter dataFormatter = new DataFormatter();

		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

			for (int sheetIndex = 0; sheetIndex < xssfWorkbook.getNumberOfSheets(); sheetIndex++) {
				XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetIndex);

				if (sheet == null) {
					return new Response<>("Sheet Is null", null, HttpStatus.BAD_REQUEST.value());
				}
//				long lastRowNum = (long) sheet.getPhysicalNumberOfRows();

				Company company = companyDao.getCompanyById(companyId);

				List<User> getallUser = userDao.getallUser(companyId);
				long count = getallUser.size();

//				for (User x : getallUser) {
//					if (x.getIsActive() == true) {
//						count++;
//					}
//				}

				System.out.println(count + "  count data for user"); // dhkfgd
//				System.out.println(lastRowNum + " The Row Value ");
				count = company.getUserLimit() - count;
//				System.out.println(lastRowNum +" Last column");

				long lastRowNum = 0;
				Iterator<Row> iterator = sheet.iterator();

				while (iterator.hasNext()) {
					Row row = iterator.next();
					boolean rowIsEmpty = rowIsEmpty(row);
					if (rowIsEmpty) {
						lastRowNum++;
					}

				}
				lastRowNum--;
//				System.out.println(lastRowNum  +  "  count of the rows" );
//				
//				System.out.println(count + " user limit");

				int rowNumber = 0;
				String[] headers = { "FIRSTNAME", "LASTNAME", "PHONE", "GENDER", "EMAIL", "DOB", "GOVT_ID", "EMPCODE",
						"PINCODE", "ROLE", "STATE", "CITY", "DEPARTMENT", "PERMISSION" };
				XSSFRow row2 = sheet.getRow(0);
				int cellCount = row2.getLastCellNum();

//				System.out.println("cellcount    ->" + cellCount);
//
//				System.out.println(headers.length + "  ====  Lenght ");

				if (cellCount != headers.length) {
					return new Response<>("Invallid Excell", null, HttpStatus.BAD_REQUEST.value());
				}

				for (int i = 0; i < headers.length; i++) {
					XSSFCell cell = row2.getCell(i);
					try {
						String cellValue = cell.getStringCellValue().trim().toUpperCase();
						if (!cellValue.equals(headers[i])) {
							System.out.println(cellValue + "  Cell Value");
							System.out.println(headers[i].toUpperCase() + " FIrst NAme");
							return new Response<>("Headers Mismatch", cell.getStringCellValue(),
									HttpStatus.BAD_REQUEST.value());
						}
					} catch (Exception e) {
						return new Response<>("Headers Must be a String", cell.getNumericCellValue(),
								HttpStatus.BAD_REQUEST.value());
					}
				}
				if (lastRowNum > count) {
					return new Response<>("User Limit Exceeded", null, HttpStatus.BAD_REQUEST.value());
				}

				Iterator<Row> iterator1 = sheet.iterator();
				while (iterator1.hasNext()) {

					Row row = iterator1.next();

					Boolean flag = true;

					if (rowNumber == 0) {
						rowNumber++;
						continue;
					}
					boolean rowIsEmpty = rowIsEmpty(row);

//					System.out.println(rowIsEmpty + "test row ");
					if (rowIsEmpty != true) {
						flag = false;
						continue;
					}
//					System.out.println(rowIsEmpty + "test row  down");
					System.out.println(row.getPhysicalNumberOfCells() + " Row Number  " + headers.length);

					if (row.getPhysicalNumberOfCells() != headers.length) {
						return new Response<>("Entries can't be Empty", null, HttpStatus.BAD_REQUEST.value());
					}
					Iterator<Cell> cells = row.iterator();

					int cid = 1;
					UserDto user = new UserDto();

					while (cells.hasNext()) {

						Cell cell = cells.next();

						switch (cid) {
						case 1:
							if (cell.getStringCellValue() != null) {
								user.setFirstName(cell.getStringCellValue().trim());
								break;
							} else {
								user.setFirstName(null);
								flag = false;
								break;
							}

						case 2:
							if (cell.getStringCellValue() != null) {
								user.setLastName(cell.getStringCellValue().trim());
								break;
							} else {
								user.setLastName(null);
								flag = false;
								break;

							}
						case 3:

							String phone = "";

							if (cell.getCellType() == CellType.NUMERIC) {

								phone = dataFormatter.formatCellValue(cell);

								System.out.println(phone + " NUMERIC VALUE");
							} else if (cell.getCellType() == CellType.STRING) {

								phone = cell.getStringCellValue();
								System.out.println(phone + " STRING SMMMMM");
							} else {

							}

							if (phone != null && Pattern.matches("\\d+", phone) && phone.length() == 10) {
								user.setPhone(phone.trim());
								break;
							} else {
								user.setPhone(null);
								flag = false;
								break;
							}

						case 4:

							if (cell != null || cell.getCellType() != CellType.BLANK) {

								String gender = cell.toString().trim();
								if (gender.equalsIgnoreCase("MALE")) {
									user.setGender(gender);
								} else if (gender.equalsIgnoreCase("FEMALE")) {
									user.setGender(gender);
								} else if (gender.equalsIgnoreCase("OTHER")) {
									user.setGender(gender);
								} else {
									user.setGender("User can Be only Male / Female");
									flag = false;

								}
								break;

							} else {
								user.setGender(null);
								flag = false;
								break;
							}

						case 5:
							if (cell.toString() != null) {
								String email = cell.toString().trim();
								String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

								if (Pattern.matches(emailRegex, email)) {
									user.setEmail(email);
								} else {
									// Invalid email format
									System.out.println("Invalid email address: " + email);
									user.setEmail(null);
									flag = false;
								}
								break;
							} else {
								user.setEmail(null);
								flag = false;
								break;
							}

						case 6:

							if (cell.toString() != null) {
								String dobString = cell.toString().trim();

								try {
									// Parse the date string using the input format "dd-MMM-yyyy"
									SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
									Date parsedDate = inputFormat.parse(dobString);

									System.out.println(parsedDate + " Parse date");

									// Format the parsed date to the desired output format "yyyy-MM-dd"
									SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
									String formattedDate = outputFormat.format(parsedDate);

									// Set the formatted date to the user's DOB
									user.setDob(formattedDate);
									break;
								} catch (ParseException e) {
									// Handle the case where the date format is not valid
									user.setDob("invalid date");
									flag = false;
									break;
								}
							} else {
								user.setDob(null);
								flag = false;
								break;
							}

						case 7:

//							System.out.println(cell.getNumericCellValue() + "  Cell the value");

							if (cell != null && cell.getCellType() != CellType.BLANK) {
								if (cell.getCellType() == CellType.NUMERIC) {
									DataFormatter formater = new DataFormatter();
									String formattedValue = formater.formatCellValue(cell);
									user.setGovtId(formattedValue.trim());
								} else if (cell.getCellType() == CellType.STRING) {
									user.setGovtId(cell.getStringCellValue());
								}
							} else {
								user.setGovtId(null);
								flag = false;
							}

						case 8:
							if (cell != null || cell.getCellType() != CellType.BLANK) {
								user.setEmpCode(cell.toString().trim());
								break;
							} else

							{
								user.setEmpCode(null);
								break;
							}

						case 9:

							if (cell != null || cell.getCellType() != CellType.BLANK) {
								String trim = cell.toString().trim();
								if (trim.length() == 6) {

									if (cell.getCellType() == CellType.NUMERIC) {
										user.setPincode(dataFormatter.formatCellValue(cell));
										break;
									} else if (cell.getCellType() == CellType.STRING) {

										user.setPincode(cell.getStringCellValue().trim());
										break;
									}

								} else {
									user.setPincode("Pin code Must be 6 digit");
									flag = false;
									break;
								}

							} else {
								user.setPincode("Pin code Must be 6 digit");
								flag = false;
								break;
							}

						case 10: // role//

							String roleName = cell.getStringCellValue().trim();
							if (roleName != null) {
								Role role = roleDao.getRole(roleName.toUpperCase());
								if (role != null) {

									if (role.getName().equals("SUPERADMIN")) {
										return new Response<>("Role 'Super Admin' cannot be added.", null, 400);
									}

									if (role.getName().equals("ADMIN")) {
										return new Response<>("Role 'Admin' cannot be added.", null, 400);
									}

									user.setRole(role);
									break;
								} else {
									user.setRole(null);
									flag = false;
									break;
								}
							} else {
								user.setRole(null);
								flag = false;
								break;
							}

						case 11: // state//

							String stateName = cell.getStringCellValue().trim();
							if (stateName != null) {
								String formattedStateName = formatStateName(stateName);
								State state = stateDao.getByName(formattedStateName);

								if (state != null) {
									user.setState(state);
									break;
								} else {
									user.setState(null);
									flag = false;
									break;
								}
							} else {
								user.setState(null);
								flag = false;
								break;
							}

						case 12: // city//
							String cityName = cell.getStringCellValue().trim();
							if (cityName != null) {
								City city = cityDao.getByName(formatStateName(cityName));
								if (city != null) {
									user.setCity(city);
									break;
								} else {
									user.setCity(null);
									flag = false;
									break;
								}
							}
						case 13: // department//
							String departmentName = cell.getStringCellValue().trim();
							if (departmentName != null) {
								Department department = departmentDao.getByCompanyIdAndDepartmentName(companyId,
										departmentName.toUpperCase().trim());
								if (department != null) {
									user.setDepartmentDto(DepartmentDto.toDepartmentDto(department));
									break;
								} else {
									user.setDepartmentDto(null);
									flag = false;
									break;
								}
							}

						case 14:

							if (cell != null || cell.getCellType() != CellType.BLANK) {

								String permission = cell.toString().trim();
								if (permission.equalsIgnoreCase("YES")) {
									user.setIsPermission(true);
								} else if (permission.equalsIgnoreCase("NO")) {
									user.setIsPermission(false);
								} else {
									user.setIsPermission(null);
									flag = false;

								}
								break;

							} else {
								user.setIsPermission(null);
								flag = false;
								break;
							}

						default:
							break;

						}
						cid++;

					}
					if (flag) {
						user.setCompany(companyDao.getCompanyById(companyId));
						successUser.add(user);
					} else {
						unsuccessUser.add(user);
					}

				}
				Map<String, List<UserDto>> allData = new HashMap<>();
				allData.put("Success", successUser);
				allData.put("UnSuccessUser", unsuccessUser);

				return new Response<>("Success", allData, HttpStatus.OK.value());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong", null, HttpStatus.BAD_REQUEST.value());
		}
		return new Response<>("Try Again", null, HttpStatus.BAD_REQUEST.value());
	}

	@SuppressWarnings("resource")
	public Response<?> convertExceltoListV2(InputStream inputStream, Integer companyId) {
		List<UserDto> successUser = new ArrayList();
		List<UserDto> unsuccessUser = new ArrayList();
		DataFormatter dataFormatter = new DataFormatter();

		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

			for (int sheetIndex = 0; sheetIndex < xssfWorkbook.getNumberOfSheets(); sheetIndex++) {
				XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetIndex);

				if (sheet == null) {
					return new Response<>("Sheet Is null", null, HttpStatus.BAD_REQUEST.value());
				}
//				long lastRowNum = (long) sheet.getPhysicalNumberOfRows();

				Company company = companyDao.getCompanyById(companyId);

				List<User> getallUser = userDao.getallUser(companyId);
				long count = getallUser.size();

//				for (User x : getallUser) {
//					if (x.getIsActive() == true) {
//						count++;
//					}
//				}

				System.out.println(count + "  count data for user"); // dhkfgd
//				System.out.println(lastRowNum + " The Row Value ");
				count = company.getUserLimit() - count;
//				System.out.println(lastRowNum +" Last column");

				long lastRowNum = 0;
				Iterator<Row> iterator = sheet.iterator();

				while (iterator.hasNext()) {
					Row row = iterator.next();
					boolean rowIsEmpty = rowIsEmpty(row);
					if (rowIsEmpty) {
						lastRowNum++;
					}

				}
				lastRowNum--;
//				System.out.println(lastRowNum  +  "  count of the rows" );
//				
//				System.out.println(count + " user limit");

				int rowNumber = 0;
				String[] headers = { "FIRSTNAME", "LASTNAME", "PHONE", "GENDER", "EMAIL", "DOB", "GOVT_ID", "EMPCODE",
						"PINCODE", "ROLE", "STATE", "CITY", "DEPARTMENT", "PERMISSION" };
				XSSFRow row2 = sheet.getRow(0);
				int cellCount = row2.getLastCellNum();

//				System.out.println("cellcount    ->" + cellCount);
//
//				System.out.println(headers.length + "  ====  Lenght ");

				if (cellCount != headers.length) {
					return new Response<>("Invallid Excell", null, HttpStatus.BAD_REQUEST.value());
				}

				for (int i = 0; i < headers.length; i++) {
					XSSFCell cell = row2.getCell(i);
					try {
						String cellValue = cell.getStringCellValue().trim().toUpperCase();
						if (!cellValue.equals(headers[i])) {
							System.out.println(cellValue + "  Cell Value");
							System.out.println(headers[i].toUpperCase() + " FIrst NAme");
							return new Response<>("Headers Mismatch", cell.getStringCellValue(),
									HttpStatus.BAD_REQUEST.value());
						}
					} catch (Exception e) {
						return new Response<>("Headers Must be a String", cell.getNumericCellValue(),
								HttpStatus.BAD_REQUEST.value());
					}
				}
				if (lastRowNum > count) {
					return new Response<>("User Limit Exceeded", null, HttpStatus.BAD_REQUEST.value());
				}

				Iterator<Row> iterator1 = sheet.iterator();
				while (iterator1.hasNext()) {

					Row row = iterator1.next();

					Boolean flag = true;

					if (rowNumber == 0) {
						rowNumber++;
						continue;
					}
					boolean rowIsEmpty = rowIsEmpty(row);

//					System.out.println(rowIsEmpty + "test row ");
					if (rowIsEmpty != true) {
						flag = false;
						continue;
					}
//					System.out.println(rowIsEmpty + "test row  down");
					System.out.println(row.getPhysicalNumberOfCells() + " Row Number  " + headers.length);

					if (row.getPhysicalNumberOfCells() != headers.length) {
						return new Response<>("Entries can't be Empty", null, HttpStatus.BAD_REQUEST.value());
					}
					Iterator<Cell> cells = row.iterator();

					int cid = 1;
					UserDto user = new UserDto();

					while (cells.hasNext()) {

						Cell cell = cells.next();

						switch (cid) {
						case 1:
							if (cell.toString() != null) {
								user.setFirstName(cell.toString().trim());
								break;
							} else {
								user.setFirstName(null);
								flag = false;
								break;
							}

						case 2:
							if (cell.getStringCellValue() != null) {
								user.setLastName(cell.getStringCellValue().trim());
								break;
							} else {
								user.setLastName(null);
								flag = false;
								break;

							}
						case 3:

							String phone = "";

							if (cell.getCellType() == CellType.NUMERIC) {

								phone = dataFormatter.formatCellValue(cell);

//								System.out.println(phone + " NUMERIC VALUE");
							} else if (cell.getCellType() == CellType.STRING) {

								phone = cell.getStringCellValue();
//								System.out.println(phone + " STRING SMMMMM");
							} else {

							}
							
							String numericPhone = phone.replaceAll("[^\\d]", "");
							
							System.out.println(numericPhone.length() +" length");

							if (phone != null  && numericPhone.length() == 10) {
								user.setPhone(numericPhone);
								break;
							} else {
								user.setPhone(null);
								flag = false;
								break;
							}

						case 4:

							if (cell != null || cell.getCellType() != CellType.BLANK) {

								String gender = cell.toString().trim();
								if (gender.equalsIgnoreCase("MALE")) {
									user.setGender(gender);
								} else if (gender.equalsIgnoreCase("FEMALE")) {
									user.setGender(gender);
								} else if (gender.equalsIgnoreCase("OTHER")) {
									user.setGender(gender);
								} else {
									user.setGender(null);
									flag = false;

								}
								break;

							} else {
								user.setGender(null);
								flag = false;
								break;
							}

						case 5:
							if (cell.toString() != null) {
								String email = cell.toString().trim();
								String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

								if (Pattern.matches(emailRegex, email)) {
									user.setEmail(email);
								} else {
									// Invalid email format
//									System.out.println("Invalid email address: " + email);
									user.setEmail(null);
									flag = false;
								}
								break;
							} else {
								user.setEmail(null);
								flag = false;
								break;
							}

						case 6:
							if (cell.toString() != null) {
								String dobString = cell.toString().trim();

								try {
									// Parse the date string using the input format "yyyy-MM-d"
									SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
									Date parsedDate = inputFormat.parse(dobString);

									// Format the parsed date to the desired output format "yyyy-MM-dd"
									SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
									String formattedDate = outputFormat.format(parsedDate);

									// Set the formatted date to the user's DOB
									user.setDob(formattedDate);
									break;
								} catch (ParseException e) {
									// Handle the case where the date format is not valid
									user.setDob(null);
									flag = false;
									break;
								}
							} else {
								user.setDob(null);
								flag = false;
								break;
							}

//						case 6:
//						    if (cell.toString() != null) {
//						        String dobString = cell.toString().trim();
//
//						        try {
//						            // Parse the date string using the input format "dd-MM-yyyy"
//						            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
//						            Date parsedDate = inputFormat.parse(dobString);
//
//						            // Check if the parsed date is in the future
//						            if (parsedDate.after(new Date())) {
//						                user.setDob(null);
//						                flag = false;
//						                break;
//						            }
//
//						            // Format the parsed date to the desired output format "yyyy-MM-dd"
//						            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//						            String formattedDate = outputFormat.format(parsedDate);
//
//						            // Set the formatted date to the user's DOB
//						            user.setDob(formattedDate);
//						            break;
//						        } catch (ParseException e) {
//						            // Handle the case where the date format is not valid
//						            user.setDob(null);
//						            flag = false;
//						            break;
//						        }
//						    } else {
//						        user.setDob(null);
//						        flag = false;
//						        break;
//						    }

						case 7:

//							System.out.println(cell.getNumericCellValue() + "  Cell the value");

							if (cell != null && cell.getCellType() != CellType.BLANK) {
								if (cell.getCellType() == CellType.NUMERIC) {
									double numericValue = cell.getNumericCellValue();
									long intValue = (long) numericValue; // Convert to long to handle large values
									user.setGovtId(String.valueOf(intValue));
								} else if (cell.getCellType() == CellType.STRING) {
									user.setGovtId(cell.getStringCellValue());
								}
							} else {
								user.setGovtId(null);
								flag = false;
							}
							break;

						case 8:
							if (cell != null && cell.getCellType() != CellType.BLANK) {
								if (cell.getCellType() == CellType.STRING) {
									user.setEmpCode(cell.getStringCellValue().trim());
									break;
								} else if (cell.getCellType() == CellType.NUMERIC) {
									double numericValue = cell.getNumericCellValue();
									DecimalFormat decimalFormat = new DecimalFormat("#");
									user.setEmpCode(decimalFormat.format(numericValue).trim());
									break;
								}
							} else {
								user.setEmpCode(null);
								flag = false;
								break;

							}

						case 9:

							String pincode = "";

							if (cell.getCellType() == CellType.NUMERIC) {

								pincode = dataFormatter.formatCellValue(cell);

//								System.out.println(phone + " NUMERIC VALUE");
							} else if (cell.getCellType() == CellType.STRING) {

								pincode = cell.getStringCellValue().trim();
//								System.out.println(phone + " STRING SMMMMM");
							} else {

							}

							String trim = pincode.replaceAll("[^\\d]", "");
							System.out.println(pincode + " inside pincode" + trim.length());
							if (pincode != null && trim.length() == 6) {
								user.setPincode(trim);
								break;
							} else {
								user.setPincode(null);
								flag = false;
								break;
							}

						case 10: // role//

							String roleName = null;
							if (cell != null && cell.getCellType() != CellType.BLANK) {
								roleName = cell.toString();
							}

							if (roleName != null) {
								Role role = roleDao.getRole(roleName.toUpperCase());
								if (role != null) {

									if (role.getName().equals("SUPERADMIN")) {
										return new Response<>("Role 'Super Admin' cannot be added.", null, 400);
									}

									if (role.getName().equals("ADMIN")) {
										return new Response<>("Role 'Admin' cannot be added.", null, 400);
									}

									user.setRole(role);
									break;
								} else {
									user.setRole(null);
									flag = false;
									break;
								}
							} else {
								user.setRole(null);
								flag = false;
								break;
							}

						case 11: // state//

							String stateName = cell.getStringCellValue().trim();
							if (stateName != null) {
								String formattedStateName = formatStateName(stateName);
								State state = stateDao.getByName(formattedStateName);

								if (state != null) {
									user.setState(state);
									break;
								} else {
									user.setState(null);
									flag = false;
									break;
								}
							} else {
								user.setState(null);
								flag = false;
								break;
							}

						case 12: // city//
							String cityName = cell.getStringCellValue().trim();
							if (cityName != null) {
								City city = cityDao.getByName(formatStateName(cityName));
								if (city != null) {
									user.setCity(city);
									break;
								} else {
									user.setCity(null);
									flag = false;
									break;
								}
							}
						case 13: // department//
							String departmentName = cell.getStringCellValue().trim();
							if (departmentName != null) {
								Department department = departmentDao.getByCompanyIdAndDepartmentName(companyId,
										departmentName.toUpperCase().trim());
								if (department != null) {
									user.setDepartmentDto(DepartmentDto.toDepartmentDto(department));
									break;
								} else {
									user.setDepartmentDto(null);
									flag = false;
									break;
								}
							}

						case 14:

							if (cell != null || cell.getCellType() != CellType.BLANK) {

								String permission = cell.toString().trim();
								if (permission.equalsIgnoreCase("YES")) {
									user.setIsPermission(true);
								} else if (permission.equalsIgnoreCase("NO")) {
									user.setIsPermission(false);
								} else {
									user.setIsPermission(null);
									flag = false;

								}
								break;

							} else {
								user.setIsPermission(null);
								flag = false;
								break;
							}

						default:
							break;

						}
						cid++;

					}
					if (flag) {
						user.setCompany(companyDao.getCompanyById(companyId));
						successUser.add(user);
					} else {
						unsuccessUser.add(user);
					}

				}
				Map<String, List<UserDto>> allData = new HashMap<>();
				allData.put("Success", successUser);
				allData.put("UnSuccessUser", unsuccessUser);

				return new Response<>("Success", allData, HttpStatus.OK.value());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Something went wrong", null, HttpStatus.BAD_REQUEST.value());
		}
		return new Response<>("Try Again", null, HttpStatus.BAD_REQUEST.value());
	}

//	private boolean rowIsEmpty(Row row) {
//	    for (Cell cell : row) {
//	        // Check if the cell is null or its value is blank
//	        if (cell == null || cell.toString().isBlank()) {
//	            return true; // If any cell is null or has a blank value, return true
//	        }
//	        return false;
//	    }
//	    return false; // If all cells have non-blank values, return false
//	}
	private boolean rowIsEmpty(Row row) {
		for (Cell cell : row) {
			// Check if the cell is not null and its value is not blank
			if (cell != null && !cell.toString().trim().isEmpty()) {
				return true; // If any cell has a non-blank value, return true
			}
		}
//	    System.out.println(row +"  row    ");
		return false; // If all cells are either null or contain blank values, return false
	}

	private String formatStateName(String stateName) {
		if (stateName.isEmpty()) {
			return stateName;
		}

		String firstLetter = stateName.substring(0, 1).toUpperCase();
		String remainingLetters = stateName.substring(1).toLowerCase();

		return firstLetter + remainingLetters;
	}

	public Response<?> convertexcelDepatmentToList(InputStream inputStream, Integer companyId) {

		List<DepartmentDto> successDepartment = new ArrayList();
		List<DepartmentDto> unsuccessDepartment = new ArrayList();

		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = xssfWorkbook.getSheet("data");

			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {

				Row row = iterator.next();
				Boolean flag = true;

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cells = row.iterator();

				int cid = 1;
				DepartmentDto departmentDto = new DepartmentDto();

				while (cells.hasNext()) {

					Cell cell = cells.next();

					switch (cid) {
					case 1:
						if (cell.getStringCellValue() != null) {
							departmentDto.setName(cell.getStringCellValue());
							break;
						} else {
							departmentDto.setName(null);
							flag = false;
							break;
						}

					default:
						break;

					}
					cid++;

				}
				if (flag) {
					departmentDto.setCompany(CompanyDTO.toCompanyDto(companyDao.getCompanyById(companyId)));
					successDepartment.add(departmentDto);
				} else {
					unsuccessDepartment.add(departmentDto);
				}

			}
			Map<String, List<DepartmentDto>> allData = new HashMap<>();
			allData.put("Success", successDepartment);
			allData.put("UnSuccessUser", unsuccessDepartment);

			return new Response<>("Success", allData, HttpStatus.OK.value());
		}

		catch (Exception e) {
			e.printStackTrace();
			return new Response<>("Invallid Excel Format", null, HttpStatus.BAD_REQUEST.value());

		}

	}

}