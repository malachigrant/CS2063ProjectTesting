package mobiledev.unb.ca.roompersistencelab;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * This DAO object validates the SQL at compile-time and associates it with a method
 */
@Dao
public interface ItemDao {
    // TODO Add app specific queries in here
    // Additional details can be found at https://developer.android.com/reference/android/arch/persistence/room/Dao

    @Query("SELECT * FROM item_table WHERE name LIKE :name")
    public abstract List<Item> searchItemsByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItems(Item... items);

    @Query("SELECT * from item_table ORDER BY id ASC")
    LiveData<List<Item>> listAllRecords();
}
