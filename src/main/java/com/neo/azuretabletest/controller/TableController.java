package com.neo.azuretabletest.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
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
@RequestMapping("/table")
public class TableController {

    @Autowired
    private CloudStorageAccount cloudStorageAccount;

    @PostMapping("/{name}")
    public ResultEntity create(@PathVariable String name) {
        try
        {
            // Create the table client.
            CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

            // Create the table if it doesn't exist.
            CloudTable cloudTable = tableClient.getTableReference(name);
            cloudTable.createIfNotExists();
            return ResultUtil.success();
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.TABLE_CREATE_FAILURE, "table create failed");
        }
    }

    @GetMapping("")
    public ResultEntity list() {
        try
        {
            // Create the table client.
            CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

            List<String> tableList = new ArrayList<>();
            // Loop through the collection of table names.
            for (String table : tableClient.listTables())
            {
                tableList.add(table);
            }
            return ResultUtil.success(tableList);
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.TABLE_LIST_FAILURE, "table list failed");
        }
    }

    @DeleteMapping("/{name}")
    public ResultEntity delete(@PathVariable String name) {
        try
        {
            // Create the table client.
            CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

            // del the table if it doesn't exist.
            CloudTable cloudTable = tableClient.getTableReference(name);
            cloudTable.deleteIfExists();
            return ResultUtil.success();
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.TABLE_DELETE_FAILURE, "table delete failed");
        }
    }
}
