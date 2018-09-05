package com.neo.azuretabletest.controller.blob;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.neo.azuretabletest.constant.ErrCodeConstant;
import com.neo.azuretabletest.entity.ResultEntity;
import com.neo.azuretabletest.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blob")
public class ContainerController {

    @Autowired
    private CloudStorageAccount cloudStorageAccount;

    @GetMapping("")
    public ResultEntity list() {
        try
        {
            // Create the table client.
            CloudBlobClient blobClient = cloudStorageAccount.createCloudBlobClient();

            List<String> containerList = new ArrayList<>();
            // Loop through the collection of table names.
            for (CloudBlobContainer container : blobClient.listContainers())
            {
                containerList.add(container.getName());
            }
            return ResultUtil.success(containerList);
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.CONTAINER_LIST_FAILURE, "container list failed");
        }
    }

}
