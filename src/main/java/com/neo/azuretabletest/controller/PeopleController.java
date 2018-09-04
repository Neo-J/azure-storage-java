package com.neo.azuretabletest.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.*;
import com.neo.azuretabletest.constant.ErrCodeConstant;
import com.neo.azuretabletest.entity.PersonEntity;
import com.neo.azuretabletest.entity.ResultEntity;
import com.neo.azuretabletest.exception.TableNotExistException;
import com.neo.azuretabletest.util.ResultUtil;
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

    @GetMapping("")
    public ResultEntity listAll() throws TableNotExistException {

        TableQuery<PersonEntity> partitionQuery =
                TableQuery.from(PersonEntity.class);

        List<PersonEntity> resultList = new ArrayList<>();
        for (PersonEntity entity : getTable().execute(partitionQuery)) {
            resultList.add(entity);
        }

        return ResultUtil.success(resultList);
    }

    @GetMapping("/partition/{partition}")
    public ResultEntity listPartition(@PathVariable String partition) throws TableNotExistException {

        String partitionFilter = TableQuery.generateFilterCondition(
                PARTITION_KEY,
                TableQuery.QueryComparisons.EQUAL,
                partition);

        TableQuery<PersonEntity> partitionQuery =
                TableQuery.from(PersonEntity.class)
                .where(partitionFilter);

        List<PersonEntity> resultList = new ArrayList<>();
        for (PersonEntity entity : getTable().execute(partitionQuery)) {
            resultList.add(entity);
        }

        return ResultUtil.success(resultList);
    }

    @PostMapping("/add")
    public ResultEntity add(@RequestBody PersonEntity personEntity) throws TableNotExistException{

        TableOperation insertPeople = TableOperation.insertOrReplace(personEntity);
        try {
            getTable().execute(insertPeople);
            return ResultUtil.success();
        } catch (StorageException e) {
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.PERSON_ADD_FAILURE, "person add failed");
        }
    }

    @GetMapping("/partition/{partition}/row/{row}")
    public ResultEntity getPartitionRow(@PathVariable String partition, @PathVariable String row) throws TableNotExistException {

        TableOperation retrieveSingle =
                TableOperation.retrieve(partition, row, PersonEntity.class);

        try {
            return ResultUtil.success(getTable().execute(retrieveSingle).getResultAsType());
        } catch (StorageException e) {
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.PERSON_GET_FAILURE, "person get failed");
        }

    }

    @PostMapping("/addBatch")
    public ResultEntity addBatch(@RequestBody List<PersonEntity> people) {
        try
        {
            TableBatchOperation batchOperation = new TableBatchOperation();

            people.forEach(batchOperation::insertOrReplace);

            getTable().execute(batchOperation);
            return ResultUtil.success();
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.PEOPLE_ADD_FAILURE, "people add failed");
        }

    }

    @DeleteMapping("/delete/partition/{partition}/row/{row}")
    public ResultEntity delete(@PathVariable String partition, @PathVariable String row) {
        try
        {
            TableOperation retrievePerson = TableOperation.retrieve(partition, row, PersonEntity.class);

            PersonEntity personEntity =
                    getTable().execute(retrievePerson).getResultAsType();

            TableOperation deletePerson = TableOperation.delete(personEntity);

            getTable().execute(deletePerson);

            return ResultUtil.success();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.PERSON_DELETE_FAILURE, "person delete failure");
        }
    }

    @GetMapping("/listEmails")
    public ResultEntity getSubsetEmails() {
        try
        {
            TableQuery<PersonEntity> projectionQuery =
                    TableQuery.from(PersonEntity.class)
                            .select(new String[] {"Email"});

            EntityResolver<String> emailResolver = (PartitionKey, RowKey, timeStamp, properties, etag) -> properties.get("Email").getValueAsString();

            List<String> result = new ArrayList<>();
            for (String projectedString :
                    getTable().execute(projectionQuery, emailResolver)) {
                result.add(projectedString);
            }
            return ResultUtil.success(result);
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
            return ResultUtil.error(ErrCodeConstant.PEOPLE_EMAILS_GET_FAILURE, "emails list failed");
        }
    }
}
