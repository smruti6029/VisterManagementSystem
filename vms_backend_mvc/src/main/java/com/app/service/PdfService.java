package com.app.service;

import java.io.IOException;

import com.app.dto.MeetingDto;

public interface PdfService {

	byte[] generateVisitorPassPDF(MeetingDto meetingDto) throws IOException, Exception;

}
