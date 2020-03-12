package mobiledev.unb.ca.roompersistencelab;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")  // Represents a SQLite table
public class Item {
    // TODO Create the values for the entity
    //  You will need to add variables for the column names of id, name, and num
    //  NOTE: id is an autogenerated attribute
    // Additional details can be found at https://developer.android.com/reference/android/arch/persistence/room/Entity

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "num")
    private int num;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
}
