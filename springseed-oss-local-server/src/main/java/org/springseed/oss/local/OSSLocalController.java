package org.springseed.oss.local;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springseed.oss.local.metadata.Metadata;
import org.springseed.oss.local.metadata.MetadataRepository;
import org.springseed.oss.local.util.FileNameCounter;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传，下载接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/v1/files")
public class OSSLocalController {
	@Autowired
	private StorageService storageService;
	@Autowired
	private MetadataRepository metadataRepository;

	@PostMapping("/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public String upload(@RequestParam("file") MultipartFile file) {
		return storageService.store(file);
	}

	@GetMapping("/download/{metadataId}")
	public Resource downloadByMetadataId(@PathVariable String metadataId) {
		return storageService.loadByMetadataId(metadataId);
	}

	@GetMapping(value = "/download/all-in-zip", produces = "application/zip")
	public void downloadByMetadataIds(@RequestParam(name = "metadataIds") List<String> metadataIds,
			HttpServletResponse response) throws IOException {

		// 查询元数据
		final List<Metadata> metadatas = this.metadataRepository.findAllById(metadataIds);				
		try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
			final FileNameCounter fileNameCounter = new FileNameCounter();
			for (final Metadata metadata : metadatas) {
				try {
					// 读取文件
					final Resource file = this.storageService.loadByMetadata(metadata);
					// 避免重复的文件名
					final ZipEntry zipEntry = new ZipEntry(fileNameCounter.convert(metadata.getName()));
					zipEntry.setSize(file.contentLength());
					zipOut.putNextEntry(zipEntry);
					StreamUtils.copy(file.getInputStream(), zipOut);
					zipOut.closeEntry();
				} catch (FileNotFoundException ex) {
					// 单个文件读取失败，不影响其他文件读取
					log.warn(ex.getMessage());
				}
			}
			zipOut.finish();
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}

	@DeleteMapping("/{metadataId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteByMetadataId(@PathVariable String metadataId) {
		storageService.removeByMetadataId(metadataId);
	}
}
