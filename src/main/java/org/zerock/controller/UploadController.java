package org.zerock.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.util.MediaUtils;
import org.zerock.util.UploadFileUtils;

@Controller
public class UploadController {
	private static final Logger logger =
			LoggerFactory.getLogger(UploadController.class);
	
	@Resource(name="uploadPath")
	private String uploadPath;
	
	@RequestMapping(value="/deleteAllFiles", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(@RequestParam("files[]") String[] files){
		
		if(files==null | files.length==0){
			return new ResponseEntity<String>("deleted", HttpStatus.OK);
		}
		
		for(String fileName : files){
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
			MediaType mType = MediaUtils.getMediaType(formatName);
			// 이미지 타입이면 원본 먼저 제거
			if(mType !=null){
				String front = fileName.substring(0, 12);
				String end = fileName.substring(14);
				new File((uploadPath+(front+end)).replace('/', File.separatorChar)).delete();
			}
			new File((uploadPath+fileName).replace('/', File.separatorChar)).delete();
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value="/uploadAjax", method=RequestMethod.GET)
	public void uploadAjax(){
	}
	
	@RequestMapping(value="/uploadAjax",method=RequestMethod.POST,
					produces="text/plain;charset=UTF-8")
	// produces 속성 : 한국어를 정상적으로 전송하기 위한 설정
	public ResponseEntity<String> uploadAjax(MultipartFile file)throws Exception{

		logger.info("originalName : "+file.getOriginalFilename());
		
		String uploadedFileName = UploadFileUtils.uploadFile(
										uploadPath, 
										file.getOriginalFilename(), 
										file.getBytes());
		
		ResponseEntity<String> entity = new ResponseEntity<>(
											uploadedFileName,
											HttpStatus.CREATED);		
		return entity;		
	}

	@ResponseBody
	@RequestMapping("/displayFile")
	// 파라미터로 전송받기 원하는 파일 이름 ('/년/월/일/파일명')
	// 결과는 byte[] 데이터가 그대로 전송
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception{
		
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		logger.info("FILE NAME : "+fileName);
		
		try{
			//확장자 문자열 가져오기
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
			//확장자 MediaType 가져오기
			MediaType mType = MediaUtils.getMediaType(formatName);
			//Http해더 생성
			HttpHeaders headers = new HttpHeaders();
			//읽어올 인풋 스트림
			in = new FileInputStream(uploadPath+fileName);
			// 이미지파일이라면
			if(mType != null){
				headers.setContentType(mType);
			} else { // 이미지 파일 아니라면(/년/월/일/UUID_파일이름.확장자)
				fileName = fileName.substring(fileName.indexOf("_")+1);
				// APPLICATION_OCTET_STREAM : MIME 타입을 다운로드 용으로 지정
				// 브라우저는 이 MIME타입을 보고 자동으로 다운로드 창을 열어줌
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				// 한글 파일은 다운로드시 파일이름이 깨지므로 인코딩처리 필요
				headers.add(
						"Content-Disposition",
						"attachment; filename=\"" 
						+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"\"");
			}
			// commons라이브러리의 기능 활용하여 대상 파일에서 byte[] 데이터 읽어오기
			byte[] data = IOUtils.toByteArray(in);
			entity = new ResponseEntity<byte[]>(data, headers, HttpStatus.CREATED);
			
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}finally{
			in.close();
		}
		return entity;
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteFile", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName){
		
		logger.info("delete file : " + fileName);
		
		String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
		
		MediaType mType = MediaUtils.getMediaType(formatName);
		
		//이미지 원본 파일 삭제
		if(mType != null){
			String front = fileName.substring(0, 12);
			String end = fileName.substring(14);
			new File(uploadPath + (front+end).replace('/', File.separatorChar)).delete();
		}
		// 썸네일이미지와 이미지가 아닌 파일 삭제
		new File((uploadPath+fileName).replace('/', File.separatorChar)).delete();
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value="/uploadForm", method=RequestMethod.GET)
	public void uploadForm(){
	}
	
	@RequestMapping(value="/uploadForm", method=RequestMethod.POST)
	public String uploadForm(MultipartFile file, Model model) throws Exception {
		// 전송된 파일의 이름
		logger.info("originalName : "+file.getOriginalFilename());
		// 파일의 size
		logger.info("size : "+file.getSize());
		// 파일의 MIME 타입
		logger.info("contentType : "+file.getContentType());
		
		String savedName = "";
		if(file.getSize()!=0){
			savedName = uploadFile(file.getOriginalFilename(),
								file.getBytes());
		}
		model.addAttribute("savedName", savedName);
		
		return "uploadResult";
	}
	
	// 실제 데이터 처리하는 메서드
	private String uploadFile(String originalName, byte[] fileData) throws Exception{
		// 중복되지 않는 고유한 키 값을 설정 하기 위함
		UUID uid = UUID.randomUUID();
		
		//저장할 주소 생성
		String savedName = uid.toString()+"_"+originalName;
		
		//저장할 파일 정보
		File target = new File(uploadPath, savedName);

		// 실제 파일 카피 처리(FileCopyUtilis)
		FileCopyUtils.copy(fileData, target);
		
		return savedName;
	}
}
