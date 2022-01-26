package hotsixturtles.tupli.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    // GCS의 key.json 파일 내용이 등록되어서 주입됨
    private final Storage storage;

    private final UserRepository userRepository;

    @SuppressWarnings("deprecation")
    public String imageUploadGCS (MultipartFile file, Long userSeq) throws IOException {
        User user = userRepository.findByUserSeq(userSeq);

        String bucketName = "tupli_profile";
        // GCS 자체 지원으로 폴더 추가할때 로직 필요 없음
        String uploadFileName = user.getUserSeq() + "/" + StringUtils.cleanPath(file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            // 가로,세로 설정 200, 200, 화질 100
            Image processedImage = ImageIO.read(inputStream);

            BufferedImage scaledBI = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaledBI.createGraphics();
            g.drawImage(processedImage, 0, 0, 200, 200, null);
            g.dispose();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(scaledBI, "jpg", os);

            InputStream processedInputStream = new ByteArrayInputStream(os.toByteArray());

            storage.create(BlobInfo.newBuilder(bucketName, uploadFileName).build(), processedInputStream);

        } catch (IOException ioe) {
            throw new IOException("이미지 저장/변환 불가 : " + uploadFileName, ioe);
        }

        // DB에 저장될 값
        String image = "/" + uploadFileName;
        return image;
    }

}
