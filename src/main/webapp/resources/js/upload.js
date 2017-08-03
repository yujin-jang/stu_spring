function checkImageType(fileName){
	
	var pattern = /jpg|gif|png|jpeg/i;
	
	return fileName.match(pattern);
}

function getFileInfo(fullName){
	
	var fileName; //원본파일명
	var imgsrc; //브라우저에 보여줄 그림파일경로(썸네일 or file.png)
	var getLink;// 원본파일 다운로드 uri 경로
	var fileLink; // UUID+원본파일명-> fileName 가져오는데 사용됨
	// 이미지 파일인 경우
	if(checkImageType(fullName)){
		// 썸네일 이미지 파일 경로
		imgsrc = "/displayFile?fileName="+fullName;
		//'s_'제외한 UUID+원본파일명
		fileLink = fullName.substr(14);
		
		var front = fullName.substr(0,12); // '/2017/07/25/'
		var end = fullName.substr(14); // 's_'제외한 UUID+원본파일명
		// 원본 파일 다운로드 uri 경로
		getLink = "/displayFile?fileName="+front+end;
	} else {
		imgsrc = "/resources/dist/img/file.png";
		//'UUID+원본파일명
		fileLink = fullName.substr(12);
		//원본 파일 다운로드 uri 경로
		getLink = "/displayFile?fileName="+fullName;
	}
	// 원본 파일명(UUID없이)
	fileName = fileLink.substr(fileLink.indexOf("_")+1);
	
	return {fileName:fileName,
			imgsrc:imgsrc,
			getLink:getLink,
			fullName:fullName};
}

