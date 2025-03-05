package com.mart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Value("${fileUpload.rootPath}")
	private String rootPath;
	private Path root;

	public void init() {

		try {
			root = Paths.get(rootPath);
			if (Files.notExists(root)) {
				Files.createDirectories(root);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Error");
		}

	}

	@Override
	public boolean saveFile(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			init();
			Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Error");
			return false;
		}

	}

	@Override
	public Resource loadFile(String fileName) {
		// TODO Auto-generated method stub
		try {
			init();
			Path file = root.resolve(fileName);
			Resource resource = (Resource) new UrlResource(file.toUri());
			if (((AbstractFileResolvingResource) resource).exists()
					|| ((AbstractFileResolvingResource) resource).isReadable()) {
				return resource;
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		return null;

	}

}
