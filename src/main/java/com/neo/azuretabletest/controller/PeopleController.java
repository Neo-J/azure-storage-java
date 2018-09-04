package com.neo.azuretabletest.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.neo.azuretabletest.entity.PeopleEntity;
import com.neo.azuretabletest.exception.TableNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private CloudStorageAccount cloudStorageAccount;

    private static final String PEOPLE_TABLE_NAME = "people";
    // Define constants for filters.
    private final String PARTITION_KEY = "PartitionKey";
    private final String ROW_KEY = "RowKey";
    private final String TIMESTAMP = "Timestamp";

    private CloudTable getTable() throws TableNotExistException {
        CloudTableClient tableClient = cloudStorageAccount.createCloudTableClient();
        try {
            return tableClient.getTableReference(PEOPLE_TABLE_NAME);
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
            throw new TableNotExistException("Table not exist");
        }
    }

    @GetMapping("/get/{id}")
    public String list(@PathVariable String id) {
        return null;
    }

    @GetMapping("")
    public List<PeopleEntity> listAll() throws TableNotExistException {

        TableQuery<PeopleEntity> partitionQuery =
                TableQuery.from(PeopleEntity.class);

        List<PeopleEntity> resultList = new ArrayList<>();
        for (PeopleEntity entity : getTable().execute(partitionQuery)) {
            resultList.add(entity);
        }

        return resultList;
    }

    @GetMapping("/partition/{partition}")
    public List<PeopleEntity> listPartition(@PathVariable String partition) throws TableNotExistException {

        String partitionFilter = TableQuery.generateFilterCondition(
                PARTITION_KEY,
                TableQuery.QueryComparisons.EQUAL,
                partition);

        TableQuery<PeopleEntity> partitionQuery =
                TableQuery.from(PeopleEntity.class)
                .where(partitionFilter);

        List<PeopleEntity> resultList = new ArrayList<>();
        for (PeopleEntity entity : getTable().execute(partitionQuery)) {
            resultList.add(entity);
        }

        return resultList;
    }

    @PostMapping("/add")
    public String add(@RequestBody PeopleEntity peopleEntity) throws TableNotExistException{

        TableOperation insertPeople = TableOperation.insertOrReplace(peopleEntity);
        try {
            getTable().execute(insertPeople);
            return "add completed";
        } catch (StorageException e) {
            e.printStackTrace();
            return "add failed";
        }
    }

    @GetMapping("/partition/{partition}/row/{row}")
    public PeopleEntity getPartitionRow(@PathVariable String partition, @PathVariable String row) throws TableNotExistException {

        TableOperation retrieveSingle =
                TableOperation.retrieve(partition, row, PeopleEntity.class);

        List<PeopleEntity> resultList = new ArrayList<>();
        try {
            PeopleEntity entity = getTable().execute(retrieveSingle).getResultAsType();
            resultList.add(entity);
        } catch (StorageException e) {
            e.printStackTrace();
        }

        return resultList.get(0);
    }



}
