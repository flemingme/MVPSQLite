[如何创建和使用SQLite数据库呢？](https://flemingme.github.io/2017/02/17/%E5%A6%82%E4%BD%95%E5%88%9B%E5%BB%BA%E5%92%8C%E4%BD%BF%E7%94%A8SQLite%E6%95%B0%E6%8D%AE%E5%BA%93/)

**1.创建类继承SQLiteOpenHelper，并重写onCreate和onUpgrade方法。**

```java
public final class DBContract {

    private DBContract() {}

    public static class UserEntity implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_UESR_ID = "_id";
        public static final String COLUMN_NAME_USER_NAME = "name";
        public static final String COLUMN_NAME_USER_AGE = "age";
    }
}
```

这里我一般会写一个契约类，通过ORM的思想，将数据表抽象成对象，用来记录数据表的相关信息，方便编辑和调用。

**2.编写sql，在onCreate中通过db.execSQL(sql)执行sql。**

```java
@Override
public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DBContract.UserEntity.TABLE_NAME + "( "
                + DBContract.UserEntity.COLUMN_NAME_UESR_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + DBContract.UserEntity.COLUMN_NAME_USER_NAME + " TEXT, "
                + DBContract.UserEntity.COLUMN_NAME_USER_AGE +" INTEGER );";
        db.execSQL(sql);
}
```

注意sql的编写规范及空格，建议关键字大写。

**3.在构造方法中传入数据库的名字和版本号等参数。**

```java
private static final int DB_VERSION = 1;

public DBHelper(Context c, String dbName) {
    super(c, dbName, null, DB_VERSION);
}
```

**4.在onUpgrade方法中可以升级数据库。**

```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntity.TABLE_NAME);
            onCreate(db);
        }
}
```

这里判断如果有新版本，就执行sql删除数据表再创建新的，也可以做其他操作。

**5.实例化该类获取db对象。**

```java
mHelper = new DBHelper(context, dbName);

//获取可写的数据库对象，用于增删改
SQLiteDatabase db = mHelper.getWritableDatabase();
//获取只读的数据库对象，用于查询
SQLiteDatabase db = mHelper.getReadableDatabase();
```

**6.CRUD操作。**

**(1) Create(创建)**

```java
public boolean insert(User user) {
    final SQLiteDatabase db = mHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    if (user.getId() != 0) values.put(DBContract.UserEntity.COLUMN_NAME_UESR_ID, user.getId());
    if (!TextUtils.isEmpty(user.getName())) values.put(DBContract.UserEntity.COLUMN_NAME_USER_NAME, user.getName());
    if (user.getAge() != 0) values.put(DBContract.UserEntity.COLUMN_NAME_USER_AGE, user.getAge());

    long _id = db.insert(DBContract.UserEntity.TABLE_NAME, null, values);

    db.close();

    return _id > 0;
}
```

这里主要是利用ContentValues类，通过SQLiteDatabase的insert方法，将值插入进去。当然熟悉sql的还可以利用sql来执行。

```java
String sql = "insert into " + TAB_PERSON + " values (?,'test','1');";
try {
    getWritableDatabase().beginTransaction();
    SQLiteStatement sqLiteStatement = getReadableDatabase().compileStatement(sql);
    int count = 0;
    while (count < 100) {
        count++;
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1, count);
        sqLiteStatement.executeInsert();
    }
    getWritableDatabase().setTransactionSuccessful();
} catch (Exception e) {
    e.printStackTrace();
} finally {
    getWritableDatabase().endTransaction();
}
```

在调用insert方法中会调用insertWithOnConflict方法

```java
public long insertWithOnConflict(String table, String nullColumnHack,
       ContentValues initialValues, int conflictAlgorithm) {
   acquireReference();
   try {
       StringBuilder sql = new StringBuilder();
       sql.append("INSERT");
       sql.append(CONFLICT_VALUES[conflictAlgorithm]);
       sql.append(" INTO ");
       sql.append(table);
       sql.append('(');

       Object[] bindArgs = null;
       int size = (initialValues != null && initialValues.size() > 0)
               ? initialValues.size() : 0;
       if (size > 0) {
           bindArgs = new Object[size];
           int i = 0;
           for (String colName : initialValues.keySet()) {
               sql.append((i > 0) ? "," : "");
               sql.append(colName);
               bindArgs[i++] = initialValues.get(colName);
           }
           sql.append(')');
           sql.append(" VALUES (");
           for (i = 0; i < size; i++) {
               sql.append((i > 0) ? ",?" : "?");
           }
        } else {
            sql.append(nullColumnHack + ") VALUES (NULL");
        }
        sql.append(')');

        SQLiteStatement statement = new SQLiteStatement(this, sql.toString(), bindArgs);
        try {
            return statement.executeInsert();
        } finally {
            statement.close();
        }
    } finally {
        releaseReference();
    }
}
```

可以看出，源码善用线程安全的StringBuilder的方式去链式调用，透过Set的Key和Value巧妙的拼凑sql，还利用预处理的方式去执行insert语句，不过没有手动控制事务，因此可以加上事务的操作提高效率。

注意：有两点可以优化db的执行效率的，一是手动执行事务操作，二是利用SQLiteStatement预处理，戳这里http://www.jianshu.com/p/c942d5d9cf2e

**(2) Retrieve(读取)**

```java
@Override
public User selectById(int userId) {
    final SQLiteDatabase db = mHelper.getReadableDatabase();
    final String selection = DBContract.UserEntity.COLUMN_NAME_UESR_ID + "=?";
    final String[] selectionArgs = new String[]{String.valueOf(userId)};

    Cursor cursor = db.query(DBContract.UserEntity.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    cursor.moveToFirst();
    User user = null;
    while (cursor.moveToNext()) {
        int _id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_UESR_ID));
        String name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_NAME));
        int age = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_AGE));
            user = new User(_id, name, age);
        }
        cursor.close();
        db.close();
        return user;
}

@Override
public List<User> selectAll() {
    final SQLiteDatabase db = mHelper.getReadableDatabase();
    List<User> list = new ArrayList<>();
    Cursor cursor = db.query(DBContract.UserEntity.TABLE_NAME, null, null, null, null, null, DBContract.UserEntity.COLUMN_NAME_UESR_ID);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
        int _id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_UESR_ID));
        String name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_NAME));
        int age = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntity.COLUMN_NAME_USER_AGE));
        list.add(new User(_id, name, age));
        cursor.moveToNext();
    }
    cursor.close();
    db.close();
    return list;
}
```

**(3) Update(修改)**

```java
@Override
public boolean update(int userId, User user) {
    final SQLiteDatabase db = mHelper.getWritableDatabase();
    final String selection = DBContract.UserEntity.COLUMN_NAME_UESR_ID + "=?";
    final String[] selectionArgs = new String[]{String.valueOf(userId)};

    ContentValues values = new ContentValues();
    if (!TextUtils.isEmpty(user.getName())) values.put(DBContract.UserEntity.COLUMN_NAME_USER_NAME, user.getName());
    if (user.getAge() != 0) values.put(DBContract.UserEntity.COLUMN_NAME_USER_AGE, user.getAge());

    Cursor query = mHelper.getReadableDatabase().query(DBContract.UserEntity.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    int _id = 0;
    if (query.getCount() > 0) {
        _id = db.update(DBContract.UserEntity.TABLE_NAME, values, selection, selectionArgs);
    }
    query.close();
    db.close();
    return _id > 0;
}
```

**(4) Delete(删除)**

```java
@Override
public boolean delete(int userId) {
    final SQLiteDatabase db = mHelper.getWritableDatabase();
    final String selection = DBContract.UserEntity.COLUMN_NAME_UESR_ID + "=?";
    final String[] selectionArgs = new String[]{String.valueOf(userId)};
    Cursor query = mHelper.getReadableDatabase().query(DBContract.UserEntity.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    int _id = 0;
    if (query.getCount() > 0) {
        _id = db.delete(DBContract.UserEntity.TABLE_NAME, selection, selectionArgs);
    }
    query.close();
    db.close();
    return _id > 0;
}
```