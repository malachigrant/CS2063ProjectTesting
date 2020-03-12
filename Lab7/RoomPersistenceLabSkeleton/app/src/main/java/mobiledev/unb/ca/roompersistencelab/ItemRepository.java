package mobiledev.unb.ca.roompersistencelab;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import mobiledev.unb.ca.roompersistencelab.Item;

public class ItemRepository {
    private ItemDao itemDao;

    public ItemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        itemDao = db.itemDao();
    }

    //  See the example project file at
    //  https://github.com/hpowell20/cs2063-winter-2020-examples/blob/master/Lecture7/RoomPersistenceLibraryDemo/app/src/main/java/mobiledev/unb/ca/roompersistencetest/repository/ItemRepository.java
    //  to see examples of how to work with the Executor Service

    // TODO Add query specific methods
    //  HINTS
    //   The insert operation needs to make use of the Runnable interface
    //   The search operation needs to make use of the Callable interface

    public void insertRecord(String name, int num) {
        Item newItem = new Item();
        newItem.setName(name);
        newItem.setNum(num);

        insertRecord(newItem);
    }

    private void insertRecord(final Item item) {
        AppDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.insertItems(item);
            }
        });
    }

    public LiveData<List<Item>> listAllRecords() {
        return itemDao.listAllRecords();
    }


    public Future searchRecords(final String name){
        Future future = AppDatabase.databaseWriterExecutor.submit(new Callable(){
            public Object call() throws Exception{
                System.out.println("Asynchronous Callable");
                return itemDao.searchItemsByName(name);
            }
        });
        return future;
    }

}
