package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFilePath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        ArrayList<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = this.createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(this.getFilePath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename){
        String ext = this.extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "."+ ext;
    }

    // 확장자를 추출하네.
    private String extractExt(String originalFileName){
        // lastIndexOf : 문자열에서 "."이 마지막으로 등장하는 인덱스(위치) 반환
        int pos = originalFileName.lastIndexOf(".");

        // substring(): pos에서 끝까지
        return originalFileName.substring(pos);
    }
}
