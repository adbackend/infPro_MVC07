package kr.bit.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class FileAddController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String UPLOAD_DIR = "file_repo";
		String uploadPath = request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIR;
		
		System.out.println(request.getServletContext());
		System.out.println(request.getServletContext().getRealPath(""));
		
		File currentDirPath = new File(uploadPath); //업로드할 경로를 File 객체로 만들기
		
		if(!currentDirPath.exists()) { //경로가 존재하지 않으면
			currentDirPath.mkdir();
		}
		
		//파일을 업로드 할때 먼저 저장될 임시 저장경로를 설정
		//file upload시 필요한 api  commons-fileipload, commons-io
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024*1024);
		
		String fileName = null;
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			
			//items--->FileItem[], FileItem[], FileItem[]
			List items = upload.parseRequest(request); //request안에 여러개의 파일이 업로드된 경우
			
			for(int i=0; i<items.size(); i++) {
				FileItem fileItem = (FileItem)items.get(i);
				
				if(fileItem.isFormField()) {//폼필드이면
					System.out.println(fileItem.getFieldName()+"="+fileItem.getString("UTF-8"));
				}else { //파일이면
					if(fileItem.getSize()>0) {
						int idx = fileItem.getName().lastIndexOf("\\");//   \\(윈도우)
						if(idx==-1) {
							idx = fileItem.getName().lastIndexOf("/"); //   /(리눅스)
						}
						fileName = fileItem.getName().substring(idx+1); //파일이름을 가져온다
						System.out.println("idx값....->"+ idx);
						System.out.println("fileName값....->"+ fileName);
						
						File uploadFile = new File(currentDirPath+"\\"+fileName);
						
						//파일 중복체크
						if(uploadFile.exists()) {
							fileName= System.currentTimeMillis()+"_"+fileName;  //시분초_파일네임
							
							uploadFile = new File(currentDirPath+"\\"+fileName);
						}
						fileItem.write(uploadFile); //임시경로->새로운경로에 파일쓰기
					}
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		//$.ajax()쪽으로 업로드된 최종파일이름을 전송시켜준다.
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print(fileName);
		
		return null;
	}
}
