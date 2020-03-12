package mobiledev.unb.ca.roompersistencelab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private LiveData<List<Item>> items;

    public LiveData<List<Item>> getItems() {
        if (null == items){
            items = updateItemsList();
        }
        return items;
    }

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    // TODO Add mapping calls between the UI and Database

    public LiveData<List<Item>> updateItemsList(){
        return itemRepository.listAllRecords();
    }

    public void insert(String name, int num) {
        itemRepository.insertRecord(name, num);
    }

    public List<Item> search(String name) {
        Future future = itemRepository.searchRecords(name);
        List<Item> list = null;
        try {
            list = (List<Item>) future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }
}
