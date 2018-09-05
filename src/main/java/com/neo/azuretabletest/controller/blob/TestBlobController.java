package com.neo.azuretabletest.controller.blob;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import com.neo.azuretabletest.entity.ResultEntity;
import com.neo.azuretabletest.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/neo-test-blob")
public class TestBlobController {

    @Autowired
    private CloudStorageAccount cloudStorageAccount;

    private CloudBlobContainer getContainer() throws URISyntaxException, StorageException {
        CloudBlobClient client = cloudStorageAccount.createCloudBlobClient();
        return client.getContainerReference("neo-test-blob");
    }

    @GetMapping("")
    public ResultEntity list() {
        try {
            List<String> resultList = new ArrayList<>();
            for (ListBlobItem blobItem : getContainer().listBlobs()) {
                resultList.add("" + blobItem.getUri());
            }
            return ResultUtil.success(resultList);
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/uploadBlock")
    public ResultEntity uploadBlock() throws URISyntaxException, StorageException, IOException {

        Random random = new Random();
        File sourceFile = null;
        sourceFile = File.createTempFile("sampleFile" + random.nextInt(128 * 1024), ".txt");

        System.out.println("Creating a sample file at: " + sourceFile.toString());
        Writer output = null;
        output = new BufferedWriter(new FileWriter(sourceFile));
        output.write("Hello Azure!");
        output.close();

        CloudBlockBlob blob = getContainer().getBlockBlobReference(sourceFile.getName());

        System.out.println("Uploading the sample file ");
        blob.uploadFromFile(sourceFile.getAbsolutePath());
        return ResultUtil.success();
    }

    @PostMapping("/uploadAppend")
    public ResultEntity uploadAppend() throws URISyntaxException, StorageException, IOException {

        Random random = new Random();
        File sourceFile = null;
        sourceFile = File.createTempFile("sampleFile" + random.nextInt(128 * 1024), ".txt");

        System.out.println("Creating a sample file at: " + sourceFile.toString());
        Writer output = null;
        output = new BufferedWriter(new FileWriter(sourceFile));
        output.write("Hello Azure!");
        output.close();

        CloudAppendBlob blob = getContainer().getAppendBlobReference(sourceFile.getName());

        System.out.println("Uploading the sample file ");
        blob.uploadFromFile(sourceFile.getAbsolutePath());
        return ResultUtil.success();
    }
}
