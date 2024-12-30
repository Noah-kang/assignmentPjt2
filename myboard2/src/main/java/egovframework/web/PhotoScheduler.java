package egovframework.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import egovframework.service.FileService;
import egovframework.vo.PhotoVO;

@Component
public class PhotoScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PhotoScheduler.class);

    @Autowired
    private FileService fileService;

    // 30초마다 실행
//   @Scheduled(fixedRate = 30000)
 // 또는 @Scheduled(cron = "0/30 * * * * ?") // cron 방식 [0초부터 매 30초마다]
    public void removeDeletedPhotos() {
        logger.info("스케줄러 시작 - 소프트 딜리트된 사진파일 정리 작업");

        try {
            List<PhotoVO> deletedPhotos = fileService.findDeletedPhotos();
            if (deletedPhotos == null || deletedPhotos.isEmpty()) {
                logger.debug("삭제 대상 사진 없음");
                return;
            }
            for (PhotoVO photo : deletedPhotos) {
                logger.info("삭제 대상 photoId={}, path={}", photo.getPhotoId(), photo.getFilePath());
                //실제파일 삭제
                fileService.deleteFileFromSystem(photo.getFilePath());
                //DB에서 영구 삭제
                //fileService.deletePhotoPermanently(photo.getPhotoId());
            }
        } catch (Exception e) {
            logger.error("스케줄러 작업 중 예외 발생", e);
        }

        logger.info("스케줄러 종료 - 소프트 딜리트된 사진파일 정리 완료");
    }
}
