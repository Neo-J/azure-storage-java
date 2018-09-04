package com.neo.azuretabletest.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
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
    public String create(@PathVariable String name) {
        try
        {
            // Create the table client.
            CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

            // Create the table if it doesn't exist.
            CloudTable cloudTable = tableClient.getTableReference(name);
            cloudTable.createIfNotExists();
            return "table " + name + " created.";
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return "create failed";
        }
    }

    @GetMapping("")
    public List<String> list() {
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
            return tableList;
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/{name}")
    public String delete(@PathVariable String name) {
        try
        {
            // Create the table client.
            CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();

            // Create the table if it doesn't exist.
            CloudTable cloudTable = tableClient.getTableReference(name);
            cloudTable.deleteIfExists();
            return "table " + name + " deleted.";
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return "delete failed";
        }
    }
}
