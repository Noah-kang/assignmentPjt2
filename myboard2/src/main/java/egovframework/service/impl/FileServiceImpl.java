package egovframework.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.mapper.FileMapper;
import egovframework.service.FileService;
import egovframework.vo.PhotoVO;

@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
    private FileMapper fileMapper;

    @Override
    public void saveFile(MultipartFile file, PhotoVO photoFile, HttpServletRequest request) {
        // 실제 파일 저장 로직
        try {
        	// 절대 경로 지정
            String uploadDir = "C:/Users/winitech/Desktop/assignmentPjt/assignmentPjt2/myboard2/src/main/webapp/resources/uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // 디렉토리가 없으면 생성
            }

            // 저장 파일 이름 설정 (UUID 기반)
            String fileExtension = photoFile.getFileName().substring(photoFile.getFileName().lastIndexOf("."));
            String savedFileName = photoFile.getUuid() + fileExtension;
            File saveFile = new File(uploadFolder, savedFileName);

            // 파일 저장
            file.transferTo(saveFile);

            // 파일 경로를 상대 경로로 설정하여 DB에 저장
            photoFile.setFilePath("/resources/uploads/" + savedFileName);

            // DB에 파일 정보 저장
            fileMapper.insertFile(photoFile);
            
            System.out.println("파일 저장 성공: " + saveFile.getAbsolutePath());
        } catch (IOException e) {
        	System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }
    
    //기존 사진파일 정보를 가져옵니다.
    @Override
    public PhotoVO getPhotoById(int photoId) {
        return fileMapper.selectPhotoById(photoId);
    }

    //사진을 소프트딜리트 합니다.
    @Override
    public void softDeletePhoto(int photoId) {
        // is_deleted = true 로 업데이트
        fileMapper.updateFileSoftDelete(photoId);
    }

    //사진파일을 실제로 삭제합니다.
    @Override
    public void deleteFileFromSystem(String filePath) {
        // 프로젝트 내 경로가 아닌 OS 절대 경로로 변환 필요
        // filePath = "/resources/uploads/xxx" 형태라면:
        String realPath = "C:/Users/winitech/Desktop/assignmentPjt/assignmentPjt2/myboard2/src/main/webapp" 
                + filePath;
        File file = new File(realPath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일 시스템에서 삭제 성공: " + realPath);
            } else {
                System.out.println("파일 시스템에서 삭제 실패: " + realPath);
            }
        }
    }

    //썸네일을 변경합니다.
    @Override
    public void updateThumbnail(int articleId, int photoId) {
        // articleId의 모든 photo 썸네일을 false 로
        fileMapper.resetThumbnail(articleId);
        // 지정된 photoId만 true 로
        fileMapper.setThumbnail(photoId);
    }

    //사진데이터를 DB에서 실제로 날립니다. (미사용예정)
    @Override
    public void deletePhotoPermanently(int photoId) {
        // DB에서 실제 row를 제거 (물리 삭제)
        fileMapper.deleteFilePermanently(photoId);
    }
    
    @Override
    public List<PhotoVO> findDeletedPhotos() {
        return fileMapper.selectDeletedPhotos();
    }
}
