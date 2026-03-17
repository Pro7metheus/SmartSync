package com.smartpantry.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartpantry.data.model.PantryItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PantryItemDao_Impl implements PantryItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PantryItem> __insertionAdapterOfPantryItem;

  private final EntityDeletionOrUpdateAdapter<PantryItem> __deletionAdapterOfPantryItem;

  private final EntityDeletionOrUpdateAdapter<PantryItem> __updateAdapterOfPantryItem;

  public PantryItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPantryItem = new EntityInsertionAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pantry_items` (`id`,`name`,`category`,`quantity`,`unit`,`minQuantity`,`purchaseDate`,`expiryDate`,`caloriesPerUnit`,`userId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PantryItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCategory());
        statement.bindDouble(4, entity.getQuantity());
        statement.bindString(5, entity.getUnit());
        statement.bindDouble(6, entity.getMinQuantity());
        statement.bindLong(7, entity.getPurchaseDate());
        statement.bindLong(8, entity.getExpiryDate());
        statement.bindLong(9, entity.getCaloriesPerUnit());
        statement.bindLong(10, entity.getUserId());
      }
    };
    this.__deletionAdapterOfPantryItem = new EntityDeletionOrUpdateAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pantry_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PantryItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPantryItem = new EntityDeletionOrUpdateAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pantry_items` SET `id` = ?,`name` = ?,`category` = ?,`quantity` = ?,`unit` = ?,`minQuantity` = ?,`purchaseDate` = ?,`expiryDate` = ?,`caloriesPerUnit` = ?,`userId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PantryItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCategory());
        statement.bindDouble(4, entity.getQuantity());
        statement.bindString(5, entity.getUnit());
        statement.bindDouble(6, entity.getMinQuantity());
        statement.bindLong(7, entity.getPurchaseDate());
        statement.bindLong(8, entity.getExpiryDate());
        statement.bindLong(9, entity.getCaloriesPerUnit());
        statement.bindLong(10, entity.getUserId());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final PantryItem item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPantryItem.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PantryItem item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPantryItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PantryItem item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPantryItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<PantryItem>> getAllItems(final long userId) {
    final String _sql = "SELECT * FROM pantry_items WHERE userId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllItemsList(final long userId,
      final Continuation<? super List<PantryItem>> $completion) {
    final String _sql = "SELECT * FROM pantry_items WHERE userId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PantryItem>>() {
      @Override
      @NonNull
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getItemById(final long id, final Continuation<? super PantryItem> $completion) {
    final String _sql = "SELECT * FROM pantry_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PantryItem>() {
      @Override
      @Nullable
      public PantryItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final PantryItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _result = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<PantryItem>> getByCategory(final String category, final long userId) {
    final String _sql = "SELECT * FROM pantry_items WHERE category = ? AND userId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<PantryItem>> searchByName(final String query, final long userId) {
    final String _sql = "SELECT * FROM pantry_items WHERE name LIKE '%' || ? || '%' AND userId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<PantryItem>> getExpiringSoon(final long thresholdDate, final long userId) {
    final String _sql = "SELECT * FROM pantry_items WHERE expiryDate <= ? AND expiryDate > 0 AND userId = ? ORDER BY expiryDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, thresholdDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getExpiringSoonList(final long thresholdDate, final long userId,
      final Continuation<? super List<PantryItem>> $completion) {
    final String _sql = "SELECT * FROM pantry_items WHERE expiryDate <= ? AND expiryDate > 0 AND userId = ? ORDER BY expiryDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, thresholdDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PantryItem>>() {
      @Override
      @NonNull
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<PantryItem>> getLowStock(final long userId) {
    final String _sql = "SELECT * FROM pantry_items WHERE quantity <= minQuantity AND userId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLowStockList(final long userId,
      final Continuation<? super List<PantryItem>> $completion) {
    final String _sql = "SELECT * FROM pantry_items WHERE quantity <= minQuantity AND userId = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PantryItem>>() {
      @Override
      @NonNull
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<Integer> getTotalCount(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM pantry_items WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getExpiringSoonCount(final long thresholdDate, final long userId) {
    final String _sql = "SELECT COUNT(*) FROM pantry_items WHERE expiryDate <= ? AND expiryDate > 0 AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, thresholdDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getOutOfStockCount(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM pantry_items WHERE quantity <= 0 AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getLowStockCount(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM pantry_items WHERE quantity <= minQuantity AND quantity > 0 AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getExpiredItems(final long now, final long userId,
      final Continuation<? super List<PantryItem>> $completion) {
    final String _sql = "SELECT * FROM pantry_items WHERE expiryDate < ? AND expiryDate > 0 AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, now);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PantryItem>>() {
      @Override
      @NonNull
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfMinQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "minQuantity");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCaloriesPerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPerUnit");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final double _tmpMinQuantity;
            _tmpMinQuantity = _cursor.getDouble(_cursorIndexOfMinQuantity);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final int _tmpCaloriesPerUnit;
            _tmpCaloriesPerUnit = _cursor.getInt(_cursorIndexOfCaloriesPerUnit);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new PantryItem(_tmpId,_tmpName,_tmpCategory,_tmpQuantity,_tmpUnit,_tmpMinQuantity,_tmpPurchaseDate,_tmpExpiryDate,_tmpCaloriesPerUnit,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
