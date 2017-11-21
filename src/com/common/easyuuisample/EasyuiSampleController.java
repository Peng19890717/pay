package com.common.easyuuisample;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EasyuiSampleController {
	@RequestMapping("uploadFile")
    public String uploadFile(@RequestParam("files") MultipartFile[] files,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
        OutputStream os = null;
		try {
			if(files!=null&&files.length>0){
	        	String realPath = request.getSession().getServletContext().getRealPath("/upload");
	            for(int i = 0;i<files.length;i++){  
	                MultipartFile file = files[i];
	                System.out.println(realPath+"/"+file.getOriginalFilename());
	                if(!file.isEmpty())file.transferTo(new File(realPath, file.getOriginalFilename()));
	            } 
	        }
			os = response.getOutputStream();
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			os.write("上传完成！".getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
		} finally {
			if(os != null)try {os.close();} catch (IOException e1) {}
		}
        return null;  
    }
}