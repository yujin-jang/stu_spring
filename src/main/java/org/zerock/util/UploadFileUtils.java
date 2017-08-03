package org.zerock.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {

	private static final Logger logger = 
			LoggerFactory.getLogger(UploadFileUtils.class);
	
	public static String uploadFile(
							String uploadPath,
							String originalName,
							byte[] fileData) throws Exception{
		
			UUID uid = UUID.randomUUID();
			
			String savedName = uid.toString() + "_" + originalName;
			String savedPath = calcPath(uploadPath);// 저장될 경로
			File target = new File(uploadPath+savedPath, savedName);
			
			//원본 파일 서버에 저장
			FileCopyUtils.copy(fileData, target);
			//확장자 정보
			String formatName = 
					originalName.substring(originalName.lastIndexOf(".")+1);
			// 브라우저에서 사용할 파일 주소
			String uploadedFileName = null;
			
			if(MediaUtils.getMediaType(formatName) != null){
				uploadedFileName = 
						makeThumbnail(uploadPath, savedPath, savedName);
			}else{
				uploadedFileName = 
						makeIcon(uploadPath, savedPath, savedName);
			}
		
		return uploadedFileName;
	}
	
	// /년/월/일 디렉토리 주소 문자열 생성 및 makeDir메서드 실행하여 디렉토리생성
	private static String calcPath(String uploadPath){
		
		Calendar cal = Calendar.getInstance();
		
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator 
						+ new DecimalFormat("00").format((cal.get(Calendar.MONTH)+1));
		String datePath = monthPath + File.separator
						+ new DecimalFormat("00").format(cal.get(Calendar.DATE));
		
		makeDir(uploadPath, yearPath, monthPath, datePath);
		
		logger.info(datePath);
		
		return datePath;
	}
	
	// 디렉토리 생성
	private static void makeDir(String uploadPath, String...paths){
		// 최종 디렉토리가 존재한다면 종료
		if(new File(paths[2]).exists()){
			return;
		}
		// 존재하지 않는다면 차례차례 생성
		for (String path : paths) {
			
			File dirPath = new File(uploadPath + path);
		
			if(!dirPath.exists()){
				dirPath.mkdirs();
			}
		}
	}
	
	// 썸네일 이미지를 생성하고 서버에 저장후 브라우저에서 사용할 경로 반환
	private static String makeThumbnail(
								String uploadPath,
								String path,
								String fileName) throws Exception{
		
		// BufferedImage 는 실제 이미지가 아닌 메모리상의 이미지
		// 원본 파일을 메모리상으로 로딩
		BufferedImage sourceImg = 
				ImageIO.read(new File((uploadPath+path), fileName));
		
		// 원본파일을 정해진 크기에 맞게 작은 이미지 파일에 복사
		BufferedImage destImg =
				// 높이 100에 맞춰서 자동적으로 축소
				Scalr.resize(
						sourceImg, 
						Scalr.Method.AUTOMATIC,
						Scalr.Mode.FIT_TO_HEIGHT, 100);
		
		// 썸네일 이미지의 파일명은 UUID값이 사용된 이후에 's_'로 시작토록 설정
		String thumbnailName =
				uploadPath + path + File.separator + "s_" + fileName;
		
		// 썸네일 이미지에 대한 파일 객체
		File newFile = new File(thumbnailName);
		// 썸네일 이미지의 확장자(jpg, png 등) 값 가져오기
		String formatName = 
				fileName.substring(fileName.lastIndexOf(".")+1);
		
		// 썸네일 이미지를 서버에 기록(기록대상, 확장자, 저장할파일객체)
		ImageIO.write(destImg, formatName, newFile);
		
		// uploadPath를 잘라내고, 윈도우의 경로에 사용하는 '\' 대신
		// 브라우저에서 사용할 '/'로 변경하여 반환
		return thumbnailName.substring(uploadPath.length())
				.replace(File.separatorChar, '/');
	}
	
	// 이미지 파일이 아닐 경우 
	//썸네일 생성 하지 않고 브라우저에서 다운로드할 경로 반환
	private static String makeIcon(
							String uploadPath,
							String path,
							String fileName) throws Exception{
		
		String iconName = 
				uploadPath + path + File.separator + fileName;
		
		// uploadPath를 잘라내고, 윈도우의 경로에 사용하는 '\' 대신
		// 브라우저에서 사용할 '/'로 변경하여 반환
		return iconName.substring(uploadPath.length())
				.replace(File.separatorChar, '/'); 
	}
}
